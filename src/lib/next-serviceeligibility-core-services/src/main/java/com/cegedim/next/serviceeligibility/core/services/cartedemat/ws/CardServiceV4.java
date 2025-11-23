package com.cegedim.next.serviceeligibility.core.services.cartedemat.ws;

import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperWebServiceCardV4;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponseBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponseContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4.CardResponseBeneficiaryV4;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4.CardResponseContratV4;
import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardServiceV4 extends CardService {

  private final MapperWebServiceCardV4 mapperWebServiceCardV4;

  @Override
  @ContinueSpan(log = "map contrat carte v4")
  public CardResponseContrat mapContrat(CarteDemat card) {
    CardResponseContratV4 contratV4 =
        mapperWebServiceCardV4.cardResponseContratToCardResponseContratV4(super.mapContrat(card));
    Contrat contratCarteDemat = card.getContrat();
    if (contratCarteDemat != null) {
      contratV4.setNumeroOperateur(contratCarteDemat.getNumOperateur());
      contratV4.setTypeConvention(contratCarteDemat.getTypeConvention());
      contratV4.setDateSouscription(contratCarteDemat.getDateSouscription());
      contratV4.setQualification(contratCarteDemat.getQualification());
      contratV4.setSituationParticuliere(contratCarteDemat.getSituationParticuliere());
    }
    return contratV4;
  }

  @Override
  @ContinueSpan(log = "map beneficiaires carte - core v4")
  public CardResponseBeneficiary mapBeneficiariesCore(
      BenefCarteDemat benefCarte, CardResponseBeneficiary beneficiary) {
    CardResponseBeneficiaryV4 beneficiaryV4 =
        mapperWebServiceCardV4.cardResponseBenefToCardResponseBenefV4(
            super.mapBeneficiariesCore(benefCarte, beneficiary));
    Beneficiaire benefCarteBenef = benefCarte.getBeneficiaire();
    if (benefCarteBenef != null) {
      Affiliation affiliation = benefCarteBenef.getAffiliation();

      if (affiliation != null) {
        beneficiaryV4.setRegimeOD1(affiliation.getRegimeOD1());
        beneficiaryV4.setCaisseOD1(affiliation.getCaisseOD1());
        beneficiaryV4.setCentreOD1(affiliation.getCentreOD1());
        beneficiaryV4.setRegimeOD2(affiliation.getRegimeOD2());
        beneficiaryV4.setCaisseOD2(affiliation.getCaisseOD2());
        beneficiaryV4.setCentreOD2(affiliation.getCentreOD2());
        beneficiaryV4.setHasMedecinTraitant(affiliation.getHasMedecinTraitant());
      }
    }
    return beneficiaryV4;
  }
}
