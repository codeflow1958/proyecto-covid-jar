package umg.edu.programacion3.proyecto.covid.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReportResponse {
    private List<ReportDto> data;

    public List<ReportDto> getData() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
