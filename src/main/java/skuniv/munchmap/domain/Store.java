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
@Table(name = "Store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="storeName", length = 32, nullable = false)
    private String storeName;

    @Column(name = "storeAddress", length = 64, nullable = false)
    private String storeAddress;

    @Column(name = "storePhoneNumber", length = 32, nullable = false)
    private String storePhoneNumber;

    @Column(name = "storeScore", nullable = false)
    private int storeScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)  // 외래 키 매핑
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
