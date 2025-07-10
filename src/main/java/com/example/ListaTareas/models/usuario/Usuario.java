package com.example.ListaTareas.models.usuario;
import com.example.ListaTareas.models.codigoVerificacion.CodigoVerificacion;
import com.example.ListaTareas.models.tarea.Tarea;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<String> roles = new ArrayList<>();
    private Boolean enabled;

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodigoVerificacion> codigosVerificacion = new ArrayList<>();

    @PrePersist
    protected void onCreate(){
        this.roles = List.of(Rol.USER.name());
        this.enabled = false;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // Spring usará este para el nombre de usuario
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public enum Rol {
        USER,
        ADMIN,
        DEV;

    }

    public void setRoles(Rol rol){
        this.roles.add(rol.name());
    }
}
