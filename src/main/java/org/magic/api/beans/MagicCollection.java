package org.magic.api.beans;

import java.io.Serializable;

public class MagicCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;

	public MagicCollection() {

	}

	public MagicCollection(String name) {
		this.name = name;
	}

	public void setName(String string) {
		this.name = string;

	}

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj==null)
			return false;
		
		if(!(obj instanceof MagicCollection))
			return false;
		
		return ((MagicCollection)obj).getName().equalsIgnoreCase(getName());
		
		
		
	}
	
}
