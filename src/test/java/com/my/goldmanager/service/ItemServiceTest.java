package com.my.goldmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

	public static class TestData {
		private final Item item;

		public TestData(Item item) {
			this.item = item;
		}
	}

	@Mock
	private ItemRepository itemRepository;
	@InjectMocks
	private ItemService itemService;

	@ParameterizedTest
	@MethodSource("generateTestData")
	void testCalculatePrice(TestData testData) {
		UUID id = null;
		float expected = 0.0f;
		if (testData.item != null) {
			id = testData.item.getId();
			expected = testData.item.getAmount_oz() * testData.item.getItemType().getMaterial().getPrice();
			when(itemRepository.get(testData.item.getId())).thenReturn(testData.item);
		} else {
			id = UUID.randomUUID();
			when(itemRepository.get(id)).thenReturn(null);
		}
		assertEquals(expected, itemService.calculatePrice(id));
	}
	
	

	static Stream<TestData> generateTestData() {
		Material gold = new Material();
		gold.setName("Gold");
		gold.setPrice(1000);
		gold.setId(UUID.randomUUID());

		ItemType typeKR = new ItemType();
		typeKR.setId(UUID.randomUUID());
		typeKR.setMaterial(gold);
		typeKR.setName("Kruegerrand");
		typeKR.setMaterial(gold);

		Item kr1 = new Item();
		kr1.setAmount_oz(0.5f);
		kr1.setName("KR 1/2 OZ");
		kr1.setId(UUID.randomUUID());
		kr1.setItemType(typeKR);

		Item kr2 = new Item();
		kr2.setAmount_oz(1.0f);
		kr2.setName("KR 1 OZ");
		kr2.setId(UUID.randomUUID());
		kr2.setItemType(typeKR);
		return Stream.of(new TestData(kr1), new TestData(kr2), new TestData(null));

	}
}
