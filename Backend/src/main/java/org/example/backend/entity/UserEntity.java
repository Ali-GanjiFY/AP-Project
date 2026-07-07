package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.RoleEnum;
import org.example.backend.enums.UserStatusEnum;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatusEnum status;



    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<AdvertisementEntity> advertisements;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FavoriteEntity> favorites;

    // Constructors
    public UserEntity() {}

    public UserEntity(String fullName, String username, String password, String phone, String email) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.role = RoleEnum.NORMAL_USER;
        this.status = UserStatusEnum.ACTIVE;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public RoleEnum getRole() {
        return role;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public List<AdvertisementEntity> getAdvertisements() {
        return advertisements;
    }

    public List<FavoriteEntity> getFavorites() {
        return favorites;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }

    public void setAdvertisements(List<AdvertisementEntity> advertisements) {
        this.advertisements = advertisements;
    }

    public void setFavorites(List<FavoriteEntity> favorites) {
        this.favorites = favorites;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
