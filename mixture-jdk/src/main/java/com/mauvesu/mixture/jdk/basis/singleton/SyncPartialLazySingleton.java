package com.mauvesu.mixture.jdk.basis.singleton;

/**
 * Lazy Singleton in sync module, using synchronized inside if statement
 * 
 * @author mauvesu
 *
 */
public class SyncPartialLazySingleton extends BaseSingleton {
	
	private static SyncPartialLazySingleton singleton = null;
	
	private SyncPartialLazySingleton() {
	}
	
	public static SyncPartialLazySingleton getInstance() {
		if (singleton == null) {
			synchronized (SyncPartialLazySingleton.class) {
				if (singleton == null) {
					singleton = new SyncPartialLazySingleton();
				}
			}
		}
		return singleton;
	}

}
