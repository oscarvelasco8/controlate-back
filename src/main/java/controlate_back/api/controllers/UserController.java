package controlate_back.api.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import controlate_back.api.models.User;
import controlate_back.api.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

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

    // Obtener todos los usuarios
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Obtener usuario
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }
        return ResponseEntity.ok(user);
    }

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

    // Eliminar un usuario por nombre de usuario
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    @GetMapping("/tmb/{username}")
    public float calculateTMB(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return userService.calculateTMB(user);
        }
        return 0;
    }

    @GetMapping("/daily-calories/{username}")
    public float calculateDailyCalories(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return userService.calculateDailyCalories(user);
        }
        return 0;
    }

    @PatchMapping("/objective/{username}")
    public ResponseEntity<?> updateObjective(@PathVariable String username, @RequestBody String objective) {
        try{
            userService.updateObjective(username, objective);
            return ResponseEntity.ok("Objetivo actualizado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/objective/{username}")
    public String getObjective(@PathVariable String username) {
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            return user.getObjective().toString();
        }
        return null;
    }
}
