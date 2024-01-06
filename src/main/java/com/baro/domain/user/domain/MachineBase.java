package com.baro.domain.user.domain;

import com.baro.domain.cocktail.domain.Base;
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
        name = "MachineBase",
        schema = "baro_db"
)
public class MachineBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "MACHINE_SEQ", nullable = false)
    private Machine machine;
    @ManyToOne
    @JoinColumn(name = "BASE_SEQ", nullable = false)
    private Base base;
    @Column(name = "LINE_NUMBER" , nullable = false)
    private int lineNumber;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;
}
