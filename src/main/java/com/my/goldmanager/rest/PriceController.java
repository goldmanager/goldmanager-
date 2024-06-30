package com.my.goldmanager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.goldmanager.rest.entity.PriceList;
import com.my.goldmanager.service.PriceService;

@RestController
@RequestMapping("/prices")
public class PriceController {

	@Autowired
	private PriceService priceService;
	
	@GetMapping
	public ResponseEntity<PriceList> listPrices(){
		
		PriceList priceList = priceService.listAll();
		return ResponseEntity.status(HttpStatus.OK).body(priceList);
	}
	
}
