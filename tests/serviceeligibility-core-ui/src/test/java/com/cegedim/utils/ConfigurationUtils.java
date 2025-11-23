package com.cegedim.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;

/**
 * Utility methods for scenario.
 */
public class ConfigurationUtils {

    /** The Constant PROPERTY_FILE. */
    private static final String PROPERTY_FILE = "application.properties";

    /** The Constant KEY_URL_CORE_API. */
    private static final String KEY_URL_SERVICE_ELIGIBILITY_CORE_API = "url.serviceeligibility.core.api";

    /** The Constant KEY_URL_AUTH_API. */
    private static final String KEY_URL_AUTH_API = "url.auth.api";

    /** The Constant KEY_URL_AUTH_PROXY. */
    private static final String KEY_URL_AUTH_PROXY = "url.auth.proxy";

    /** The Constant KEY_AUTH_USER. */
    private static final String KEY_AUTH_USER = "auth.user";

    /** The Constant KEY_AUTH_PASS. */
    private static final String KEY_AUTH_PASS = "auth.pass";

    private static final String KEY_AUTHENTICATION_ENABLED = "authentication.enabled";
    public static final String TARGET_ENV = "TARGET_ENV";

    /** The logger. */
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    /** The properties. */
    private static Properties properties;

    /**
     * Instantiates a new configuration.
     */
    private ConfigurationUtils() {
        // do not instantiate
    }

    /**
     * Load from property file.
     */
    private static void loadFromPropertyFile() {
        if (properties == null) {
            properties = new Properties();
            ClassLoader classLoader = ConfigurationUtils.class.getClassLoader();
            try (InputStream inputStream = classLoader.getResourceAsStream(PROPERTY_FILE)) {
                properties.load(inputStream);
            }
            catch (IOException e) {
                logger.error(e);
            }
        }
    }

    /**
     * Gets the notification url core api.
     *
     * @return the url core api of notification
     */
    public static String getServiceEligibilityCoreApi() {
        loadFromPropertyFile();
        return retrieveUrl(KEY_URL_SERVICE_ELIGIBILITY_CORE_API);
    }

    /**
     * Gets the url auth api.
     *
     * @return the url auth api
     */
    public static String getUrlAuthApi() {
        loadFromPropertyFile();
        String env = System.getenv(TARGET_ENV) != null ? System.getenv(TARGET_ENV) : "it";
        return String.format(properties.getProperty(KEY_URL_AUTH_API, ""), env);
    }

    /**
     * Gets the auth user.
     *
     * @return the auth user
     */
    public static String getAuthUser() {
        loadFromPropertyFile();
        return retrieveUrl(KEY_AUTH_USER);
    }

    /**
     * Gets the url auth proxy.
     *
     * @return the url auth proxy
     */
    public static String getUrlAuthProxy() {
        loadFromPropertyFile();
        return retrieveUrl(KEY_URL_AUTH_PROXY);
    }

    /**
     * Gets the auth pass.
     *
     * @return the auth pass
     */
    public static String getAuthPass() {
        loadFromPropertyFile();
        return retrieveUrl(KEY_AUTH_PASS);
    }

    /**
     * Checks if is authentication enabled.
     *
     * @return true, if is authentication enabled
     */
    public static boolean isAuthenticationEnabled() {
        return Boolean.valueOf(properties.getProperty(KEY_AUTHENTICATION_ENABLED, "false"));
    }

    public static String retrieveUrl(String key) {
        String env = System.getenv(TARGET_ENV) != null ? System.getenv(TARGET_ENV) : "it";
        return String.format(properties.getProperty(key, ""), env);
    }
}
