package com.cegedim.next.serviceeligibility.core.config.s3;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.filestorage.minio.client.constants.MinioClientConfigurationConstants;
import com.cegedim.filestorage.minio.client.utility.MinioClientInitializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MinioClientInitializerImpl implements MinioClientInitializer {

  private final BeyondPropertiesService beyondPropertiesService;

  public String getProxyHost() {
    return beyondPropertiesService.getProperty(PROXY_HOST).orElse(null);
  }

  public Integer getProxyPort() {
    return beyondPropertiesService
        .getIntegerProperty(PROXY_PORT)
        .orElse(MinioClientConfigurationConstants.NO_PORT);
  }

  public Boolean getSecureRequest() {
    return beyondPropertiesService.getBooleanProperty(S3_SECURE).orElse(true);
  }

  public String getRegion() {
    return beyondPropertiesService.getProperty(S3_REGION).orElse("eu-west-1");
  }

  public String getHost() {
    return beyondPropertiesService.getProperty(S3_HOST).orElse("storage-et1.cloud.cegedim.com");
  }

  public Integer getPort() {
    return beyondPropertiesService
        .getIntegerProperty(S3_PORT)
        .orElse(MinioClientConfigurationConstants.NO_PORT);
  }

  public String getAccessKey() {
    return beyondPropertiesService.getProperty(S3_ACCESS_KEY).orElse("nzua4ydzoc19biobbyqz");
  }

  public String getSecretKey() {
    return beyondPropertiesService
        .getProperty(S3_SECRET_ACCESS_KEY)
        .orElse("NiVJi/CZj/wH2Bbg38+1pGL+In9VvHbi8yYBlRYw");
  }

  public Long getConnectionTimeout() {
    return beyondPropertiesService
        .getLongProperty(S3_CONNECTION_TIMEOUT)
        .orElse(MinioClientConfigurationConstants.DEFAULT_S3_TIMEOUT);
  }

  public Long getWriteTimeout() {
    return beyondPropertiesService
        .getLongProperty(S3_WRITE_TIMEOUT)
        .orElse(MinioClientConfigurationConstants.DEFAULT_S3_TIMEOUT);
  }

  public Long getReadTimeout() {
    return beyondPropertiesService
        .getLongProperty(S3_READ_TIMEOUT)
        .orElse(MinioClientConfigurationConstants.DEFAULT_S3_TIMEOUT);
  }
}
