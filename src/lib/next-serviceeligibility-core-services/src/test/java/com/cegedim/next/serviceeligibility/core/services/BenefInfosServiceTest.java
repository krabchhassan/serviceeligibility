package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Amc;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class BenefInfosServiceTest {

  @Autowired private BenefInfosService benefInfos;

  public ContratAICommun generateContrat(String dateSouscription, String dateResiliation) {
    ContratAICommun contratAI = new ContratAIV6();
    contratAI.setNumero("11111");
    contratAI.setIdDeclarant("12345678");
    contratAI.setSocieteEmettrice("KLESIA CARCEPT");
    contratAI.setDateSouscription(dateSouscription);
    contratAI.setDateResiliation(dateResiliation);
    contratAI.setNumeroAdherent("11111");
    contratAI.setNumeroAdherentComplet("11111");
    contratAI.setApporteurAffaire("Courtier & Co");
    contratAI.setGestionnaire("IGestion");
    contratAI.setQualification("BASE");
    contratAI.setOrdrePriorisation("1");
    return contratAI;
  }

  private Assure generateAssure(
      List<Periode> periodesAssure,
      List<Periode> periodesDroits,
      String numeroPersonne,
      String dateRadiation) {
    Assure assure = new Assure();
    DataAssure dataAssureV5 = new DataAssure();
    List<DestinatairePrestations> destinatairePrestationsList = new ArrayList<>();
    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    destinatairePrestations.setIdDestinatairePaiements("123456");
    destinatairePrestations.setIdBeyondDestinatairePaiements("123456-AAA");
    NomDestinataire nomDestinataire = new NomDestinataire();
    nomDestinataire.setCivilite("Mr");
    nomDestinataire.setNomFamille("NOM");
    nomDestinataire.setNomUsage("NOMU");
    nomDestinataire.setPrenom("PRE");
    nomDestinataire.setRaisonSociale("RAISON");
    destinatairePrestations.setNom(nomDestinataire);
    destinatairePrestationsList.add(destinatairePrestations);
    dataAssureV5.setDestinatairesPaiements(destinatairePrestationsList);
    IdentiteContrat identite = new IdentiteContrat();
    identite.setNumeroPersonne(numeroPersonne);
    assure.setIdentite(identite);
    assure.setData(dataAssureV5);
    assure.setPeriodes(periodesAssure);
    List<DroitAssure> droits = new ArrayList<>();
    for (Periode periodeDroit : periodesDroits) {
      DroitAssure droit = new DroitAssure();
      droit.setPeriode(periodeDroit);
      droits.add(droit);
    }
    assure.setDroits(droits);
    assure.setDateRadiation(dateRadiation);

    return assure;
  }

  private BenefAIV5 generateBenef() {
    BenefAIV5 benefAIV5 = new BenefAIV5();
    benefAIV5.setServices(List.of("ServicePrestation"));
    Amc amc = new Amc();
    amc.setIdDeclarant("A");
    benefAIV5.setAmc(amc);

    List<ContratV5> contrats = new ArrayList<>();
    ContratV5 contrat = new ContratV5();
    contrat.setNumeroContrat("1111");
    List<Periode> periodesContrat = new ArrayList<>();
    periodesContrat.add(new Periode("2024-04-02", "2024-06-12"));
    periodesContrat.add(new Periode("2024-09-20", "2024-10-11"));
    contrat.setPeriodes(periodesContrat);
    contrat.setSocieteEmettrice("OS1");
    contrats.add(contrat);

    ContratV5 contrat2 = new ContratV5();
    contrat2.setNumeroContrat("2222");
    List<Periode> periodesContrat2 = new ArrayList<>();
    periodesContrat2.add(new Periode("2024-01-01", "2024-04-10"));
    contrat2.setPeriodes(periodesContrat2);
    contrat2.setSocieteEmettrice("OS2");
    contrats.add(contrat2);
    benefAIV5.setContrats(contrats);

    ContratV5 contrat3 = new ContratV5();
    contrat3.setNumeroContrat("3333");
    List<Periode> periodesContrat3 = new ArrayList<>();
    periodesContrat3.add(new Periode("2024-08-21", "2024-09-19"));
    contrat3.setPeriodes(periodesContrat3);
    contrat3.setSocieteEmettrice("OS1");
    contrats.add(contrat3);
    benefAIV5.setContrats(contrats);

    List<SocieteEmettrice> societesEmettrices = new ArrayList<>();
    SocieteEmettrice societeEmettrice = new SocieteEmettrice();
    societeEmettrice.setCode("0000452433");
    societeEmettrice.setPeriodes(periodesContrat);
    societesEmettrices.add(societeEmettrice);
    benefAIV5.setSocietesEmettrices(societesEmettrices);

    return benefAIV5;
  }

  public List<Periode> genererPeriodes(
      String debut1, String fin1, String debut2, String fin2, String debut3, String fin3) {
    List<Periode> periodes = new ArrayList<>();
    periodes.add(new Periode(debut1, fin1));
    if (debut2 != null) {
      periodes.add(new Periode(debut2, fin2));
    }
    if (debut3 != null && fin3 != null) {
      periodes.add(new Periode(debut3, fin3));
    }
    return periodes;
  }

  @Test
  void handlePeriodesContratForBenefTest() {
    List<Periode> periodesAssure =
        genererPeriodes("2024-01-03", "2024-07-12", "2024-08-08", "2024-12-31", null, null);
    List<Periode> periodesDroits =
        genererPeriodes(
            "2024-01-02", "2024-05-10", "2024-05-12", "2024-07-14", "2024-08-10", "2024-12-31");
    Periode periodeExpected = new Periode("2024-01-03", "2024-05-10");
    Periode periodeExpected2 = new Periode("2024-05-12", "2024-07-12");
    Periode periodeExpected3 = new Periode("2024-08-10", "2024-12-31");
    List<Assure> assures = new ArrayList<>();
    assures.add(generateAssure(periodesAssure, periodesDroits, "779520", null));
    ContratAICommun contrat = generateContrat("2024-01-01", "2024-12-31");
    List<Periode> periodesContratForAssure =
        benefInfos.handlePeriodesContratForBenef(contrat, assures.get(0));
    Assertions.assertEquals(periodeExpected.getDebut(), periodesContratForAssure.get(0).getDebut());
    Assertions.assertEquals(periodeExpected.getFin(), periodesContratForAssure.get(0).getFin());
    Assertions.assertEquals(
        periodeExpected2.getDebut(), periodesContratForAssure.get(1).getDebut());
    Assertions.assertEquals(periodeExpected2.getFin(), periodesContratForAssure.get(1).getFin());
    Assertions.assertEquals(
        periodeExpected3.getDebut(), periodesContratForAssure.get(2).getDebut());
    Assertions.assertEquals(periodeExpected3.getFin(), periodesContratForAssure.get(2).getFin());

    List<Periode> periodesDroit2 =
        genererPeriodes("2024-07-13", "2024-08-07", null, null, null, null);
    List<Assure> assures2 = new ArrayList<>();
    assures2.add(generateAssure(periodesAssure, periodesDroit2, "779520", null));
    ContratAICommun contrat2 = generateContrat("2024-01-01", "2024-12-31");
    List<Periode> periodesContratForAssure2 =
        benefInfos.handlePeriodesContratForBenef(contrat2, assures2.get(0));
    Assertions.assertTrue(CollectionUtils.isEmpty(periodesContratForAssure2));

    List<Periode> periodesDroit3 =
        genererPeriodes("2024-06-06", "2024-08-18", null, null, null, null);
    Periode periodeExpected4 = new Periode("2024-06-06", "2024-07-12");
    Periode periodeExpected5 = new Periode("2024-08-08", "2024-08-18");
    List<Assure> assures3 = new ArrayList<>();
    assures3.add(generateAssure(periodesAssure, periodesDroit3, "779520", null));
    ContratAICommun contrat3 = generateContrat("2024-01-01", "2024-12-31");
    List<Periode> periodesContratForAssure3 =
        benefInfos.handlePeriodesContratForBenef(contrat3, assures3.get(0));
    Assertions.assertEquals(
        periodeExpected4.getDebut(), periodesContratForAssure3.get(0).getDebut());
    Assertions.assertEquals(periodeExpected4.getFin(), periodesContratForAssure3.get(0).getFin());
    Assertions.assertEquals(
        periodeExpected5.getDebut(), periodesContratForAssure3.get(1).getDebut());
    Assertions.assertEquals(periodeExpected5.getFin(), periodesContratForAssure3.get(1).getFin());
  }

  @Test
  void handlePeriodesContratForBenefTest_withContiniousPeriods() {
    // Case : overlapping periods
    List<Periode> periodesAssure =
        genererPeriodes(
            "2024-01-02", "2024-01-28", "2024-01-03", "2024-07-12", "2024-07-13", "2024-12-31");
    List<Periode> periodesDroit =
        genererPeriodes("2024-01-02", "2024-05-10", "2024-05-12", "2024-07-18", null, null);
    Periode periodeExpected = new Periode("2024-01-02", "2024-05-10");
    Periode periodeExpected2 = new Periode("2024-05-12", "2024-07-18");

    List<Assure> assures = new ArrayList<>();
    assures.add(generateAssure(periodesAssure, periodesDroit, "779520", null));
    ContratAICommun contrat = generateContrat("2024-01-01", "2024-12-31");
    List<Periode> periodesContratForAssure =
        benefInfos.handlePeriodesContratForBenef(contrat, assures.get(0));
    Assertions.assertEquals(periodeExpected.getDebut(), periodesContratForAssure.get(0).getDebut());
    Assertions.assertEquals(periodeExpected.getFin(), periodesContratForAssure.get(0).getFin());
    Assertions.assertEquals(
        periodeExpected2.getDebut(), periodesContratForAssure.get(1).getDebut());
    Assertions.assertEquals(periodeExpected2.getFin(), periodesContratForAssure.get(1).getFin());

    // Case : same periods
    List<Periode> periodesAssure2 =
        genererPeriodes(
            "2024-01-01", "2024-02-02", "2024-01-01", "2024-02-02", "2024-03-03", "2024-04-04");
    Periode periodeExpected3 = new Periode("2024-01-02", "2024-02-02");
    Periode periodeExpected4 = new Periode("2024-03-03", "2024-04-04");
    List<Assure> assures2 = new ArrayList<>();
    assures2.add(generateAssure(periodesAssure2, periodesDroit, "779520", null));
    List<Periode> periodesContratForAssure2 =
        benefInfos.handlePeriodesContratForBenef(contrat, assures2.get(0));
    Assertions.assertEquals(
        periodeExpected3.getDebut(), periodesContratForAssure2.get(0).getDebut());
    Assertions.assertEquals(periodeExpected3.getFin(), periodesContratForAssure2.get(0).getFin());
    Assertions.assertEquals(
        periodeExpected4.getDebut(), periodesContratForAssure2.get(1).getDebut());
    Assertions.assertEquals(periodeExpected4.getFin(), periodesContratForAssure2.get(1).getFin());

    // Case : overlapping periods but with a end date null
    List<Periode> periodesAssure3 = genererPeriodes("2024-01-01", null, null, null, null, null);
    List<Periode> periodesDroit2 =
        genererPeriodes("2024-01-01", "2024-02-29", "2024-03-01", null, null, null);
    Periode periodeExpected5 = new Periode("2024-01-01", "9999-12-31");

    List<Assure> assures3 = new ArrayList<>();
    assures3.add(generateAssure(periodesAssure3, periodesDroit2, "779520", null));
    ContratAICommun contrat2 = generateContrat("2024-01-01", null);
    List<Periode> periodesContratForAssure3 =
        benefInfos.handlePeriodesContratForBenef(contrat2, assures3.get(0));
    Assertions.assertEquals(
        periodeExpected5.getDebut(), periodesContratForAssure3.get(0).getDebut());
    Assertions.assertEquals(periodeExpected5.getFin(), periodesContratForAssure3.get(0).getFin());
  }

  @Test
  void handlePeriodesContratForBenefTest_withSeveralBenef() {
    // Assure 1
    List<Periode> periodesAssure =
        genererPeriodes("2024-02-14", "2024-04-15", "2024-08-10", "2024-11-30", null, null);
    List<Periode> periodesDroit =
        genererPeriodes("2024-02-20", "2024-05-05", null, null, null, null);
    // Assure 2
    List<Periode> periodesAssure2 =
        genererPeriodes("2024-01-03", "2024-12-01", null, null, null, null);
    List<Periode> periodesDroit2 =
        genererPeriodes("2024-05-10", "2024-11-30", null, null, null, null);

    Periode periodeExpectedForAssure1 = new Periode("2024-02-20", "2024-04-15");
    Periode periodeExpectedForAssure2 = new Periode("2024-05-10", "2024-10-20");
    List<Assure> assures = new ArrayList<>();
    assures.add(generateAssure(periodesAssure, periodesDroit, "779520", null));
    assures.add(generateAssure(periodesAssure2, periodesDroit2, "559520", "2024-10-20"));

    ContratAICommun contrat = generateContrat("2024-01-01", null);
    List<Periode> periodesContratForAssure =
        benefInfos.handlePeriodesContratForBenef(contrat, assures.get(0));
    Assertions.assertEquals(
        periodeExpectedForAssure1.getDebut(), periodesContratForAssure.get(0).getDebut());
    Assertions.assertEquals(
        periodeExpectedForAssure1.getFin(), periodesContratForAssure.get(0).getFin());
    List<Periode> periodesContratForAssure2 =
        benefInfos.handlePeriodesContratForBenef(contrat, assures.get(1));
    Assertions.assertEquals(
        periodeExpectedForAssure2.getDebut(), periodesContratForAssure2.get(0).getDebut());
    Assertions.assertEquals(
        periodeExpectedForAssure2.getFin(), periodesContratForAssure2.get(0).getFin());
  }

  @Test
  void handlePeriodesContratForBenefTest_withEndDateNull() {
    List<Periode> periodesAssure =
        genererPeriodes("2024-01-02", null, "2024-07-13", null, null, null);
    List<Periode> periodesDroit =
        genererPeriodes("2024-01-02", null, "2024-05-12", "2024-07-18", null, null);
    Periode periodeExpected = new Periode("2024-01-02", "9999-12-31");

    List<Assure> assures = new ArrayList<>();
    assures.add(generateAssure(periodesAssure, periodesDroit, "779520", null));
    ContratAICommun contrat = generateContrat("2024-01-01", null);
    List<Periode> periodesContratForAssure =
        benefInfos.handlePeriodesContratForBenef(contrat, assures.get(0));
    Assertions.assertEquals(periodeExpected.getDebut(), periodesContratForAssure.get(0).getDebut());
    Assertions.assertEquals(periodeExpected.getFin(), periodesContratForAssure.get(0).getFin());
  }

  @Test
  void handlePeriodesSocieteEmettriceForBenefTest() {
    BenefAIV5 benef = generateBenef();
    SocieteEmettrice societeEmettrice = new SocieteEmettrice();
    societeEmettrice.setCode("OS1");
    List<Periode> societeEmettricePeriode = new ArrayList<>();
    societeEmettricePeriode.add(new Periode("2024-04-02", "2024-06-12"));
    societeEmettricePeriode.add(new Periode("2024-08-21", "2024-10-11"));
    societeEmettrice.setPeriodes(societeEmettricePeriode);

    SocieteEmettrice societeEmettrice2 = new SocieteEmettrice();
    societeEmettrice2.setCode("OS2");
    List<Periode> societeEmettricePeriode2 = new ArrayList<>();
    societeEmettricePeriode2.add(new Periode("2024-01-01", "2024-04-10"));
    societeEmettrice2.setPeriodes(societeEmettricePeriode2);

    List<SocieteEmettrice> societeEmettriceListe =
        benefInfos.handlePeriodesSocieteEmettriceForBenef(benef.getContrats());
    SocieteEmettrice resultSocEmettrice1 = societeEmettriceListe.get(0);
    SocieteEmettrice resultSocEmettrice2 = societeEmettriceListe.get(1);
    Assertions.assertEquals(societeEmettrice.getCode(), resultSocEmettrice1.getCode());
    Periode resultPeriodeSocEmettrice = resultSocEmettrice1.getPeriodes().get(0);
    Assertions.assertEquals(
        societeEmettrice.getPeriodes().get(0).getDebut(), resultPeriodeSocEmettrice.getDebut());
    Assertions.assertEquals(
        societeEmettrice.getPeriodes().get(0).getFin(), resultPeriodeSocEmettrice.getFin());
    Periode resultPeriodeSocEmettrice2 = resultSocEmettrice1.getPeriodes().get(1);
    Assertions.assertEquals(
        societeEmettrice.getPeriodes().get(1).getDebut(), resultPeriodeSocEmettrice2.getDebut());
    Assertions.assertEquals(
        societeEmettrice.getPeriodes().get(1).getFin(), resultPeriodeSocEmettrice2.getFin());

    Assertions.assertEquals(societeEmettrice2.getCode(), resultSocEmettrice2.getCode());
    Periode result2PeriodeSocEmettrice = resultSocEmettrice2.getPeriodes().get(0);
    Assertions.assertEquals(
        societeEmettrice2.getPeriodes().get(0).getDebut(), result2PeriodeSocEmettrice.getDebut());
    Assertions.assertEquals(
        societeEmettrice2.getPeriodes().get(0).getFin(), result2PeriodeSocEmettrice.getFin());
  }
}
