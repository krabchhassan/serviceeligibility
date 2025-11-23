package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trigger")
@Data
public class Trigger {
  @Id private String id;
  private TriggerStatus status;
  private TriggerEmitter origine;
  private String dateEffet;
  private Date dateFinStandBy;
  private Date dateDebutTraitement;
  private Date dateFinTraitement;
  private String nomFichierOrigine;

  private Integer nbBenef = 0;
  private Integer nbBenefKO = 0;
  private Integer nbBenefWarning = 0;
  private Integer nbBenefToProcess = 0;

  private String amc;
  private boolean isRdo;

  private List<RecyclingPeriods> periodes = new ArrayList<>();
  private boolean eventReprise;
  private boolean exported;

  /** Données d'audit */
  private LocalDateTime dateCreation;

  private LocalDateTime dateModification;
  private String userCreation;
  private String userModification;
  private String dateRestitution;

  private List<String> benefsToRecycle = new ArrayList<>();
}
