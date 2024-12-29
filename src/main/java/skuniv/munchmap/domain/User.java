package skuniv.munchmap.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="login_id", length = 32, nullable = false, unique = true)
    private String loginId;

    @Column(name="password", length = 255, nullable = false)
    private String password;

    @Column(name="name", length = 32, nullable = false)
    private String name;

    @Column(name="email", length = 64, nullable = false)
    private String email;

    @Column(name="favor", length = 32, nullable = false)
    private String favor;

    @Column(name="address", length = 64, nullable = false)
    private String address;

    @Column(name="sns_id", nullable = true)
    private int snsId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
