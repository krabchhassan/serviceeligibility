package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5Recipient;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6Light;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicePrestationService {

  private final ServicePrestationDao servicePrestationDao;

  @ContinueSpan(log = "anotherContractExists")
  public boolean anotherContractExists(String idDeclarant, String numeroPersonne, String fileName) {
    Criteria criteria =
        Criteria.where(Constants.ID_DECLARANT)
            .is(idDeclarant)
            .and(Constants.SERVICE_PRESTATION_ASSURES_IDENTITE_NUMERO)
            .is(numeroPersonne)
            .and(Constants.NOM_FICHIER_ORIGINE)
            .ne(fileName);

    Query query = new Query(criteria);

    return servicePrestationDao.existContrat(query);
  }

  @ContinueSpan(log = "deleteContratById")
  public long deleteContratById(String id) {
    return servicePrestationDao.deleteContratById(id);
  }

  public List<ContratAIV6Light> getContratsLightByFileName(String fileName) {
    return servicePrestationDao.getContratsLightByFileName(fileName);
  }

  @ContinueSpan(log = "deleteContratsByAmc")
  public long deleteContratsByAmc(String idDeclarant) {
    return servicePrestationDao.deleteContratsByAmc(idDeclarant);
  }

  @ContinueSpan(log = "getContract")
  public ContratAIV6 getContract(String contratAiId) {
    return servicePrestationDao.getContratById(contratAiId);
  }

  @ContinueSpan(log = "getContratByUK")
  public ContratAIV6 getContratByUK(String idDeclarant, String numero, String numeroAdherent) {
    return servicePrestationDao.getContratByUK(idDeclarant, numero, numeroAdherent);
  }

  @ContinueSpan(log = "getAllContratForParametrageStream")
  public Stream<ContratAIV6> getAllContratForParametrageStream(String idDeclarant) {
    return servicePrestationDao.getAllContratForParametrageStream(idDeclarant);
  }

  @ContinueSpan(log = "getContratForParametrageStream")
  public Stream<ContratAIV6> getContratForParametrageStream(
      ParametrageCarteTP param,
      Date dateTraitement,
      String dateValiditeDroit,
      String batchExecutionId) {
    return servicePrestationDao.getContratForParametrageStream(
        param, dateTraitement, dateValiditeDroit, batchExecutionId);
  }

  @ContinueSpan(log = "generateBeyondId")
  public void generateBeyondId(ContratAIV6 contract) {
    List<Assure> assureV5List = contract.getAssures();
    if (assureV5List != null && !assureV5List.isEmpty()) {
      int indiceDestPaiement = 1;
      int indiceDestRelevePrest = 1;
      for (Assure assure : assureV5List) {
        DataAssure data = assure.getData();
        if (data != null) {
          generateDestinatairePrestationV5(
              data.getDestinatairesPaiements(), contract, assure, indiceDestPaiement);
          generateDestinataireRelevePrestationV5(
              data.getDestinatairesRelevePrestations(), contract, assure, indiceDestRelevePrest);
        }
      }
    }
  }

  public List<ContratAIV5Recipient> getContractsRecipientsPaginated(long batchSize, int page) {
    return servicePrestationDao.getContractsRecipientsPaginated(batchSize, page);
  }

  private void generateDestinatairePrestationV5(
      List<DestinatairePrestations> destinatairePrestationsV4List,
      ContratAICommun contract,
      AssureCommun assure,
      int indiceDestPaiement) {
    if (destinatairePrestationsV4List != null)
      for (DestinatairePrestations destinatairePrestationsV4 : destinatairePrestationsV4List) {
        String idDestinatairePaiements = destinatairePrestationsV4.getIdDestinatairePaiements();
        if (idDestinatairePaiements == null) {
          idDestinatairePaiements =
              MapperContrat.getIdDestinataire(
                  contract.getNumeroAdherent(),
                  assure.getIdentite().getNumeroPersonne(),
                  Constants.TYPE_DESTINATAIRE_PAIEMENT,
                  indiceDestPaiement++);
          destinatairePrestationsV4.setIdDestinatairePaiements(idDestinatairePaiements);
        }
        destinatairePrestationsV4.setIdBeyondDestinatairePaiements(
            idDestinatairePaiements + "-" + contract.getIdDeclarant());
      }
  }

  private void generateDestinataireRelevePrestationV5(
      List<DestinataireRelevePrestations> destinataireRelevePrestationsV5List,
      ContratAICommun contract,
      AssureCommun assure,
      int indiceDestRelevePrest) {
    if (destinataireRelevePrestationsV5List != null)
      for (DestinataireRelevePrestations destinataireRelevePrestationsV5 :
          destinataireRelevePrestationsV5List) {
        String idDestinataireRelevePrestations =
            destinataireRelevePrestationsV5.getIdDestinataireRelevePrestations();
        if (idDestinataireRelevePrestations == null) {
          idDestinataireRelevePrestations =
              MapperContrat.getIdDestinataire(
                  contract.getNumeroAdherent(),
                  assure.getIdentite().getNumeroPersonne(),
                  Constants.TYPE_DESTINATAIRE_RELEVE_PRESTATION,
                  indiceDestRelevePrest++);
          destinataireRelevePrestationsV5.setIdDestinataireRelevePrestations(
              idDestinataireRelevePrestations);
        }
        destinataireRelevePrestationsV5.setIdBeyondDestinataireRelevePrestations(
            idDestinataireRelevePrestations + "-" + contract.getIdDeclarant());
      }
  }

  @ContinueSpan(log = "updateContractsExecutionId")
  public long updateContractsExecutionId(List<ObjectId> contractIdList, String batchExecutionId) {
    return servicePrestationDao.updateContractsExecutionId(contractIdList, batchExecutionId);
  }

  @ContinueSpan(log = "getServicePrestationV6")
  public List<ContratAIV6> getServicePrestationV6(String idDeclarant, String numeroPersonne) {
    return servicePrestationDao.findServicePrestationV6(idDeclarant, numeroPersonne);
  }

  @ContinueSpan(log = "updateContrat")
  public void updateContrat(ContratAIV6 contratAIV6) {
    servicePrestationDao.updateContrat(contratAIV6);
  }

  @ContinueSpan(log = "getAllContrats")
  public Stream<ContratAIV6> getAllContrats() {
    return servicePrestationDao.getAllContrats();
  }
}
