package org.magic.api.beans;

import java.io.Serializable;

public class MagicNews implements Serializable {

	private int id;
	private String name;
	private String categorie;
	private String url;
	private NEWS_TYPE type;
	
	public enum NEWS_TYPE {RSS,TWITTER,FORUM}
	
	public MagicNews() {
		id=-1;
	}
	
	public NEWS_TYPE getType() {
		return type;
	}
	
	public void setType(NEWS_TYPE type) {
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
			this.url = url;
	}
	
	public String toString() {
		return getName();
	}
	
}
