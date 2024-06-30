package com.my.goldmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.goldmanager.entity.Material;

public interface MaterialRepository  extends JpaRepository<Material, String> {

}
