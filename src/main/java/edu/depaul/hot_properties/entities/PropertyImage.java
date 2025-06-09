package edu.depaul.hot_properties.entities;

import jakarta.persistence.*;

@Entity
public class PropertyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the file path or URL to the image on the server/storage.
    private String filePath;

    // Many-to-One relationship with Property.
    // Many images can belong to one property.
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading for performance
    @JoinColumn(name = "property_id", nullable = false) // Foreign key column
    private Property property;

    // Default constructor is required by JPA
    public PropertyImage() {
    }

    public PropertyImage(String filePath, Property property) {
        this.filePath = filePath;
        this.property = property;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", property_id=" + (property != null ? property.getId() : "null") +
                '}';
    }
}
