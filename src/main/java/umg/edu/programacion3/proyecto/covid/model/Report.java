package umg.edu.programacion3.proyecto.covid.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String region;
    private String province;

    private int confirmed;
    private int deaths;
    private int recovered;
}
