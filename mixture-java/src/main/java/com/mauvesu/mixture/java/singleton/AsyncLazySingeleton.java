package com.mauvesu.mixture.java.singleton;

/**
 * Lazy Singleton implement in async module, isn't thread safe.
 * 
 * @author mauvesu
 *
 */
public class AsyncLazySingeleton  extends BaseSingleton{
	
	private static AsyncLazySingeleton singeleton = null;
	
	public static AsyncLazySingeleton getInstance() {
		if (singeleton == null)
			singeleton = new AsyncLazySingeleton();
		return singeleton;
	}
}
