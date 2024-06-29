package com.my.goldmanager.entity;

public abstract class GoldItem implements Item {

	private String id;
	private float price;
	private float size;
	private String name;

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public float getPrice() {

		return price;
	}

	@Override
	public float getSize() {

		return size;
	}

	@Override
	public String getItemName() {

		return name;
	}

	@Override
	public void setId(String id) {
		this.id = id;

	}

	@Override
	public String getId() {

		return id;
	}

}
