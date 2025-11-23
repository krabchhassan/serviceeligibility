package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ServicePrestationsRdo;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5Recipient;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6Light;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public interface ServicePrestationDao {
  /**
   * Retourne un iterator sur l'ensemble des services prestations concernés par le paramétrage de
   * carte TP
   *
   * @param param Le paramétrage de carte TP
   * @param dateTraitement Date de traitement demandé par le batch
   * @return la liste de contrat "streamé"
   */
  Stream<ContratAIV6> getContratForParametrageStream(
      ParametrageCarteTP param,
      Date dateTraitement,
      String dateValiditeDroit,
      String batchExecutionId);

  /**
   * Retourne le services prestations identifié par l'amc et le n°
   *
   * @param idDeclarant Numéro de l'amc
   * @param numero Numéro de contrat
   * @param numeroAdherent Numéro d'adhérent
   * @return contrat
   */
  ContratAIV6 getContratByUK(String idDeclarant, String numero, String numeroAdherent);

  ContratAIV6 getContratById(String id);

  ContratAIV6 getContratByIdDeclarant(String idDeclarant) throws ParseException;

  long deleteContratById(String id);

  void remove(ContratAIV6 contratAIV6);

  List<ContratAIV6> getContratsByFileName(String fileName);

  List<ContratAIV6> getContratsByAmc(String idDeclarant);

  List<ContratAIV6Light> getContratsLightByFileName(String fileName);

  List<ContratAIV6Light> getContratsLightByAmc(String idDeclarant);

  long deleteContratsByFileName(String fileName);

  long deleteContratsByAmc(String idDeclarant);

  List<ServicePrestationV6> findServicePrestationV6(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String debutPeriodeSoin,
      String finPeriodeSoin,
      String nir);

  List<ContratAIV6> findServicePrestationV6(String idDeclarant, String numeroPersonne);

  List<ContratAIV6> findContratAIV6ForBlb(
      final String dateNaissance, final String rangNaissance, final String nir);

  ContratAIV6 findServicePrestationByContractNumber(
      String contractNumber, String insurerId, String subscriberId);

  List<ServicePrestationsRdo> findServicePrestationsRdo(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String nir,
      List<String> contractNumberList);

  List<ServicePrestationV6> getServicePrestation(Aggregation agg);

  List<ServicePrestationsRdo> getServicePrestationRdo(Aggregation agg);

  Criteria getCriteriaAdherent(String idDeclarant, String numeroAdherent);

  Criteria getCriteriaAdherentContrat(
      final String idDeclarant, final String numeroAdherent, List<String> contractNumberList);

  Criteria getCriteriaAssure(String numeroPersonne);

  ContratAIV6 getContratByNumeroContractAndAmc(String numeroContrat, String idDeclarant);

  List<ContratAIV5Recipient> getContractsRecipientsPaginated(long batchSize, int page);

  List<ContratAIV6> getContratAIV6(Query query);

  long updateContractsExecutionId(List<ObjectId> contractIdList, String batchExecutionId);

  void updateContrat(ContratAIV6 contrat);

  boolean existContrat(Query query);

  Stream<ContratAIV6> getAllContratForParametrageStream(String idDeclarant);

  Stream<ContratAIV6> getAllContrats();

  void create(Object servicePrestation);

  void dropCollection();
}
