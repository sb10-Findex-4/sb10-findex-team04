package com.sprint.mission.findex;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
public class BaseEntitiy {
    @Id
    private UUID id;
    @CreatedDate
    private Instant createdAt;
    @CreatedDate
    private Instant updatedAt;

    public BaseEntitiy() {
        this.id = UUID.randomUUID();
    }
}
