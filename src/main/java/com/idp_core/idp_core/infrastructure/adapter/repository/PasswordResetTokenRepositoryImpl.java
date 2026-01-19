package com.idp_core.idp_core.infrastructure.adapter.repository;


import com.idp_core.idp_core.domain.model.PasswordResetToken;
import com.idp_core.idp_core.domain.port.repository.PasswordResetTokenRepositoryPort;
import com.idp_core.idp_core.infrastructure.adapter.entities.PasswordResetTokenEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepositoryImpl
        implements PasswordResetTokenRepositoryPort {

    private final JpaPasswordResetTokenRepository jpaRepository;

    public PasswordResetTokenRepositoryImpl(
            JpaPasswordResetTokenRepository jpaRepository
    ) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = toEntity(token);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<PasswordResetToken> findActiveToken() {
        return jpaRepository
                .findFirstByUsedAtIsNullAndExpiresAtAfter(LocalDateTime.now())
                .map(this::toDomain);
    }

    @Override
    public void invalidateTokensByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }

    /* ======================
       MAPPERS
       ====================== */

    private PasswordResetTokenEntity toEntity(PasswordResetToken token) {

        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();

        entity.setId(token.getId());
        entity.setUserId(token.getUserId());
        entity.setTokenHash(token.getTokenHash());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setUsedAt(token.getUsedAt());

        return entity;
    }

    private PasswordResetToken toDomain(PasswordResetTokenEntity entity) {

        PasswordResetToken token = new PasswordResetToken(
                entity.getUserId(),
                entity.getTokenHash(),
                entity.getExpiresAt()
        );

        token.setId(entity.getId());

        // restaurar estado
        if (entity.getUsedAt() != null) {
            token.restoreUsedAt(entity.getUsedAt());
        }

        return token;
    }
}


