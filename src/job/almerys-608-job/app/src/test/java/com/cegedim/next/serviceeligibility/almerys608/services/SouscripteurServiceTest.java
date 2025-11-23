package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.InfosSouscripteur;
import com.cegedim.next.serviceeligibility.almerys608.model.Souscripteur;
import config.Test608Configuration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = Test608Configuration.class)
class SouscripteurServiceTest {

  @Autowired SouscripteurService souscripteurService;

  @Test
  void casHtp() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            true, "X", "V", null, List.of("2020-01-01"), "A", "A", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2020-01-01"), "A", "B", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurTrue(souscripteurs.get(0), "A");
    testSouscripteurFalse(souscripteurs.get(1), "B");
  }

  @Test
  void casTalendQualiteAValide() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "A", "V", null, List.of("2020-01-01"), "A", "A", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2020-01-01"), "A", "B", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurTrue(souscripteurs.get(0), "A");
    testSouscripteurFalse(souscripteurs.get(1), "B");
  }

  @Test
  void casTalendQualiteAFermeSansRadiation() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "A", "R", null, List.of("2020-01-01"), "A", "A", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "C", "R", null, List.of("2020-01-01"), "A", "B", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurTrue(souscripteurs.get(0), "A");
    testSouscripteurFalse(souscripteurs.get(1), "B");
  }

  @Test
  void casAdherentSouscripteur() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "C", "R", "2020-01-01", List.of("2020-01-01"), "A", "123", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "A", "V", "2020-01-01", List.of("2020-01-01"), "A", "012", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "123");
    testSouscripteurTrue(souscripteurs.get(1), "012");
  }

  @Test
  void casConjointSouscripteur() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "X", "R", null, List.of("2020-01-01"), "A", "123", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "C", "R", null, List.of("2020-01-01"), "A", "012", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "123");
    testSouscripteurTrue(souscripteurs.get(1), "012");
  }

  @Test
  void casNumeroPersonne() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2020-01-01"), "A", "123", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2020-01-01"), "A", "012", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "123");
    testSouscripteurTrue(souscripteurs.get(1), "012");
  }

  @Test
  void casNumeroPersonne2() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(false, "X", "V", null, null, "A", "B", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(false, "X", "V", null, null, "A", "A", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "B");
    testSouscripteurTrue(souscripteurs.get(1), "A");
  }

  @Test
  void casDiffRightsEnd() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2020-01-01"), "A", "A", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(false, "X", "V", null, null, "A", "B", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "A");
    testSouscripteurTrue(souscripteurs.get(1), "B");
  }

  @Test
  void casDiffRightsEnd2() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2021-01-01"), "A", "A", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2022-01-01"), "A", "B", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "A");
    testSouscripteurTrue(souscripteurs.get(1), "B");
  }

  @Test
  void casDiffRank() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2021-01-01"), "B", "A", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "X", "V", null, List.of("2021-01-01"), "A", "B", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(2, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "A");
    testSouscripteurTrue(souscripteurs.get(1), "B");
  }

  @Test
  void casTroisPersonnes() {
    List<InfosSouscripteur> infosSouscripteurList = new ArrayList<>();
    InfosSouscripteur infosSouscripteur =
        new InfosSouscripteur(
            false, "A", "V", null, List.of("2021-01-01"), "3", "123", null, null, null);
    InfosSouscripteur infosSouscripteur2 =
        new InfosSouscripteur(
            false, "A", "V", null, List.of("2021-01-01"), "2", "245", null, null, null);
    InfosSouscripteur infosSouscripteur3 =
        new InfosSouscripteur(
            false, "E", "V", null, List.of("2021-01-01"), "1", "01", null, null, null);
    infosSouscripteurList.add(infosSouscripteur);
    infosSouscripteurList.add(infosSouscripteur2);
    infosSouscripteurList.add(infosSouscripteur3);
    List<Souscripteur> souscripteurs =
        souscripteurService.findAllAdherent(infosSouscripteurList, "211");
    Assertions.assertEquals(3, souscripteurs.size());
    testSouscripteurFalse(souscripteurs.get(0), "123");
    testSouscripteurTrue(souscripteurs.get(1), "245");
    testSouscripteurFalse(souscripteurs.get(2), "01");
  }

  private static void testSouscripteurFalse(Souscripteur souscripteur2, String numeroPersonne) {
    Assertions.assertEquals("211", souscripteur2.getNumeroContrat());
    Assertions.assertEquals(numeroPersonne, souscripteur2.getRefInterneOs());
    Assertions.assertEquals("02", souscripteur2.getPosition());
    Assertions.assertFalse(souscripteur2.isSouscripteur());
  }

  private static void testSouscripteurTrue(Souscripteur souscripteur, String numeroPersonne) {
    Assertions.assertEquals("211", souscripteur.getNumeroContrat());
    Assertions.assertEquals(numeroPersonne, souscripteur.getRefInterneOs());
    Assertions.assertEquals("01", souscripteur.getPosition());
    Assertions.assertTrue(souscripteur.isSouscripteur());
  }
}
