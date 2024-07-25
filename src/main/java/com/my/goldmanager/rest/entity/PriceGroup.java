package com.my.goldmanager.rest.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PriceGroup {
	@Getter
	@Setter
	private List<Price> prices;
	@Getter
	@Setter
	private float totalPrice;
	@Getter
	@Setter
	private float amount;
	
}
