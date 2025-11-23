package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CodePeriode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinataireRelevePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Teletransmission;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "triggeredBeneficiary")
@Data
public class TriggeredBeneficiary {
  @Id private String id;
  private String idTrigger;
  private String parametrageCarteTPId;

  private TriggeredBeneficiaryStatusEnum statut;
  private TriggeredBeneficiaryAnomaly derniereAnomalie;
  private List<TriggeredBeneficiaryStatus> historiqueStatuts = new ArrayList<>();

  // Info contrat Service Prestation
  private String servicePrestationId;
  private String numeroContrat;
  private Boolean isContratIndividuel;
  private String collectivite;
  private String college;
  private String critereSecondaireDetaille;
  private String critereSecondaire;

  private boolean isSouscripteurAlmv3 = false;
  private String nomPorteur;
  private String prenomPorteur;
  private String civilitePorteur;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String qualification;
  private String numeroContratCollectif;

  private String identifiantCollectivite;
  private String raisonSociale;
  private String siret;
  private String groupePopulation;

  private String numeroExterneContratIndividuel;
  private String numeroExterneContratCollectif;
  private String gestionnaire;

  // hotfix/BLUE-4943
  private ServicePrestationTriggerBenef newContract;
  private ServicePrestationTriggerBenef oldContract;

  private int nbDeclarationsOuverture = 0;
  private int nbDeclarationsFermeture = 0;

  // Info assure du contrat Service Prestation
  private String dateNaissance;
  private String rangNaissance;
  private String amc;
  private String numeroPersonne;
  private String refExternePersonne;
  private String nom;
  private String nomPatronymique;
  private String nomMarital;
  private String prenom;
  private String civilite;

  private String nir;
  private String cleNir;
  private List<NirRattachementRO> affiliationsRO;

  private String periodeDebutAffiliation;
  private String periodeFinAffiliation;

  private String qualite;
  private Boolean hasMedecinTraitant;
  private String codeRegimeParticulier;
  private Boolean isBeneficiaireACS;
  private Boolean isTeleTransmission;
  private String typeAssure;
  private AdresseAssure adresse;
  private Contact contact;
  private String rangAdministratif;
  private String modePaiementPrestations;
  private String ordrePriorisation;

  private String parametreAction;

  private Boolean isCartePapierAEditer;
  private Boolean isCarteDematerialisee;
  private Boolean isCartePapier;

  private String etatSuspension;

  private List<DestinataireRelevePrestations> destinatairesRelevePrestation;

  private List<Periode> periodesMedecinTraitant;
  private List<Teletransmission> teletransmissions;
  private List<CodePeriode> regimesParticuliers;
  private List<CodePeriode> situationsParticulieres;
}
