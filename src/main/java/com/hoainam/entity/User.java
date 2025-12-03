package com.hoainam.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "Username", length = 50)
    private String username; // PK

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "Fullname", length = 100)
    private String fullname;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Admin")
    private Boolean admin; // true: admin, false: user thường

    @Column(name = "Active")
    private Boolean active;

    @Column(name = "Images")
    private String images;

    // 1 User có nhiều Category
    @OneToMany(mappedBy = "user")
    private List<Category> categories;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}