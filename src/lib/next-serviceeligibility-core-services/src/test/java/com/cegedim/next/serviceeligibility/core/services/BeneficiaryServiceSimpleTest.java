package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class BeneficiaryServiceSimpleTest {

  @Autowired BeneficiaryService beneficiaryService;

  ContratAIV6 prepareContract() {
    ContratAIV6 c = new ContratAIV6();
    Assure ass = new Assure();

    DataAssure data = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setNomUsage("bob");
    data.setNom(nom);
    Contact contact = new Contact();
    contact.setEmail("mymail");
    data.setContact(contact);
    ass.setData(data);
    IdentiteContrat id = new IdentiteContrat();
    id.setNumeroPersonne("numero personn");
    ass.setIdentite(id);
    Periode periode = new Periode();
    periode.setDebut("2024-01-01");
    periode.setFin("2024-02-01");
    ass.setPeriodes(List.of(periode));
    DroitAssure droitAssure = new DroitAssure();
    droitAssure.setPeriode(periode);
    ass.setDroits(List.of(droitAssure));
    List<Assure> list = new ArrayList<>();
    list.add(ass);
    c.setDateSouscription("2020-01-01");
    c.setSocieteEmettrice("ABC");
    c.setAssures(list);

    return c;
  }

  @Test
  void shouldCreateBenef() {
    ContratAIV6 contratAIV6 = prepareContract();
    List<BenefAIV5> list =
        beneficiaryService.extractBenefFromContrat(
            contratAIV6.getAssures(), contratAIV6, "keycloaksUser", "traceId");
    Assertions.assertEquals(1, list.size());
  }
}
