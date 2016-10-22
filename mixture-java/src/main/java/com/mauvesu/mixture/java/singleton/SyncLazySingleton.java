package com.mauvesu.mixture.java.singleton;

/**
 * Lazy Singleton in sync module, using synchronized method
 * 
 * @author mauvesu
 *
 */
public class SyncLazySingleton extends BaseSingleton {
	
	private static SyncLazySingleton singleton = null;
	
	private SyncLazySingleton() {
	}
	
	public synchronized static SyncLazySingleton getInstance() {
		if (singleton == null)
			singleton = new SyncLazySingleton();
		return singleton;
	}
}
