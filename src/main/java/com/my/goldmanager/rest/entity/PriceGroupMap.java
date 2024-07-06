package com.my.goldmanager.rest.entity;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class PriceGroupMap {

	@Getter
	@Setter
	private Map<String,PriceGroup> priceGroups;
}
