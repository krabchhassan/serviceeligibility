package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.FileFlowMetadata;

public interface FileFlowMetadataDao {
  FileFlowMetadata createFileFlowMetadata(String nomFichier, String source, String version);

  FileFlowMetadata updateFileFlowMetadata(FileFlowMetadata fileFlowMetadata);
}
