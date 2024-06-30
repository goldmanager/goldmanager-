package com.my.goldmanager.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.service.ItemService;

@RestController("items/")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@PostMapping
	public UUID create(Item item) {
		return itemService.create(item);
	}
	
	@GetMapping
	public List<Item> list(){
		return itemService.list();
	}
	
	
}
