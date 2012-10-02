package com.eclipsesource.jaxrs.connector.example.ds.provider;

public class Product {

	private String Name;
	private String Description;

	public Product(String name, String description) {
		super();
		Name = name;
		Description = description;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	
}
