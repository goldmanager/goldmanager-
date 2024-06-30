
package com.my.goldmanager.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.entity.ItemType;
import com.my.goldmanager.service.ItemTypeService;

@RestController
public class ItemTypeController {

	@Autowired
	private ItemTypeService itemTypeService;

	@PostMapping
	public void create(ItemType itemType) {
		itemTypeService.create(itemType);
	}

	public List<ItemType> list() {
		return itemTypeService.list();
	}

}
