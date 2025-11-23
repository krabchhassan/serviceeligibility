package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarypaymentrecipients.RequestBeneficiaryPaymentRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.paymentrecipients.InsuredPeriodDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperPaymentRecipient;
import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ContratV6;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationCommun;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository("servicePrestationDao")
public class ServicePrestationDaoImpl extends MongoGenericDao<ServicePrestationCommun>
    implements ServicePrestationDao {
  com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao servicePrestationDao;

  private final MapperPaymentRecipient mapperPaymentRecipient;

  public ServicePrestationDaoImpl(
      MongoTemplate mongoTemplate,
      com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao servicePrestationDao,
      MapperPaymentRecipient mapperPaymentRecipient) {
    super(mongoTemplate);
    this.servicePrestationDao = servicePrestationDao;
    this.mapperPaymentRecipient = mapperPaymentRecipient;
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV6")
  public ContratV6 findServicePrestationV6(
      final String idDeclarant,
      final String numeroAdherent,
      final String dateNaissance,
      final String rangNaissance,
      final String debutPeriodeSoin,
      final String finPeriodeSoin,
      final String nir) {

    final ContratV6 contrat = new ContratV6();
    contrat.setContrats(
        this.servicePrestationDao.findServicePrestationV6(
            idDeclarant,
            numeroAdherent,
            dateNaissance,
            rangNaissance,
            debutPeriodeSoin,
            finPeriodeSoin,
            nir));
    return contrat;
  }

  @Override
  @ContinueSpan(log = "findServicePrestationV6")
  public List<ServicePrestationV6> findServicePrestationWithPaymentRecipients(
      final RequestBeneficiaryPaymentRecipientsDto requestBeneficiaryPaymentRecipientsDto) {
    final Aggregation agg =
        this.getAggregationServicePrestationForPaymentRecipients(
            requestBeneficiaryPaymentRecipientsDto);
    final List<ServicePrestationV6> servicePrestations =
        this.servicePrestationDao.getServicePrestation(agg);
    final LocalDate dateToTest =
        LocalDate.parse(requestBeneficiaryPaymentRecipientsDto.getDate(), DateUtils.FORMATTER);
    this.updatePaymentsBeneficiary(dateToTest, servicePrestations);
    if (!CollectionUtils.isEmpty(servicePrestations)) {
      return servicePrestations;
    }
    return Collections.emptyList();
  }

  @Override
  @ContinueSpan(log = "getPaymentRecipients")
  public List<DestinatairePrestations> getPaymentRecipients(
      List<InsuredPeriodDto> insuredPeriods,
      String insurerId,
      String personNumber,
      String subscriberId,
      String contractNumber) {
    final ContratAIV6 servicePrestation =
        this.servicePrestationDao.findServicePrestationByContractNumber(
            contractNumber, insurerId, subscriberId);
    if (servicePrestation != null) {
      Assure assure =
          servicePrestation.getAssures().stream()
              .filter(assure1 -> personNumber.equals(assure1.getIdentite().getNumeroPersonne()))
              .findFirst()
              .orElse(null);
      if (assure != null) {
        insuredPeriods.addAll(mapperPaymentRecipient.mapInsuredPeriods(assure.getPeriodes()));
        return assure.getData().getDestinatairesPaiements();
      }
    }
    return Collections.emptyList();
  }

  @Override
  public void updatePaymentsBeneficiary(
      final LocalDate dateToTest, final List<ServicePrestationV6> benefitServiceList) {
    benefitServiceList.forEach(
        benefitServiceV5 -> {
          final List<DestinatairePrestations> tempList =
              benefitServiceV5.getAssure().getData().getDestinatairesPaiements();
          final Comparator<DestinatairePrestations> comparator =
              Comparator.comparing(p -> p.getPeriode().getDebut());
          tempList.sort(comparator.reversed());
          final List<DestinatairePrestations> newList = new ArrayList<>();

          for (final DestinatairePrestations destinatairePrestationsV4 : tempList) {
            final boolean alreadyExist =
                newList.stream()
                    .anyMatch(
                        destinataire ->
                            destinatairePrestationsV4.getIdDestinatairePaiements() != null
                                && destinatairePrestationsV4
                                    .getIdDestinatairePaiements()
                                    .equals(destinataire.getIdDestinatairePaiements()));

            if (!alreadyExist) {
              LocalDate dateFin = null;
              if (destinatairePrestationsV4.getPeriode().getFin() != null) {
                dateFin =
                    LocalDate.parse(
                        destinatairePrestationsV4.getPeriode().getFin(), DateUtils.FORMATTER);
              }
              if (DateUtils.fromDate(dateToTest, dateFin)) {
                newList.add(destinatairePrestationsV4);
              }
            }
          }
          benefitServiceV5.getAssure().getData().setDestinatairesPaiements(newList);
        });
  }

  private Aggregation getAggregationServicePrestationForPaymentRecipients(
      final RequestBeneficiaryPaymentRecipientsDto requestBeneficiaryPaymentRecipientsDto) {
    // Contruction des critères de recherches
    final Criteria adherentCriteria =
        this.servicePrestationDao.getCriteriaAdherent(
            requestBeneficiaryPaymentRecipientsDto.getInsurerId(),
            requestBeneficiaryPaymentRecipientsDto.getSubscriberId());
    final Criteria assureCriteria =
        this.servicePrestationDao.getCriteriaAssure(
            requestBeneficiaryPaymentRecipientsDto.getPersonNumber());

    // Construction de l'aggrégation de recherche
    return this.getServicePrestation(adherentCriteria, assureCriteria);
  }

  private Aggregation getServicePrestation(
      final Criteria adherentCriteria, final Criteria assureCriteria) {
    return Aggregation.newAggregation(
        Aggregation.match(adherentCriteria),
        Aggregation.match(assureCriteria),
        Aggregation.unwind(Constants.ASSURES),
        Aggregation.match(assureCriteria));
  }

  @Override
  @ContinueSpan(log = "findServicePrestation")
  public List<ServicePrestationV6> findServicePrestation(
      final String idDeclarant, final String numeroPersonne) {
    final Aggregation agg = this.getAggrationFindServidePrestation(idDeclarant, numeroPersonne);
    return this.servicePrestationDao.getServicePrestation(agg);
  }

  @Override
  @ContinueSpan(log = "getServicePrestationV5ById")
  public ContratAIV6 getServicePrestationV6ById(final String id) {
    return this.getMongoTemplate().findById(id, ContratAIV6.class);
  }

  /**
   * Construction de l'aggregation permettant la recherche de service Prestation en V1 et V2
   *
   * @param idDeclarant N° de déclarant (AMC)
   * @param numeroPersonne N° de personne
   * @return la liste des services prestation de l'assuré
   */
  private Aggregation getAggrationFindServidePrestation(
      final String idDeclarant, final String numeroPersonne) {
    // Contruction des critères de recherches
    final Criteria adherentCriteria =
        this.servicePrestationDao.getCriteriaAdherent(idDeclarant, null);
    final Criteria assureCriteria = this.servicePrestationDao.getCriteriaAssure(numeroPersonne);

    // Construction de l'aggrégation de recherche
    return Aggregation.newAggregation(
        Aggregation.match(adherentCriteria.andOperator(assureCriteria)),
        Aggregation.unwind(Constants.ASSURES),
        Aggregation.match(assureCriteria));
  }

  @Override
  @ContinueSpan(log = "create servicePrestation")
  public void create(Object servicePrestation) {
    this.servicePrestationDao.create(servicePrestation);
  }

  @Override
  @ContinueSpan(log = "dropCollection servicePrestation")
  public void dropCollection() {
    this.servicePrestationDao.dropCollection();
  }
}
