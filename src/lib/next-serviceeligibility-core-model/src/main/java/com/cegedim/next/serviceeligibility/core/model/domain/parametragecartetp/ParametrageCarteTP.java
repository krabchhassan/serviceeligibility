package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parametragesCarteTP")
@Data
public class ParametrageCarteTP implements Serializable {
  @Id private String id;

  private String amc;
  private String amcNom;

  private String identifiantCollectivite;
  private String groupePopulation;
  private String critereSecondaireDetaille;
  private ParametrageCarteTPStatut statut;
  private String dateDebutValidite;
  private LocalDateTime dateCreation;
  private LocalDateTime dateModification;
  private String userCreation;
  private String userModification;

  /** Paramétrage renouvellement */
  private ParametrageRenouvellement parametrageRenouvellement;

  /** Paramétrage des droits de carte TP */
  private ParametrageDroitsCarteTP parametrageDroitsCarteTP;

  private int priorite;

  private List<GarantieTechnique> garantieTechniques = new ArrayList<>();

  private List<String> idLots = new ArrayList<>();
}
