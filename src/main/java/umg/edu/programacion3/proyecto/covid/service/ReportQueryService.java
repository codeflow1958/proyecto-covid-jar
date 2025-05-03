package umg.edu.programacion3.proyecto.covid.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import umg.edu.programacion3.proyecto.covid.model.Report;
import umg.edu.programacion3.proyecto.covid.persistence.JpaUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportQueryService {

    private static final Logger logger = LogManager.getLogger(ReportQueryService.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Map<String, Report> getReportsByDateAndCountry(String dateStr, String countryIso) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
        Map<String, Report> groupedReports = new TreeMap<>();

        try {
            Query query = em.createQuery(
                    "SELECT r FROM Report r WHERE r.date = :date AND r.region = :countryIso", Report.class);
            query.setParameter("date", date.toString());
            query.setParameter("countryIso", countryIso);

            List<Report> reports = query.getResultList();

            for (Report report : reports) {
                groupedReports.put(report.getProvince(), report); // Use province as key
            }

            logger.info("Reports found: {}", groupedReports.size());
            groupedReports.forEach((province, report) -> logger.info("{}: {}", province, report));

        } catch (NoResultException e) {
            logger.info("No reports found for {} on {}", countryIso, dateStr);
        } catch (Exception e) {
            logger.error("Error fetching reports for {} on {}: {}", countryIso, dateStr, e.getMessage(), e);
        } finally {
            em.close();
        }

        return groupedReports;
    }
}