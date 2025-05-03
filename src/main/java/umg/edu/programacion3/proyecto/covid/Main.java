package umg.edu.programacion3.proyecto.covid;

import umg.edu.programacion3.proyecto.covid.config.Scheduler;
import umg.edu.programacion3.proyecto.covid.service.CovidApiService;
import umg.edu.programacion3.proyecto.covid.persistence.ExecutionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("🔥 Starting the application");
        CovidApiService service = new CovidApiService();
        ExecutionRepository executionRepository = new ExecutionRepository();
        Scheduler scheduler = new Scheduler(service, executionRepository);
        scheduler.iniciar();
        logger.info("🏁 Application started");
    }
}