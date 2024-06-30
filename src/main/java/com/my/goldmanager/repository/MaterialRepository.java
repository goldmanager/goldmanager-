package com.my.goldmanager.repository;

import org.springframework.stereotype.Repository;

import com.my.goldmanager.entity.Material;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class MaterialRepository extends AbstractRepository<Material> {
	@PersistenceContext
	private EntityManager entityManager;

	public MaterialRepository() {
		super(Material.class);
		
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}
