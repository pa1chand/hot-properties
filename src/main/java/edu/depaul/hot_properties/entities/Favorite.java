package edu.depaul.hot_properties.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //• Many-to-one: user who favorited a property (User).
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;
    //• Many-to-one: the favorited property (Favorite).
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private LocalDateTime createdAt;




    public Favorite() {}

    public Favorite(Long id, User buyer, Property property, LocalDateTime createdAt) {
        this.id = id;
        this.buyer = buyer;
        this.property = property;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}