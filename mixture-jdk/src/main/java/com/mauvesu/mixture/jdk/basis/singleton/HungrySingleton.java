package com.mauvesu.mixture.jdk.basis.singleton;

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
