package edu.depaul.hot_properties.initializers;

import edu.depaul.hot_properties.entities.Role;
import edu.depaul.hot_properties.entities.User;
import edu.depaul.hot_properties.repositories.RoleRepository;
import edu.depaul.hot_properties.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0 && roleRepository.count() == 0) {
            Role roleBuyer = new Role("ROLE_BUYER");
            Role roleAgent = new Role("ROLE_AGENT");
            Role roleAdmin = new Role("ROLE_ADMIN");

            roleRepository.save(roleBuyer);
            roleRepository.save(roleAgent);
            roleRepository.save(roleAdmin);

            User u1 = new User("mason.lee",
                    passwordEncoder.encode("ml.123"),
                    "Mason",
                    "Lee",
                    "mason.lee@email.com",
                    Set.of(roleBuyer),
                    "image1.jpg");

            User u2 = new User("olivia.bennett",
                    passwordEncoder.encode("ob.123"),
                    "Olivia",
                    "Bennett",
                    "olivia.bennett@email.com",
                    Set.of(roleBuyer),
                    "image2.jpg");

            User u3 = new User("ava.thompson",
                    passwordEncoder.encode("at.123"),
                    "Ava",
                    "Thompson",
                    "ava.thompson@email.com",
                    Set.of(roleBuyer),
                    "image3.jpg");

            User u4 = new User("liam.ramirez",
                    passwordEncoder.encode("lr.123"),
                    "Liam",
                    "Ramirez",
                    "liam.ramirez@email.com",
                    Set.of(roleBuyer),
                    "image4.jpg");

            User u5 = new User("sophia.nguyen",
                    passwordEncoder.encode("sn.123"),
                    "Sophia",
                    "Nguyen",
                    "sophia.nguyen@email.com",
                    Set.of(roleBuyer),
                    "image5.jpg");

            User u6 = new User("noah.patel",
                    passwordEncoder.encode("np.123"),
                    "Noah",
                    "Patel",
                    "noah.patel@email.com",
                    Set.of(roleBuyer),
                    "image6.jpg");

            User u7 = new User("isabella.carter",
                    passwordEncoder.encode("ic.123"),
                    "Isabella",
                    "Carter",
                    "isabella.carter@email.com",
                    Set.of(roleAgent),
                    "image7.jpg");

            User u8 = new User("ethan.brooks",
                    passwordEncoder.encode("eb.123"),
                    "Ethan",
                    "Brooks",
                    "ethan.brooks@email.com",
                    Set.of(roleAgent),
                    "image8.jpg");

            User u9 = new User("grace.mitchell",
                    passwordEncoder.encode("gm.123"),
                    "Grace",
                    "Mitchell",
                    "grace.mitchell@email.com",
                    Set.of(roleAgent),
                    "image9.jpg");

            User u10 = new User("henry.wallace",
                    passwordEncoder.encode("hw.123"),
                    "Henry",
                    "Wallace",
                    "henry.wallace@email.com",
                    Set.of(roleAdmin),
                    "image10.jpg");

            userRepository.saveAll(List.of(u1, u2, u3, u4, u5, u6, u7, u8, u9, u10));

            System.out.println("🟢 Initial users and updated roles inserted.");
        } else {
            System.out.println("🟡 Users and roles already exist, skipping initialization.");
        }
    }
}
