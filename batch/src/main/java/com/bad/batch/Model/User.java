package com.bad.batch.Model;

import com.bad.batch.Enum.UserRole; // Tu enum de roles
import jakarta.persistence.*;
import lombok.AllArgsConstructor;   // ¡Añadido! Necesario con @NoArgsConstructor y @Builder a veces
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;    // ¡Añadido! Necesario para JPA
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set; // Asegúrate de que las entidades Profile, Content, Message existan

@Data
@Builder
@NoArgsConstructor    // ¡Importante para JPA!
@AllArgsConstructor   // ¡Importante si usas @Builder con constructor con todos los args!
@Entity
@Table(name = "users")
public class User implements UserDetails { // Correcto que implemente UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relations (Asegúrate de que estas clases de entidad existan y estén correctamente mapeadas)
    // Nota: Si aún no tienes estas entidades (Profile, Content, Message), déjalas comentadas por ahora.
    // O si ya las tienes, asegúrate de que sus paquetes sean correctos.

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile; // Asume que Profile es una entidad en el paquete Model

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private Set<Content> createdContent; // Asume que Content es una entidad en el paquete Model

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Message> sentMessages; // Asume que Message es una entidad en el paquete Model

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private Set<Message> receivedMessages; // Asume que Message es una entidad en el paquete Model


    // --- Implementación de los 7 métodos de la interfaz UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Implementación correcta: devuelve una lista con el rol del usuario, prefijado con "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        // Implementación correcta: devuelve la contraseña almacenada en la entidad
        return password;
    }

    @Override
    public String getUsername() {
        // Implementación correcta: devuelve el email como el "nombre de usuario" para Spring Security
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Para el MVP, asumimos que la cuenta nunca expira
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Para el MVP, asumimos que la cuenta nunca está bloqueada
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Para el MVP, asumimos que las credenciales nunca expiran
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Implementación correcta: Mapea a tu campo 'isActive'
        return isActive;
    }
}