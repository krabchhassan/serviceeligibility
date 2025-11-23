package com.cegedim.next.serviceeligibility.core.business.serviceprestation.service;

import com.cegedim.next.serviceeligibility.core.model.entity.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.entity.ContratV6;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV5;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.ContratCollectif;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContexteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import java.util.ArrayList;
import java.util.List;

public class MapperServicePrestation {

  private MapperServicePrestation() {
    throw new IllegalStateException("Utility class");
  }

  public static ContratV5 mapContratV6ToV5(ContratV6 contratV6) {
    ContratV5 contratV5 = new ContratV5();
    List<ServicePrestationV5> contrats = new ArrayList<>();
    contratV5.setContrats(contrats);
    for (ServicePrestationV6 servicePrestationV6 : contratV6.getContrats()) {
      contrats.add(mapServicePrestationV6ToV5(servicePrestationV6));
    }
    return contratV5;
  }

  public static ServicePrestationV5 mapServicePrestationV6ToV5(
      ServicePrestationV6 servicePrestationV6) {
    ServicePrestationV5 servicePrestationV5 = null;
    if (servicePrestationV6 != null) {
      servicePrestationV5 = mapCommonToV5(servicePrestationV6);
      if (servicePrestationV6.getContexteTiersPayant() != null) {
        servicePrestationV5.setContexteTiersPayant(
            mapContexteTP(
                servicePrestationV6.getContexteTiersPayant(),
                servicePrestationV6.getContratCollectif()));
      }
      servicePrestationV5.setContratCollectif(
          mapContratCollectif(servicePrestationV6.getContratCollectif()));
      servicePrestationV5.setAssure(servicePrestationV6.getAssure());
      servicePrestationV5.setPeriodesContratCMUOuvert(
          servicePrestationV6.getPeriodesContratCMUOuvert());
      servicePrestationV5.setPeriodesSuspension(servicePrestationV6.getPeriodesSuspension());
    }
    return servicePrestationV5;
  }

  private static ServicePrestationV5 mapCommonToV5(ServicePrestationV6 servicePrestationV6) {
    ServicePrestationV5 servicePrestationV5 = new ServicePrestationV5();
    servicePrestationV5.setIdDeclarant(servicePrestationV6.getIdDeclarant());
    servicePrestationV5.setNumero(servicePrestationV6.getNumero());
    servicePrestationV5.setNumeroExterne(servicePrestationV6.getNumeroExterne());
    servicePrestationV5.setNumeroAdherent(servicePrestationV6.getNumeroAdherent());
    servicePrestationV5.setNumeroAdherentComplet(servicePrestationV6.getNumeroAdherentComplet());
    servicePrestationV5.setDateSouscription(servicePrestationV6.getDateSouscription());
    servicePrestationV5.setDateResiliation(servicePrestationV6.getDateResiliation());
    servicePrestationV5.setApporteurAffaire(servicePrestationV6.getApporteurAffaire());
    servicePrestationV5.setPeriodesContratResponsableOuvert(
        servicePrestationV6.getPeriodesContratResponsableOuvert());
    servicePrestationV5.setCritereSecondaire(servicePrestationV6.getCritereSecondaire());
    servicePrestationV5.setCritereSecondaireDetaille(
        servicePrestationV6.getCritereSecondaireDetaille());
    servicePrestationV5.setIsContratIndividuel(servicePrestationV6.getIsContratIndividuel());
    servicePrestationV5.setGestionnaire(servicePrestationV6.getGestionnaire());
    servicePrestationV5.setQualification(servicePrestationV6.getQualification());
    servicePrestationV5.setOrdrePriorisation(servicePrestationV6.getOrdrePriorisation());
    servicePrestationV5.setSocieteEmettrice(servicePrestationV6.getSocieteEmettrice());
    return servicePrestationV5;
  }

