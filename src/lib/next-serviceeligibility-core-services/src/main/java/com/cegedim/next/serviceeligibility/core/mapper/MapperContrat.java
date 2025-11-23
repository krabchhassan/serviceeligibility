package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.model.kafka.ContratCollectif;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContexteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;

public class MapperContrat {

  private MapperContrat() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode permettant de mapper un contrat (partie commune) en contrat V6
   *
   * @param contrat contrat commun
   * @return
   */
  public static ContratAIV6 mapCommonToV6(ContratAICommun contrat) {
    return mapCommonToV6(null, contrat);
  }

  public static ContratAIV6 mapCommonToV6(ContratAIV6 existingContrat, ContratAICommun contrat) {
    ContratAIV6 contratAIV6 = existingContrat;
    if (contratAIV6 == null) {
      contratAIV6 = new ContratAIV6();
      contratAIV6.setId(contrat.getId());
      contratAIV6.setTraceId(contrat.getTraceId());
      contratAIV6.setIdDeclarant(contrat.getIdDeclarant());
      contratAIV6.setNumero(contrat.getNumero());
    }

    contratAIV6.setVersion("6");
    contratAIV6.setNumeroExterne(contrat.getNumeroExterne());
    contratAIV6.setNumeroAdherent(contrat.getNumeroAdherent());
    contratAIV6.setNumeroAdherentComplet(contrat.getNumeroAdherentComplet());
    contratAIV6.setDateSouscription(contrat.getDateSouscription());
    contratAIV6.setDateResiliation(contrat.getDateResiliation());
    contratAIV6.setApporteurAffaire(contrat.getApporteurAffaire());
    contratAIV6.setPeriodesContratResponsableOuvert(contrat.getPeriodesContratResponsableOuvert());
    contratAIV6.setCritereSecondaire(contrat.getCritereSecondaire());
    contratAIV6.setCritereSecondaireDetaille(contrat.getCritereSecondaireDetaille());
    contratAIV6.setIsContratIndividuel(contrat.getIsContratIndividuel());
    contratAIV6.setGestionnaire(contrat.getGestionnaire());
    contratAIV6.setQualification(contrat.getQualification());
    contratAIV6.setOrdrePriorisation(contrat.getOrdrePriorisation());
    contratAIV6.setSocieteEmettrice(contrat.getSocieteEmettrice());
    return contratAIV6;
  }

  public static ContratAIV6 mapV5toV6(ContratAIV5 contratV5) {
    ContratAIV6 contratAIV6 = new ContratAIV6();
    if (contratV5 != null) {
      // Données communes
      contratAIV6 = mapCommonToV6(contratV5);
      if (contratV5.getContexteTiersPayant() != null) {
        contratAIV6.setContexteTiersPayant(mapContexteTP(contratV5.getContexteTiersPayant()));
      }
      contratAIV6.setContratCollectif(mapContratCollectif(contratV5.getContratCollectif()));
      // Différences
      contratAIV6.setAssures(contratV5.getAssures());
      contratAIV6.setPeriodesContratCMUOuvert(contratV5.getPeriodesContratCMUOuvert());
      contratAIV6.setPeriodesSuspension(contratV5.getPeriodesSuspension());
    }
    return contratAIV6;
  }

  private static ContexteTPV6 mapContexteTP(ContexteTP contexteTiersPayant) {
    ContexteTPV6 contexteTPV6 = new ContexteTPV6();
    if (contexteTiersPayant != null) {
      contexteTPV6.setIsCartePapier(contexteTiersPayant.getIsCartePapier());
      contexteTPV6.setIsCarteDematerialisee(contexteTiersPayant.getIsCarteDematerialisee());
      contexteTPV6.setPeriodesDroitsCarte(contexteTiersPayant.getPeriodesDroitsCarte());
      contexteTPV6.setIsCartePapierAEditer(contexteTiersPayant.getIsCartePapierAEditer());
      contexteTPV6.setDateRestitutionCarte(contexteTiersPayant.getDateRestitutionCarte());
    }
    return contexteTPV6;
  }

  private static ContratCollectifV6 mapContratCollectif(ContratCollectif contratCollectif) {
    if (contratCollectif != null) {
      ContratCollectifV6 contratCollectifV6 = new ContratCollectifV6();
      contratCollectifV6.setNumero(contratCollectif.getNumero());
      contratCollectifV6.setNumeroExterne(contratCollectif.getNumeroExterne());
      contratCollectifV6.setRaisonSociale(Constants.N_A);
      contratCollectifV6.setSiret(null);
      contratCollectifV6.setIdentifiantCollectivite(Constants.N_A);
      contratCollectifV6.setGroupePopulation(Constants.N_A);
      return contratCollectifV6;
    }
    return null;
  }

  public static String getIdDestinataire(
      String numeroAdherent,
      String numeroPersonne,
      String typeDestinataire,
      int indiceDestinataire) {
    return String.format(
        "%s-%s-%s-%s", numeroAdherent, numeroPersonne, typeDestinataire, indiceDestinataire);
  }
}
