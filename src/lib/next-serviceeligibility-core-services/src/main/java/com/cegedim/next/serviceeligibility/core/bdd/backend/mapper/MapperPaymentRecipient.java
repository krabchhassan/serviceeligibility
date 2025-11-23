package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.InsuredPeriodDto;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.NomDestinataire;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.PeriodeDestinataire;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Address;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.BenefitPaymentMode;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.NameCorporate;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperPaymentRecipient {
  @ContinueSpan(log = "mapNameCorporate")
  public NameCorporate mapNameCorporate(final NomDestinataire nom) {
    final NameCorporate name = new NameCorporate();
    if (nom != null) {
      name.setFirstName(nom.getPrenom());
      name.setLastName(nom.getNomFamille());
      name.setCommonName(nom.getNomUsage());
      name.setCivility(nom.getCivilite());
      name.setCorporateName(nom.getRaisonSociale());
    }
    return name;
  }

  @ContinueSpan(log = "mapAdress")
  public Address mapAdress(final Adresse adresse) {
    final Address address = new Address();
    if (adresse != null) {
      address.setLine1(adresse.getLigne1());
      address.setLine2(adresse.getLigne2());
      address.setLine3(adresse.getLigne3());
      address.setLine4(adresse.getLigne4());
      address.setLine5(adresse.getLigne5());
      address.setLine6(adresse.getLigne6());
      address.setLine7(adresse.getLigne7());
      address.setPostcode(adresse.getCodePostal());
    }
    return address;
  }

  @ContinueSpan(log = "mapPayment")
  public BenefitPaymentMode mapPayment(final ModePaiement modePaiement) {
    final BenefitPaymentMode paymentRecipient = new BenefitPaymentMode();
    if (modePaiement != null) {
      paymentRecipient.setCode(modePaiement.getCode());
      paymentRecipient.setLabel(modePaiement.getLibelle());
      paymentRecipient.setCurrencyCode(modePaiement.getCodeMonnaie());
    }
    return paymentRecipient;
  }

  @ContinueSpan(log = "mapPeriod")
  public Period mapPeriod(PeriodeDestinataire periode) {
    final Period period = new Period();
    if (periode != null) {
      period.setStart(periode.getDebut());
      period.setEnd(periode.getFin());
    }
    return period;
  }

  public List<InsuredPeriodDto> mapInsuredPeriods(List<Periode> periodes) {
    List<InsuredPeriodDto> insuredPeriods = new ArrayList<>();
    for (Periode periode : periodes) {
      InsuredPeriodDto insuredPeriod = new InsuredPeriodDto();
      insuredPeriod.setStart(periode.getDebut());
      insuredPeriod.setEnd(periode.getFin());
      insuredPeriods.add(insuredPeriod);
    }
    return insuredPeriods;
  }
}
