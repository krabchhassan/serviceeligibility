package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.InfosSouscripteur;
import com.cegedim.next.serviceeligibility.almerys608.model.Souscripteur;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.*;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/** BLUE-6958 : AG2R/Almerys : Gestion du souscripteur du contrat */
public class SouscripteurService {

  public Souscripteur findSouscripteur(
      List<InfosSouscripteur> infosSouscripteurList, String numeroContrat) {
    return findAllAdherent(infosSouscripteurList, numeroContrat).stream()
        .filter(Souscripteur::isSouscripteur)
        .findFirst()
        .orElseThrow(RuntimeException::new);
  }

  List<Souscripteur> findAllAdherent(
      List<InfosSouscripteur> infosSouscripteurList, String numeroContrat) {
    // cas htp où les infos sont bien remplis depuis la génération des droitsTP
    long nbreSouscripteur =
        infosSouscripteurList.stream()
            .filter(infosSouscripteur -> Boolean.TRUE.equals(infosSouscripteur.isSouscripteur()))
            .count();
    if (nbreSouscripteur == 1) {
      return getSouscripteurFromHTP(infosSouscripteurList, numeroContrat);
    }
    List<Souscripteur> souscripteurList = new ArrayList<>();
    int nombreSouscripteur =
        getSouscripteurFromTalendRule(infosSouscripteurList, numeroContrat, souscripteurList);
    if (nombreSouscripteur == 1) {
      return souscripteurList;
    } else if (nombreSouscripteur == 0) {
      List<InfosSouscripteur> benefQualiteA =
          infosSouscripteurList.stream()
              .filter(infosSouscripteur -> Constants.QUALITE_A.equals(infosSouscripteur.qualite()))
              .toList();
      if (!CollectionUtils.isEmpty(benefQualiteA) && benefQualiteA.size() == 1) {
        souscripteurList.stream()
            .filter(
                souscripteur ->
                    souscripteur.getRefInterneOs().equals(benefQualiteA.get(0).numeroPersonne()))
            .forEach(
                souscripteur -> {
                  souscripteur.setSouscripteur(true);
                  souscripteur.setPosition("01");
                });
        return souscripteurList;
      }
      List<InfosSouscripteur> benefQualiteC =
          infosSouscripteurList.stream()
              .filter(infosSouscripteur -> Constants.QUALITE_C.equals(infosSouscripteur.qualite()))
              .toList();
      if (!CollectionUtils.isEmpty(benefQualiteC) && benefQualiteC.size() == 1) {
        souscripteurList.stream()
            .filter(
                souscripteur ->
                    souscripteur.getRefInterneOs().equals(benefQualiteC.get(0).numeroPersonne()))
            .forEach(
                souscripteur -> {
                  souscripteur.setSouscripteur(true);
                  souscripteur.setPosition("01");
                });
        return souscripteurList;
      }
      getSouscripteursByRightsEnd(infosSouscripteurList, souscripteurList);
      return souscripteurList;
    } else {
      List<String> allSouscripteur =
          souscripteurList.stream()
              .filter(Souscripteur::isSouscripteur)
              .map(Souscripteur::getRefInterneOs)
              .toList();
      // remise des souscripteurs à false :
      souscripteurList.forEach(
          souscripteur -> {
            souscripteur.setPosition("02");
            souscripteur.setSouscripteur(false);
          });
      getSouscripteursByRightsEnd(
          infosSouscripteurList.stream()
              .filter(
                  infosSouscripteur -> allSouscripteur.contains(infosSouscripteur.numeroPersonne()))
              .toList(),
          souscripteurList);
      return souscripteurList;
    }
  }

  private static void getSouscripteursByRightsEnd(
      List<InfosSouscripteur> infosSouscripteurList, List<Souscripteur> souscripteurList) {
    Stream<InfosSouscripteur> streamNullEndDate =
        infosSouscripteurList.stream()
            .filter(infosSouscripteur -> infosSouscripteur.dateFinDroit() == null);
    long nbreFinNull = streamNullEndDate.count();
    if (nbreFinNull == 1) {
      // il n'y a que 1 enregistrement avec date de fin à null, donc ok
      InfosSouscripteur infosSouscripteur =
          infosSouscripteurList.stream()
              .filter(infosSouscripteur1 -> infosSouscripteur1.dateFinDroit() == null)
              .toList()
              .get(0);
      souscripteurList.stream()
          .filter(
              souscripteur ->
                  souscripteur.getRefInterneOs().equals(infosSouscripteur.numeroPersonne()))
          .forEach(
              souscripteur -> {
                souscripteur.setSouscripteur(true);
                souscripteur.setPosition("01");
              });
    } else if (nbreFinNull > 1) {
      // il y a plusieurs enregistrements avec la date de fin null
      getSouscripteursByAdministrativeRank(
          infosSouscripteurList.stream()
              .filter(infosSouscripteur1 -> infosSouscripteur1.dateFinDroit() == null)
              .toList(),
          souscripteurList);
    } else {
      String maxEndDate =
          infosSouscripteurList.stream()
              .flatMap(infosSouscripteur -> infosSouscripteur.dateFinDroit().stream())
              .filter(StringUtils::isNotBlank)
              .max(String::compareTo)
              .orElse(null);
      Stream<InfosSouscripteur> stream =
          infosSouscripteurList.stream()
              .filter(
                  infosSouscripteur ->
                      infosSouscripteur.dateFinDroit() != null
                          && infosSouscripteur.dateFinDroit().contains(maxEndDate));
      long nombreMaxEndDate = stream.count();
      if (nombreMaxEndDate == 1) {
        // il n'y a que 1 enregistrement avec date de fin maximum, donc ok
        InfosSouscripteur infosSouscripteur =
            infosSouscripteurList.stream()
                .filter(
                    infosSouscripteur1 ->
                        infosSouscripteur1.dateFinDroit() != null
                            && infosSouscripteur1.dateFinDroit().contains(maxEndDate))
                .toList()
                .get(0);
        souscripteurList.stream()
            .filter(
                souscripteur ->
                    souscripteur.getRefInterneOs().equals(infosSouscripteur.numeroPersonne()))
            .forEach(
                souscripteur -> {
                  souscripteur.setSouscripteur(true);
                  souscripteur.setPosition("01");
                });
      } else {
        // il y a plusieurs enregistrements avec la même date
        getSouscripteursByAdministrativeRank(
            infosSouscripteurList.stream()
                .filter(
                    infosSouscripteur1 ->
                        infosSouscripteur1.dateFinDroit() != null
                            && infosSouscripteur1.dateFinDroit().contains(maxEndDate))
                .toList(),
            souscripteurList);
      }
    }
  }

