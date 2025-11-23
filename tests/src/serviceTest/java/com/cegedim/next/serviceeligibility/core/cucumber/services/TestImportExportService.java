package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.importexport.ImportExportDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.importexport.RestImportDto;
import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.model.entity.*;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestImportExportService {

  private final DeclarationDao declarationDao;
  private final CarteDematDao carteDematDao;
  private final VolumetrieDao volumetrieDao;
  private final MongoTemplate mongoTemplate;
  private final FluxDao fluxDao;
  private final DeclarantDaoImpl declarantDaoImpl;
  private final CircuitDaoImpl circuitDao;
  private final ServiceDroitsDaoImpl serviceDroitsDao;
  private final ParametreBddDaoImpl parametreBddDao;
  private final TranscodageDaoImpl transcodageDao;
  private final TranscoParametrageDaoImpl transcoParametrageDao;

  public void importAll(RestImportDto restImportDto) {
    this.importData(restImportDto.getData());
  }

  private void importData(ImportExportDto importExport) {
    deleteAll();
    List<Declaration> declarations = importExport.getDeclarations();
    if (declarations != null) {
      for (Declaration d : declarations) {
        declarationDao.createDeclaration(d, null);
      }
    }
    List<CarteDemat> cartes = importExport.getCards();
    if (cartes != null) {
      for (CarteDemat d : cartes) {
        carteDematDao.create(d);
      }
    }
    List<Flux> flux = importExport.getFlux();
    if (flux != null) {
      for (Flux itemFlux : flux) {
        fluxDao.create(itemFlux);
      }
    }
    List<Volumetrie> volumetries = importExport.getVolumetries();
    if (volumetries != null) {
      for (Volumetrie volumetrie : volumetries) {
        volumetrieDao.create(volumetrie);
      }
    }
    List<Declarant> declarants = importExport.getDeclarants();
    if (declarants != null) {
      for (Declarant declarant : declarants) {
        declarantDaoImpl.create(declarant);
      }
    }
    List<Circuit> circuits = importExport.getCircuits();
    if (circuits != null) {
      for (Circuit circuit : circuits) {
        circuitDao.create(circuit);
      }
    }
    List<ServiceDroits> services = importExport.getServices();
    if (services != null) {
      for (ServiceDroits service : services) {
        serviceDroitsDao.create(service);
      }
    }
    List<ParametreBdd> parametres = importExport.getParametres();
    if (parametres != null) {
      for (ParametreBdd parametre : parametres) {
        parametreBddDao.create(parametre);
      }
    }
    List<Transcodage> transcodages = importExport.getTranscodage();
    if (transcodages != null) {
      for (Transcodage transcodage : transcodages) {
        transcodageDao.create(transcodage);
      }
    }
    List<TranscoParametrage> transcoParametrages = importExport.getTranscoParametrage();
    if (transcoParametrages != null) {
      for (TranscoParametrage transcoParametrage : transcoParametrages) {
        transcoParametrageDao.create(transcoParametrage);
      }
    }
    /*
    TODO ?
    List<HistoriqueExecution> histosExec = importExport.getHistoriqueExecution();
    if (histosExec != null) {
      for (HistoriqueExecution histoExec : histosExec) {
        historiqueExecutionDao.save(histoExec);
      }
    }*/
  }

  private void deleteAll() {
    carteDematDao.dropCollection(CarteDemat.class);
    fluxDao.dropCollection(Flux.class);
    volumetrieDao.dropCollection(Volumetrie.class);
    mongoTemplate.dropCollection(Constants.CONTRATS_COLLECTION_NAME);
    // Clear de la collection declaration plutot que drop pour les tests de services
    // (la collection declarations doit toujours exister)
    mongoTemplate.findAllAndRemove(new Query(), Constants.DECLARATION_COLLECTION);
  }
}
