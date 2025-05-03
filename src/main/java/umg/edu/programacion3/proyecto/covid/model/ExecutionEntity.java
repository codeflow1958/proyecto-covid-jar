
package umg.edu.programacion3.proyecto.covid.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name = "executed_reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"execution_date", "country_iso"})
})
public class ExecutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "execution_date", nullable = false)
    private LocalDate executionDate;

    @Column(name = "country_iso", nullable = false)
    private String countryIso;

    public ExecutionEntity() {}

    public ExecutionEntity(LocalDate executionDate, String countryIso) {
        this.executionDate = executionDate;
        this.countryIso = countryIso;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }
}
