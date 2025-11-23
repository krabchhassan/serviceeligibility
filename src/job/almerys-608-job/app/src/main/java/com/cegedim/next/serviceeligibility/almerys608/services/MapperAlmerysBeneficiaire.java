package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsExtractions;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
@RequiredArgsConstructor
public class MapperAlmerysBeneficiaire {

  private final UtilService utilService;

  public void mapBeneficiaire(
      TmpObject2 tmpObject2,
      final List<Transcodage> typeBeneficiaireTranscoList,
      final List<Transcodage> codeMouvementTranscoList,
      List<Beneficiaire> beneficiaireList,
      List<Rejet> rejetBulk,
      Date dateDerniereExecution,
      HistoriqueExecution608 historiqueExecution608) {
    Beneficiaire beneficiaire = new Beneficiaire();
    boolean fromHistory =
        tmpObject2.getEffetDebut() != null
            && dateDerniereExecution != null
            && tmpObject2.getEffetDebut().compareTo(dateDerniereExecution) < 0;
    if (Constants.QUALITE_A.equals(tmpObject2.getBeneficiaire().getAffiliation().getQualite())
        || dateDerniereExecution == null
        || tmpObject2.getEffetDebut().compareTo(dateDerniereExecution) >= 0) {

      String isCarteTPaEditer = "0";
      if (!fromHistory && tmpObject2.isCarteTPaEditer()) {
        isCarteTPaEditer = "1";
      }
      rejetA04(tmpObject2, typeBeneficiaireTranscoList, rejetBulk, beneficiaire);
      rejetA05(tmpObject2, codeMouvementTranscoList, rejetBulk, beneficiaire, isCarteTPaEditer);

      beneficiaire.setNumeroContrat(UtilService.mapRefNumContrat(tmpObject2));
      beneficiaire.setRefInterneOS(tmpObject2.getBeneficiaire().getNumeroPersonne());
      beneficiaire.setContratResponsable(
          Boolean.TRUE.equals(tmpObject2.getContrat().getIsContratResponsable()) ? 1 : 0);
      beneficiaire.setCodeGrandRegime(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getRegimeOD1(), 2));
      beneficiaire.setNni(tmpObject2.getBeneficiaire().getNirOd1());
      beneficiaire.setCentreSS(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getCentreOD1(), 4));
      beneficiaire.setCodeCaisseRo(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getCaisseOD1(), 3));
      beneficiaire.setCodeGrandRegime2(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getRegimeOD2(), 2));
      beneficiaire.setCentreSS2(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getCentreOD2(), 4));
      beneficiaire.setCodeCaisseRo2(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getCaisseOD2(), 3));
      beneficiaire.setNni2(tmpObject2.getBeneficiaire().getNirOd2());
      beneficiaire.setNoemise(
          Boolean.TRUE.equals(tmpObject2.getBeneficiaire().getAffiliation().getIsTeleTransmission())
              ? 1
              : 0);
      beneficiaire.setFondCarte(
          UtilService.resizeField(tmpObject2.getContrat().getFondCarte(), 10));
      beneficiaire.setAnnexe1Carte(
          UtilService.resizeField(tmpObject2.getContrat().getAnnexe1Carte(), 10));
      beneficiaire.setAnnexe2Carte(
          UtilService.resizeField(tmpObject2.getContrat().getAnnexe2Carte(), 10));
      beneficiaireList.add(beneficiaire);

      historiqueExecution608.incNombreMouvementCarte(1);
      if (beneficiaire.getCodeMouvementCarte() != null) {
        switch (beneficiaire.getCodeMouvementCarte()) {
          case "CC":
            historiqueExecution608.incNombreMouvementCC(1);
            break;
          case "CS":
            historiqueExecution608.incNombreMouvementCS(1);
            break;
          case "MC":
            historiqueExecution608.incNombreMouvementMC(1);
            break;
          case "MS":
            historiqueExecution608.incNombreMouvementMS(1);
            break;
          case "RC":
            historiqueExecution608.incNombreMouvementRC(1);
            break;
          case "RS":
            historiqueExecution608.incNombreMouvementRS(1);
            break;
          default:
            log.error("Code mouvement inconnu {}", beneficiaire.getCodeMouvementCarte());
            break;
        }
      } else {
        log.error("Code mouvement non paramétré");
      }
    }
  }

  /** Transcodage et rejet en fonction de la qualite en event ou type assure en tdb */
  private void rejetA04(
      TmpObject2 tmpObject2,
      List<Transcodage> typeBeneficiaireTranscoList,
      List<Rejet> rejetBulk,
      Beneficiaire beneficiaire) {
    Affiliation affiliation = tmpObject2.getBeneficiaire().getAffiliation();
    if (affiliation.getTypeAssure() != null) {
      String type =
          UtilService.isHTP(tmpObject2) ? affiliation.getQualite() : affiliation.getTypeAssure();
      typeBeneficiaireTranscoList.stream()
          .filter(t -> t.getCle().contains(type))
          .map(Transcodage::getCodeTransco)
          .findFirst()
          .ifPresentOrElse(
              beneficiaire::setTypeBenef,
              () -> // rejet A04
              rejetBulk.add(
                      utilService.getRejet(
                          tmpObject2,
                          UtilService.mapRefNumContrat(tmpObject2),
                          ConstantesRejetsExtractions.REJET_A04.getCode(),
                          ConstantesRejetsExtractions.REJET_A04.toString(type))));
    }
  }

  private void rejetA05(
      TmpObject2 tmpObject2,
      List<Transcodage> codeMouvementTranscoList,
      List<Rejet> rejetBulk,
      Beneficiaire beneficiaire,
      String isCarteTPaEditer) {
    if (tmpObject2.getBeneficiaire().getAffiliation().getTypeAssure() != null) {
      Optional<String> code =
          codeMouvementTranscoList.stream()
              .filter(
                  t -> {
                    List<String> cles = t.getCle();
                    if (!CollectionUtils.isEmpty(cles) && cles.size() == 3) {
                      return (cles.get(0)
                              .equals(
                                  tmpObject2
                                      .getDomaineDroit()
                                      .getPeriodeDroit()
                                      .getMotifEvenement())
                          && cles.get(1)
                              .equals(
                                  tmpObject2.getDomaineDroit().getPeriodeDroit().getModeObtention())
                          && cles.get(2).equals(isCarteTPaEditer));
                    }
                    return false;
                  })
              .map(Transcodage::getCodeTransco)
              .findFirst();
      if (code.isPresent()) {
        beneficiaire.setCodeMouvementCarte(code.get());
      } else {
        // rejet A05
        rejetBulk.add(
            utilService.getRejet(
                tmpObject2,
                UtilService.mapRefNumContrat(tmpObject2),
                ConstantesRejetsExtractions.REJET_A05.getCode(),
                ConstantesRejetsExtractions.REJET_A05.toString(
                    tmpObject2.getDomaineDroit().getPeriodeDroit().getMotifEvenement()
                        + "|"
                        + tmpObject2.getDomaineDroit().getPeriodeDroit().getModeObtention()
                        + "|"
                        + isCarteTPaEditer)));
      }
    }
  }
}
