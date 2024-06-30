package com.my.goldmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.my.goldmanager.entity.Item;
import com.my.goldmanager.entity.ItemType;
import com.my.goldmanager.entity.Material;
import com.my.goldmanager.repository.ItemRepository;
import com.my.goldmanager.rest.entity.Price;
import com.my.goldmanager.rest.entity.PriceList;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

	static class TestData {
		private final List<Item> items;

		public TestData(List<Item> items) {
			this.items = items;
		}

	}

	@Mock
	private ItemRepository itemRepository;
	@InjectMocks
	private PriceService service;

	@ParameterizedTest
	@MethodSource("generateTestData")
	void testListAll(TestData testData) {
		when(itemRepository.findAll()).thenReturn(testData.items);
		PriceList result = service.listAll();
		assertNotNull(result);
		assertEquals(testData.items.size(), result.getPrices().size());
		assertEquals(calculateTotalPrize(testData.items), result.getTotalPrize());

		for (Item item : testData.items) {
			Price resultPrice = result.getPrices().stream()
					.filter(p -> p.getItem() != null && p.getItem().getId().equals(item.getId())).findFirst()
					.orElseGet(null);
			assertNotNull(resultPrice);
			assertEquals(item.getName(), resultPrice.getItem().getName());
			assertEquals(calculatePrize(item), resultPrice.getPrice());
			assertEquals(calculatePrize(item), calculatePrize(resultPrice.getItem()));

		}
	}

	private static float calculatePrize(Item item) {
		return item.getAmount_oz() * item.getItemType().getMaterial().getPrice();
	}

	private static float calculateTotalPrize(List<Item> items) {
		float result = 0;
		for (Item item : items) {
			result += calculatePrize(item);
		}
		return result;
	}

	static Stream<TestData> generateTestData() {
		List<Item> items = new LinkedList<>();

		Material gold = new Material();
		gold.setId(UUID.randomUUID().toString());
		gold.setName("Gold");
		gold.setPrice(2000.50f);

		ItemType kr = new ItemType();
		kr.setId(UUID.randomUUID().toString());
		kr.setName("Kruegerrand");
		kr.setMaterial(gold);

		Item kr1 = new Item();
		kr1.setName("1 OZ Kruegerrand");
		kr1.setId(UUID.randomUUID().toString());
		kr1.setAmount_oz(1f);
		kr1.setItemType(kr);
		items.add(kr1);

		Item kr2 = new Item();
		kr2.setId(UUID.randomUUID().toString());
		kr2.setName("1/2 OZ Kruegerrand");
		kr2.setAmount_oz(0.5f);
		kr2.setItemType(kr);

		items.add(kr2);

		return Stream.of(new TestData(items), new TestData(List.of()));

	}

}
