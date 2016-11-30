package com.mauvesu.mixture.java.lang.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Sample {
	public void hello(String name);
}

public class ProxySample implements Sample {
	
	public void hello(String name) {
		System.out.println(String.format("Hello %s", name));
	}
	
	public static void main(String[] args) {
		Sample sample = new ProxySample();
		Sample proxySample = (Sample)Proxy.newProxyInstance(Sample.class.getClassLoader(), 
				new Class<?>[]{Sample.class}, new Handler(sample));
		proxySample.hello("test");
	}
}

class Handler implements InvocationHandler {
	
	private Object target;
	
	public Handler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(String.format("method %s begins at %d", method.getName(), System.currentTimeMillis()));
		method.invoke(this.target, args);
		System.out.println(String.format("method %s ends at %d", method.getName(), System.currentTimeMillis()));
		return null;
	}
	
}
