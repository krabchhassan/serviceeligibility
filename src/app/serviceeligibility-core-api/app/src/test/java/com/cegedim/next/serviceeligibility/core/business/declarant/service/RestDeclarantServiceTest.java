package com.cegedim.next.serviceeligibility.core.business.declarant.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.business.beneficiaire.dao.BeneficiaireDao;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao.PrestIJDao;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao.ServicePrestationMongoDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationMongo;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.ContratIJ;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Oc;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class RestDeclarantServiceTest {

  @Autowired RestDeclarantService restDeclarantService;
  @Autowired BeneficiaireDao benefDao;
  @Autowired PrestIJDao pijDao;

  @Autowired private ServicePrestationMongoDao spmDao;

  @Test
  void deletePrestationsNoAMCTest() {
    String declarant = "000000001";
    String service = "ServicePrestation";

    Mockito.when(restDeclarantService.findById(Mockito.anyString())).thenReturn(null);
    List<String> compteRendu = restDeclarantService.deletePrestations(List.of(declarant), service);
    Assertions.assertEquals(1, compteRendu.size());
    Assertions.assertEquals("L'AMC n°000000001 n'existe pas.", compteRendu.get(0));
  }

  @Test
  void deletePrestationsServicePrestationTest() {
    String declarant = "000000001";
    String service = "ServicePrestation";

    ServicePrestationMongo servicePrestationMongo = new ServicePrestationMongo();
    servicePrestationMongo.setIdDeclarant(declarant);
    servicePrestationMongo.setNumero("");

    Mockito.when(restDeclarantService.findById(Mockito.anyString())).thenReturn(getDeclarant());
    Mockito.when(benefDao.removeService(Mockito.anyString(), Mockito.anyString())).thenReturn(0L);

    Mockito.when(spmDao.findServicePrestationMongo(Mockito.anyString()))
        .thenReturn(List.of(servicePrestationMongo));

    List<String> compteRendu = restDeclarantService.deletePrestations(List.of(declarant), service);
    Assertions.assertEquals(1, compteRendu.size());
    Assertions.assertEquals(
        "AMC : "
            + declarant
            + " - Nb ServicePrestation supprimé(s) : 1 - Nb bénéficiaires modifié(s) : 0",
        compteRendu.get(0));
  }

  @Test
  void deletePrestationsPrestIJTest() {
    String declarant = "000000001";
    String service = "PrestIJ";

    PrestIJ prestIJ = new PrestIJ();
    prestIJ.setContrat(new ContratIJ());
    prestIJ.setOc(new Oc());

    Mockito.when(restDeclarantService.findById(Mockito.anyString())).thenReturn(getDeclarant());
    Mockito.when(benefDao.removeService(Mockito.anyString(), Mockito.anyString())).thenReturn(0L);

    Mockito.when(pijDao.findServicePrestIJ(Mockito.anyString())).thenReturn(List.of(prestIJ));

    List<String> compteRendu = restDeclarantService.deletePrestations(List.of(declarant), service);
    Assertions.assertEquals(1, compteRendu.size());
    Assertions.assertEquals(
        "AMC : "
            + declarant
            + " - Nb PrestIJ supprimée(s) : "
            + 1
            + " - Nb bénéficiaires modifié(s) : 0",
        compteRendu.get(0));
  }

  private Declarant getDeclarant() {
    Declarant declarant = new Declarant();
    declarant.set_id("000000001");
    declarant.setNom("AMC Test JUnit");
    declarant.setIdClientBO("Unidentified");

    return declarant;
  }
}
