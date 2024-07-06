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
	private float totalPrize;
	@Getter
	@Setter
	private float amount;
	
}
