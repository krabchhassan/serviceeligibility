/**
 *
 */
package com.cegedim.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Import data before launching e2e tests.
 */
public class ImportUtils {
    private ImportUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(ImportUtils.class);

    private static final String KEY_URL_BDD_CORE_API_IMPORT_PARAM = ConfigurationUtils.getServiceEligibilityCoreApi()
            + "/v1/importParametrage";
    private static final String KEY_URL_BDD_CORE_API_IMPORT = ConfigurationUtils.getServiceEligibilityCoreApi()
            + "/v1/import";

    private static final String rawAuthent = "grant_type=password&client_id=postman&username="
            + ConfigurationUtils.getAuthUser() + "&password=" + ConfigurationUtils.getAuthPass();

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static String authenticate() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(ConfigurationUtils.getUrlAuthApi());

        if (StringUtils.isNotEmpty(ConfigurationUtils.getUrlAuthProxy())) {
            HttpHost proxy = HttpHost.create(ConfigurationUtils.getUrlAuthProxy());
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            post.setConfig(config);
        }
        String token = "";

        // add header
        StringEntity input;
        try {
            input = new StringEntity(rawAuthent);
            input.setContentType("application/x-www-form-urlencoded");
            post.setEntity(input);
            HttpResponse response = client.execute(post);
            JsonNode node = jsonMapper.readTree(response.getEntity().getContent());
            token = node.get("access_token").asText();
        }
        catch (IOException ex) {
            logger.error("Cannot authenticate : " + ex.getMessage(), ex);
        }
        return token;

    }

    /**
     * import data set for declarants / AMC
     */
    public static void doDeclarantsImport() {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            logger.info("#doDeclarantsImport : Adresse import : " + KEY_URL_BDD_CORE_API_IMPORT_PARAM);
            /*
             * HttpPost post = new HttpPost(
             * ConfigurationUtils.getServiceEligibilityCoreApi() +
             * KEY_URL_BDD_CORE_API_IMPORT_PARAM);
             */
            HttpPost post = new HttpPost(KEY_URL_BDD_CORE_API_IMPORT_PARAM);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder
                    .addBinaryBody(
                            "uploadedFile",
                                ImportUtils.class.getResourceAsStream("/declarants.json"),
                                ContentType.APPLICATION_JSON,
                                "json");
            HttpEntity multipart = builder.build();
            post.setEntity(multipart);
            String token = authenticate();
            post.addHeader("Authorization", "bearer " + token);
            CloseableHttpResponse response = client.execute(post);
            client.close();
            logger.info("Response : " + response.getStatusLine().getStatusCode());
        }
        catch (IOException ex) {
            logger.error("Error when importing declarants : " + ex.getMessage(), ex);
        }
    }

    /**
     * import data set for declarants / AMC
     */
    public static void doVolumetrieFluxDeclarationImport() {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(KEY_URL_BDD_CORE_API_IMPORT);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder
                    .addBinaryBody(
                            "uploadedFile",
                                ImportUtils.class.getResourceAsStream("/serviceeligibility.json"),
                                ContentType.APPLICATION_JSON,
                                "json");
            HttpEntity multipart = builder.build();
            post.setEntity(multipart);
            String token = authenticate();
            post.addHeader("Authorization", "bearer " + token);
            CloseableHttpResponse response = client.execute(post);
            client.close();
            logger.info("Response : " + response.getStatusLine().getStatusCode());
        }
        catch (IOException ex) {
            logger.error("Error when importing serviceeligibility : " + ex.getMessage(), ex);
        }
    }
}
