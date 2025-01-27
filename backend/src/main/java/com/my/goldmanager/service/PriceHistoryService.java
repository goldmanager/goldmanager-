/** Copyright 2025 fg12111

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
 * 
 */
package com.my.goldmanager.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.entity.MaterialHistory;
import com.my.goldmanager.repository.ItemRepository;
import com.my.goldmanager.repository.MaterialHistoryRepository;
import com.my.goldmanager.repository.MaterialRepository;
import com.my.goldmanager.rest.entity.Price;
import com.my.goldmanager.rest.entity.PriceHistory;
import com.my.goldmanager.rest.entity.PriceHistoryList;
import com.my.goldmanager.rest.entity.PriceList;
import com.my.goldmanager.service.util.PriceCalculatorUtil;

@Service

public class PriceHistoryService {

	@Autowired
	private MaterialRepository materialRepository;
	@Autowired
	private MaterialHistoryRepository materialHistoryRepository;
	@Autowired
	private ItemRepository itemRepository;

	public Optional<PriceHistoryList> listAllForMaterial(String materialId, Date startDate, Date endDate) {
		if (!materialRepository.existsById(materialId)) {
			return Optional.empty();
		}
		List<Item> items = itemRepository.findByMaterialId(materialId);
		PriceHistoryList result = new PriceHistoryList();
		result.setPriceHistories(new LinkedList<>());

		List<MaterialHistory> historyList = getMaterialHistory(materialId, startDate, endDate);
		for (MaterialHistory history : historyList) {

			PriceHistory priceHistory = new PriceHistory();
			priceHistory.setMaterialPrice(history.getPrice());
			priceHistory.setMaterialHistoryId(history.getId());
			priceHistory.setDate(history.getEntryDate());
			PriceList priceList = new PriceList();
			priceHistory.setPriceList(priceList);
			result.getPriceHistories().add(priceHistory);
			priceList.setPrices(new LinkedList<>());
			if (!items.isEmpty()) {
				for (Item item : items) {
					Price price = new Price();
					price.setItem(item);
					price.setPrice(PriceCalculatorUtil.calculateSingleItemPrice(item, history.getPrice()));
					price.setPriceTotal(PriceCalculatorUtil.calculateTotalItemPrice(item, history.getPrice()));
					priceList.getPrices().add(price);
					priceList.setTotalPrice(priceList.getTotalPrice() + price.getPriceTotal());
				}
				priceList.setTotalPrice(
						new BigDecimal(priceList.getTotalPrice()).setScale(2, RoundingMode.HALF_DOWN).floatValue());
			}
		}
		return Optional.of(result);
	}

	private List<MaterialHistory> getMaterialHistory(String materialId, Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			return materialHistoryRepository.findByMaterialInRange(materialId, startDate, endDate);
		}
		if (startDate != null && endDate == null) {
			return materialHistoryRepository.findByMaterialStartAt(materialId, startDate);
		}
		if (startDate == null && endDate != null) {
			return materialHistoryRepository.findByMaterialEndAt(materialId, endDate);
		}
		return materialHistoryRepository.findByMaterial(materialId);

	}
}
