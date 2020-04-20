package org.magic.api.beans;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class MTGKeyWord implements Serializable{

	private static final long serialVersionUID = 1L;

	public enum TYPE {
		ABILITIES, ACTION, WORD
	}

	public enum EVENT {
		STATIC, ACTIVATED, TRIGGERED
	}

	private String keyword;
	private String description;
	private EVENT event;
	private TYPE type;

	public MTGKeyWord() {

	}
	
	public MTGKeyWord(String keyword, TYPE type) {
			this.keyword=keyword;
			this.type=type;
	}
	
	public MTGKeyWord(String keyword, EVENT event, TYPE type) {
		super();
		this.keyword = keyword;
		this.event = event;
		this.type = type;
	}

	public EVENT getEvent() {
		return event;
	}

	public void setEvent(EVENT event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return StringUtils.capitalize(getKeyword());
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	
}
