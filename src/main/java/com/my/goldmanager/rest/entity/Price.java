package com.my.goldmanager.rest.entity;

import com.my.goldmanager.entity.Item;

import lombok.Getter;
import lombok.Setter;

public class Price {

	@Getter
	@Setter
	private Item item;
	
	@Getter
	@Setter
	private float price;
}
