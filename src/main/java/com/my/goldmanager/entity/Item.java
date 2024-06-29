package com.my.goldmanager.entity;

public interface Item {

	void setId(String id);
	String getId();
	String getItemName();

	String getType();

	float getSize();
	float getPrice();

}
