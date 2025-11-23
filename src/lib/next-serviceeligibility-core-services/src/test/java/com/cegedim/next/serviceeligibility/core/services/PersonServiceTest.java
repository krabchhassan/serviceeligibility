package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinataireRelevePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class PersonServiceTest {

  @Autowired private PersonService personService;

  @Test
  void extractBenefFromContratCommunListAssure() {
    List<Assure> assures = generateAssures();
    ContratAICommun contrat = generateContrat();
    String idBo = "BO";
    List<Periode> expectedPeriodes = getExpectedPeriodes();

    List<BenefAIV5> benefs =
        personService.extractBenefFromContratCommun(assures, contrat, idBo, "");
    assertions(assures, contrat, idBo, expectedPeriodes, benefs);
  }

  @Test
  void extractBenefFromContratCommunContraAIV5() {
    List<Assure> assures = generateAssures();
    ContratAIV6 contrat = generateContratAIV6();
    contrat.setAssures(assures);
    String idBo = "BO";
    List<Periode> expectedPeriodes = getExpectedPeriodes();

    List<BenefAIV5> benefs = personService.extractBenefFromContratCommun(contrat, idBo, "", "0000");
    assertions(assures, contrat, idBo, expectedPeriodes, benefs);
  }

  private void assertions(
      List<Assure> assures,
      ContratAICommun contrat,
      String idBo,
      List<Periode> expectedPeriodes,
      List<BenefAIV5> benefs) {
    Assertions.assertFalse(benefs.isEmpty());
    Assure assureTested = assures.get(0);
    BenefAIV5 benefTested = benefs.get(0);

    Assertions.assertFalse(benefTested.getContrats().isEmpty());
    Assertions.assertEquals(assureTested.getData(), benefTested.getContrats().get(0).getData());
    Assertions.assertEquals(
        assureTested.getIdentite().getNir(), benefTested.getIdentite().getNir());
    Assertions.assertEquals(
        assureTested.getIdentite().getDateNaissance(),
        benefTested.getIdentite().getDateNaissance());
    Assertions.assertEquals(
        assureTested.getIdentite().getRangNaissance(),
        benefTested.getIdentite().getRangNaissance());
    Assertions.assertEquals(
        assureTested.getIdentite().getNumeroPersonne(),
        benefTested.getIdentite().getNumeroPersonne());

    Assertions.assertEquals(contrat.getIdDeclarant(), benefTested.getAmc().getIdDeclarant());
    Assertions.assertEquals(contrat.getNumeroAdherent(), benefTested.getNumeroAdherent());
    Assertions.assertEquals(idBo, benefTested.getIdClientBO());
    Assertions.assertEquals(expectedPeriodes, benefTested.getContrats().get(0).getPeriodes());
    Assertions.assertEquals(
        contrat.getSocieteEmettrice(), benefTested.getSocietesEmettrices().get(0).getCode());
    Assertions.assertEquals(
        expectedPeriodes, benefTested.getSocietesEmettrices().get(0).getPeriodes());
  }

  private List<Assure> generateAssures() {
    Assure assure = new Assure();
    DataAssure data = new DataAssure();

    NomAssure nom = new NomAssure("PIPO", "PIPAU", "Jean", "M");
    data.setNom(nom);

    Contact contact = new Contact("00000", "11111", "a@mail.fr");
    data.setContact(contact);

    AdresseAssure adresse = new AdresseAssure("pipo", "", "", "", "", "", "", "31500");
    data.setAdresse(adresse);

    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    data.setDestinatairesPaiements(List.of(destinatairePrestations));

    DestinataireRelevePrestations destinataireRelevePrestations =
        new DestinataireRelevePrestations();
    data.setDestinatairesRelevePrestations(List.of(destinataireRelevePrestations));

    assure.setData(data);

    IdentiteContrat identiteContrat = new IdentiteContrat();

    Nir nir = new Nir("0000", "1111");
    identiteContrat.setNir(nir);

    identiteContrat.setDateNaissance("01/01/1900");
    identiteContrat.setNumeroPersonne("0000");
    identiteContrat.setRangNaissance("1");
    identiteContrat.setRefExternePersonne("0000");

    assure.setIdentite(identiteContrat);

    Periode periodeAssure1 = new Periode();
    periodeAssure1.setDebut("2024-01-03");
    periodeAssure1.setFin("2024-07-12");
    Periode periodeAssure2 = new Periode();
    periodeAssure2.setDebut("2024-08-08");
    periodeAssure2.setFin("2024-12-31");
    assure.setPeriodes(List.of(periodeAssure1, periodeAssure2));

    Periode periodeDroit = new Periode();
    periodeDroit.setDebut("2024-01-02");
    periodeDroit.setFin("2024-05-10");
    Periode periodeDroit2 = new Periode();
    periodeDroit2.setDebut("2024-05-12");
    periodeDroit2.setFin("2024-07-14");
    Periode periodeDroit3 = new Periode();
    periodeDroit3.setDebut("2024-08-10");
    periodeDroit3.setFin("2024-12-31");
    DroitAssure droit = new DroitAssure();
    droit.setPeriode(periodeDroit);
    DroitAssure droit2 = new DroitAssure();
    droit2.setPeriode(periodeDroit2);
    DroitAssure droi3 = new DroitAssure();
    droi3.setPeriode(periodeDroit3);
    assure.setDroits(List.of(droit, droit2, droi3));

    return List.of(assure);
  }

  private ContratAICommun generateContrat() {
    ContratAICommun contratAICommun = new ContratAIV6();
    contratAICommun.setId("0000");
    contratAICommun.setNumero("1111");
    contratAICommun.setNumeroAdherent("1111");
    contratAICommun.setIdDeclarant("A");
    contratAICommun.setDateSouscription("2024-01-01");
    contratAICommun.setSocieteEmettrice("000452433");
    return contratAICommun;
  }

  private ContratAIV6 generateContratAIV6() {
    ContratAIV6 contratAICommun = new ContratAIV6();
    contratAICommun.setId("0000");
    contratAICommun.setNumero("1111");
    contratAICommun.setNumeroAdherent("1111");
    contratAICommun.setIdDeclarant("A");
    contratAICommun.setDateSouscription("2024-01-01");
    contratAICommun.setSocieteEmettrice("000452433");
    return contratAICommun;
  }

  private List<Periode> getExpectedPeriodes() {
    List<Periode> expectedPeriodes = new ArrayList<>();
    Periode periodeExpected1 = new Periode();
    periodeExpected1.setDebut("2024-01-03");
    periodeExpected1.setFin("2024-05-10");
    Periode periodeExpected2 = new Periode();
    periodeExpected2.setDebut("2024-05-12");
    periodeExpected2.setFin("2024-07-12");
    Periode periodeExpected3 = new Periode();
    periodeExpected3.setDebut("2024-08-10");
    periodeExpected3.setFin("2024-12-31");
    expectedPeriodes.add(periodeExpected1);
    expectedPeriodes.add(periodeExpected2);
    expectedPeriodes.add(periodeExpected3);
    return expectedPeriodes;
  }
}
