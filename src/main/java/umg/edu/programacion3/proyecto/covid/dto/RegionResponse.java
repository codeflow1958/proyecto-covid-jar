package umg.edu.programacion3.proyecto.covid.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegionResponse {
    private List<RegionDto> data;

    public List<RegionDto> getData() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
