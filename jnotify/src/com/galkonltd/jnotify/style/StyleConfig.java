package com.galkonltd.jnotify.style;

import java.awt.Color;
import java.awt.Image;

public final class StyleConfig {

	private Image icon;
	private Color background;
	private Color stroke;
	private Color innerStroke;
	private Color fontColor;
	private Color fontShadow;
	private int fontSize = 0;
	private int fontWeight = -1;
	private String fontFamily = null;
	private int borderRadius = -1;
	
	public StyleConfig() {
	}

	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public Color getStroke() {
		return stroke;
	}

	public void setStroke(Color stroke) {
		this.stroke = stroke;
	}

	public Color getInnerStroke() {
		return innerStroke;
	}

	public void setInnerStroke(Color innerStroke) {
		this.innerStroke = innerStroke;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Color getFontShadow() {
		return fontShadow;
	}

	public void setFontShadow(Color fontShadow) {
		this.fontShadow = fontShadow;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(int fontWeight) {
		this.fontWeight = fontWeight;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public int getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
	}
}