
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

import com.my.goldmanager.entity.ItemType;
import com.my.goldmanager.service.ItemTypeService;

@RestController()
@RequestMapping("/itemTypes")
public class ItemTypeController {

	@Autowired
	private ItemTypeService itemTypeService;

	@PostMapping
	public ResponseEntity<ItemType> create(@RequestBody ItemType itemType) {
		ItemType savedItemType =itemTypeService.create(itemType); 
		return ResponseEntity.status(HttpStatus.CREATED).body(savedItemType);
		
	}

	@GetMapping
	public List<ItemType> list() {
		return itemTypeService.list();
	}

}
