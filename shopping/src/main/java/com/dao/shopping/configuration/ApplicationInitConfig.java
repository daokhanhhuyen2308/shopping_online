package com.dao.shopping.configuration;

import com.dao.shopping.constant.DefaultRoles;
import com.dao.shopping.entity.RoleEntity;
import com.dao.shopping.entity.UserEntity;
import com.dao.shopping.repository.RoleRepository;
import com.dao.shopping.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@Component
@Slf4j
public class ApplicationInitConfig implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${admin.username}")
    private String ADMIN_USER_NAME;

    @Value("${admin.password}")
    private String ADMIN_USER_PASSWORD;

    public ApplicationInitConfig(
            UserRepository userRepository,
            @Lazy PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing ApplicationRunner...");

        if (userRepository.findUserByRoleId(DefaultRoles.ADMIN_ROLE).isEmpty()) {
            roleRepository.save(
                    RoleEntity.builder().name(DefaultRoles.USER_ROLE).description("User role").build());

            var roles = new HashSet<RoleEntity>();

            roles.add(RoleEntity.builder().name(DefaultRoles.ADMIN_ROLE).description("Admin role").build());

            UserEntity userEntity = new UserEntity();
                            userEntity.setUsername(ADMIN_USER_NAME);
                            userEntity.setEmail("admin@gmail.com");
                            userEntity.setPassword(passwordEncoder.encode(ADMIN_USER_PASSWORD));
                            userEntity.setRoles(roles);
                            userEntity.setDob(LocalDate.now());
                            userEntity.setCreatedBy("Administrator");
                            userEntity.setCreatedDate(LocalDateTime.now());
                            userEntity.setLastModifiedBy("Administrator");
                            userEntity.setLastModifiedDate(LocalDateTime.now());
            roleRepository.saveAll(roles);
            userRepository.save(userEntity);
            log.error("Admin has been created with default. Please change it!");
        }

    }
}
