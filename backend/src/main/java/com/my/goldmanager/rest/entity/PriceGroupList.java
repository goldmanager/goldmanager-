package com.my.goldmanager.rest.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * List of PriceGroups
 */
public class PriceGroupList {

	@Getter
	@Setter
	private List<PriceGroup> priceGroups;
}
