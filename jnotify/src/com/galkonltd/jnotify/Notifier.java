package com.galkonltd.jnotify;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Notifier {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Rectangle WINDOW_BOUNDS = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    private static final int SCREEN_WIDTH = (int) WINDOW_BOUNDS.getWidth();
    private static final int SCREEN_HEIGHT = (int) WINDOW_BOUNDS.getHeight();
    private static final int MAX_Y = SCREEN_HEIGHT - 63;

    private static List<Notification> notifications = new ArrayList<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ExecutorService worker = Executors.newCachedThreadPool();

    /**
     * Returns whether or not the Notifier has notifications in it currently.
     *
     * @return
     */
    public static boolean isEmpty() {
        return notifications.isEmpty();
    }

    /**
     * Pushes the notification to the user's desktop.
     *
     * @param notification
     */
    public static void push(Notification notification) {
        setPosition(notification, notifications.size());
        notifications.add(notification);
    }

    /**
     * Sets the notification's position based on the notification's index.
     *
     * @param notification
     * @param index
     */
    private static void setPosition(Notification notification, int index) {
        int x = SCREEN_WIDTH - (notification.getWidth() + 6);
        int y = (int) SCREEN_SIZE.getHeight();
        int targetY = SCREEN_HEIGHT - (63 * (index + 1));
        int targetOffset = y - targetY;
        notification.setLocation(x, y);
        notification.setVisible(true);
        worker.submit(new Runnable() {
            int offset = 0;

            @Override
            public void run() {
                while (offset < targetOffset) {
                    offset++;
                    notification.setLocation(x, y - offset);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (notification.autoCloses()) {
                    notification.setCloseFuture(scheduler.schedule(() -> hideNotification(notification), notification.getCloseDelay(), TimeUnit.MILLISECONDS));
                }
                shiftNotifications();
            }
        });
    }

    /**
     * Hides the specified {@link Notification}.
     *
     * @param notification
     */
    public static void hideNotification(Notification notification) {
        if (notification.isHidden()) {
            return;
        }
        notification.setHidden(true);
        for (MouseListener listener : notification.getListeners(MouseListener.class)) {
            notification.removeMouseListener(listener);
        }
        if (notification.getCloseFuture() != null) {
            notification.getCloseFuture().cancel(false);
        }
        worker.submit((Runnable) () -> {
            while (notification.getOpacity() > 0.0F) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notification.setOpacity(notification.getOpacity() - 0.01F);
                if (notification.getOpacity() <= 0.01000067F) {
                    notification.setOpacity(0.0F);
                }
            }
            removeNotification(notification);
        });
    }

    /**
     * Removes the specified {@link Notification}.
     *
     * @param notification
     */
    private static void removeNotification(Notification notification) {
        notification.dispose();
        synchronized (notifications) {
            notifications.remove(notification);
            if (!notifications.isEmpty()) {
                shiftNotifications();
            }
        }
    }

    /**
     * Shifts the notifications when one is removed.
     */
    private static void shiftNotifications() {
        Iterator<Notification> it = notifications.iterator();
        while (it.hasNext()) {
            Notification window = it.next();
            if (window.getY() < MAX_Y) {
                int targetY = getTargetYForIndex(notifications.indexOf(window));
                worker.submit((Runnable) () -> {
                    while (window.getY() < targetY) {
                        window.setLocation(window.getX(), window.getY() + 1);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int tY = getTargetYForIndex(notifications.indexOf(window));
                    if (targetY != tY) {
                        //TODO
                    }
                });
            }
        }
    }

    /**
     * Gets the target Y position for the given notification index.
     *
     * @param index
     * @return
     */
    private static int getTargetYForIndex(int index) {
        return SCREEN_HEIGHT - (63 * (index + 1));
    }

}