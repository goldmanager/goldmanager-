package com.my.goldmanager.service;

import java.util.List;
import java.util.Optional;

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

	public Optional<Item> update(String id, Item item) {
		if (repository.existsById(id)) {
			item.setId(id);
			return Optional.of(repository.save(item));
		}
		return Optional.empty();
	}

	public Optional<Item> getById(String id) {

		return repository.findById(id);
	}

	public boolean delete(String id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}

}
