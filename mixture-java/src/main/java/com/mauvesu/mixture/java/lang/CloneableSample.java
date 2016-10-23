package com.mauvesu.mixture.java.lang;

public class CloneableSample implements Cloneable{
	
	private String name;
	private Daughter daughter;
	
	public CloneableSample(String name, Daughter daughter) {
		this.name = name;
		this.daughter = daughter;
	}
	
	public static class Son implements Cloneable {
		private String name;
		private Daughter daughter;
		
		public Son(String name, Daughter daughter) {
			this.name = name;
			this.daughter = daughter;
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
		
		public String getName() {
			return this.name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setDaughter(Daughter daughter) {
			this.daughter = daughter;
		}
		
		public Daughter getDaughter() {
			return this.daughter;
		}
	}
	
	public static class Daughter implements Cloneable {
		private String name;
		
		public Daughter(String name) {
			this.name = name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new CloneableSample(this.name, (Daughter)daughter.clone());
	}
	
	public String getName() {
		return this.name;
	}
	
	public Daughter getDaughter() {
		return this.daughter;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDaughter(Daughter daughter) {
		this.daughter = daughter;
	}

}
