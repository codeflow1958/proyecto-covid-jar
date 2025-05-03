package umg.edu.programacion3.proyecto.covid.service;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import static java.lang.Math.log;
import lombok.extern.log4j.Log4j2;
import umg.edu.programacion3.proyecto.covid.dto.*;
import umg.edu.programacion3.proyecto.covid.mapper.DtoMapper;
import umg.edu.programacion3.proyecto.covid.model.*;
import umg.edu.programacion3.proyecto.covid.persistence.JpaUtil;
import umg.edu.programacion3.proyecto.covid.util.CovidApiClient;

import java.util.List;

@Log4j2
public class CovidApiService {

    private final CovidApiClient client;
    private final Gson gson;

    public CovidApiService() {
        this.client = new CovidApiClient();
        this.gson = new Gson();
    }

    public void fetchAndPersistCovidData(String iso, String date) {
        log.info("Iniciando persistencia");
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 🔹 REGIONS
            String jsonRegions = client.getRegions();
            RegionResponse regionResponse = gson.fromJson(jsonRegions, RegionResponse.class);
            List<RegionDto> regions = regionResponse.getData();

            for (RegionDto dto : regions) {
                Region entity = DtoMapper.toRegion(dto);
                em.persist(entity);
            }
            log.info("✅ Saved {} regions", regions.size());

            // 🔹 PROVINCES
            String jsonProvinces = client.getProvinces(iso);
            ProvinceResponse provinceResponse = gson.fromJson(jsonProvinces, ProvinceResponse.class);
            List<ProvinceDto> provinces = provinceResponse.getData();

            if (provinces != null && !provinces.isEmpty()) {
                for (ProvinceDto dto : provinces) {
                    Province entity = DtoMapper.toProvince(dto);
                    em.persist(entity);
                }
                log.info("✅ Saved {} provinces", provinces.size());
            } else {
                log.warn("❌ No provinces found for the specified region.");
            }

            // 🔹 REPORTS
            String jsonReports = client.getReport(iso, date);
            ReportResponse reportResponse = gson.fromJson(jsonReports, ReportResponse.class);
            List<ReportDto> reports = reportResponse.getData();

            if (reports != null && !reports.isEmpty()) {
                for (ReportDto dto : reports) {
                    Report entity = DtoMapper.toReport(dto);
                    em.persist(entity);
                }
                log.info("✅ Saved {} reports", reports.size());
            } else {
                log.warn("❌ No reports found for the specified region and date.");
            }

            tx.commit();

        } catch (Exception e) {
            log.error("❌ Error during COVID API processing: {}", e.getMessage(), e);
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
    }
}