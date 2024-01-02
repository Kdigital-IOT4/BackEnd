package com.baro.domain.user.domain;

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
        name = "BasicUser",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_PHONE_NUMBER", columnNames = {"PHONE_NUMBER"})
        },
        schema = "baro_db"
)


@Data
public class BasicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;

    @Column(name = "PHONE_NUMBER" , nullable = false , unique = true)
    private int phoneNumber;
    @Column(name = "PASSWORD" , nullable = false , length = 200)
    private String password;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;
}
