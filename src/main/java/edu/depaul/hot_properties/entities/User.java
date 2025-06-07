package edu.depaul.hot_properties.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")  // avoid using "user" — it's a reserved keyword in many SQL dialects
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)  // EAGER fetch to load roles during login
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Each user has a manager
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    // Each manager has a list of users they manage
    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private List<User> subordinates = new ArrayList<>();

    // agent

    @Column()
    private String profilePicture; // stores filename or relative path

    // --- Constructors ---
    public User() {}

    public User(String username, String password, String firstName, String lastName,
                String email, Set<Role> roles, String profilePicture) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public Set<Role> getRoles() {
        return roles;
    }
    // 🫡 there will be only one role
    public String getRoleName() {
        String res = "";
        for (Role role : roles) {
            res = role.getName();
        }
        return res;
    }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<User> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<User> subordinates) {
        this.subordinates = subordinates;
    }

    public void addEmployee(User u1) {
        this.subordinates.add(u1);
        u1.setManager(this);
    }
}

