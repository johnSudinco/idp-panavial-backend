package com.idp_core.idp_core.application.usecase;

import com.idp_core.idp_core.domain.model.Role;
import com.idp_core.idp_core.domain.model.User;
import com.idp_core.idp_core.domain.port.repository.RoleRepositoryPort;
import com.idp_core.idp_core.domain.port.repository.UserRepositoryPort;
import com.idp_core.idp_core.application.dto.RegisterRequest;
import com.idp_core.idp_core.application.dto.RegisterResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(
            UserRepositoryPort userRepository,
            RoleRepositoryPort roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse execute(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username ya Existe");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email ya Existe");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                hashedPassword,
                "ACTIVE",
                request.getTwoFactor(),
                request.getName(),
                request.getLastname(),
                request.getIdentification(),
                request.getPhone()
        );

        //  ASIGNACIÓN AUTOMÁTICA DE ROL
        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() ->
                        new IllegalStateException("Rol USER no existe"));

        user.addRole(defaultRole);
        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),          // ahora sí tienes el userId
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getLastname(),
                savedUser.getIdentification(),
                savedUser.getPhone(),
                "Usuario Registrado Satisfactoriamente"
        );
    }
}

