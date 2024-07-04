package com.my.goldmanager.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

		return createPriceList(itemRepository.findAll());

	}

	public Optional<PriceList> listForMaterial(String materialId) {

		List<Item> items = itemRepository.findAll().stream()
				.filter(item -> item.getItemType().getMaterial().getId().equals(materialId)).toList();

		if (items != null && !items.isEmpty()) {
			return Optional.of(createPriceList(items));
		}
		return Optional.empty();
	}

	private PriceList createPriceList(List<Item> items) {
		PriceList result = new PriceList();
		result.setPrices(new LinkedList<>());
		items.stream().forEach(item -> result.getPrices().add(calculatePrice(item)));
		calculateSummaryPrice(result);
		return result;
	}

	public Optional<Price> getPriceofItem(String itemId) {
		Optional<Item> optional = itemRepository.findById(itemId);
		if (optional.isPresent()) {
			return Optional.of(calculatePrice(optional.get()));
		}
		return Optional.empty();
	}

	private static void calculateSummaryPrice(PriceList priceList) {

		priceList.getPrices().stream().forEach(p -> priceList.setTotalPrize(priceList.getTotalPrize() + p.getPrice()));
	}

	private static Price calculatePrice(Item item) {
		Price result = new Price();
		if (item != null) {
			result.setItem(item);
			result.setPrice(item.getAmount() * item.getUnit().getFactor() * item.getItemType().getModifier()
					* item.getItemType().getMaterial().getPrice());
		}
		return result;
	}
}
