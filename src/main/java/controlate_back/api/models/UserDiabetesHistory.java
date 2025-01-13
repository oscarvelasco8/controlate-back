package controlate_back.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_diabetes_history")
@Schema(description = "Modelo que representa el historial de diabetes de un usuario.")
public class UserDiabetesHistory {

    @Schema(description = "Identificador único del registro del historial de diabetes.", example = "67890", requiredMode = Schema.RequiredMode.REQUIRED)
    @Id
    @Column(name = "log_id", nullable = false, unique = true)
    private String logId;

    @Schema(description = "Información del usuario asociada al historial. Ignorada en la serialización para evitar problemas de proxies de Hibernate.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @JsonIgnore
    private User userInformation;

    @Schema(description = "Nombre de usuario asociado al historial.", example = "juanperez", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String username;

    @Schema(description = "Tipo de comida asociada al registro (desayuno, almuerzo, cena, etc.).", example = "desayuno", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(length = 50)
    private String meal;

    @Schema(description = "Nombre del alimento registrado.", example = "Manzana", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String foodName;

    @Schema(description = "Cantidad de carbohidratos en gramos.", example = "22.5", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "0")
    @Column(nullable = false)
    private Float carbohydrates = 0f;

    @Schema(description = "Cantidad consumida del alimento (en las unidades especificadas).", example = "1.5", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "0")
    @Column(nullable = false)
    private Float quantity = 0f;

    @Schema(description = "Identificador único del alimento registrado.", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Integer foodId;

    @Schema(description = "Fecha del registro del historial.", example = "2025-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate logDate;

    @Schema(description = "Unidades en las que se mide la cantidad del alimento (gramos, piezas, etc.).", example = "gramos", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String units;

    @Schema(description = "Número de porciones consumidas.", example = "2.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Float portions;
}
