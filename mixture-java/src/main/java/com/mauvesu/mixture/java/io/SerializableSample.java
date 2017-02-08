package com.mauvesu.mixture.java.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableSample implements Serializable {

	private static final long serialVersionUID = -7959939228668487286L;
	
	private String name;
	private int id;
	
	public SerializableSample(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File(System.getProperty("user.home") + File.separator + "test.txt");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(new SerializableSample(0, "test"));
		out.writeObject(new SerializableSample(1, "test"));
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		SerializableSample sample = (SerializableSample)in.readObject();
		System.out.println(sample.id + " " + sample.name);
		sample = (SerializableSample)in.readObject();
		System.out.println(sample.id + " " + sample.name);
		out.close();
		in.close();
		file.delete();
	}

}
