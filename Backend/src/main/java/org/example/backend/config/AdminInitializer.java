package org.example.backend.config;

import org.example.backend.entity.UserEntity;
import org.example.backend.enums.RoleEnum;
import org.example.backend.enums.UserStatusEnum;
import org.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Represents admin initializer.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Default admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123456";
    private static final String ADMIN_PHONE = "09120000000";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_FULLNAME = "Administrator";

    /**
     * Constructs a new AdminInitializer.
     * @param userRepository the user repository
     * @param passwordEncoder the password encoder
     */
    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs.
     * @param args the args
     */
    @Override
    public void run(String... args) {
        // Skip if admin already exists
        if (userRepository.existsByUsername(ADMIN_USERNAME)) {
            return;
        }

        // Create and configure admin user
        UserEntity admin = new UserEntity(
                ADMIN_FULLNAME,
                ADMIN_USERNAME,
                passwordEncoder.encode(ADMIN_PASSWORD),
                ADMIN_PHONE,
                ADMIN_EMAIL
        );
        admin.setRole(RoleEnum.ADMIN);
        admin.setStatus(UserStatusEnum.ACTIVE);

        // Persist admin account
        userRepository.save(admin);
        System.out.println("Default admin account created: " + ADMIN_USERNAME + " / " + ADMIN_PASSWORD);
    }
}