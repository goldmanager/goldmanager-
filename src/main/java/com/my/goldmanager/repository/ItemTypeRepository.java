package com.my.goldmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.goldmanager.entity.ItemType;

public interface ItemTypeRepository extends JpaRepository<ItemType, String>{

}
