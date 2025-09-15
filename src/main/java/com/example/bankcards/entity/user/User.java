package com.example.bankcards.entity.user;

import com.example.bankcards.entity.bankcard.BankCard;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * Сущность, представляющая пользователя системы.
 * <p>
 * Пользователь может иметь одну или несколько ролей
 * ({@link Role}), а также список банковских карт ({@link BankCard}),
 * которыми он владеет.
 * </p>
 *
 * <ul>
 *     <li>{@link #username} — уникальное имя пользователя;</li>
 *     <li>{@link #password} — пароль, хранящийся в зашифрованном виде;</li>
 *     <li>{@link #roles} — набор ролей, определяющих права доступа;</li>
 *     <li>{@link #bankCardList} — список карт, принадлежащих пользователю.</li>
 * </ul>
 *
 * По умолчанию каждому новому пользователю при создании присваивается роль {@link Role#USER}.
 *
 * @author ksenya
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** Уникальный идентификатор пользователя. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Уникальное имя пользователя. */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /** Пароль пользователя (хранится в зашифрованном виде). */
    @Column(name = "password", nullable = false)
    private String password;

    /** Роли пользователя (USER, ADMIN и т. д.). */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles;

    /** Банковские карты, принадлежащие пользователю. */
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "owner",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<BankCard> bankCardList = new java.util.ArrayList<>();

    /**
     * Устанавливает роль {@link Role#USER} по умолчанию,
     * если роли не заданы явно.
     */
    @PrePersist
    public void prePersist() {
        if (roles == null || roles.isEmpty()) {
            roles = Set.of(Role.USER);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Возвращает строковое представление пользователя
     * (без отображения пароля).
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
