package com.my.goldmanager.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.rest.entity.Price;
import com.my.goldmanager.rest.entity.PriceGroupMap;
import com.my.goldmanager.rest.entity.PriceList;
import com.my.goldmanager.service.PriceService;

@RestController
@RequestMapping("/prices")
public class PriceController {

	@Autowired
	private PriceService priceService;

	@GetMapping
	public ResponseEntity<PriceList> listPrices() {

		PriceList priceList = priceService.listAll();
		return ResponseEntity.ok(priceList);
	}

	@GetMapping(path = "/item/{id}")
	public ResponseEntity<Price> getPriceForItem(@PathVariable("id") String itemId) {
		Optional<Price> result = priceService.getPriceofItem(itemId);
		if (result.isPresent()) {
			return ResponseEntity.ok(result.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(path = "/material/{id}")
	public ResponseEntity<PriceList> listPricesForMaterial(@PathVariable("id") String materialId) {
		Optional<PriceList> result = priceService.listForMaterial(materialId);
		if (result.isPresent()) {
			return ResponseEntity.ok(result.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(path = "/groupBy/material")
	public ResponseEntity<PriceGroupMap> groupByMaterial() {

		return ResponseEntity.ok(priceService.groupByMaterial());
	}
	@GetMapping(path = "/groupBy/itemType")
	public ResponseEntity<PriceGroupMap> groupByItemtype() {

		return ResponseEntity.ok(priceService.groupByItemType());
	}
}
