package com.my.goldmanager.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.my.goldmanager.entity.Item;

@Repository
public class ItemRepository {
	private static final Map<String, Item> items = new ConcurrentHashMap<String, Item>();

	public Item create(Item item) {
		item.setId(UUID.randomUUID().toString());
		return items.put(item.getId(), item);
	}

	public Item get(String id) {
		return items.get(id);
	}

	public List<Item> list() {
		return new LinkedList<Item>(items.values());
	}

}
