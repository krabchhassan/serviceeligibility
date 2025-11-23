package com.cegedim.next.serviceeligibility.core.business.declarant.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclarant;
import com.cegedim.next.serviceeligibility.core.business.beneficiaire.dao.BeneficiaireDao;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestIJTrace;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationMongo;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationTrace;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class RestDeclarantService {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(RestDeclarantService.class);

  @Autowired private DeclarantDao dao;

  @Autowired private BeneficiaireDao benefDao;

  @Autowired private ServicePrestationMongoDao spmDao;

  @Autowired private PrestIJDao pijDao;

  @Autowired private ServicePrestationTraceDao tracePrestationDao;

  @Autowired private ServicePrestIJTraceDao tracePrestIJDao;

  @Autowired private MapperDeclarant mapperDeclarant;

  @CacheEvict(value = "declarantCache", key = "#declarant.get_id()")
  @ContinueSpan(log = "create")
  public void create(Declarant declarant) {
    dao.create(declarant);
  }

  @ContinueSpan(log = "findById Declarant")
  public Declarant findById(String id) {
    return dao.findById(id);
  }

  @ContinueSpan(log = "findAll Declarant")
  public List<Declarant> findAll() {
    return dao.findAll();
  }

  @ContinueSpan(log = "findAllDto Declarant")
  public List<DeclarantDto> findAllDto() {
    return mapperDeclarant.entityListToDtoList(dao.findAll(), null, false, false, null);
  }

  @ContinueSpan(log = "deletePrestations")
  public List<String> deletePrestations(List<String> idDeclarant, String service) {
    List<String> compteRendu = new ArrayList<>();
    for (String idDecl : idDeclarant) {
      int nbContrat = 0;
      Long nbBenef = 0L;
      Declarant decl = findById(idDecl);
      if (decl == null) {
        compteRendu.add("L'AMC n°" + idDecl + " n'existe pas.");
      } else {
        if ("ServicePrestation".equals(service)) {
          List<ServicePrestationMongo> prestationList = spmDao.findServicePrestationMongo(idDecl);
          nbContrat = extractMongoContracts(nbContrat, prestationList);
          nbBenef = nbBenef + benefDao.removeService(idDecl, service);
          compteRendu.add(
              "AMC : "
                  + idDecl
                  + " - Nb ServicePrestation supprimé(s) : "
                  + nbContrat
                  + " - Nb bénéficiaires modifié(s) : "
                  + nbBenef);
        } else if ("PrestIJ".equals(service)) {
          List<PrestIJ> prestIJList = pijDao.findServicePrestIJ(idDecl);
          nbContrat = extractContracts(nbContrat, prestIJList);
          nbBenef = nbBenef + benefDao.removeService(idDecl, service);
          compteRendu.add(
              "AMC : "
                  + idDecl
                  + " - Nb PrestIJ supprimée(s) : "
                  + nbContrat
                  + " - Nb bénéficiaires modifié(s) : "
                  + nbBenef);
        } else {
          LOGGER.info("Le service '{}' n'est pas une option autorisée.", service);
          compteRendu.add("Le service " + service + " n'est pas une option autorisée.");
        }
      }
    }
    return compteRendu;
  }

  private int extractMongoContracts(int nbContrat, List<ServicePrestationMongo> prestationList) {
    if (!CollectionUtils.isEmpty(prestationList)) {
      for (ServicePrestationMongo prestation : prestationList) {
        this.cleanTracesPrestations(prestation.get_id());
        spmDao.delete(prestation);
        nbContrat = nbContrat + 1;
      }
    }
    return nbContrat;
  }

  private int extractContracts(int nbContrat, List<PrestIJ> prestIJList) {
    if (!CollectionUtils.isEmpty(prestIJList)) {
      for (PrestIJ prestIJ : prestIJList) {
        this.cleanTracesPrestIJ(prestIJ.get_id());
        pijDao.delete(prestIJ);
        nbContrat = nbContrat + 1;
      }
    }
    return nbContrat;
  }

  private void cleanTracesPrestations(String id) {
    List<ServicePrestationTrace> traceList = tracePrestationDao.findServicePrestationTrace(id);
    for (ServicePrestationTrace trace : traceList) {
      tracePrestationDao.delete(trace);
    }
  }

  private void cleanTracesPrestIJ(String id) {
    List<ServicePrestIJTrace> traceList = tracePrestIJDao.findServicePrestIJTrace(id);
    for (ServicePrestIJTrace trace : traceList) {
      tracePrestIJDao.delete(trace);
    }
  }
}
