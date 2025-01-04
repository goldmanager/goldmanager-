package com.my.goldmanager.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class TestData {

	@Getter
	@Setter
	private List<Unit> units;

	@Getter
	@Setter
	private List<Material> metals;

	@Getter
	@Setter
	private List<UserLogin> users;

	@Getter
	@Setter
	private List<ItemStorage> itemStorages;

	@Getter
	@Setter
	private List<MaterialHistory> materialHistories;

	@Getter
	@Setter
	private List<ItemType> itemTypes;

	@Getter
	@Setter
	private List<Item> items;
}
