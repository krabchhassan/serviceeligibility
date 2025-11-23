package com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheBeneficiaireService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheSegmentService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Classe d'echange entre le process et la couche service. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeInfoBeneficiaire {

  private TypeRechercheSegmentService typeRechercheSegment;
  private String segmentRecherche;
  private List<String> listeSegmentRecherche = new ArrayList<>();
  private Date dateReference;
  private Date dateFin;
  private String dateNaissance;
  private String rangNaissance;
  private String nirBeneficiaire;
  private String cleNirBneficiare;
  private String numeroPrefectoral;
  private String numeroAdherent;
  private TypeProfondeurRechercheService profondeurRecherche;
  private TypeRechercheBeneficiaireService typeRechercheBeneficiaire;
}
