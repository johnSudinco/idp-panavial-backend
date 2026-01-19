package com.idp_core.idp_core.infrastructure.adapter.repository;


import com.idp_core.idp_core.infrastructure.adapter.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaPasswordResetTokenRepository
        extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity>
    findFirstByUsedAtIsNullAndExpiresAtAfter(LocalDateTime now);

    void deleteByUserId(Long userId);
}

