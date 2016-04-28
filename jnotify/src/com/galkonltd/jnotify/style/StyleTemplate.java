package com.galkonltd.jnotify.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.HashMap;

import com.galkonltd.jnotify.NotifyType;

public class StyleTemplate {
	
	private final HashMap<NotifyType, StyleConfig> configs = new HashMap<NotifyType, StyleConfig>();

	public void putConfig(NotifyType type, StyleConfig config) {
		configs.put(type, config);
	}
	
	public StyleConfig getConfig(NotifyType type) {
		return configs.get(type);
	}

	public Image getIcon(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for icon: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		return configs.get(type).getIcon();
	}
	
	public Color getBackground(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for background: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		return configs.get(type).getBackground();
	}

	public int getBorderRadius(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for stroke: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		return configs.get(type).getBorderRadius();
	}
	
	public Color getStroke(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for stroke: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		return configs.get(type).getStroke();
	}

	public Color getInnerStroke(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for inner stroke: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		return configs.get(type).getInnerStroke();
	}

	public Color getFontColor(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for font color: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		return configs.get(type).getFontColor();
	}
	
	public Color getFontShadow(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for font shadow: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		return configs.get(type).getFontShadow();
	}
	
	public Font getFont(NotifyType type) {
		if (!configs.containsKey(type)) {
			System.err.println("Unrecognized NotifyType for font: " + type.toString());
			type = NotifyType.DEFAULT;
		}
		StyleConfig config = configs.get(type);
		return new Font(config.getFontFamily(), config.getFontWeight(), config.getFontSize());
	}

}
