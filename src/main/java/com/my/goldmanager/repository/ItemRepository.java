package com.my.goldmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.my.goldmanager.entity.Item;

public interface ItemRepository extends JpaRepository<Item, String> {

	@Query(value = "Select item FROM Item item JOIN item.itemType it JOIN it.material m WHERE m.id = :materialId")
	List<Item> findByMaterialId(@Param("materialId") String materialId);

}
