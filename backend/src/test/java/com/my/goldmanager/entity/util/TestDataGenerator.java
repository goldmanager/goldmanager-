package com.my.goldmanager.entity.util;

import java.security.SecureRandom;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.my.goldmanager.encoder.PasswordEncoderImpl;
import com.my.goldmanager.entity.Item;
import com.my.goldmanager.entity.ItemStorage;
import com.my.goldmanager.entity.ItemType;
import com.my.goldmanager.entity.Material;
import com.my.goldmanager.entity.MaterialHistory;
import com.my.goldmanager.entity.TestData;
import com.my.goldmanager.entity.Unit;
import com.my.goldmanager.entity.UserLogin;

/**
 * Generates testdata to be imported into the database during a spring boot
 * test. The generated testdata is ment to be committed in the test resources
 * directory
 */
public class TestDataGenerator {
	private static final PasswordEncoderImpl passwordEncoder = new PasswordEncoderImpl();
	private static final SecureRandom random = new SecureRandom();

	private TestDataGenerator() {
		// static class
	}

	public static TestData generateTestData() {
		List<UserLogin> userlogins = new LinkedList<UserLogin>();
		UserLogin user1 = new UserLogin();
		user1.setActive(true);
		user1.setPassword(passwordEncoder.encode("Test1245"));
		user1.setUserid("user1");
		userlogins.add(user1);

		UserLogin user2 = new UserLogin();
		user2.setActive(false);
		user2.setPassword(passwordEncoder.encode("Testagaghgha6677"));
		user2.setUserid("user2");
		userlogins.add(user2);

		List<Unit> units = new LinkedList<Unit>();
		Unit oz = new Unit();
		oz.setFactor(1);
		oz.setName("Oz");

		units.add(oz);

		Unit gramm = new Unit();
		gramm.setFactor(1.0f / 31.1034768f);
		gramm.setName("Gramm");
		units.add(gramm);

		List<Material> materials = new LinkedList<Material>();
		Material gold = new Material();
		gold.setName("Gold");
		gold.setEntryDate(new Date());
		gold.setPrice(5000f);
		gold.setId(UUID.randomUUID().toString());
		materials.add(gold);

		Material silver = new Material();
		silver.setEntryDate(new Date());
		silver.setPrice(500f);
		silver.setName("Silver");
		silver.setId(UUID.randomUUID().toString());
		materials.add(silver);

		Material platinum = new Material();
		platinum.setEntryDate(new Date());
		platinum.setPrice(4000f);
		platinum.setName("Platinum");
		platinum.setId(UUID.randomUUID().toString());
		materials.add(platinum);

		List<ItemStorage> itemStorages = new LinkedList<>();
		for (int i = 0; i < 100; i++) {
			ItemStorage itemStorage = new ItemStorage();
			itemStorage.setName("MyStorage " + i);
			if (i % 3 == 0) {
				itemStorage.setDescription(null);
			} else if (i % 7 == 0) {
				itemStorage.setDescription("");
			} else {
				itemStorage.setDescription("My Description " + i);
			}
			itemStorage.setId(UUID.randomUUID().toString());
			itemStorages.add(itemStorage);
		}

		List<ItemType> itemTypes = new LinkedList<ItemType>();

		ItemType goldBar = new ItemType();
		goldBar.setModifier(1.0f);
		goldBar.setMaterial(gold);
		goldBar.setName("GoldBar 999 fine gold");
		goldBar.setId(UUID.randomUUID().toString());
		itemTypes.add(goldBar);

		ItemType goldBar965 = new ItemType();
		goldBar965.setModifier(1.0f / 965f);
		goldBar965.setMaterial(gold);
		goldBar965.setName("GoldBar 965 gold");
		goldBar965.setId(UUID.randomUUID().toString());
		itemTypes.add(goldBar965);

		ItemType goldCoin = new ItemType();
		goldCoin.setModifier(1.0f);
		goldCoin.setMaterial(gold);
		goldCoin.setName("Cold Coin");

		goldCoin.setId(UUID.randomUUID().toString());
		itemTypes.add(goldCoin);

		ItemType silverBar = new ItemType();
		silverBar.setModifier(1.0f);
		silverBar.setMaterial(silver);
		silverBar.setName("SilverBar");

		silverBar.setId(UUID.randomUUID().toString());
		itemTypes.add(silverBar);
		List<Item> items = new LinkedList<>();

		// Bars
		List<Float> ozSizes = List.of(0.25f, 0.5f, 1f, 1.5f, 2.0f);
		List<Float> grammSizes = List.of(10f, 1f, 5f, 20f, 50f, 100f);

		// 999 GoldBars
		generateItems(oz, itemStorages, goldBar, items, ozSizes, 100);
		generateItems(gramm, itemStorages, goldBar, items, grammSizes, 100);

		// Silver Bars
		generateItems(gramm, itemStorages, silverBar, items, grammSizes, 150);
		generateItems(oz, itemStorages, silverBar, items, ozSizes, 15);

		// 965 GoldBars
		generateItems(oz, itemStorages, goldBar965, items, ozSizes, 100);
		generateItems(gramm, itemStorages, goldBar965, items, grammSizes, 100);

		// Gold coins
		ozSizes = List.of(0.125f, 0.25f, 0.5f, 1f);
		generateItems(oz, itemStorages, goldCoin, items, ozSizes, 50);

		int historySize = 3000;
		List<MaterialHistory> materialHistories = new LinkedList<>();
		for (Material m : materials) {
			int number = 0;
			while (number < historySize) {
				MaterialHistory mh = new MaterialHistory();
				mh.setEntryDate(new Date(m.getEntryDate().toInstant().toEpochMilli() - (number + 1) * 1000));
				mh.setPrice(m.getPrice() - number + 1);
				mh.setMaterial(m);
				mh.setId(UUID.randomUUID().toString());
				number++;
				materialHistories.add(mh);
			}
			historySize = historySize - 10;
		}

		TestData testData = new TestData();
		testData.setMetals(materials);
		testData.setMaterialHistories(materialHistories);
		testData.setUnits(units);
		testData.setUsers(userlogins);
		testData.setItemStorages(itemStorages);
		testData.setItemTypes(itemTypes);
		testData.setItems(items);

		return testData;
	}

	private static void generateItems(Unit unit, List<ItemStorage> itemStorages, ItemType itemType, List<Item> items,
			List<Float> sizes, int initialItemCount) {
		int current = 1;
		for (Float size : sizes) {

			int selectedStorage = random.nextInt(itemStorages.size() + 1);
			Item item = new Item();
			item.setId(UUID.randomUUID().toString());
			if (selectedStorage < itemStorages.size()) {
				item.setItemStorage(itemStorages.get(selectedStorage));
			}
			item.setItemType(itemType);
			item.setAmount(size);
			item.setName(itemType.getName() + " " + unit.getName());
			item.setUnit(unit);
			int itemCount = initialItemCount / current;
			if (itemCount < 1) {
				itemCount = 1;
			}
			item.setItemCount(itemCount);
			items.add(item);
		}
	}
}
