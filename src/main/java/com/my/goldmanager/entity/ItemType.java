package com.my.goldmanager.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name="itemtype")
public class ItemType {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	@Setter
	private UUID id;
	
	@Column
	@Getter
	@Setter
	private String name;
}
