package com.my.goldmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.my.goldmanager.entity.ItemStorage;

public interface ItemStorageRepository extends JpaRepository<ItemStorage, String> {

	@Query(value = "Select count(*) >0 as RESULT FROM ItemStorage s WHERE s.name = :name")
	boolean existsByName(@Param("name") String name);
}
