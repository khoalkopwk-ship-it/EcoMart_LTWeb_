package vn.iotstar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Category")
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c ORDER BY c.id")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private int id;

    @Column(name = "cate_name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "icons", length = 255)
    private String icon;

    @Column(name = "status", nullable = false)
    private int status = 1;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category() { }
    public Category(int id, String name, String icon) { this(id, name, icon, 1); }
    public Category(int id, String name, String icon, int status) {
        this.id = id; this.name = name; this.icon = icon; this.status = status;
    }
    public Category(String name, String icon) { this(name, icon, 1); }
    public Category(String name, String icon, int status) {
        this.name = name; this.icon = icon; this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
