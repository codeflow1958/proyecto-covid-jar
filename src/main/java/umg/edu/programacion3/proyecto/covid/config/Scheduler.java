package umg.edu.programacion3.proyecto.covid.config;

import umg.edu.programacion3.proyecto.covid.service.CovidApiService;
import umg.edu.programacion3.proyecto.covid.persistence.ExecutionRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scheduler {

    private final CovidApiService service;
    private final ExecutionRepository executionRepository;
    private static final Logger logger = LogManager.getLogger(Scheduler.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Scheduler(CovidApiService service, ExecutionRepository executionRepository) {
        this.service = service;
        this.executionRepository = executionRepository;
    }

    public void iniciar() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        int delaySeconds = AppProperties.getInt("la espera de 15", 15); // Obtiene el delay desde config.properties
        String reportDateStr = AppProperties.get("obtenemos la fecha des de el confi.propietis"); // Obtiene la fecha desde config.properties

        if (reportDateStr == null || reportDateStr.trim().isEmpty()) {
            logger.error("la fecha no esta configurada");// Registra un error si la fecha no está configurada
            return; // Stop execution if date is missing
        }

        LocalDate reportDate;
        try {
            reportDate = LocalDate.parse(reportDateStr, dateFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            logger.error("formato de la fecha incorrecto", reportDateStr, e); // Registra un error si el formato de la fecha es incorrecto
            return; // se para la ejecución si no hay fecha
        }

        executor.schedule(() -> {
            logger.info("se inicia la API");

            processCountry("GTM", reportDate);
            processCountry("USA", reportDate);

            executor.shutdown();
        }, delaySeconds, TimeUnit.SECONDS);
    }

    private void processCountry(String countryIso, LocalDate reportDate) {
        if (executionRepository.hasExecuted(reportDate, countryIso)) {
            logger.info(" salta el pais si ya se proceso", countryIso, reportDate); // Registra que se omite el país si ya se procesó
            return;
        }

        logger.info("inicia el proceso del pai", countryIso, reportDate);  // Registra el inicio del procesamiento para el país
        service.fetchAndPersistCovidData(countryIso, reportDate.toString()); // Obtiene y guarda los datos

        executionRepository.markExecuted(reportDate, countryIso); // Marca la ejecución como completada
        logger.info("  proceso completo", countryIso, reportDate);
    }
}