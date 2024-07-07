package com.my.goldmanager.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.my.goldmanager.service.exception.ValidationException;

@RestController
@RequestMapping("/materials")
public class MaterialController {
	private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);
	@Autowired
	private MaterialService materialService;

	@PostMapping
	public ResponseEntity<Material> create(@RequestBody Material material) {
		Material savedmaterial = materialService.store(material);
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
	public ResponseEntity<Material> update(@PathVariable(name = "id") String id, @RequestBody Material material) {
		try {
			Optional<Material> result = materialService.update(id, material);
			if (result.isPresent()) {
				return ResponseEntity.ok(result.get());
			}
			return ResponseEntity.notFound().build();
		} catch (ValidationException ve) {
			logger.error("Validation error during updating Material", ve);
			return ResponseEntity.badRequest().header("fault", ve.getMessage()).build();
		}
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
