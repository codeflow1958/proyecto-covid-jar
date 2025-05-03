package umg.edu.programacion3.proyecto.covid.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import umg.edu.programacion3.proyecto.covid.model.ExecutionEntity;  // Import the ExecutionEntity class

public class ExecutionRepository {

    private static final Logger logger = LogManager.getLogger(ExecutionRepository.class);

    public boolean hasExecuted(LocalDate date, String countryIso) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
             // Crea una consulta para contar las ejecuciones para la fecha y el país
            Long count = em.createQuery(
                    "SELECT COUNT(e) FROM ExecutionEntity e WHERE e.executionDate = :date AND e.countryIso = :countryIso", Long.class)
                    .setParameter("date", date)
                    .setParameter("countryIso", countryIso)
                    .getSingleResult();
            return count > 0; // Devuelve true si ya existe al menos una ejecución
        } catch (NoResultException e) {
            logger.info("no se encontraron ejecuciones previas", countryIso, date);
            return false;
        } catch (Exception e) {
            logger.error(" error al verificar la ejecución", countryIso, date, e.getMessage(), e);
            return false; // O lanza una excepción, dependiendo de tu política de manejo de errores
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    public void markExecuted(LocalDate date, String countryIso) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ExecutionEntity execution = new ExecutionEntity();
            execution.setExecutionDate(date);
            execution.setCountryIso(countryIso);
            em.persist(execution);
            tx.commit();
            logger.info("se marco la ejeccucion", countryIso, date);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.error("Registra un error al marcar la ejecución", countryIso, date, e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}