  private static ContexteTP mapContexteTP(
      ContexteTPV6 contexteTPV6, ContratCollectifV6 contratCollectifV6) {
    ContexteTP contexteTP = new ContexteTP();

    if (contexteTPV6 != null) {
      contexteTP.setIsCartePapier(contexteTPV6.getIsCartePapier());
      contexteTP.setIsCarteDematerialisee(contexteTPV6.getIsCarteDematerialisee());
      contexteTP.setPeriodesDroitsCarte(contexteTPV6.getPeriodesDroitsCarte());
      contexteTP.setIsCartePapierAEditer(contexteTPV6.getIsCartePapierAEditer());
      contexteTP.setDateRestitutionCarte(contexteTPV6.getDateRestitutionCarte());
      if (contratCollectifV6 != null) {
        contexteTP.setCollectivite(contratCollectifV6.getIdentifiantCollectivite());
        contexteTP.setCollege(contratCollectifV6.getGroupePopulation());
      }
    }
    return contexteTP;
  }

  private static ContratCollectif mapContratCollectif(ContratCollectifV6 contratCollectifV6) {
    ContratCollectif contratCollectif = new ContratCollectif();
    if (contratCollectifV6 != null) {
      contratCollectif.setNumero(contratCollectifV6.getNumero());
      contratCollectif.setNumeroExterne(contratCollectifV6.getNumeroExterne());
    }
    return contratCollectif;
  }

  public static ContratAIV5 mapContratAIV6ToV5(ContratAIV6 contratAIV6) {
    ContratAIV5 contratAIV5 = null;
    if (contratAIV6 != null) {
      contratAIV5 = mapCommonToV5(contratAIV6);
      if (contratAIV6.getContexteTiersPayant() != null) {
        contratAIV5.setContexteTiersPayant(
            mapContexteTP(contratAIV6.getContexteTiersPayant(), contratAIV6.getContratCollectif()));
      }
      contratAIV5.setContratCollectif(mapContratCollectif(contratAIV6.getContratCollectif()));
      contratAIV5.setAssures(contratAIV6.getAssures());
      contratAIV5.setPeriodesContratCMUOuvert(contratAIV6.getPeriodesContratCMUOuvert());
      contratAIV5.setPeriodesSuspension(contratAIV6.getPeriodesSuspension());
    }
    return contratAIV5;
  }

  private static ContratAIV5 mapCommonToV5(ContratAIV6 contratAIV6) {
    ContratAIV5 contratAIV5 = new ContratAIV5();
    contratAIV5.setId(contratAIV6.getId());
    contratAIV5.setTraceId(contratAIV6.getTraceId());
    contratAIV5.setIdDeclarant(contratAIV6.getIdDeclarant());
    contratAIV5.setNumero(contratAIV6.getNumero());
    contratAIV5.setNumeroExterne(contratAIV6.getNumeroExterne());
    contratAIV5.setNumeroAdherent(contratAIV6.getNumeroAdherent());
    contratAIV5.setNumeroAdherentComplet(contratAIV6.getNumeroAdherentComplet());
    contratAIV5.setDateSouscription(contratAIV6.getDateSouscription());
    contratAIV5.setDateResiliation(contratAIV6.getDateResiliation());
    contratAIV5.setApporteurAffaire(contratAIV6.getApporteurAffaire());
    contratAIV5.setPeriodesContratResponsableOuvert(
        contratAIV6.getPeriodesContratResponsableOuvert());
    contratAIV5.setCritereSecondaire(contratAIV6.getCritereSecondaire());
    contratAIV5.setCritereSecondaireDetaille(contratAIV6.getCritereSecondaireDetaille());
    contratAIV5.setIsContratIndividuel(contratAIV6.getIsContratIndividuel());
    contratAIV5.setGestionnaire(contratAIV6.getGestionnaire());
    contratAIV5.setQualification(contratAIV6.getQualification());
    contratAIV5.setOrdrePriorisation(contratAIV6.getOrdrePriorisation());
    contratAIV5.setSocieteEmettrice(contratAIV6.getSocieteEmettrice());
    return contratAIV5;
  }
}
