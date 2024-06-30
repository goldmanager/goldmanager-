package com.my.goldmanager.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.entity.Material;
import com.my.goldmanager.service.MaterialService;

@RestController("materials/")
public class MaterialController {

	@Autowired
	private MaterialService materialService;
	
	@PostMapping
	public void create(Material material) {
		materialService.create(material);
	}
	
	@GetMapping
	public List<Material> list(){
		return materialService.list();
	}
	
}
