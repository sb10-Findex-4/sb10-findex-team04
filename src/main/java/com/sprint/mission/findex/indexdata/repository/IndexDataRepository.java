package com.sprint.mission.findex.indexdata.repository;

import com.sprint.mission.findex.indexdata.entity.IndexData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
    IndexData save(IndexData indexData);
    Optional<IndexData> findById(Long id);
    List<IndexData> findAll();
    void deleteById(Long id);
}
