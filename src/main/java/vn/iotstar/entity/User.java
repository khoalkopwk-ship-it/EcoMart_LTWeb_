package vn.iotstar.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Column(nullable = false, unique = true, length = 255) private String email;
    @Column(nullable = false, unique = true, length = 100) private String username;
    @Column(nullable = false, length = 255) private String fullname;
    @Column(nullable = false, length = 512) private String password;
    @Column(nullable = false) private int status;
    @Column(name = "createdDate", nullable = false) private LocalDateTime createdDate;
    // Profile
    @Column(length = 20) private String phone;
    @Column(length = 255) private String images;

    public User() { }
    @PrePersist public void prePersist() { if (createdDate == null) createdDate = LocalDateTime.now(); }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    //Profile
    public String getPhone() { return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getImages() {return images;}
    public void setImages(String images) {this.images = images;}

}
