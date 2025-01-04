/** Copyright 2025 fg12111

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
package com.my.goldmanager.service.dataexpimp;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zafarkhaja.semver.Version;
import com.my.goldmanager.repository.ItemRepository;
import com.my.goldmanager.repository.ItemStorageRepository;
import com.my.goldmanager.repository.ItemTypeRepository;
import com.my.goldmanager.repository.MaterialHistoryRepository;
import com.my.goldmanager.repository.MaterialRepository;
import com.my.goldmanager.repository.UnitRepository;
import com.my.goldmanager.repository.UserLoginRepository;
import com.my.goldmanager.service.AuthenticationService;
import com.my.goldmanager.service.VersionInfoService;
import com.my.goldmanager.service.entity.ExportData;
import com.my.goldmanager.service.entity.ExportEntities;
import com.my.goldmanager.service.exception.ImportDataException;
import com.my.goldmanager.service.exception.VersionLoadingException;

@Component
public class DataImporter {
	@Autowired
	private MaterialRepository materialRepository;
	@Autowired
	private MaterialHistoryRepository materialHistoryRepository;
	@Autowired
	private UnitRepository unitRepository;
	@Autowired
	private ItemStorageRepository itemStorageRepository;
	@Autowired
	private UserLoginRepository userLoginRepository;
	@Autowired
	private ItemTypeRepository itemTypeRepository;
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private VersionInfoService versionInfoService;

	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * Imports from provided data and revokes all authentication keys
	 * 
	 * @param data
	 */
	public void importData(ExportData data) throws ImportDataException {

		if (!Version.isValid(data.getVersion())) {
			throw new ImportDataException("ExportedData version is invalid.");
		}
		Version versionFromData = Version.parse(data.getVersion());
		try {
			if (!versionFromData.equals(versionInfoService.getVersion())) {
				throw new ImportDataException("Can only import data from the current version. (Expected "
						+ versionInfoService.getVersion().toString() + ", but got " + versionFromData.toString() + ")");
			}
		} catch (VersionLoadingException e) {
			throw new ImportDataException("Can not verify the version of imported data", e);
		}

		ExportEntities exportEntities = deserializeEntities(data);
		importEntities(exportEntities);
	}

	private void importEntities(ExportEntities exportEntities) {
		// First step, clear current entities:
		itemRepository.deleteAll();
		itemTypeRepository.deleteAll();
		itemStorageRepository.deleteAll();
		userLoginRepository.deleteAll();
		unitRepository.deleteAll();
		materialHistoryRepository.deleteAll();
		materialRepository.deleteAll();
		
		// Second Step logout all users
		authenticationService.logoutAll();

		// Third Step import entities
		userLoginRepository.saveAllAndFlush(exportEntities.getUsers());

		materialRepository.saveAllAndFlush(exportEntities.getMetals());
		unitRepository.saveAllAndFlush(exportEntities.getUnits());
		materialHistoryRepository.saveAllAndFlush(exportEntities.getMaterialHistories());
		itemStorageRepository.saveAllAndFlush(exportEntities.getItemStorages());
		itemTypeRepository.saveAllAndFlush(exportEntities.getItemTypes());
		itemRepository.saveAllAndFlush(exportEntities.getItems());

	}

	private ExportEntities deserializeEntities(ExportData data) throws ImportDataException {

		try {
			return objectMapper.readValue(data.getExportEntityData(), ExportEntities.class);
		} catch (IOException e) {
			throw new ImportDataException("Could not deserialize entities for import", e);
		}
	}
}
