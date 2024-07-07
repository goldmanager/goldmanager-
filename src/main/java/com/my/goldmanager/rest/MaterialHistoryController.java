package com.my.goldmanager.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.entity.MaterialHistory;
import com.my.goldmanager.service.MaterialHistoryService;

@RestController
@RequestMapping("/materialHistory")
public class MaterialHistoryController {

	@Autowired
	private MaterialHistoryService service;

	@GetMapping
	public List<MaterialHistory> list() {
		return service.getAll();
	}

	@GetMapping(path = "/byMaterial/{materialId}")
	public List<MaterialHistory> listByMaterial(@PathVariable("materialId") String materialId) {
		return service.findAllByMaterial(materialId);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<MaterialHistory> getById(@PathVariable(name = "id") String id) {
		Optional<MaterialHistory> result = service.getbyId(id);
		if (result.isPresent()) {
			return ResponseEntity.ok(result.get());
		}
		return ResponseEntity.notFound().build();

	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id) {
		if (service.deleteById(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping(path = "/byMaterial/{materialId}")
	public ResponseEntity<Void> deleteByMaterial(@PathVariable("materialId") String materialId) {
		 service.deleteAllByMaterial(materialId);
		 return ResponseEntity.noContent().build();
	}

}
