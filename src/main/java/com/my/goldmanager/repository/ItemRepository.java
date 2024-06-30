package com.my.goldmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.goldmanager.entity.Item;

public interface ItemRepository extends JpaRepository<Item, String>{

}
