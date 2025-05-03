/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umg.edu.programacion3.proyecto.covid.config;

import umg.edu.programacion3.proyecto.covid.service.CovidApiService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static umg.edu.programacion3.proyecto.covid.config.AppProperties.getInt;

public class Scheduler {

    private final CovidApiService service;

    public Scheduler(CovidApiService service) {
        this.service = service;
    }

    public void iniciar() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        int delaySeconds = getInt("scheduler.initial.delay", 15);

        executor.schedule(() -> {
            System.out.println("[Scheduler] Starting API fetch...");
            service.fetchAndPersistCovidData("GTM", "2022-04-16");
            service.fetchAndPersistCovidData("USA", "2022-04-16"); 
            executor.shutdown();
        }, delaySeconds , TimeUnit.SECONDS); // <-- espera 15 segundos
    }
}

