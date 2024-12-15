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

    @Column
    private Float icr;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 50)
    private gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "objective", length = 50)
    private objective objective;

    public enum FactorActividad {
        POCO_SEDENTARIO,
        SEDENTARIO,
        MODERADAMENTE_SEDENTARIO,
        ACTIVO,
        MUY_ACTIVO
    }

    public enum gender{
        MALE,
        FEMALE
    }

    public enum objective{
        BAJAR_LIGERO,
        BAJAR_MODERADO,
        MANTENIMIENTO,
        SUBIR_LIGERO,
        SUBIR_MODERADO
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getIcr() {
        return icr;
    }

    public void setIcr(Float icr) {
        this.icr = icr;
    }

    public FactorActividad getActivityFactor() {
        return activityFactor;
    }

    public void setActivityFactor(FactorActividad activityFactor) {
        this.activityFactor = activityFactor;
    }

    public Float getInsulinaFactor() {
        return insulinaFactor;
    }

    public void setInsulinaFactor(Float insulinaFactor) {
        this.insulinaFactor = insulinaFactor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.gender getGender() {
        return gender;
    }

    public void setGender(User.gender gender) {
        this.gender = gender;
    }

    public User.objective getObjective() {
        return objective;
    }

    public void setObjective(User.objective objective) {
        this.objective = objective;
    }
}

