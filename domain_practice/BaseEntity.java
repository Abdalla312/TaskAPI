package domain_practice;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseEntity {
    private final String id;
    private final LocalDateTime createdAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
