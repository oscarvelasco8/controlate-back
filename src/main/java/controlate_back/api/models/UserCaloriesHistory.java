package controlate_back.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_calories_history")
public class UserCaloriesHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;  // Clave primaria auto-incremental

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @JsonIgnore  // Ignoramos la relación para evitar problemas de serialización con proxies de Hibernate
    private User userInformation;

    @Column(nullable = false, length = 50)
    private String username;  // El campo "username" es ahora un atributo de la entidad

    @Column(length = 50)
    private String meal;

    @Column(nullable = false, length = 100)
    private String foodName;

    @Column(nullable = false)
    private Float carbohydrates = 0f;

    @Column(nullable = false)
    private Float fats = 0f;

    @Column(nullable = false)
    private Float proteins = 0f;

    @Column(nullable = false)
    private Float calories = 0f;

    @Column(nullable = false)
    private Float quantity = 0f;

    @Column(nullable = false)
    private Integer foodId;

    @Column(nullable = false)
    private String logDate;

    @Column(nullable = false)
    private String units;
}
