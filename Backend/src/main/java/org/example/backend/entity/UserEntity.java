package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enums.RoleEnum;
import org.example.backend.enums.UserStatusEnum;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents user entity.
 */
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

    /**
     * Constructs a new UserEntity.
     */
    public UserEntity() {}

    /**
     * Constructs a new UserEntity.
     * @param fullName the full name
     * @param username the username
     * @param password the password
     * @param phone the phone
     * @param email the email
     */
    public UserEntity(String fullName, String username, String password, String phone, String email) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.role = RoleEnum.NORMAL_USER;
        this.status = UserStatusEnum.ACTIVE;
    }

    /**
     * Gets id.
     * @return the result
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets full name.
     * @return the result
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets username.
     * @return the result
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets password.
     * @return the result
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets phone.
     * @return the result
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets email.
     * @return the result
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets role.
     * @return the result
     */
    public RoleEnum getRole() {
        return role;
    }

    /**
     * Gets status.
     * @return the result
     */
    public UserStatusEnum getStatus() {
        return status;
    }

    /**
     * Gets advertisements.
     * @return the result
     */
    public List<AdvertisementEntity> getAdvertisements() {
        return advertisements;
    }

    /**
     * Gets favorites.
     * @return the result
     */
    public List<FavoriteEntity> getFavorites() {
        return favorites;
    }

    /**
     * Gets created at.
     * @return the result
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets id.
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets full name.
     * @param fullName the full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Sets username.
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets password.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets phone.
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets email.
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = (email != null && email.trim().isEmpty()) ? null : email;
    }

    /**
     * Sets role.
     * @param role the role
     */
    public void setRole(RoleEnum role) {
        this.role = role;
    }

    /**
     * Sets status.
     * @param status the status
     */
    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }

    /**
     * Sets advertisements.
     * @param advertisements the advertisements
     */
    public void setAdvertisements(List<AdvertisementEntity> advertisements) {
        this.advertisements = advertisements;
    }

    /**
     * Sets favorites.
     * @param favorites the favorites
     */
    public void setFavorites(List<FavoriteEntity> favorites) {
        this.favorites = favorites;
    }

    /**
     * Sets created at.
     * @param createdAt the created at
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
