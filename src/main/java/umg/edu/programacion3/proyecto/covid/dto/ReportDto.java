package umg.edu.programacion3.proyecto.covid.dto;

import lombok.Data;

@Data
public class ReportDto {
    private String date;
    private RegionInfoDto region;
    private ProvinceInfoDto province;

    private int confirmed;
    private int deaths;
    private int recovered;
}
