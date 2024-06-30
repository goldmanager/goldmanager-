package com.my.goldmanager.repository;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;


public abstract class AbstractRepository<T> {

	private final Class<T> clazz;
	
	public AbstractRepository(Class<T> class1) {
	
		this.clazz = class1;
	}
	protected abstract EntityManager getEntityManager();
	public void store(T object) {
		getEntityManager().persist(object);
		getEntityManager().flush();
	}

	public T get(UUID id) {
		 return getEntityManager().find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> list() {
		return getEntityManager().createQuery("from "+clazz.getSimpleName()).getResultList();
	}
}
