package com.my.goldmanager.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Entity(name="itemtype")
public class ItemType {

	@Id
	@Getter
	@Setter
	private String id;
	
	@Column
	@Getter
	@Setter
	private String name;
	

	@ManyToOne
	@JoinColumn(name = "material")
	@Setter
	@Getter
	private Material material;
	
	@PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
