package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.dao.FileFlowMetadataDao;
import com.cegedim.next.serviceeligibility.core.model.entity.FileFlowMetadata;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileFlowMetadataService {
  @Autowired private FileFlowMetadataDao dao;

  public FileFlowMetadata createFileFlowMetadata(String nomFichier, String source, String version) {
    return dao.createFileFlowMetadata(nomFichier, source, version);
  }

  @ContinueSpan(log = "updateFileFlowMetadata")
  public FileFlowMetadata updateFileFlowMetadata(FileFlowMetadata fileFlowMetadata) {
    return dao.updateFileFlowMetadata(fileFlowMetadata);
  }
}
