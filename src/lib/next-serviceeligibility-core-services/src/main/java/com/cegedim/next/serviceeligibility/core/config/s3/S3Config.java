package com.cegedim.next.serviceeligibility.core.config.s3;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.client.exceptions.S3Exception;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.filestorage.minio.client.service.MinioStorageService;
import com.cegedim.filestorage.minio.client.utility.MinioClientInitializer;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
  private final BeyondPropertiesService beyondPropertiesService;

  public S3Config(BeyondPropertiesService beyondPropertiesService) {
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public MinioClientInitializer bddMinioClientInitializer() {
    return new MinioClientInitializerImpl(beyondPropertiesService);
  }

  @Bean
  public S3StorageService bddS3StorageService(MinioClientInitializer bddMinioClientInitializer)
      throws S3Exception {
    final MinioClient minioClient = bddMinioClientInitializer.buildMinioClient();
    return new MinioStorageService(minioClient);
  }
}
