package com.mbe.viapdv.config;

import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.model.user.User;
import com.mbe.viapdv.model.userRole.UserRole;
import com.mbe.viapdv.model.userRole.UserRoleId;
import com.mbe.viapdv.repository.IRoleRepository;
import com.mbe.viapdv.repository.IUserRepository;
import com.mbe.viapdv.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
public class AdminUserConfiguration implements CommandLineRunner {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    private IRoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfiguration(
            IRoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName("Admin");
        var userAdmin = userRepository.findByName("admin");

        roleAdmin.ifPresentOrElse(
                role -> {
                    System.out.println("Role alredy exist");
                },
                () -> {
                    Role role = new Role();
                    role.setName("Admin");
                    role.setDescription("INITIAL USER");
                    roleRepository.save(role);
                }
        );

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Admin alredy exist");
                },
                () -> {
                    var user = new User();
                    user.setName("admin");
                    user.setEmail("admin@email.com");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setActive(true);
                    user.setCreated(LocalDateTime.now());
                    if(userRepository.save(user) != null) {
                        UserRoleId userRoleID = new UserRoleId(user, roleAdmin.get());
                        UserRole newUserRole = new UserRole(userRoleID);
                        UserRole userRoleSaved = userRoleRepository.save(newUserRole);
                    }
                }
        );
    }
}