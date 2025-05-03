package umg.edu.programacion3.proyecto.covid.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iso;
    private String name;
}