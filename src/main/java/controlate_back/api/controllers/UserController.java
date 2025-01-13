package controlate_back.api.controllers;

import controlate_back.api.models.UserCaloriesHistory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import controlate_back.api.models.User;
import controlate_back.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Tag(name = "Controlador del usuario", description = "Operaciones relacionadas con los usuarios de la aplicación")

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final SecretKey jwtSecretKey;

    @Value("${jwt.expiration}") // Lee el tiempo de expiración desde application.properties
    private long jwtExpiration;

    public UserController(@Value("${jwt.secret}") String jwtSecret) {
        // Convertimos la clave secreta en una instancia de SecretKey
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve todos los usuarios de la aplicación almacenados en la base de datos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuarios obtenidos",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            }
    )

    // Obtener todos los usuarios
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Obtener un usuario",
            description = "Devuelve un usuario de la aplicación almacenado en la base de datos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario obtenido",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Nombre de usuario",
                            example = "juanperez",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )

    // Obtener usuario
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autenticación de usuario",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario autenticado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(name = "username", description = "Nombre de usuario", example = "juanperez", required = true),
                    @Parameter(name = "password", description = "Contraseña", example = "password123", required = true)
            }
    )

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.getUserByUsername(username).orElse(null);

        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Generar el token JWT
                String token = Jwts.builder()
                        .setSubject(user.getUsername())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                        .signWith(jwtSecretKey, SignatureAlgorithm.HS512) // Usamos la clave segura
                        .compact();

                // Devolver el token al cliente
                return ResponseEntity.ok(new JwtResponse(token));
            }
        }
        return ResponseEntity.badRequest().body("Usuario o contraseña incorrectos.");
    }

    // Clase interna para encapsular el token en la respuesta
    @Getter
    private static class JwtResponse {
        private final String token;

        public JwtResponse(String token) {
            this.token = token;
        }
    }

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Crea un nuevo usuario en la aplicación",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario creado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Información del nuevo usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            )
    )

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso.");
        }
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está en uso.");
        }

        // Hashear la contraseña antes de guardarla
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Actualizar un usuario",
            description = "Actualiza un usuario existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario actualizado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Usuario a actualizar",
                            example = "juanperez",
                            required = true,
                            in = ParameterIn.PATH
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del usuario a actualizar",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            )
    )

    // Actualizar un usuario existente
    @PatchMapping("/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(username, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina un usuario existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario eliminado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(
                            name = "username",
                            example = "juanperez",
                            description = "Usuario a eliminar",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )

    // Eliminar un usuario por nombre de usuario
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    @Operation(
            summary = "Calcular el TMB del usuario",
            description = "Calcula el TMB del usuario",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "TMB calculado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(
                            name = "username",
                            example = "juanperez",
                            description = "Nombre de usuario",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )

    @GetMapping("/tmb/{username}")
    public float calculateTMB(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return userService.calculateTMB(user);
        }
        return 0;
    }

    @Operation(
            summary = "Calcular el requerimiento calórico diario del usuario",
            description = "Calcula el requerimiento calórico diario del usuario",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Requerimiento calórico diario calculado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(
                            name = "username",
                            example = "juanperez",
                            description = "Nombre de usuario",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )

    @GetMapping("/daily-calories/{username}")
    public float calculateDailyCalories(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return userService.calculateDailyCalories(user);
        }
        return 0;
    }

    @Operation(
            summary = "Actualizar el objetivo del usuario",
            description = "Actualiza el objetivo del usuario",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Objetivo actualizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(
                            name = "username",
                            example = "juanperez",
                            description = "Nombre de usuario",
                            required = true,
                            in = ParameterIn.PATH
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objetivo del usuario", required = true, content = @Content(mediaType = "text/plain"))
    )

    @PatchMapping("/objective/{username}")
    public ResponseEntity<?> updateObjective(@PathVariable String username, @RequestBody String objective) {
        try{
            userService.updateObjective(username, objective);
            return ResponseEntity.ok("Objetivo actualizado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Obtener el objetivo del usuario",
            description = "Obtiene el objetivo del usuario",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Objetivo obtenido",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No Autorizado",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain")
                    )
            },
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Nombre de usuario",
                            example = "juanperez",
                            required = true,
                            in = ParameterIn.PATH
                    )
            }
    )

    @GetMapping("/objective/{username}")
    public String getObjective(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return user.getObjective().toString();
        }
        return null;
    }
}
