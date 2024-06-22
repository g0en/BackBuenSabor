package com.entidades.buenSabor.domain.entities;

import com.entidades.buenSabor.domain.enums.Rol;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
@Audited
public class Usuario  extends Base{
    private String auth0Id;
    private String userName;
    private String email;
    private Rol rol;
}
