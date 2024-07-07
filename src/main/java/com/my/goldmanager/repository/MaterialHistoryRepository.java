package com.my.goldmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.my.goldmanager.entity.MaterialHistory;

public interface MaterialHistoryRepository extends JpaRepository<MaterialHistory, String> {
	@Query(value = "Select mh FROM MaterialHistory mh JOIN mh.material m  WHERE m.id = :materialId")
	List<MaterialHistory> findByMaterial(@Param("materialId") String materialId);
}
