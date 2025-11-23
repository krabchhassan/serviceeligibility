package com.cegedim.next.serviceeligibility.core.model.kafka;

import com.cegedim.next.serviceeligibility.core.model.enumeration.TraceSource;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class TraceCommun {
  @Id private String id;
  private String originId;
  private LocalDateTime dateCreation;
  private LocalDateTime dateModification;
  private String message;
  private String contratAiId;
  private TraceStatus status;
  private String errorMessage;
  private String sourceObjectId;
  private String nomFichierOrigine;
  private TraceSource source;
  private Long lineNumber;
  private String fileName;
  private String idDeclarant;
  private String numeroContrat;
  private String numeroAdherent;
}
