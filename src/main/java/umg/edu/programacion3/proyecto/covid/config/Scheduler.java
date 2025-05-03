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
        int delaySeconds = AppProperties.getInt("scheduler.initial.delay", 15); // Obtiene el delay desde config.properties
        String reportDateStr = AppProperties.get("covid.report.date"); // Obtiene la fecha desde config.properties

        if (reportDateStr == null || reportDateStr.trim().isEmpty()) {
            logger.error("ERROR: La fecha del reporte (covid.report.date) no está configurada en config.properties. El Scheduler no se ejecutará.");
            return; // Detiene la ejecución si no hay fecha
        }

        LocalDate reportDate;
        try {
            reportDate = LocalDate.parse(reportDateStr, dateFormatter); // Convierte la fecha de String a LocalDate
        } catch (java.time.format.DateTimeParseException e) {
            logger.error("ERROR: Formato de fecha inválido en config.properties: '{}'. Se esperaba el formato 'yyyy-MM-dd'. El Scheduler no se ejecutará.", reportDateStr, e);
            return; // Detiene la ejecución si el formato es incorrecto
        }

        executor.schedule(() -> {
            logger.info("INICIO: Iniciando la obtención de datos de la API...");

            processCountry("GTM", reportDate); // Procesa los datos para Guatemala
            processCountry("USA", reportDate); // Procesa los datos para USA

            executor.shutdown();
        }, delaySeconds, TimeUnit.SECONDS);
    }

    private void processCountry(String countryIso, LocalDate reportDate) {
        if (executionRepository.hasExecuted(reportDate, countryIso)) {
            logger.info("OMITIDO: El país {} ya fue procesado el {}. Se omite.", countryIso, reportDate);
            return;
        }

        logger.info("PROCESANDO: Iniciando el procesamiento de {} para el {}", countryIso, reportDate);
        service.fetchAndPersistCovidData(countryIso, reportDate.toString());

        executionRepository.markExecuted(reportDate, countryIso);
        logger.info("COMPLETADO: Procesamiento completado y registrado para {} en el {}", countryIso, reportDate);
    }
}