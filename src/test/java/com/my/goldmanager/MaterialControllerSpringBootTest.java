package com.my.goldmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.goldmanager.entity.Material;
import com.my.goldmanager.repository.MaterialRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MaterialControllerSpringBootTest {
	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@AfterEach
	public void cleanUp() {
		materialRepository.deleteAll();
	}

	@Test
	public void testList() throws Exception {
		Material gold = new Material();
		gold.setName("gold");
		gold.setPrice(100.1f);

		materialRepository.save(gold);

		Material silver = new Material();
		silver.setName("silver");
		silver.setPrice(50.1f);
		materialRepository.save(silver);

		String body = mockMvc.perform(get("/materials")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getContentAsString();

		JsonNode node = objectMapper.readTree(body);
		assertFalse(node.isEmpty());
		assertEquals(2, node.size());

		
		Material result = objectMapper.readValue(node.get(0).toString(), Material.class);
		assertNotNull(result);
		assertEquals(gold.getName(), result.getName());
		assertEquals(gold.getPrice(), result.getPrice());
		
		result = objectMapper.readValue(node.get(1).toString(), Material.class);
		assertNotNull(result);
		assertEquals(silver.getName(), result.getName());
		assertEquals(silver.getPrice(), result.getPrice());

	}

	@Test
	public void testCreate() throws JsonProcessingException, Exception {
		Material material = new Material();
		material.setName("gold");
		material.setPrice(100.1f);
		mockMvc.perform(post("/materials").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(material))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("gold")).andExpect(jsonPath("$.price").value(100.1f));
	}

	@Test
	public void testUpdate() throws JsonProcessingException, Exception {
		Material material = new Material();
		material.setName("gold");
		material.setPrice(100.1f);

		Material created = materialRepository.save(material);
		created.setPrice(200);

		mockMvc.perform(put("/materials/" + created.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(created))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("gold")).andExpect(jsonPath("$.price").value(200));

	}
}
