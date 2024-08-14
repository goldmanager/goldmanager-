/** Copyright 2024 fg12111

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
 * 
 */
package com.my.goldmanager.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ITEM")
public class Item {

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
	private float amount;

	@Setter
	@Getter
	@Column(name = "itemcount")
	private int itemCount =1;
	
	@ManyToOne
	@JoinColumn(name = "itemtype")
	@Setter
	@Getter
	private ItemType itemType;
	

	@ManyToOne
	@JoinColumn(name = "unit")
	@Setter
	@Getter
	private Unit unit;
	
	@ManyToOne
	@JoinColumn(name = "itemstorage")
	@Setter
	@Getter
	private ItemStorage itemStorage;

	@PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
