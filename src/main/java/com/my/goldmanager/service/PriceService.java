package com.my.goldmanager.service;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.repository.ItemRepository;
import com.my.goldmanager.rest.entity.Price;
import com.my.goldmanager.rest.entity.PriceList;

@Service
public class PriceService {

	@Autowired
	private ItemRepository itemRepository;

	public PriceList listAll() {

		PriceList result = new PriceList();
		result.setPrices(new LinkedList<>());
		itemRepository.findAll().stream().forEach(item -> result.getPrices().add(calculatePrice(item)));
		calculateSummaryPrice(result);
		return result;
	}

	private static void calculateSummaryPrice(PriceList priceList) {

		priceList.getPrices().stream().forEach(p -> priceList.setTotalPrize(priceList.getTotalPrize() + p.getPrice()));
	}

	private static Price calculatePrice(Item item) {
		Price result = new Price();
		if (item != null) {
			result.setItem(item);
			result.setPrice(item.getAmount()*item.getUnit().getFactor() * item.getItemType().getModifier()* item.getItemType().getMaterial().getPrice());
		}
		return result;
	}
}
