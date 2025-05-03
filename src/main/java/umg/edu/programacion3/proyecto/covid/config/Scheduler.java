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
        int delaySeconds = AppProperties.getInt("scheduler.initial.delay", 15);
        String reportDateStr = AppProperties.get("covid.report.date");

        if (reportDateStr == null || reportDateStr.trim().isEmpty()) {
            logger.error("❌ covid.report.date is not configured in config.properties. Scheduler will not run.");
            return; // Stop execution if date is missing
        }

        LocalDate reportDate;
        try {
            reportDate = LocalDate.parse(reportDateStr, dateFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            logger.error("❌ Invalid date format in config.properties: '{}'.  Expected 'yyyy-MM-dd'.  Scheduler will not run.", reportDateStr, e);
            return; // Stop execution if date format is wrong
        }

        executor.schedule(() -> {
            logger.info("▶️ Starting API fetch...");

            processCountry("GTM", reportDate);
            processCountry("USA", reportDate);

            executor.shutdown();
        }, delaySeconds, TimeUnit.SECONDS);
    }

    private void processCountry(String countryIso, LocalDate reportDate) {
        if (executionRepository.hasExecuted(reportDate, countryIso)) {
            logger.info("  ⏩ Country {} already processed on {}. Skipping.", countryIso, reportDate);
            return;
        }

        logger.info("  ▶️ Processing {} for {}", countryIso, reportDate);
        service.fetchAndPersistCovidData(countryIso, reportDate.toString());

        executionRepository.markExecuted(reportDate, countryIso);
        logger.info("  ✅ Processed and marked {} for {}", countryIso, reportDate);
    }
}