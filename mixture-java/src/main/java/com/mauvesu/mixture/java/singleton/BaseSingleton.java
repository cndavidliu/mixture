package com.mauvesu.mixture.java.singleton;

/**
 * Abstract class for singleton to extend
 * 
 * @author mauvesu
 * 
 */
public class BaseSingleton {
	
	private String name = "";
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(BaseSingleton singleton) {
		return this.name.equals(singleton.getName());
	}
}
