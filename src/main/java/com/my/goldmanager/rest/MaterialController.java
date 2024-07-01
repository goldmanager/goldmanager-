package com.my.goldmanager.rest;

import java.util.List;

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

import com.my.goldmanager.entity.Material;
import com.my.goldmanager.service.MaterialService;

@RestController
@RequestMapping("/materials")
public class MaterialController {

	@Autowired
	private MaterialService materialService;

	@PostMapping
	public ResponseEntity<Material> create(@RequestBody Material material) {
		Material savedmaterial = materialService.save(material);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedmaterial);
	}

	@GetMapping
	public List<Material> list() {
		return materialService.list();
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Material> getbyId(@PathVariable(name = "id") String id) {
		Material result = materialService.getById(id);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		}
		return ResponseEntity.notFound().build();

	}
	@PutMapping(path = "/{id}")
	public ResponseEntity<Material> update(@PathVariable(name = "id") String id, @RequestBody Material material){
		Material result = materialService.getById(id);
		if (result != null) {
			result.setName(material.getName());
			result.setPrice(material.getPrice());
			materialService.save(result);
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.notFound().build();
	}
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
		if (materialService.deleteById(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
