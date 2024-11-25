package skuniv.munchmap.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@DynamicInsert
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Setter
    @Column(name = "login_id", columnDefinition = "varchar(32)")
    private String loginId;

    @Setter
    @Column(name = "password", columnDefinition = "varchar(255) NOT NULL")
    private String password;

    @Setter
    @Column(name = "name", columnDefinition = "varchar(32)")
    private String name;

    @Setter
    @Column(name = "email", columnDefinition = "varchar(32)")
    private String email;

    @Setter
    @Column(name = "address", columnDefinition = "varchar(64)")
    private String address;

    // 생성된 salt값 저장
    @Setter
    @Column(name = "salt", columnDefinition = "varchar(64)")
    private String salt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserFavorCategory> userFavorCategories = new ArrayList<>();

    @Builder
    public User(String loginId, String password, String name, String email, String address, String salt) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
        this.salt = salt;
    }

}
