package com.fan.framework.db;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName= "importantRequest")  
public class RequestImportant implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -351506327704710406L;

	public RequestImportant(int id, String params, boolean is, double a,
			byte b, char c) {
		super();
		this.id = id;
		this.params = params;
		this.is = is;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public RequestImportant(){
		
	}

	@DatabaseField(id= true) 
	private int id;

	@DatabaseField
	private String params;

	@DatabaseField
	private boolean is;
	
	@DatabaseField
	private double a;
	
	@DatabaseField
	private byte b;
	
	@DatabaseField
	private char c;
	
	
}
