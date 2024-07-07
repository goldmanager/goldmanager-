package com.my.goldmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.MaterialHistory;
import com.my.goldmanager.repository.MaterialHistoryRepository;

@Service
public class MaterialHistoryService {

	@Autowired
	private MaterialHistoryRepository repository;

	public List<MaterialHistory> getAll() {
		return repository.findAll();
	}
	
	public Optional<MaterialHistory> getbyId(String id){
		return repository.findById(id);
	}

	public List<MaterialHistory> findAllByMaterial(String materialId) {
		return repository.findByMaterial(materialId);
	}

	public boolean deleteById(String id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			repository.flush();
			return true;
		}
		return false;
	}

	public void deleteAllByMaterial(String materialId) {
		repository.flush();
		repository.deleteAllInBatch(findAllByMaterial(materialId));
		repository.flush();
	}

}
