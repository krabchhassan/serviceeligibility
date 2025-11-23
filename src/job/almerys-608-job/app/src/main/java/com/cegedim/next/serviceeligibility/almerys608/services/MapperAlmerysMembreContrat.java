package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.MembreContrat;
import com.cegedim.next.serviceeligibility.almerys608.model.Souscripteur;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.CodeModePaiement;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.CodeRegime;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class MapperAlmerysMembreContrat {
  public void mapMembreContrat(
      TmpObject2 tmpObject2,
      List<String> processedKeys,
      List<BulkObject> membreContratsList,
      List<Transcodage> modePaiementTranscoList,
      Souscripteur souscripteur) {
    String refInterneOs = tmpObject2.getBeneficiaire().getNumeroPersonne();
    String membreContratKey = tmpObject2.getContrat().getNumero() + "#" + refInterneOs;
    if (!processedKeys.contains(membreContratKey)) {
      // process membreContrat
      MembreContrat membreContrat = new MembreContrat();
      membreContrat.setNumeroContrat(UtilService.mapRefNumContrat(tmpObject2));
      setSouscripteurPosition(tmpObject2, souscripteur, membreContrat);

      membreContrat.setTypeRegime(CodeRegime.RC.value());
      membreContrat.setDateEntree(tmpObject2.getContrat().getDateSouscription()); // yyyy/MM/dd
      membreContrat.setRefInterneOs(refInterneOs);
      membreContrat.setDateNaissance(
          DateUtils.formatDate(
              DateUtils.parseLocalDate(
                  tmpObject2.getBeneficiaire().getDateNaissance(), DateUtils.YYYYMMDD),
              DateUtils.FORMATTER)); // yyyy-MM-dd
      membreContrat.setRangNaissance(tmpObject2.getBeneficiaire().getRangNaissance());
      membreContrat.setNomPatronimique(
          UtilService.resizeField(
              tmpObject2.getBeneficiaire().getAffiliation().getNomPatronymique(), 80));
      membreContrat.setNomUsage(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getNom(), 80));
      membreContrat.setPrenom(
          UtilService.resizeField(tmpObject2.getBeneficiaire().getAffiliation().getPrenom(), 40));
      membreContrat.setMedecinTraitant(
          Boolean.TRUE.equals(tmpObject2.getBeneficiaire().getAffiliation().getHasMedecinTraitant())
              ? 1
              : 0);
      membreContrat.setAutonome(
          !Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration())
                  && Boolean.TRUE.equals(
                      tmpObject2.getBeneficiaire().getAffiliation().getIsTeleTransmission())
              ? 1
              : 0);

      if (StringUtils.isNotBlank(tmpObject2.getContrat().getModePaiementPrestations())) {
        String codeTransco =
            modePaiementTranscoList.stream()
                .filter(
                    t -> t.getCle().contains(tmpObject2.getContrat().getModePaiementPrestations()))
                .map(Transcodage::getCodeTransco)
                .findFirst()
                .orElse(tmpObject2.getContrat().getModePaiementPrestations());
        membreContrat.setModePaiement(codeTransco);
      } else {
        membreContrat.setModePaiement(CodeModePaiement.AU.value());
      }

      membreContrat.setNni(tmpObject2.getBeneficiaire().getNirOd1());
      membreContrat.setNniRatt(tmpObject2.getBeneficiaire().getNirOd2());
      membreContrat.setRegimeSpecial(
          StringUtils.defaultIfBlank(
              tmpObject2.getBeneficiaire().getAffiliation().getRegimeParticulier(), null));
      if (Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration())) {
        membreContrat.setDateRadiation(tmpObject2.getBeneficiaire().getDateRadiation());
      } else {
        // TODO Format de date htp otp date / string
        membreContrat.setDateRadiation(
            Constants.QUALITE_A.equals(tmpObject2.getBeneficiaire().getAffiliation().getQualite())
                ? tmpObject2.getBeneficiaire().getDateRadiation()
                : null);
      }
      membreContratsList.add(membreContrat);
      processedKeys.add(membreContratKey);
    }
  }

  private void setSouscripteurPosition(
      TmpObject2 tmpObject2, Souscripteur souscripteur, MembreContrat membreContrat) {
    if (souscripteur.getRefInterneOs().equals(tmpObject2.getBeneficiaire().getNumeroPersonne())) {
      membreContrat.setSouscripteur(1);
      membreContrat.setPosition("01");
    } else {
      membreContrat.setSouscripteur(0);
      membreContrat.setPosition("02");
    }
  }
}
