package com.mauvesu.mixture.java.lang.reflect.proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class SimpleProxyImpl implements Serializable {

	private static final long serialVersionUID = 6498232712948023797L;
	private static final String FOLDER_PREFIX = "src/main/java";
	
	public static Object newProxyInstance(Class<?> target, InvocationHandler handler) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String content = getProxyContent(target);
		String path = String.format("%s/%s/%s.java", System.getProperty("user.dir"), FOLDER_PREFIX,
				getProxyName(target).replaceAll("\\.", "/"));
		String interfacePath = String.format("%s/%s/%s.java", System.getProperty("user.dir"), FOLDER_PREFIX,
				target.getName().replaceAll("\\.", "/"));
		File interfaceFile = new File(interfacePath);
		File javaFile = new File(path);
		Writer writer = new FileWriter(javaFile);
		writer.write(content);
		writer.close();
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null,null, null);
		Iterable<? extends JavaFileObject> iter = manager.getJavaFileObjects(interfaceFile, javaFile);
		CompilationTask task = compiler.getTask(null, manager, null, null, null, iter);
		task.call();
		manager.close();
		
		URL[] urls = new URL[]{new URL(String.format("file://%s/%s/", System.getProperty("user.dir"), FOLDER_PREFIX))};
		URLClassLoader loader = new URLClassLoader(urls);
		Class<?> c = Class.forName(getProxyName(target), false, loader);
		Constructor<?> constructor = c.getConstructor(InvocationHandler.class);
		Object instance = constructor.newInstance(handler);
		
		javaFile.delete();
		new File(path.substring(0, path.length() - 4) + "class").delete();
		new File(interfacePath.substring(0, interfacePath.length() - 4) + "class").delete();
		
		return instance;
	}
	
	private static String getProxyContent(Class<?> target) {
		Method[] methods = target.getMethods();
		StringBuilder builder = new StringBuilder(1024);
		builder.append("package com.mauvesu.mixture.java.lang.reflect.proxy;\n\n");
		builder.append("import java.lang.reflect.Method;\n");
		builder.append("import java.lang.reflect.InvocationHandler;\n");
		builder.append("import ").append(target.getName()).append(";\n\n");
		builder.append("public class ").append(getProxySimpleName(target)).append(" implements ")
			.append(target.getSimpleName()).append(" {\n\n");
		builder.append("\tprivate InvocationHandler h;\n\n");
		builder.append("\tpublic ").append(target.getSimpleName()).append("Proxy(InvocationHandler h) {\n");
		builder.append("\t\tthis.h = h;\n\t}\n\n");
		for (Method method : methods) {
			builder.append("\tpublic ").append(method.getReturnType().getSimpleName())
				.append(" ").append(method.getName()).append("(");
			for (int i = 0; i < method.getParameterCount(); i ++) {
				builder.append(method.getParameterTypes()[i].getSimpleName()).append(" ")
					.append(method.getParameterTypes()[i].getSimpleName().toLowerCase()).append("Var");
				if (i != method.getParameterCount() - 1)	
					builder.append(", ");
			}
			builder.append(") {\n");
			builder.append("\t\tObject[] args = new Object[").append(method.getParameters().length).append("];\n");
			for (int i = 0; i < method.getParameterTypes().length; i ++) {
				builder.append("\t\targs[").append(i).append("] = ").append(method.getParameterTypes()[i].getSimpleName().toLowerCase()).append("Var;\n");
			}
			builder.append("\t\ttry {\n");
			builder.append("\t\t\tMethod method = ").append(target.getSimpleName())
				.append(".class.getMethod(\"").append(method.getName()).append("\", ")
				.append("new Class<?>[]{");
			for (int i = 0; i < method.getParameterCount(); i ++) {
				builder.append(method.getParameterTypes()[i].getSimpleName()).append(".class");
				if (i == method.getParameterCount() - 1)
					builder.append(", ");
			}
			if (builder.charAt(builder.length() - 1) == ' ') 
				builder.delete(builder.length() - 2, builder.length());
			builder.append("});\n");
			builder.append("\t\t\tthis.h.invoke(null, method, args);\n");
			builder.append("\t\t} catch(Throwable e) {\n");
			if (!method.getReturnType().equals(void.class)) {
				builder.append("\t\t\treturn null;\n");
			}
			builder.append("\t\t}\n\t}\n\n");
		}
		builder.append("}");
		return builder.toString();
	}
	
	private static String getProxySimpleName(Class<?> target) {
		return target.getSimpleName() + "Proxy";
	}
	
	private static String getProxyName(Class<?> target) {
		return target.getName() + "Proxy";
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Sample sample = new ProxySample();
		Sample proxySample = (Sample)newProxyInstance(Sample.class, new Handler(sample));
		sample.hello("test");
		proxySample.hello("test");
	}
}