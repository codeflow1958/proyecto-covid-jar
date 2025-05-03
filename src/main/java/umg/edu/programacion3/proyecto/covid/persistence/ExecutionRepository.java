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
            Long count = em.createQuery(
                    "SELECT COUNT(e) FROM ExecutionEntity e WHERE e.executionDate = :date AND e.countryIso = :countryIso", Long.class)
                    .setParameter("date", date)
                    .setParameter("countryIso", countryIso)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            logger.info("No previous execution found for {} on {}", countryIso, date);
            return false;
        } catch (Exception e) {
            logger.error("Error checking execution for {} on {}: {}", countryIso, date, e.getMessage(), e);
            return false; // Or throw an exception, depending on your error handling policy
        } finally {
            em.close();
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
            logger.info("Execution marked for {} on {}", countryIso, date);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.error("Error marking execution for {} on {}: {}", countryIso, date, e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}