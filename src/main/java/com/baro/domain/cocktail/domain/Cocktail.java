package com.baro.domain.cocktail.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Cocktail",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_name", columnNames = {"NAME"})
        },
        schema = "baro_db"
)


@Data
public class Cocktail {
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
    @Column(name = "ALCOHOL" , nullable = false)
    private int alcohol;
    @Column(name = "FILE_URL" , nullable = false , length = 300)
    private String fileURL;
}
