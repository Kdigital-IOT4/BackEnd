package com.baro.domain.cocktail.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Base",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_name", columnNames = {"NAME"})
        },
        schema = "baro_db"
)


@Data
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;

    @Column(name = "NAME" , nullable = false , length = 40 , unique = true)
    private String name;  //en
    @Column(name = "KOREAN_NAME" , nullable = false , length = 30)
    private String krName;
    @Column(name = "PRICE" , nullable = false)
    private int price;
    @Column(name = "AMOUNT" , nullable = false)
    private int amount;
    @Column(name = "ALCOHOL" , nullable = false)
    private int alcohol;
    @Column(name = "FILE_URL" , nullable = false , length = 300)
    private String fileURL;

    @Column(name = "CONTENT" , nullable = false , length = 400)
    private String contentL;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;

}
