package com.my.goldmanager.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository repository;

	public UUID create(Item item) {
		repository.store(item);
		return item.getId();
	}

	public List<Item> list() {
		return repository.list();
	}
	
	
	public float calculatePrice(UUID id) {
		Item item = repository.get(id);
		if (item != null) {
			return item.getAmount_oz() * item.getItemType().getMaterial().getPrice();
		}
		return 0.0f;
	}

}
