# GRUPO #3

## 📊 Proyecto COVID-19 – Programación 3

Este proyecto fue desarrollado como parte de la materia **Programación 3** de la carrera de informática. El objetivo principal fue aplicar conocimientos de programación orientada a objetos, uso de APIs, persistencia de datos con JPA y manejo de hilos (threads), desarrollando una aplicación capaz de consumir datos de una API pública de COVID-19 y almacenarlos de forma estructurada en una base de datos relacional.

---

## 🎯 Objetivos del Proyecto

- Aplicar los principios fundamentales de la programación orientada a objetos en un proyecto real.
- Utilizar bibliotecas y tecnologías como JPA, HTTPClient y MySQL.
- Comprender y aplicar el concepto de multithreading para tareas concurrentes.
- Consumir servicios RESTful (API externa) y procesar sus datos.
- Implementar una arquitectura en capas siguiendo buenas prácticas.

---

## ⚙️ Tecnologías y herramientas utilizadas

- **Java 17**
- **Apache Maven** – Gestión de dependencias
- **JPA / Hibernate** – Mapeo objeto-relacional (ORM)
- **MySQL / MariaDB** – Base de datos relacional
- **HTTPClient** – Consumo de API REST
- **Multithreading** – Hilos para procesamiento paralelo
- **JSON** – Formato de datos para comunicación API
- **IDE**: IntelliJ IDEA / NetBeans / Eclipse (según el desarrollador)

---

## 🔁 Flujo del proceso

1. **Inicialización del sistema**  
   Al ejecutar el programa, se inicia la clase `MainApp.java`, que coordina el flujo del proyecto.

2. **Consumo de la API COVID-19**  
   Una clase dedicada realiza una solicitud HTTP a una API pública (por ejemplo, `https://api.covid19api.com/summary`), recibe una respuesta en formato JSON y convierte esos datos a objetos Java usando librerías integradas o externas como Gson o Jackson.

3. **Transformación y limpieza de datos**  
   Los datos se filtran y transforman si es necesario para cumplir con el modelo de datos definido.

4. **Persistencia de datos**  
   Las entidades Java (por ejemplo, `CountryStat`) están mapeadas a tablas de la base de datos mediante anotaciones JPA (`@Entity`, `@Table`, `@Id`, etc.). Luego, usando un `Repository`, se guarda la información en la base de datos.

5. **Uso de hilos (threads)**  
   Para mejorar el rendimiento y evitar bloqueos, se implementan hilos que separan el consumo de la API y la escritura a la base de datos. Esto permite que las tareas se ejecuten de forma concurrente.

6. **Finalización**  
   Al finalizar el proceso, se muestra un mensaje indicando que los datos fueron procesados y almacenados correctamente.

---

## 📁 Estructura del proyecto

```
└── 📁main
    └── 📁java
        └── 📁umg
            └── 📁edu
                └── 📁programacion3
                    └── 📁proyecto
                        └── 📁covid
                            └── 📁config
                                └── AppProperties.java
                                └── Scheduler.java
                            └── 📁dto
                                └── ProvinceDto.java
                                └── ProvinceInfoDto.java
                                └── ProvinceResponse.java
                                └── RegionDto.java
                                └── RegionInfoDto.java
                                └── RegionResponse.java
                                └── ReportDto.java
                                └── ReportResponse.java
                            └── Main.java
                            └── 📁mapper
                                └── DtoMapper.java
                            └── 📁model
                                └── Province.java
                                └── Region.java
                                └── Report.java
                            └── 📁persistence
                                └── JpaUtil.java
                            └── 📁service
                                └── CovidApiService.java
                            └── 📁util
                                └── CovidApiClient.java
    └── 📁resources
        └── config.properties
        └── log4j2.xml
        └── 📁META-INF
            └── persistence.xml
```

---

## logs

![log 1](img_log/1.png)

![log 2](img_log/2.png)

![log3](img_log/3.png)

---.
