package com.my.goldmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository repository;

	public Item create(Item item) {
		return repository.save(item);
		
	}

	public List<Item> list() {
		return repository.findAll();
	}
	
	

}
