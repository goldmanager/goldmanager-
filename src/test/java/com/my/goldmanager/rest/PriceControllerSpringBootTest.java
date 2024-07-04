package com.my.goldmanager.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.goldmanager.entity.Item;
import com.my.goldmanager.entity.ItemType;
import com.my.goldmanager.entity.Material;
import com.my.goldmanager.entity.Unit;
import com.my.goldmanager.repository.ItemRepository;
import com.my.goldmanager.repository.ItemTypeRepository;
import com.my.goldmanager.repository.MaterialRepository;
import com.my.goldmanager.repository.UnitRepository;
import com.my.goldmanager.rest.entity.Price;
import com.my.goldmanager.rest.entity.PriceList;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PriceControllerSpringBootTest {

	@Autowired
	private ItemTypeRepository itemTypeRepository;
	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Material gold;
	private Material silver;

	private Unit oz;
	private Unit gramm;

	List<Item> items;

	@BeforeEach
	public void setUp() {
		Material m = new Material();
		m.setName("Gold");
		m.setPrice(2000);
		gold = materialRepository.save(m);

		m = new Material();
		m.setName("Silver");
		m.setPrice(30);

		silver = materialRepository.save(m);

		oz = new Unit();
		oz.setFactor(1.0f);
		oz.setName("Oz");

		gramm = new Unit();
		gramm.setName("Gramm");
		gramm.setFactor(1.0f / 0.32f);

		unitRepository.save(oz);
		unitRepository.save(gramm);

		ItemType fineGold999 = new ItemType();
		fineGold999.setMaterial(gold);
		fineGold999.setModifier(1.0f);
		fineGold999.setName("Bar Finegold 0.999");
		fineGold999 = itemTypeRepository.save(fineGold999);

		ItemType gold585 = new ItemType();
		gold585.setMaterial(gold);
		gold585.setModifier(0.585f / 0.999f);
		gold585.setName("Bar Gold 0.585");
		gold585 = itemTypeRepository.save(gold585);

		ItemType fineSilver999 = new ItemType();
		fineSilver999.setMaterial(silver);
		fineSilver999.setModifier(1);
		fineSilver999.setName("FineSilver");

		fineSilver999 = itemTypeRepository.save(fineSilver999);

		items = new LinkedList<Item>();

		Item item = new Item();
		item.setName("1 OZ Bar");
		item.setItemType(fineGold999);
		item.setAmount(1);
		item.setUnit(oz);

		item = itemRepository.save(item);
		items.add(item);

		item = new Item();
		item.setName("1/2 OZ Bar");
		item.setItemType(fineGold999);
		item.setAmount(0.5f);
		item.setUnit(oz);
		item = itemRepository.save(item);
		items.add(item);

		item = new Item();
		item.setName("20g Bar");
		item.setItemType(fineGold999);
		item.setAmount(20);
		item.setUnit(gramm);
		item = itemRepository.save(item);
		items.add(item);

		item = new Item();
		item.setName("5g Bar");
		item.setItemType(gold585);
		item.setAmount(5);
		item.setUnit(gramm);
		item = itemRepository.save(item);
		items.add(item);

		item = new Item();
		item.setName("500g Bar");
		item.setItemType(fineSilver999);
		item.setAmount(500);
		item.setUnit(gramm);
		item = itemRepository.save(item);
		items.add(item);

	}

	@Test
	void testListAll() throws UnsupportedEncodingException, Exception {

		String body = mockMvc.perform(get("/prices")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getContentAsString();

		PriceList result = objectMapper.readValue(body, PriceList.class);
		assertNotNull(result);
		assertEquals(items.size(), result.getPrices().size());
		assertEquals(getPriceSummary(items), result.getTotalPrize());

		for (int current = 0; current < items.size(); current++) {
			Item expected = items.get(current);
			Price price = result.getPrices().get(current);
			assertEquals(getPrice(expected), price.getPrice());
			assertEquals(expected.getName(), price.getItem().getName());
			assertEquals(expected.getId(), price.getItem().getId());
		}

	}

	@Test
	void testlistPricesForMaterialGold() throws UnsupportedEncodingException, Exception {

		String body = mockMvc.perform(get("/prices/material/" + gold.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getContentAsString();

		PriceList result = objectMapper.readValue(body, PriceList.class);
		assertNotNull(result);

		List<Item> goldItems = items.stream()
				.filter(item -> item.getItemType().getMaterial().getId().equals(gold.getId())).toList();
		assertEquals(goldItems.size(), result.getPrices().size());
		assertEquals(getPriceSummary(goldItems), result.getTotalPrize());

		for (int current = 0; current < goldItems.size(); current++) {
			Item expected = goldItems.get(current);
			Price price = result.getPrices().get(current);
			assertEquals(getPrice(expected), price.getPrice());
			assertEquals(expected.getName(), price.getItem().getName());
			assertEquals(expected.getId(), price.getItem().getId());
		}

	}

	@Test
	void testlistPricesForMaterialSilver() throws UnsupportedEncodingException, Exception {

		String body = mockMvc.perform(get("/prices/material/" + silver.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getContentAsString();

		PriceList result = objectMapper.readValue(body, PriceList.class);
		assertNotNull(result);

		List<Item> goldItems = items.stream()
				.filter(item -> item.getItemType().getMaterial().getId().equals(silver.getId())).toList();
		assertEquals(goldItems.size(), result.getPrices().size());
		assertEquals(getPriceSummary(goldItems), result.getTotalPrize());

		for (int current = 0; current < goldItems.size(); current++) {
			Item expected = goldItems.get(current);
			Price price = result.getPrices().get(current);
			assertEquals(getPrice(expected), price.getPrice());
			assertEquals(expected.getName(), price.getItem().getName());
			assertEquals(expected.getId(), price.getItem().getId());
		}

	}

	@Test
	void testlistPricesForMaterialUnknown() throws UnsupportedEncodingException, Exception {

		mockMvc.perform(get("/prices/material/unknown")).andExpect(status().isNotFound());
	}

	@Test
	void testGetByItem() throws UnsupportedEncodingException, Exception {

		for (int current = 0; current < items.size(); current++) {
			Item expected = items.get(current);
			String body = mockMvc.perform(get("/prices/item/" + expected.getId())).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
					.getContentAsString();

			Price result = objectMapper.readValue(body, Price.class);
			assertNotNull(result);

			assertEquals(getPrice(expected), result.getPrice());
			assertEquals(expected.getName(), result.getItem().getName());
			assertEquals(expected.getId(), result.getItem().getId());
		}
		mockMvc.perform(get("/prices/item/notexistent")).andExpect(status().isNotFound());

	}

	private float getPriceSummary(List<Item> items) {
		float result = 0;
		for (Item item : items) {
			result += getPrice(item);
		}
		return result;
	}

	private float getPrice(Item item) {
		return item.getAmount() * item.getUnit().getFactor() * item.getItemType().getModifier()
				* item.getItemType().getMaterial().getPrice();
	}

	@AfterEach
	public void cleanUp() {
		itemRepository.deleteAll();
		itemTypeRepository.deleteAll();
		materialRepository.deleteAll();
		unitRepository.deleteAll();
		gold = null;
		silver = null;
		items.clear();
	}

}
