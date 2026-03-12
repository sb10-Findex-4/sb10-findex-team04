package com.sprint.mission.findex.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@MappedSuperclass
@NoArgsConstructor
public class BaseEntitiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private Timestamp createdAt;
    @CreatedDate
    private Timestamp updatedAt;
}
