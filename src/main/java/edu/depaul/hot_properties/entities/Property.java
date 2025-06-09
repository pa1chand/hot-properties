package edu.depaul.hot_properties.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer size;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PropertyImage> images = new ArrayList<>();

    public Property() {}

    public Property(Long id, String title, Double price, String description, String location, Integer size) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.location = location;
        this.size = size;
    }

    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public Integer getSize() { return size; }

    public void setSize(Integer size) { this.size = size; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public List<PropertyImage> getImages() { return images; }

    public void setImages(List<PropertyImage> images) {
        this.images.clear();
        for (PropertyImage image : images) {
            addImage(image);
        }
    }


    public void addImage(PropertyImage image) {
        images.add(image);
        image.setProperty(this); // maintain bidirectional consistency
    }

    public void removeImage(PropertyImage image) {
        images.remove(image);
        image.setProperty(null);
    }
}


