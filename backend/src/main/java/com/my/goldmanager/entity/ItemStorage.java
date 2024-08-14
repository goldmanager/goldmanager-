package com.my.goldmanager.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ITEM_STORAGE")
public class ItemStorage {
	@Id	
	@Getter
	@Setter
	private String id;

	@Setter
	@Getter
	@Column
	private String name;

	@Setter
	@Getter
	@Column
	private String description;

	
	@PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
