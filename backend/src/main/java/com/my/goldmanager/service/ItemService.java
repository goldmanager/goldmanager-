/** Copyright 2024 fg12111

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
 * 
 */
package com.my.goldmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.repository.ItemRepository;
import com.my.goldmanager.service.exception.ValidationException;

@Service
public class ItemService {

	@Autowired
	private ItemRepository repository;

	public Item create(Item item) throws ValidationException {
		if (item.getName() == null || item.getName().isBlank()) {
			throw new ValidationException("Item name is mandatory.");
		}
		try {
			return repository.save(item);
		} catch (DataIntegrityViolationException e) {

			throw new RuntimeException("An item with the same name already exists.", e);
		}
	}

	public List<Item> list() {
		return repository.findAll();
	}

	public Optional<Item> update(String id, Item item) throws ValidationException {
		if (repository.existsById(id)) {
			if (item.getName() == null || item.getName().isBlank()) {
				throw new ValidationException("Item name is mandatory.");
			}
			try {
				item.setId(id);
				return Optional.of(repository.save(item));
			} catch (DataIntegrityViolationException e) {
				throw new RuntimeException("An an item type with the same name already exists.", e);
			}
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
