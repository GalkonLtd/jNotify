package com.galkonltd.jnotify;

public enum NotifyType {
	
	DEFAULT, 
	INFO, 
	SUCCESS, 
	WARNING, 
	ERROR, 
	MESSAGE,
	LINK;
	
	public static NotifyType typeFor(String string) {
		for (NotifyType type : NotifyType.values()) {
			if (type.toString().toLowerCase().equalsIgnoreCase(string)) {
				return type;
			}
		}
		return null;
	}

}