  private static void getSouscripteursByAdministrativeRank(
      List<InfosSouscripteur> infosSouscripteurList, List<Souscripteur> souscripteurList) {
    if (infosSouscripteurList.stream().map(InfosSouscripteur::rangAdministratif).distinct().count()
        > 1) {
      // all are different
      Optional<String> minRank =
          infosSouscripteurList.stream()
              .map(InfosSouscripteur::rangAdministratif)
              .min(String::compareTo);
      InfosSouscripteur infosSouscripteur =
          infosSouscripteurList.stream()
              .filter(
                  infosSouscripteur1 ->
                      minRank.get().equals(infosSouscripteur1.rangAdministratif()))
              .toList()
              .get(0);
      souscripteurList.stream()
          .filter(
              souscripteur ->
                  souscripteur.getRefInterneOs().equals(infosSouscripteur.numeroPersonne()))
          .forEach(
              souscripteur -> {
                souscripteur.setSouscripteur(true);
                souscripteur.setPosition("01");
              });
    } else {
      getSouscripteursNumeroPersonne(infosSouscripteurList, souscripteurList);
    }
  }

  private static void getSouscripteursNumeroPersonne(
      List<InfosSouscripteur> infosSouscripteurList, List<Souscripteur> souscripteurList) {
    Optional<String> minNumeroPersonne =
        infosSouscripteurList.stream()
            .map(InfosSouscripteur::numeroPersonne)
            .min(String::compareTo);
    minNumeroPersonne.ifPresent(
        s ->
            souscripteurList.stream()
                .filter(souscripteur -> souscripteur.getRefInterneOs().equals(s))
                .forEach(
                    souscripteur -> {
                      souscripteur.setSouscripteur(true);
                      souscripteur.setPosition("01");
                    }));
  }

  private static List<Souscripteur> getSouscripteurFromHTP(
      List<InfosSouscripteur> infosSouscripteurList, String numeroContrat) {
    List<Souscripteur> souscripteurList = new ArrayList<>();

    for (InfosSouscripteur infosSouscripteur : infosSouscripteurList) {
      Souscripteur souscripteur = new Souscripteur();
      souscripteur.setNumeroContrat(numeroContrat);
      souscripteur.setRefInterneOs(infosSouscripteur.numeroPersonne());
      if (Boolean.TRUE.equals(infosSouscripteur.isSouscripteur())) {
        souscripteur.setSouscripteur(true);
        souscripteur.setPosition("01");
      } else {
        souscripteur.setSouscripteur(false);
        souscripteur.setPosition("02");
      }
      souscripteurList.add(souscripteur);
    }
    return souscripteurList;
  }

  private static int getSouscripteurFromTalendRule(
      List<InfosSouscripteur> infosSouscripteurList,
      String numeroContrat,
      List<Souscripteur> souscripteurList) {
    int nombreSouscripteur = 0;
    for (InfosSouscripteur infosSouscripteur : infosSouscripteurList) {
      Souscripteur souscripteur = new Souscripteur();
      souscripteur.setNumeroContrat(numeroContrat);
      souscripteur.setRefInterneOs(infosSouscripteur.numeroPersonne());
      if (!Constants.QUALITE_A.equals(infosSouscripteur.qualite())
          || (!Constants.CODE_ETAT_VALIDE.equals(infosSouscripteur.codeEtat())
              || infosSouscripteur.dateRadiation() != null)) {
        souscripteur.setSouscripteur(false);
        souscripteur.setPosition("02");
      } else {
        souscripteur.setSouscripteur(true);
        souscripteur.setPosition("01");
        nombreSouscripteur++;
      }
      souscripteurList.add(souscripteur);
    }
    return nombreSouscripteur;
  }
}
