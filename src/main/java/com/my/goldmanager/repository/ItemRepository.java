package com.my.goldmanager.repository;

import org.springframework.stereotype.Repository;

import com.my.goldmanager.entity.Item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ItemRepository extends AbstractRepository<Item> {


	@PersistenceContext
	private EntityManager entityManager;

	public ItemRepository() {
		super(Item.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}
