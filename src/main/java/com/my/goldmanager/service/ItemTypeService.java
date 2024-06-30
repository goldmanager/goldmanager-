package com.my.goldmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.ItemType;
import com.my.goldmanager.repository.ItemTypeRepository;

@Service
public class ItemTypeService {

	@Autowired
	private ItemTypeRepository repository;

	public void create(ItemType itemType) {
		repository.store(itemType);
	}
	
	public List<ItemType> list(){
		return repository.list();
	}
}
