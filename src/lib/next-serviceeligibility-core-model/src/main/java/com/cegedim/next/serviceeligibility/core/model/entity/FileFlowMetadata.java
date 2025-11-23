package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.enumeration.StatutFileFlowMetaData;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fileFlowMetadata")
public class FileFlowMetadata {

  @Id private String id;
  private Long nbLignesLues;
  private Long nbLignesIntegrees;
  private Long nbLignesRejetees;
  private String nomFichier;
  private StatutFileFlowMetaData statut;
  private String source;
  private String version;
  private String nomFichierARL;
  private LocalDateTime debutTraitement;
  private LocalDateTime finTraitement;
  private List<String> tempCollections;
}
