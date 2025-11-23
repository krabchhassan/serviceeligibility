package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.*;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class RestPrestIJServiceTest {
  @Autowired MongoTemplate mongoTemplate;

  @Autowired RestPrestIJService restPrestIJService;

  @Test
  void deleteBenefFromContractNotFound() {
    PrestIJ prestIJ = new PrestIJ();
    Oc oc = new Oc();
    oc.setDenomination("denomination");
    oc.setIdentifiant("000000001");
    oc.setIdClientBO("");
    prestIJ.setOc(oc);
    prestIJ.setTraceId("traceId");
    Assure assure = new Assure();
    DataAssure data = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setCivilite("Mme");
    nom.setNomFamille("Delmotte");
    nom.setPrenom("Pierre");
    data.setNom(nom);
    assure.setData(data);
    assure.setNumeroPersonne("288939000");
    assure.setDateNaissance("19791006");
    assure.setRangNaissance("1");
    assure.setNir(new Nir("1701062498046", "02"));
    prestIJ.setAssures(List.of(assure));
    ContratIJ contrat = new ContratIJ();
    contrat.setNumero("numContrat");
    contrat.setNumeroAdherent("numAdherent");
    prestIJ.setContrat(contrat);
    List<BenefAIV5> benefAIV5List = restPrestIJService.prestIJMapping(prestIJ);

    Assertions.assertNotNull(benefAIV5List);
    Assertions.assertEquals("PrestIJ", benefAIV5List.get(0).getServices().get(0));
    Assertions.assertEquals("288939000", benefAIV5List.get(0).getIdentite().getNumeroPersonne());
    Assertions.assertEquals("19791006", benefAIV5List.get(0).getIdentite().getDateNaissance());
    Assertions.assertEquals("1", benefAIV5List.get(0).getIdentite().getRangNaissance());
    Assertions.assertEquals("1701062498046", benefAIV5List.get(0).getIdentite().getNir().getCode());
    Assertions.assertEquals("02", benefAIV5List.get(0).getIdentite().getNir().getCle());
    Assertions.assertEquals(
        "19791006",
        benefAIV5List
            .get(0)
            .getIdentite()
            .getHistoriqueDateRangNaissance()
            .get(0)
            .getDateNaissance());
    Assertions.assertEquals(
        "1",
        benefAIV5List
            .get(0)
            .getIdentite()
            .getHistoriqueDateRangNaissance()
            .get(0)
            .getRangNaissance());
    Assertions.assertEquals("000000001", benefAIV5List.get(0).getAmc().getIdDeclarant());
    Assertions.assertEquals("denomination", benefAIV5List.get(0).getAmc().getLibelle());
    Assertions.assertEquals(
        "numContrat", benefAIV5List.get(0).getContrats().get(0).getNumeroContrat());
    Assertions.assertEquals(
        "Delmotte", benefAIV5List.get(0).getContrats().get(0).getData().getNom().getNomFamille());
    Assertions.assertEquals("numAdherent", benefAIV5List.get(0).getNumeroAdherent());
    Assertions.assertEquals("traceId", benefAIV5List.get(0).getTraceId());
  }
}
