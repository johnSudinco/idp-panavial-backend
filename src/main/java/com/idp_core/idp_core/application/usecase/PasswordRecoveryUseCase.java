package com.idp_core.idp_core.application.usecase;

import com.idp_core.idp_core.domain.model.PasswordResetToken;
import com.idp_core.idp_core.domain.model.User;
import com.idp_core.idp_core.domain.port.external.EmailServicePort;
import com.idp_core.idp_core.domain.port.repository.PasswordResetTokenRepositoryPort;
import com.idp_core.idp_core.domain.port.repository.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordRecoveryUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordResetTokenRepositoryPort tokenRepository;
    private final EmailServicePort emailServicePort;
    private final PasswordEncoder passwordEncoder;

    public PasswordRecoveryUseCase(
            UserRepositoryPort userRepository,
            PasswordResetTokenRepositoryPort tokenRepository,
            EmailServicePort emailServicePort,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailServicePort = emailServicePort;
        this.passwordEncoder = passwordEncoder;
    }

    /* ======================
       FORGOT PASSWORD
       ====================== */
    public void generateRecoveryToken(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            //  Generar token plano
            String rawToken = UUID.randomUUID().toString();

            //  Hashear token
            String tokenHash = passwordEncoder.encode(rawToken);

            //  Crear token de dominio
            PasswordResetToken resetToken = new PasswordResetToken(
                    user.getId(),
                    tokenHash,
                    LocalDateTime.now().plusMinutes(15)
            );

            tokenRepository.save(resetToken);

            // Enviar link con token PLANO
            String resetLink =
                    "https://idp-panavial-backend.com/auth/reset-password?token=" + rawToken;

            emailServicePort.sendPasswordResetEmail(user.getEmail(), resetLink);
        });

        //  Si el email no existe → NO pasa nada
    }

    /* ======================
       RESET PASSWORD
       ====================== */
    public void resetPassword(String rawToken, String newPassword) {

        PasswordResetToken resetToken =
                tokenRepository.findActiveToken()
                        .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token expirado");
        }

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token ya utilizado");
        }

        // COMPARACIÓN SEGURA
        if (!passwordEncoder.matches(rawToken, resetToken.getTokenHash())) {
            throw new RuntimeException("Token inválido");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.markAsUsed();
        tokenRepository.save(resetToken);
    }
}
