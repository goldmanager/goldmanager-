package com.my.goldmanager.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.service.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@PostMapping
	public ResponseEntity<Item> create(@RequestBody Item item) {
		Item savedItem =itemService.create(item); 
		return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
		
	}
	
	@GetMapping
	public List<Item> list(){
		return itemService.list();
	}
	
	
}
