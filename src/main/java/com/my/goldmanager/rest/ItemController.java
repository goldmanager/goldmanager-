package com.my.goldmanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.service.ItemService;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;

	@PutMapping
	public Item create(Item item) {
		return itemService.create(item);
	}
}
