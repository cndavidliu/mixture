package com.mauvesu.mixture.jdk.basis;

public enum EnumSample {
	
	MATH("math", 5, 2.0f),
	CHINESE("chinese", 3, 1.5f),
	ENGLISH("english", 2, 3.5f);
	
	private String name;
	private int level;
	private float credit;
	
	private EnumSample(String name, int levvel, float credit) {
		this.name = name;
		this.level = levvel;
		this.credit = credit;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public float getCredit() {
		return this.credit;
	}
	
	@Override
	public String toString() {
		return String.format("subject[%s(%d)]'s credit is %f", this.name, this.level, this.credit);
	}
}
