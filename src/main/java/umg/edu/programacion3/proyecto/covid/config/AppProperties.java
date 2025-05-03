package umg.edu.programacion3.proyecto.covid.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Enumeration; // Import this

public class AppProperties {

    private static final Properties properties = new Properties();
    private static final Logger logger = LogManager.getLogger(AppProperties.class);

    static {
        // *** ADD THIS CODE ***
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        try {
            Enumeration<java.net.URL> resources = AppProperties.class.getClassLoader().getResources("");
            while (resources.hasMoreElements()) {
                System.out.println("Resource URL: " + resources.nextElement());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // *** END OF ADDED CODE ***

        try (InputStream input = AppProperties.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                String errorMessage = "⚠️ config.properties not found in classpath";
                logger.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
            properties.load(input);
            logger.info("✅ config.properties loaded successfully");
        } catch (IOException e) {
            String errorMessage = "❌ Failed to load config.properties";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }

        Enumeration<?> e = properties.propertyNames(); // Already had this
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            System.out.println("Property: " + key + " = " + value);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            // Try a simple key (for non-Spring Boot usage)
            value = properties.getProperty(key.replace("spring.datasource.", "").replace("spring.jpa.", "").replace("logging.level.root", "logging.level"));
            if (value == null) {
                logger.warn("Property '{}' not found, returning null", key);
            }
        }
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer format for property '{}', returning default value: {}", key, defaultValue);
            return defaultValue;
        } catch (NullPointerException e) {
            logger.warn("Property '{}' not found, returning default value: {}", key, defaultValue);
            return defaultValue;
        }
    }

    // New methods to get database properties (if needed)
    public static String getDbUrl() {
        return get("spring.datasource.url");
    }

    public static String getDbUsername() {
        return get("spring.datasource.username");
    }

    public static String getDbPassword() {
        return get("spring.datasource.password");
    }

    public static String getApiHost() {
        return get("api.host");
    }

    public static String getApiKey() {
        return get("api.key");
    }

    public static String getLoggingLevel() {
        return get("logging.level.root");
    }

    public static String getDdlAuto() {
        return get("spring.jpa.hibernate.ddl-auto");
    }

    public static String getShowSql() {
        return get("spring.jpa.show-sql");
    }
}