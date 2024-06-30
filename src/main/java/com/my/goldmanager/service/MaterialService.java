package com.my.goldmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.Material;
import com.my.goldmanager.repository.MaterialRepository;

@Service
public class MaterialService {

	@Autowired
	private MaterialRepository materialRepository;

	public void create(Material material) {
		materialRepository.store(material);
	}

	public List<Material> list() {
		return materialRepository.list();
	}
}
