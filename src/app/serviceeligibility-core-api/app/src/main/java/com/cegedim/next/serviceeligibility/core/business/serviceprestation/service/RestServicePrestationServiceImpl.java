package com.cegedim.next.serviceeligibility.core.business.serviceprestation.service;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.FORMATTER;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.ContractWithPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.RequestBeneficiaryPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperPaymentRecipient;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperBeneficiaryPaymentRecipients;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao.ServicePrestationTraceDao;
import com.cegedim.next.serviceeligibility.core.model.entity.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.BenefitPaymentMode;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.PaymentRecipient;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Rib;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("servicePrestation")
public class RestServicePrestationServiceImpl implements RestServicePrestationService {

  private final ServicePrestationDao dao;

  private final ServicePrestationTraceDao tracePrestationDao;

  private final MapperBeneficiaryPaymentRecipients mapperBeneficiaryPaymentRecipients;

  private final AuthorizationScopeHandler authorizationScopeHandler;

  private final MapperPaymentRecipient mapperPaymentRecipient;

  private final Logger logger = LoggerFactory.getLogger(RestServicePrestationServiceImpl.class);

  public RestServicePrestationServiceImpl(
      ServicePrestationDao dao,
      MapperBeneficiaryPaymentRecipients mapperBeneficiaryPaymentRecipients,
      ServicePrestationTraceDao tracePrestationDao,
      AuthorizationScopeHandler authorizationScopeHandler,
      MapperPaymentRecipient mapperPaymentRecipient) {
    this.dao = dao;
    this.mapperBeneficiaryPaymentRecipients = mapperBeneficiaryPaymentRecipients;
    this.tracePrestationDao = tracePrestationDao;
    this.authorizationScopeHandler = authorizationScopeHandler;
    this.mapperPaymentRecipient = mapperPaymentRecipient;
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV4")
  public ContratV6 findServicePrestationV6(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String debutPeriodeSoin,
      String finPeriodeSoin,
      String nir) {
    return dao.findServicePrestationV6(
        idDeclarant,
        numeroAdherent,
        dateNaissance,
        rangNaissance,
        debutPeriodeSoin,
        finPeriodeSoin,
        nir);
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV5")
  public ContratV5 findServicePrestationV5(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String debutPeriodeSoin,
      String finPeriodeSoin,
      String nir) {
    ContratV6 contratV6 =
        dao.findServicePrestationV6(
            idDeclarant,
            numeroAdherent,
            dateNaissance,
            rangNaissance,
            debutPeriodeSoin,
            finPeriodeSoin,
            nir);
    return MapperServicePrestation.mapContratV6ToV5(contratV6);
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV5")
  public List<ServicePrestationV6> findServicePrestationV6(
      String idDeclarant, String numeroPersonne) {
    List<ServicePrestationV6> allPrestations =
        dao.findServicePrestation(idDeclarant, numeroPersonne);

    List<ServicePrestationV6> authorizedPrestations =
        allPrestations.stream().filter(authorizationScopeHandler::isAuthorized).toList();

    if (authorizedPrestations.isEmpty()) {
      logger.warn("Aucune prestation autorisée trouvée !");
    }

    return authorizedPrestations;
  }

  @Override
  public List<ServicePrestationV5> findServicePrestationV5(
      String idDeclarant, String numeroPersonne) {
    List<ServicePrestationV5> servicePrestationV5List = new ArrayList<>();
    List<ServicePrestationV6> servicePrestationV6List =
        dao.findServicePrestation(idDeclarant, numeroPersonne);
    for (ServicePrestationV6 servicePrestationV6 : servicePrestationV6List) {
      servicePrestationV5List.add(
          MapperServicePrestation.mapServicePrestationV6ToV5(servicePrestationV6));
    }
    return servicePrestationV5List;
  }

  @Override
  public ContratAIV6 findServicePrestationV6(String id) {
    return dao.getServicePrestationV6ById(id);
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV5")
  public ContratAIV5 findServicePrestationV5(String id) {
    ContratAIV6 servicePrestationV6 = dao.getServicePrestationV6ById(id);
    return MapperServicePrestation.mapContratAIV6ToV5(servicePrestationV6);
  }

  @Override
  @ContinueSpan(log = "create servicePrestation")
  public void create(Object servicePrestation) {
    dao.create(servicePrestation);
  }

  @Override
  @ContinueSpan(log = "dropCollection servicePrestation")
  public void dropCollection() {
    dao.dropCollection();
  }

  @Override
  @ContinueSpan(log = "getBeneficiaryPaymentRecipients")
  public List<ContractWithPaymentRecipientsDto> getBeneficiaryPaymentRecipients(
      RequestBeneficiaryPaymentRecipientsDto request) {
    List<ServicePrestationV6> prestationV6List =
        dao.findServicePrestationWithPaymentRecipients(request);
    List<ContractWithPaymentRecipientsDto> contractWithPaymentRecipientsDtoList = new ArrayList<>();
    for (ServicePrestationV6 servicePrestationV6 : prestationV6List) {
      contractWithPaymentRecipientsDtoList.add(
          mapperBeneficiaryPaymentRecipients.entityToDto(
              servicePrestationV6, null, false, false, null));
    }
    return contractWithPaymentRecipientsDtoList;
  }

  @Override
  @ContinueSpan(log = "getPaymentRecipientsByContract")
  public List<PaymentRecipientsByContractDto> getPaymentRecipientsByContract(
      RequestPaymentRecipientsByContract request) {
    List<PaymentRecipientsByContractDto> paymentRecipientsByContractList = new ArrayList<>();
    List<PaymentRecipient> paymentRecipientList = new ArrayList<>();
    // Identifie personNumber en récupérant tout ce qui suit le premier tiret dans
    // beneficiaryId
    String personNumber =
        request.getBeneficiaryId().substring(request.getBeneficiaryId().indexOf('-') + 1);

    for (RequestContract contract : request.getContracts()) {
      PaymentRecipientsByContractDto paymentRecipientsByContract =
          new PaymentRecipientsByContractDto();
      ContractIdDto contractId = new ContractIdDto();
      List<InsuredPeriodDto> insuredPeriods = new ArrayList<>();
      contractId.setInsurerId(request.getInsurerId());
      contractId.setNumber(contract.getNumber());
      contractId.setSubscriberId(contract.getSubscriberId());

      List<DestinatairePrestations> allPaymentRecipients =
          dao.getPaymentRecipients(
              insuredPeriods,
              request.getInsurerId(),
              personNumber,
              contract.getSubscriberId(),
              contract.getNumber());
      if (CollectionUtils.isNotEmpty(allPaymentRecipients)) {
        List<DestinatairePrestations> filteredRecipients =
            allPaymentRecipients.size() > 1
                ? filterPaymentRecipientsByReferenceDate(
                    allPaymentRecipients, request.getReferenceDate())
                : allPaymentRecipients;

        paymentRecipientList =
            filteredRecipients.stream().map(this::mapToPaymentRecipient).toList();
      }

      paymentRecipientsByContract.setContractId(contractId);
      paymentRecipientsByContract.setInsuredPeriods(insuredPeriods);
      paymentRecipientsByContract.setPaymentRecipients(
          CollectionUtils.isNotEmpty(allPaymentRecipients)
              ? paymentRecipientList
              : Collections.emptyList());
      paymentRecipientsByContractList.add(paymentRecipientsByContract);
    }
    return paymentRecipientsByContractList;
  }

  private PaymentRecipient mapToPaymentRecipient(DestinatairePrestations destinatairePrestations) {
    PaymentRecipient paymentRecipient = new PaymentRecipient();
    paymentRecipient.setPaymentRecipientId(destinatairePrestations.getIdDestinatairePaiements());
    paymentRecipient.setBeyondPaymentRecipientId(
        destinatairePrestations.getIdBeyondDestinatairePaiements());

    Rib rib = new Rib();
    if (destinatairePrestations.getRib() != null) {
      rib.setBic(destinatairePrestations.getRib().getBic());
      rib.setIban(destinatairePrestations.getRib().getIban());
    }
    paymentRecipient.setRib(rib);
    ModePaiement modePaiementPrestations = destinatairePrestations.getModePaiementPrestations();
    BenefitPaymentMode benefitPaymentMode = new BenefitPaymentMode();
    if (modePaiementPrestations != null) {
      benefitPaymentMode.setCode(modePaiementPrestations.getCode());
      benefitPaymentMode.setCurrencyCode(modePaiementPrestations.getCodeMonnaie());
      benefitPaymentMode.setLabel(modePaiementPrestations.getLibelle());
    }
    paymentRecipient.setBenefitPaymentMode(benefitPaymentMode);
    paymentRecipient.setName(
        mapperPaymentRecipient.mapNameCorporate(destinatairePrestations.getNom()));
    paymentRecipient.setAddress(
        mapperPaymentRecipient.mapAdress(destinatairePrestations.getAdresse()));
    paymentRecipient.setPeriod(
        mapperPaymentRecipient.mapPeriod(destinatairePrestations.getPeriode()));

    return paymentRecipient;
  }

  public List<DestinatairePrestations> filterPaymentRecipientsByReferenceDate(
      List<DestinatairePrestations> paymentRecipientsList, String referenceDate) {
    if (CollectionUtils.isEmpty(paymentRecipientsList)) {
      return Collections.emptyList();
    }

    // Filtrer les périodes valides
    paymentRecipientsList = paymentRecipientsList.stream().filter(this::isValidPeriod).toList();

    // Filtrer les destinataires valides à la date de référence
    List<DestinatairePrestations> validAtReferenceDate =
        paymentRecipientsList.stream()
            .filter(
                paymentRecipients ->
                    DateUtils.betweenString(
                        referenceDate,
                        paymentRecipients.getPeriode().getDebut(),
                        paymentRecipients.getPeriode().getFin()))
            .toList();

    if (!validAtReferenceDate.isEmpty()) {
      return validAtReferenceDate;
    }

    // Si aucun destinataire à la date de référence, chercher les suivants après la
    // référence
    Optional<LocalDate> earliestStartDate =
        paymentRecipientsList.stream()
            .filter(
                paymentRecipients ->
                    DateUtils.after(
                        paymentRecipients.getPeriode().getDebut(), referenceDate, FORMATTER))
            .map(paymentRecipients -> LocalDate.parse(paymentRecipients.getPeriode().getDebut()))
            .min(LocalDate::compareTo);

    if (earliestStartDate.isPresent()) {
      return paymentRecipientsList.stream()
          .filter(
              paymentRecipients ->
                  LocalDate.parse(paymentRecipients.getPeriode().getDebut())
                      .isEqual(earliestStartDate.get()))
          .toList();
    }

    // Si aucun destinataire après, chercher les précédents avant la référence
    Optional<LocalDate> latestEndDate =
        paymentRecipientsList.stream()
            .filter(
                paymentRecipients -> {
                  String dateFinStr = paymentRecipients.getPeriode().getFin();
                  return dateFinStr != null
                      && DateUtils.before(
                          paymentRecipients.getPeriode().getDebut(), referenceDate, FORMATTER);
                })
            .map(paymentRecipients -> LocalDate.parse(paymentRecipients.getPeriode().getFin()))
            .max(LocalDate::compareTo);

    if (latestEndDate.isPresent()) {
      return paymentRecipientsList.stream()
          .filter(
              paymentRecipients ->
                  LocalDate.parse(paymentRecipients.getPeriode().getFin())
                      .isEqual(latestEndDate.get()))
          .toList();
    }

    // Aucun destinataire valide trouvé
    return Collections.emptyList();
  }

  private boolean isValidPeriod(DestinatairePrestations paymentRecipients) {
    String dateDebutStr = paymentRecipients.getPeriode().getDebut();
    String dateFinStr = paymentRecipients.getPeriode().getFin();
    return DateUtils.isPeriodeValide(dateDebutStr, dateFinStr, DateUtils.FORMATTER);
  }
}
