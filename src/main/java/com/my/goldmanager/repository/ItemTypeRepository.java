package com.my.goldmanager.repository;

import com.my.goldmanager.entity.ItemType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class ItemTypeRepository extends AbstractRepository<ItemType> {

	public ItemTypeRepository() {
		super(ItemType.class);

	}

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {

		return entityManager;
	}

}
