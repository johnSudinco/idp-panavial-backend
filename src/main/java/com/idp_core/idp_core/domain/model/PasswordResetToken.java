package com.idp_core.idp_core.domain.model;

import java.time.LocalDateTime;

public class PasswordResetToken {

    private Long id;
    private Long userId;
    private String tokenHash;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;

    public PasswordResetToken(
            Long userId,
            String tokenHash,
            LocalDateTime expiresAt
    ) {
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }

    /* ======================
       GETTERS
       ====================== */

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    /* ======================
       DOMINIO
       ====================== */

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public void markAsUsed() {
        this.usedAt = LocalDateTime.now();
    }

    /* ======================
       SETTERS CONTROLADOS
       ====================== */

    public void setId(Long id) {
        this.id = id;
    }
    public void restoreUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

}
