package controlate_back.api.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_information")
@Schema(description = "Modelo que representa a un usuario en la API.")
public class User {

    @Schema(description = "Nombre del usuario", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String name;

    @Schema(description = "Apellido del usuario", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String lastname;

    @Schema(description = "Correo electrónico único del usuario", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Schema(description = "Edad del usuario", example = "30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column
    private Integer age;

    @Schema(description = "Peso del usuario en kilogramos", example = "70.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column
    private Float weight;

    @Schema(description = "Altura del usuario en metros", example = "1.75", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column
    private Float height;

    @Schema(description = "Índice de carbohidratos recomendado (ICR)", example = "10.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column
    private Float icr;

    @Schema(
            description = "Factor de actividad del usuario",
            example = "ACTIVO",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"POCO_SEDENTARIO", "SEDENTARIO", "MODERADAMENTE_SEDENTARIO", "ACTIVO", "MUY_ACTIVO"}
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_factor", nullable = false, length = 50)
    private FactorActividad activityFactor;

    @Schema(description = "Factor de insulina recomendado", example = "1.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "insulina_factor")
    private Float insulinaFactor;

    @Schema(description = "Nombre de usuario único (ID)", example = "juanperez123", requiredMode = Schema.RequiredMode.REQUIRED)
    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Schema(description = "Contraseña del usuario", example = "securepassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String password;

    @Schema(
            description = "Género del usuario",
            example = "MALE",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"MALE", "FEMALE"}
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 50)
    private gender gender;

    @Schema(
            description = "Objetivo del usuario",
            example = "MANTENIMIENTO",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            allowableValues = {"BAJAR_LIGERO", "BAJAR_MODERADO", "MANTENIMIENTO", "SUBIR_LIGERO", "SUBIR_MODERADO"}
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "objective", length = 50)
    private objective objective;

    @Schema(description = "Enumeración que representa el factor de actividad del usuario.")
    public enum FactorActividad {
        POCO_SEDENTARIO,
        SEDENTARIO,
        MODERADAMENTE_SEDENTARIO,
        ACTIVO,
        MUY_ACTIVO
    }

    @Schema(description = "Enumeración que representa el género del usuario.")
    public enum gender {
        MALE,
        FEMALE
    }

    @Schema(description = "Enumeración que representa el objetivo del usuario.")
    public enum objective {
        BAJAR_LIGERO,
        BAJAR_MODERADO,
        MANTENIMIENTO,
        SUBIR_LIGERO,
        SUBIR_MODERADO
    }
}
