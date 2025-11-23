package com.cegedim.next.serviceeligibility.core.business.digitalcontractinfos;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.ConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.DomainDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.PaymentRecipientDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.digitalcontractinformations.RegroupedDomainDto;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.RattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.util.ArrayList;
import java.util.List;

public class MapperDigitalContractInfos {

  private MapperDigitalContractInfos() {
    throw new IllegalStateException("Utility class");
  }

  public static List<PaymentRecipientDto> mapPaymentRecipients(
      List<DestinatairePrestations> destinatairesPaiements) {
    List<PaymentRecipientDto> listPaymentRecipientDto = new ArrayList<>();
    for (DestinatairePrestations destinatairePrestations : destinatairesPaiements) {
      PaymentRecipientDto paymentRecipientDto = new PaymentRecipientDto();
      paymentRecipientDto.setBeyondPaymentRecipientId(
          destinatairePrestations.getIdBeyondDestinatairePaiements());
      paymentRecipientDto.setPaymentRecipientId(
          destinatairePrestations.getIdDestinatairePaiements());
      NameCorporate nameCorporate = new NameCorporate();
      nameCorporate.setCorporateName(destinatairePrestations.getNom().getRaisonSociale());
      nameCorporate.setCommonName(destinatairePrestations.getNom().getNomUsage());
      nameCorporate.setLastName(destinatairePrestations.getNom().getNomFamille());
      nameCorporate.setFirstName(destinatairePrestations.getNom().getPrenom());
      nameCorporate.setCivility(destinatairePrestations.getNom().getCivilite());
      paymentRecipientDto.setName(nameCorporate);
      listPaymentRecipientDto.add(paymentRecipientDto);
    }
    return listPaymentRecipientDto;
  }

  public static Name mapName(NomAssure nom) {
    Name name = new Name();
    if (nom.getNomUsage() != null) {
      name.setCommonName(nom.getNomUsage());
    }
    name.setLastName(nom.getNomFamille());
    name.setFirstName(nom.getPrenom());
    name.setCivility(nom.getCivilite());
    return name;
  }

  public static Name mapName(Affiliation affiliation) {
    Name name = new Name();
    if (affiliation != null) {
      name.setCommonName(affiliation.getNomMarital());
      name.setLastName(affiliation.getNomPatronymique());
      name.setFirstName(affiliation.getPrenom());
      name.setCivility(affiliation.getCivilite());
    }
    return name;
  }

  public static Identity mapBeneficiareIdentite(
      IdentiteContrat identiteBenef, String dateReference) {
    Identity identity = new Identity();
    identity.setPersonNumber(identiteBenef.getNumeroPersonne());
    identity.setBirthDate(identiteBenef.getDateNaissance());
    identity.setBirthRank(identiteBenef.getRangNaissance());
    if (identiteBenef.getNir() != null) {
      identity.setNir(mapNir(identiteBenef.getNir()));
    }
    if (identiteBenef.getAffiliationsRO() != null) {
      identity.setAffiliationsRO(
          mapAffiliationsRO(identiteBenef.getAffiliationsRO(), dateReference));
    }
    if (identiteBenef.getRefExternePersonne() != null) {
      identity.setPersonExternalRef(identiteBenef.getRefExternePersonne());
    }
    return identity;
  }

  public static List<AffiliationRO> mapAffiliationsRO(
      List<NirRattachementRO> affiliationsRO, String dateReference) {
    List<AffiliationRO> affiliationROList = new ArrayList<>();
    for (NirRattachementRO nirRattachementRO : affiliationsRO) {
      if (DateUtils.betweenString(
          dateReference,
          nirRattachementRO.getPeriode().getDebut(),
          nirRattachementRO.getPeriode().getFin())) {
        AffiliationRO affiliationRO = new AffiliationRO();
        affiliationRO.setNir(mapNir(nirRattachementRO.getNir()));
        if (nirRattachementRO.getRattachementRO() != null) {
          affiliationRO.setAttachementRO(mapAttachementRO(nirRattachementRO.getRattachementRO()));
        }
        affiliationRO.setPeriod(mapPeriod(nirRattachementRO.getPeriode()));
        affiliationROList.add(affiliationRO);
      }
    }
    return affiliationROList;
  }

  public static Nir mapNir(com.cegedim.next.serviceeligibility.core.model.kafka.Nir nir) {
    Nir newNir = new Nir();
    newNir.setCode(nir.getCode());
    newNir.setKey(nir.getCle());
    return newNir;
  }

  public static Period mapPeriod(Periode periode) {
    Period period = new Period();
    period.setStart(periode.getDebut());
    period.setEnd(periode.getFin());
    return period;
  }

  public static AttachementRO mapAttachementRO(RattachementRO rattachementRO) {
    AttachementRO attachementRO = new AttachementRO();
    attachementRO.setRegimeCode(rattachementRO.getCodeRegime());
    attachementRO.setHealthInsuranceCompanyCode(rattachementRO.getCodeCaisse());
    attachementRO.setCenterCode(rattachementRO.getCodeCentre());
    return attachementRO;
  }

  public static ConventionDto mapConventionDto(ConventionnementContrat conventionnement) {
    ConventionDto conventionDto = new ConventionDto();
    conventionDto.setConventionCode(conventionnement.getTypeConventionnement().getCode());
    conventionDto.setConventionLabel(conventionnement.getTypeConventionnement().getLibelle());
    conventionDto.setConventionPriority(conventionnement.getPriorite());
    return conventionDto;
  }

  public static DomainDto mapDomain(String domainCode, String libelleDomain) {
    DomainDto domainDto = new DomainDto();
    mapDomainDto(domainDto, domainCode, libelleDomain);
    return domainDto;
  }

  public static void mapDomainDto(
      DomainDto domainGroup, String domainSource, String libelleDomain) {
    domainGroup.setDomainCode(domainSource);
    domainGroup.setDomainLabel(libelleDomain);
  }

  public static RegroupedDomainDto mapRegroupedDomainDto(
      String domainCode, List<ConventionDto> conventions, String libelleDomain) {
    RegroupedDomainDto regroupedDomain = new RegroupedDomainDto();
    regroupedDomain.setDomainCode(domainCode);
    regroupedDomain.setDomainLabel(libelleDomain);
    regroupedDomain.setConventions(conventions);
    return regroupedDomain;
  }
}
