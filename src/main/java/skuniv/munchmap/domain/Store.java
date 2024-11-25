package skuniv.munchmap.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "store_name", columnDefinition = "varchar(32) NOT NULL")
    private String storeName;

    @Column(name = "store_address", columnDefinition = "varchar(64) NOT NULL")
    private String storeAddress;

    @Column(name = "store_phonenumber", columnDefinition = "varchar(32) NOT NULL")
    private String storePhoneNumber;

    @Column(name = "store_category", columnDefinition = "varchar(16) NOT NULL")
    private String storeCategory;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();
}
