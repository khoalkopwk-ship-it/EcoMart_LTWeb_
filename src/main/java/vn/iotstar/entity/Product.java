package vn.iotstar.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Products")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") private int id;
    @Column(name = "product_name", nullable = false, length = 255) private String name;
    @Column(name = "price", nullable = false, precision = 18, scale = 2) private BigDecimal price;
    @Column(name = "description") private String description;
    @Column(name = "image", length = 255) private String image;
    @Column(name = "createdDate", nullable = false) private LocalDateTime createdDate;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cate_id", nullable = false) private Category category;

    public Product() { }
    @PrePersist public void prePersist() { if (createdDate == null) createdDate = LocalDateTime.now(); }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
