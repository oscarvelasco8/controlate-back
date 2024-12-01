package controlate_back.api.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_information")
public class User {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String lastname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column
    private Integer age;

    @Column
    private Float weight;

    @Column
    private Float height;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_factor", nullable = false, length = 50)
    private FactorActividad activityFactor;

    @Column(name = "insulina_factor")
    private Float insulinaFactor;

    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    public enum FactorActividad {
        POCO_SEDENTARIO,
        SEDENTARIO,
        MODERADAMENTE_SEDENTARIO,
        ACTIVO,
        MUY_ACTIVO
    }
}

