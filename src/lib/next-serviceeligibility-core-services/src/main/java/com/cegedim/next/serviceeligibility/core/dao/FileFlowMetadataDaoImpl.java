package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.FileFlowMetadata;
import com.cegedim.next.serviceeligibility.core.model.enumeration.StatutFileFlowMetaData;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FileFlowMetadataDaoImpl implements FileFlowMetadataDao {

  @Autowired private MongoTemplate template;

  @Override
  @ContinueSpan(log = "createFileFlowMetadata")
  public FileFlowMetadata createFileFlowMetadata(String nomFichier, String source, String version) {

    FileFlowMetadata fileFlowMetadata = new FileFlowMetadata();
    fileFlowMetadata.setNomFichier(nomFichier);
    fileFlowMetadata.setSource(source);
    fileFlowMetadata.setVersion(version);
    fileFlowMetadata.setDebutTraitement(LocalDateTime.now(ZoneOffset.UTC));
    fileFlowMetadata.setStatut(StatutFileFlowMetaData.Pending);

    return template.save(fileFlowMetadata, Constants.FILE_FLOW_METADATA_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "updateFileFlowMetadata")
  public FileFlowMetadata updateFileFlowMetadata(FileFlowMetadata fileFlowMetadata) {
    return template.save(fileFlowMetadata, Constants.FILE_FLOW_METADATA_COLLECTION);
  }
}
