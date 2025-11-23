package com.cegedim.next.serviceeligibility.core.features.importexport.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.CircuitDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ServiceDroitsDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscoParametrageDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscodageDao;
import com.cegedim.next.serviceeligibility.core.business.historiqueexecution.dao.HistoriqueExecutionDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.features.importexport.dto.ParametrageImportExportDto;
import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroits;
import com.cegedim.next.serviceeligibility.core.model.entity.TranscoParametrage;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("parametrageImportExportService")
public class ParametrageImportExportService {

  @Autowired private DeclarantDao declarantDao;

  @Autowired private CircuitDao circuitDao;

  @Autowired private ServiceDroitsDao serviceDroitsDao;

  @Autowired private ParametreBddDao parametresDao;

  @Autowired private TranscodageDao transcodageDao;

  @Autowired private TranscoParametrageDao transcoParametrageDao;

  @Autowired private HistoriqueExecutionDao historiqueExecutionDao;

  @ContinueSpan(log = "importData")
  public void importData(ParametrageImportExportDto importExport) {
    deleteAll();
    List<Declarant> declarants = importExport.getDeclarants();
    createDeclarants(declarants);

    List<Circuit> circuits = importExport.getCircuits();
    createCircuits(circuits);

    List<ServiceDroits> services = importExport.getServices();
    createServices(services);

    List<ParametreBdd> parameters = importExport.getParametres();
    createParameters(parameters);

    List<Transcodage> transcodage = importExport.getTranscodage();
    if (transcodage != null) {
      for (Transcodage d : transcodage) {
        transcodageDao.create(d);
      }
    }

    List<TranscoParametrage> transcoParametrage = importExport.getTranscoParametrage();
    if (transcoParametrage != null) {
      for (TranscoParametrage d : transcoParametrage) {
        transcoParametrageDao.create(d);
      }
    }

    List<HistoriqueExecution> historiqueExecution = importExport.getHistoriqueExecution();
    if (historiqueExecution != null) {
      for (HistoriqueExecution d : historiqueExecution) {
        historiqueExecutionDao.create(d);
      }
    }
  }

  private void createParameters(List<ParametreBdd> parameters) {
    if (parameters != null) {
      for (ParametreBdd d : parameters) {
        parametresDao.create(d);
      }
    }
  }

  private void createServices(List<ServiceDroits> services) {
    if (services != null) {
      for (ServiceDroits d : services) {
        serviceDroitsDao.create(d);
      }
    }
  }

  private void createCircuits(List<Circuit> circuits) {
    if (circuits != null) {
      for (Circuit d : circuits) {
        circuitDao.create(d);
      }
    }
  }

  private void createDeclarants(List<Declarant> declarants) {
    if (declarants != null) {
      for (Declarant d : declarants) {
        declarantDao.create(d);
      }
    }
  }

  @ContinueSpan(log = "exportAll")
  public ParametrageImportExportDto exportAll() {
    ParametrageImportExportDto data = new ParametrageImportExportDto();
    data.setDeclarants(declarantDao.findAll());
    data.setCircuits(circuitDao.findAll(Circuit.class));
    data.setServices(serviceDroitsDao.findAll(ServiceDroits.class));
    data.setParametres(parametresDao.findAll(ParametreBdd.class));
    data.setTranscodage(transcodageDao.findAll(Transcodage.class));
    data.setTranscoParametrage(transcoParametrageDao.findAll(TranscoParametrage.class));
    return data;
  }

  @ContinueSpan(log = "deleteAll")
  void deleteAll() {
    declarantDao.dropCollection();
    circuitDao.dropCollection(Circuit.class);
    serviceDroitsDao.dropCollection(ServiceDroits.class);
    parametresDao.dropCollection(ParametreBdd.class);
    transcodageDao.dropCollection(Transcodage.class);
    transcoParametrageDao.dropCollection(TranscoParametrage.class);
    historiqueExecutionDao.dropCollection(HistoriqueExecution.class);
  }
}
