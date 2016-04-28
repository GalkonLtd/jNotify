package com.galkonltd.jnotify.style;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.galkonltd.jnotify.NotifyType;

public final class StyleParser {
	
	public static StyleTemplate parse(InputStream inputStream) {
		StyleTemplate template = new StyleTemplate();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			NotifyType type = null;
			StyleConfig config = null;
			boolean listenForConfig = false;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("/*")) {
					continue;
				}
				if (line.startsWith("*/")) {
					if (!(line.contains(";") && line.indexOf("*/") > line.indexOf(";"))) {
						continue;
					}
				}
				if (line.contains("{")) {
					type = NotifyType.typeFor(line.substring(line.indexOf(".") + 1, line.indexOf(" ")).trim());
					config = new StyleConfig();
					listenForConfig = true;
					continue;
				}
				if (line.contains("}")) {
					if (type != null && config != null) {
						if (type != NotifyType.DEFAULT) {
							StyleConfig defaultConfig = template.getConfig(NotifyType.DEFAULT);
							if (config.getBackground() == null) {
								config.setBackground(defaultConfig.getBackground());
							}
							if (config.getStroke() == null) {
								config.setStroke(defaultConfig.getStroke());
							}
							if (config.getInnerStroke() == null) {
								config.setInnerStroke(defaultConfig.getInnerStroke());
							}
							if (config.getFontColor() == null) {
								config.setFontColor(defaultConfig.getFontColor());
							}
							if (config.getFontShadow() == null) {
								config.setFontShadow(defaultConfig.getFontShadow());
							}
							if (config.getFontSize() == 0) {
								config.setFontSize(defaultConfig.getFontSize());
							}
							if (config.getFontWeight() == -1) {
								config.setFontWeight(defaultConfig.getFontWeight());
							}
							if (config.getFontFamily() == null) {
								config.setFontFamily(defaultConfig.getFontFamily());
							}
							if (config.getIcon() == null) {
								config.setIcon(defaultConfig.getIcon());
							}
							if (config.getBorderRadius() == -1) {
								config.setBorderRadius(defaultConfig.getBorderRadius());
							}
						}
						template.putConfig(type, config);
					}
					listenForConfig = false;
					continue;
				}
				if (listenForConfig) {
					if (line.contains(":")) {
						line = line.trim();
						String param = line.substring(0, line.indexOf(":"));
						String value = line.substring(line.indexOf(": ") + 2, line.indexOf(";"));
						if (param.equalsIgnoreCase("background-color")) {
							config.setBackground(Color.decode(value));
						} else if (param.equalsIgnoreCase("border-color")) {
							config.setStroke(Color.decode(value));
						} else if (param.equalsIgnoreCase("border-inner")) {
							config.setInnerStroke(Color.decode(value));
						} else if (param.equalsIgnoreCase("border-radius")) {
							config.setBorderRadius(Integer.parseInt(value.replaceAll("px", "")));
						} else if (param.equalsIgnoreCase("font-color")) {
							config.setFontColor(Color.decode(value));
						} else if (param.equalsIgnoreCase("font-shadow")) {
							config.setFontShadow(Color.decode(value));
						} else if (param.equalsIgnoreCase("font-family")) {
							config.setFontFamily(value.replaceAll("\"", ""));
						} else if (param.equalsIgnoreCase("font-size")) {
							config.setFontSize(Integer.parseInt(value.replaceAll("px", "")));
						} else if (param.equalsIgnoreCase("font-weight")) {
							if (value.equalsIgnoreCase("bold")) {
								config.setFontWeight(Font.BOLD);
							} else {
								config.setFontWeight(Font.PLAIN);
							}
						} else if (param.equalsIgnoreCase("icon")) {
							config.setIcon(ImageIO.read(StyleParser.class.getResourceAsStream(value.replaceAll("\"", ""))));
						}
					} else {
						System.out.println("Unexpected config for " + type + " type: " + line);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return template;
	}

}