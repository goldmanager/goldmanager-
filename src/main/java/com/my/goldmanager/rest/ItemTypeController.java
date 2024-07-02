
package com.my.goldmanager.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	@PutMapping(path = "/{id}")
	public ResponseEntity<ItemType> update(@PathVariable(name = "id") String id, @RequestBody ItemType itemType){
		Optional<ItemType> result = itemTypeService.update(id, itemType);
		if(result.isPresent()) {
			return ResponseEntity.ok(result.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(path ="/{id}")
	public ResponseEntity<ItemType> get(@PathVariable(name = "id") String id){
		Optional<ItemType> result = itemTypeService.getById(id);
		if(result.isPresent()) {
			return ResponseEntity.ok(result.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") String id){
		if(itemTypeService.delete(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	@GetMapping
	public List<ItemType> list() {
		return itemTypeService.list();
	}

}
