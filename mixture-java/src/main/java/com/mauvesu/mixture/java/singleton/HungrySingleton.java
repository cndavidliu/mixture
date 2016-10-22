package com.mauvesu.mixture.java.singleton;

/**
 * Hungry Singleton
 * 
 * @author mauvesu
 *
 */
public class HungrySingleton extends BaseSingleton {
	
	private static HungrySingleton singleton = new HungrySingleton();
	
	private HungrySingleton() {
	}
	
	public static HungrySingleton getInstance() {
		return singleton;
	}

}
