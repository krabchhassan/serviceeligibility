package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.beyond.schemas.*;
import com.cegedim.beyond.schemas.Dematerialisation;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipientMessageService {

  private final MessageService messageService;

  private final Logger logger = LoggerFactory.getLogger(RecipientMessageService.class);

  public void prepareAndSendRecipientBenefitsMessage(ContratAIV6 contract) {
    if (contract.getId() == null) {
      logger.info("prepareAndSendRecipientBenefitsMessage contract id null !");
      return;
    }
    prepareAndSendRecipientBenefitsMessage(
        contract,
        contract.getAssures().stream().map(Assure::getData).filter(Objects::nonNull).toList());
  }

  public void prepareAndSendRecipientBenefitsMessage(
      ContratAIV6 contract, List<DataAssure> listDataAssure) {
    // shard key
    String key =
        contract.getIdDeclarant() + "-" + contract.getNumeroAdherent() + "-" + contract.getNumero();

    List<BenefitStatementRecipient> setBenefitStatementRecipient =
        listDataAssure.stream()
            .map(DataAssure::getDestinatairesRelevePrestations)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet())
            .stream()
            .filter(Objects::nonNull)
            .map(this::mapDestinataireRelevePrestations)
            .toList();

    List<PaymentRecipient> setPaymentRecipient =
        listDataAssure.stream()
            .map(DataAssure::getDestinatairesPaiements)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet())
            .stream()
            .filter(Objects::nonNull)
            .map(this::mapDestinatairePrestations)
            .toList();

    RecipientsMessageDto recipientsMessageDto =
        new RecipientsMessageDto()
            .withId(contract.getId())
            .withNumber(contract.getNumero())
            .withInsurerId(contract.getIdDeclarant())
            .withSubscriberId(contract.getNumeroAdherent())
            .withIssuingCompanyCode(contract.getSocieteEmettrice())
            .withBenefitStatementRecipients(setBenefitStatementRecipient)
            .withPaymentRecipients(setPaymentRecipient);

    messageService.sendRecipientBenefitsMessage(key, recipientsMessageDto);

    logger.debug(
        "Payment or Benefit change: message sent to recipient for contract {}", contract.getId());
  }

  private BenefitStatementRecipient mapDestinataireRelevePrestations(
      DestinataireRelevePrestations destinatairesRelevePrestations) {
    BenefitStatementRecipient benefitStatementRecipient = new BenefitStatementRecipient();

    benefitStatementRecipient.setBenefitStatementRecipientId(
        destinatairesRelevePrestations.getIdDestinataireRelevePrestations());
    benefitStatementRecipient.setBeyondBenefitStatementRecipientId(
        destinatairesRelevePrestations.getIdBeyondDestinataireRelevePrestations());

    if (destinatairesRelevePrestations.getNom() != null) {
      Name__1 name = new Name__1();
      name.setCommonName(destinatairesRelevePrestations.getNom().getNomUsage());
      name.setCorporateName(destinatairesRelevePrestations.getNom().getRaisonSociale());
      name.setFirstName(destinatairesRelevePrestations.getNom().getPrenom());
      name.setLastName(destinatairesRelevePrestations.getNom().getNomFamille());
      name.setCivility(destinatairesRelevePrestations.getNom().getCivilite());
      benefitStatementRecipient.setName(name);
    }

    if (destinatairesRelevePrestations.getAdresse() != null) {
      Address__1 address = new Address__1();
      address.setLine1(destinatairesRelevePrestations.getAdresse().getLigne1());
      address.setLine2(destinatairesRelevePrestations.getAdresse().getLigne2());
      address.setLine3(destinatairesRelevePrestations.getAdresse().getLigne3());
      address.setLine4(destinatairesRelevePrestations.getAdresse().getLigne4());
      address.setLine5(destinatairesRelevePrestations.getAdresse().getLigne5());
      address.setLine6(destinatairesRelevePrestations.getAdresse().getLigne6());
      address.setLine7(destinatairesRelevePrestations.getAdresse().getLigne7());
      address.setPostcode(destinatairesRelevePrestations.getAdresse().getCodePostal());
      benefitStatementRecipient.setAddress(address);
    }

    if (destinatairesRelevePrestations.getPeriode() != null) {
      Period__2 period = new Period__2();
      period.setStart(destinatairesRelevePrestations.getPeriode().getDebut());
      period.setEnd(destinatairesRelevePrestations.getPeriode().getFin());
      benefitStatementRecipient.setPeriod(period);
    }

    if (destinatairesRelevePrestations.getDematerialisation() != null) {
      Dematerialisation demat = new Dematerialisation();
      demat.setEmail(destinatairesRelevePrestations.getDematerialisation().getEmail());
      demat.setMobile(destinatairesRelevePrestations.getDematerialisation().getMobile());
      demat.setIsDematerialise(
          destinatairesRelevePrestations.getDematerialisation().getIsDematerialise());
      benefitStatementRecipient.setDematerialisation(demat);
    }

    return benefitStatementRecipient;
  }

  private PaymentRecipient mapDestinatairePrestations(
      DestinatairePrestations destinatairePrestations) {
    PaymentRecipient paymentRecipient = new PaymentRecipient();

    paymentRecipient.setPaymentRecipientId(destinatairePrestations.getIdDestinatairePaiements());
    paymentRecipient.setBeyondPaymentRecipientId(
        destinatairePrestations.getIdBeyondDestinatairePaiements());

    if (destinatairePrestations.getNom() != null) {
      Name name = new Name();
      name.setCommonName(destinatairePrestations.getNom().getNomUsage());
      name.setCorporateName(destinatairePrestations.getNom().getRaisonSociale());
      name.setFirstName(destinatairePrestations.getNom().getPrenom());
      name.setLastName(destinatairePrestations.getNom().getNomFamille());
      name.setCivility(destinatairePrestations.getNom().getCivilite());
      paymentRecipient.setName(name);
    }

    if (destinatairePrestations.getAdresse() != null) {
      Address address = new Address();
      address.setLine1(destinatairePrestations.getAdresse().getLigne1());
      address.setLine2(destinatairePrestations.getAdresse().getLigne2());
      address.setLine3(destinatairePrestations.getAdresse().getLigne3());
      address.setLine4(destinatairePrestations.getAdresse().getLigne4());
      address.setLine5(destinatairePrestations.getAdresse().getLigne5());
      address.setLine6(destinatairePrestations.getAdresse().getLigne6());
      address.setLine7(destinatairePrestations.getAdresse().getLigne7());
      address.setPostcode(destinatairePrestations.getAdresse().getCodePostal());
      paymentRecipient.setAddress(address);
    }

    if (destinatairePrestations.getPeriode() != null) {
      Period__1 period = new Period__1();
      period.setStart(destinatairePrestations.getPeriode().getDebut());
      period.setEnd(destinatairePrestations.getPeriode().getFin());
      paymentRecipient.setPeriod(period);
    }

    if (destinatairePrestations.getModePaiementPrestations() != null) {
      BenefitPaymentMode benefitPaymentMode = new BenefitPaymentMode();
      benefitPaymentMode.setCode(destinatairePrestations.getModePaiementPrestations().getCode());
      benefitPaymentMode.setLabel(
          destinatairePrestations.getModePaiementPrestations().getLibelle());
      benefitPaymentMode.setCurrencyCode(
          destinatairePrestations.getModePaiementPrestations().getCodeMonnaie());
      paymentRecipient.setBenefitPaymentMode(benefitPaymentMode);
    }

    if (destinatairePrestations.getRib() != null) {
      Rib rib = new Rib();
      rib.setBic(destinatairePrestations.getRib().getBic());
      rib.setIban(destinatairePrestations.getRib().getIban());
      paymentRecipient.setRib(rib);
    }

    return paymentRecipient;
  }
}
