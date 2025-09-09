package com.example.bankcards.entity.user;

import com.example.bankcards.entity.bankcard.BankCard;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username" ,nullable = false, unique = true)
    private String username;

    @Column(name = "password" ,nullable = false)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "owner",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    List<BankCard> bankCartList;

    @PrePersist
    public void prePersist() {
        roles = Set.of(Role.USER);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}




