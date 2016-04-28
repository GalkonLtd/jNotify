package com.galkonltd.jnotify;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import javax.swing.JPanel;
import javax.swing.JWindow;

import com.galkonltd.jnotify.style.StyleParser;
import com.galkonltd.jnotify.style.StyleTemplate;

public final class Notification extends JWindow {

	private static final int MIN_WIDTH = 400, MIN_HEIGHT = 57;
	private static StyleTemplate style;
	
	/**
	 * Sets the static {@link StyleTemplate} for the notification window.
	 * @param notifyStyle
	 */
	public static void setStyle(StyleTemplate notifyStyle) {
		style = notifyStyle;
	}
	
	/**
	 * Returns a slightly lighter version of the color for the hovered color.
	 * @param color
	 * @return
	 */
	private static Color hover(Color color) {
		int offset = 15;
		int red = color.getRed() + offset > 255 ? 255 : color.getRed() + offset;
		int green = color.getGreen() + offset > 255 ? 255 : color.getGreen() + offset;
		int blue = color.getBlue() + offset > 255 ? 255 : color.getBlue() + offset;
		return new Color(red, green, blue);
	}

	private Optional<NotifyCallback> callback;
	private ScheduledFuture<?> closeFuture;
	private int closeDelay;
	private boolean hovered;
	private boolean hidden;
	
	/**
	 * Initializes a DEFAULT {@link NotifyType} {@link Notification} with the specified message.
	 * @param message
	 */
	public Notification(String message) {
		this(NotifyType.DEFAULT, message, null);
	}
	
	/**
	 * Initializes a {@link Notification} with the specified type and message.
	 * @param type
	 * @param message
	 */
	public Notification(NotifyType type, String message) {
		this(type, message, null);
	}

	/**
	 * Initialize the {@link Notification} with the specified type, message, and callback.
	 * @param type
	 * @param message
	 * @param callback
	 */
	public Notification(NotifyType type, String message, NotifyCallback callback) {
		if (style == null) {
			style = StyleParser.parse(Notifier.class.getResourceAsStream("/com/galkonltd/jnotify/style/resources/style.css"));
		}
		this.callback = Optional.ofNullable(callback);
		Font font = style.getFont(type);
		int strWidth = this.getFontMetrics(font).stringWidth(message);
		int width = MIN_WIDTH;
		if (strWidth + 5 >= (MIN_WIDTH - 52)) {
			width += (strWidth - (MIN_WIDTH - 52)) + 10;
		}
		this.setSize(width, MIN_HEIGHT);
		this.setAlwaysOnTop(true);
		this.setLayout(new BorderLayout());
		this.add(new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				
				Color color = style.getBackground(type);
				g2d.setColor(hovered ? hover(color) : color);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				
				color = style.getInnerStroke(type);
				if (color == null) {
					color = style.getBackground(type);
				}
				g2d.setColor(hovered ? hover(color) : color);
				g2d.drawRect(1, 1, getWidth() - 3, getHeight() - 3);

				color = style.getStroke(type);
				if (color == null) {
					color = style.getBackground(type);
				}
				g2d.setColor(hovered ? hover(color) : color);
				g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				
				Image icon = style.getIcon(type);
				if (icon != null) {
					g2d.drawImage(icon, 15, getHeight() / 2 - icon.getHeight(null) / 2, null);
				}
				
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				
				int messageX = icon == null ? 12 : 52;
				
				g2d.setFont(font);
				g2d.setColor(style.getFontShadow(type));
				g2d.drawString(message, messageX + 1, (getHeight() - 5) / 2 + (getFontMetrics(font).getHeight() / 2) + 1);
				
				color = style.getFontColor(type);
				g2d.setColor(hovered ? hover(color) : color);
				g2d.drawString(message, messageX, (getHeight() - 5) / 2 + (getFontMetrics(font).getHeight() / 2));

				RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(100, 100, 240, 160, 10, 10);
				g2d.draw(roundedRectangle);
			}
		}, BorderLayout.CENTER);
		
		Notification instance = this;
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (hasCallback() && !hidden) {
					getCallback().perform(instance);
				}
				Notifier.hideNotification(instance);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				hovered = true;
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				hovered = false;
				repaint();
			}
		});

		this.setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), style.getBorderRadius(type), style.getBorderRadius(type)));
	}
	
	/**
	 * Sets the delay until the notification auto closes itself.
	 * @param delay
	 * @return
	 */
	public Notification autoClose(int delay) {
		this.closeDelay = delay;
		return this;
	}

	/**
	 * Sets the notification callback action for when the notification window is clicked.
	 * @param callback
	 * @return
	 */
	public Notification callback(NotifyCallback callback) {
		this.callback = Optional.ofNullable(callback);
		return this;
	}
	
	/**
	 * Sets the notifications {@link ScheduledFuture} task for auto closing.
	 * @param future
	 */
	public void setCloseFuture(ScheduledFuture<?> future) {
		this.closeFuture = future;
	}
	
	/**
	 * Returns the {@link ScheduledFuture} task for auto closing.
	 * @return
	 */
	public ScheduledFuture<?> getCloseFuture() {
		return this.closeFuture;
	}
	
	/**
	 * Returns whether or not the notification has a callback action present.
	 * @return
	 */
	public boolean hasCallback() {
		return this.callback.isPresent();
	}
	
	/**
	 * Returns the {@link NotifyCallback} instance if it exists.
	 * @return
	 */
	public NotifyCallback getCallback() {
		return this.callback.get();
	}
	
	/**
	 * Returns whether or not the notification is currently hidden.
	 * @return
	 */
	public boolean isHidden() {
		return this.hidden;
	}
	
	/**
	 * Sets the notification window as hidden.
	 * @param hidden
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	/**
	 * Returns whether or not the notification window auto closes (if the close delay is greater than 0, return true).
	 * @return
	 */
	public boolean autoCloses() {
		return this.closeDelay > 0;
	}
	
	/**
	 * Returns the delay until the notification window is auto closed.
	 * @return
	 */
	public int getCloseDelay() {
		return this.closeDelay;
	}

}
