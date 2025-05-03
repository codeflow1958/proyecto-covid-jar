package umg.edu.programacion3.proyecto.covid;

import umg.edu.programacion3.proyecto.covid.config.Scheduler;
import umg.edu.programacion3.proyecto.covid.service.CovidApiService;

public class Main {
    public static void main(String[] args) {
        CovidApiService service = new CovidApiService();
        Scheduler scheduler = new Scheduler(service);
        scheduler.iniciar();

        System.out.println("🟢 App started. Waiting for scheduler...");
    }
}
