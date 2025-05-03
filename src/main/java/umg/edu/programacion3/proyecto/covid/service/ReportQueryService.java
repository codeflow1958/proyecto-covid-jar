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
        LocalDate date = LocalDate.parse(dateStr, dateFormatter); // Convierte la fecha de String a LocalDate
        Map<String, Report> groupedReports = new TreeMap<>(); // Usa TreeMap para agrupar y ordenar los reportes

        try {
            // Crea una consulta para obtener los reportes para la fecha y el país
            Query query = em.createQuery(
                    "SELECT r FROM Report r WHERE r.date = :date AND r.region = :countryIso", Report.class);
            query.setParameter("date", date.toString());
            query.setParameter("countryIso", countryIso);

            List<Report> reports = query.getResultList(); // Ejecuta la consulta y obtiene los resultados

            for (Report report : reports) {
                groupedReports.put(report.getProvince(), report); // Usa la provincia como clave en el TreeMap
            }

            logger.info("INFO: Se encontraron {} reportes.", groupedReports.size()); // Registra la cantidad de reportes encontrados
            groupedReports.forEach((province, report) -> logger.info("   {}: {}", province, report)); // Registra cada reporte

        } catch (NoResultException e) {
            logger.info("INFO: No se encontraron reportes para {} en {}", countryIso, dateStr);
        } catch (Exception e) {
            logger.error("ERROR: Error al obtener los reportes para {} en {}: {}", countryIso, dateStr, e.getMessage(), e);
        } finally {
            em.close(); // Cierra el EntityManager
        }

        return groupedReports; // Devuelve los reportes agrupados
    }
}