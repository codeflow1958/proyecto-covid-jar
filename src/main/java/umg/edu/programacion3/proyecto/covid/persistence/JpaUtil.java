package umg.edu.programacion3.proyecto.covid.persistence;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JpaUtil {
    private static EntityManagerFactory emf = buildEntityManagerFactory();
    private static final Logger logger = LogManager.getLogger(JpaUtil.class);

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            // Assuming persistence unit name is "covidPU" (check your persistence.xml)
            return Persistence.createEntityManagerFactory("covidPU");
        } catch (Exception e) {
            logger.error("❌ Failed to create EntityManagerFactory", e);
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}