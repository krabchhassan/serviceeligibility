package com.cegedim.next.serviceeligibility.core.features.importexport.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.VolumetrieDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.importexport.ImportExportDto;
import com.cegedim.next.serviceeligibility.core.business.declaration.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Flux;
import com.cegedim.next.serviceeligibility.core.model.entity.Volumetrie;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportExportService {

  @Autowired private DeclarationDao declarationDao;

  @Autowired private CarteDematDao carteDematDao;

  @Autowired private FluxDao fluxDao;

  @Autowired private VolumetrieDao volumetrieDao;

  @ContinueSpan(log = "exportAll")
  public ImportExportDto exportAll() {
    ImportExportDto data = new ImportExportDto();
    data.setDeclarations(declarationDao.findAll());
    data.setCards(carteDematDao.findAll(CarteDemat.class));
    data.setFlux(fluxDao.findAll(Flux.class));
    data.setVolumetries(volumetrieDao.findAll(Volumetrie.class));
    return data;
  }
}
