package com.my.goldmanager.rest.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PriceList {
	@Getter
	@Setter
	private List<Price> prices;
	
	@Getter
	@Setter
	private float totalPrize;
}
