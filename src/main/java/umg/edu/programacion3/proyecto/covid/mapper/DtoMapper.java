package umg.edu.programacion3.proyecto.covid.mapper;

import umg.edu.programacion3.proyecto.covid.dto.*;
import umg.edu.programacion3.proyecto.covid.dto.ProvinceDto;
import umg.edu.programacion3.proyecto.covid.dto.RegionDto;
import umg.edu.programacion3.proyecto.covid.dto.ReportDto;
import umg.edu.programacion3.proyecto.covid.model.*;

public class DtoMapper {

    public static Region toRegion(RegionDto dto) {
        Region region = new Region();
        region.setIso(dto.getIso());
        region.setName(dto.getName());
        return region;
    }

    public static Province toProvince(ProvinceDto dto) {
        Province province = new Province();
        province.setProvince(dto.getProvince());
        province.setIso(dto.getIso());
        province.setName(dto.getName());
        return province;
    }

    public static Report toReport(ReportDto dto) {
        Report report = new Report();
        report.setDate(dto.getDate());

        // Desanidar valores
        if (dto.getProvince() != null) {
            report.setProvince(dto.getProvince().getName());
        }
        if (dto.getRegion() != null) {
            report.setRegion(dto.getRegion().getName()); // o dto.getRegion().getIso()
        }

        report.setConfirmed(dto.getConfirmed());
        report.setDeaths(dto.getDeaths());
        report.setRecovered(dto.getRecovered());

        return report;
    }
}
