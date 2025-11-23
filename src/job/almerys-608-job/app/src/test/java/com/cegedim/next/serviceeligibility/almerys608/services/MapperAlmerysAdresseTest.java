package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.AdresseAdherent;
import com.cegedim.next.serviceeligibility.almerys608.model.InfoCentreGestion;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
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
public class MapperAlmerysAdresseTest {

  @Autowired MapperAlmerysAdresse mapperAlmerysAdresse;

  @Test
  void testMapperADHtp() {
    List<InfoCentreGestion> infoCentreGestions =
        mapperAlmerysAdresse.mapInfoCentreGestions(createTmpObject2(true, false), createPilotage());
    Assertions.assertEquals(0, infoCentreGestions.size());
  }

  @Test
  void testMapperCGHtp() {
    List<InfoCentreGestion> infoCentreGestions =
        mapperAlmerysAdresse.mapInfoCentreGestions(createTmpObject2(true, true), createPilotage());
    Assertions.assertEquals(1, infoCentreGestions.size());
    InfoCentreGestion infoCentreGestion = infoCentreGestions.get(0);
    Assertions.assertEquals("CodeInterneCode", infoCentreGestion.getRefInterneCG());
    Assertions.assertEquals(5, infoCentreGestion.getInfoCartes().size());
    Assertions.assertEquals(
        "NomCommercialNomCommercialNomC", infoCentreGestion.getGestionnaireContrat());
    Assertions.assertEquals(
        "TypeGestionnaireBOTypeGestionnaireBOTypeGestionnai",
        infoCentreGestion.getTypeGestionnaire());
    Adresse adresseCG = infoCentreGestion.getAdresseCG();
    Assertions.assertEquals(
        "Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Lign",
        adresseCG.getLigne1());
    Assertions.assertEquals(
        "Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Lign",
        adresseCG.getLigne2());
    Assertions.assertEquals(
        "Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Lign",
        adresseCG.getLigne3());
    Assertions.assertEquals(
        "Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Lign",
        adresseCG.getLigne4());
    Assertions.assertEquals(
        "Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Lign",
        adresseCG.getLigne5());
    Assertions.assertEquals(
        "Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Lign",
        adresseCG.getLigne6());
    Assertions.assertEquals(
        "Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Lign",
        adresseCG.getLigne7());
    Assertions.assertEquals("12345", adresseCG.getCodePostal());
    Assertions.assertNull(adresseCG.getPays());
  }

  @Test
  void testMapperCGTdb() {
    List<InfoCentreGestion> infoCentreGestions =
        mapperAlmerysAdresse.mapInfoCentreGestions(createTmpObject2(false, true), createPilotage());
    Assertions.assertEquals(1, infoCentreGestions.size());
    InfoCentreGestion infoCentreGestion = infoCentreGestions.get(0);
    Assertions.assertEquals("GestionnaireGes", infoCentreGestion.getRefInterneCG());
    Assertions.assertEquals(5, infoCentreGestion.getInfoCartes().size());
    Assertions.assertEquals(
        "LibelleGestionnaireBOLibelleGe", infoCentreGestion.getGestionnaireContrat());
    Assertions.assertEquals(
        "TypeGestionnaireBOTypeGestionnaireBOTypeGestionnai",
        infoCentreGestion.getTypeGestionnaire());
    Adresse adresseCG = infoCentreGestion.getAdresseCG();
    Assertions.assertEquals(
        "Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Lign",
        adresseCG.getLigne1());
    Assertions.assertEquals(
        "Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Lign",
        adresseCG.getLigne2());
    Assertions.assertEquals(
        "Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Lign",
        adresseCG.getLigne3());
    Assertions.assertEquals(
        "Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Lign",
        adresseCG.getLigne4());
    Assertions.assertEquals(
        "Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Lign",
        adresseCG.getLigne5());
    Assertions.assertEquals(
        "Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Lign",
        adresseCG.getLigne6());
    Assertions.assertEquals(
        "Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Lign",
        adresseCG.getLigne7());
    Assertions.assertEquals("12345", adresseCG.getCodePostal());
    Assertions.assertEquals("0761094698", adresseCG.getTelephone());
    Assertions.assertEquals("toto@gmail.com", adresseCG.getEmail());
    Assertions.assertNull(adresseCG.getPays());
  }

  @Test
  void testMapperAdh() {
    List<AdresseAdherent> adresseAdherents =
        mapperAlmerysAdresse.mapAdressesAdherent(createTmpObject2(true, false));
    Assertions.assertEquals(1, adresseAdherents.size());
    AdresseAdherent adresseAdherent = adresseAdherents.get(0);
    Assertions.assertEquals("NumeroPersonne", adresseAdherent.getRefInterneOS());
    Assertions.assertEquals("NumeroContrat-NumeroAdherent", adresseAdherent.getNumeroContrat());
    AdresseAvecFixe adresse = adresseAdherent.getAdresse();
    Assertions.assertEquals(
        "Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Lign",
        adresse.getLigne1());
    Assertions.assertEquals(
        "Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Lign",
        adresse.getLigne2());
    Assertions.assertEquals(
        "Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Lign",
        adresse.getLigne3());
    Assertions.assertEquals(
        "Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Lign",
        adresse.getLigne4());
    Assertions.assertEquals(
        "Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Lign",
        adresse.getLigne5());
    Assertions.assertEquals(
        "Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Lign",
        adresse.getLigne6());
    Assertions.assertEquals(
        "Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Lign",
        adresse.getLigne7());
    Assertions.assertEquals("0761094698", adresse.getTelephone());
    Assertions.assertEquals("0561094698", adresse.getFixe());
    Assertions.assertEquals("12345", adresse.getCodePostal());
    Assertions.assertEquals("toto@gmail.com", adresse.getEmail());
    Assertions.assertNull(adresse.getPays());
  }

  private Pilotage createPilotage() {
    Pilotage pilotage = new Pilotage();
    InfoPilotage infoPilotage = new InfoPilotage();
    infoPilotage.setLibelleGestionnaireBO(
        "LibelleGestionnaireBOLibelleGestionnaireBOLibelleGestionnaireBOLibelleGestionnaireBOLibelleGestionnaireBOLibelleGestionnaireBOLibelleGestionnaireBO");
    infoPilotage.setTypeGestionnaireBO(
        "TypeGestionnaireBOTypeGestionnaireBOTypeGestionnaireBOTypeGestionnaireBOTypeGestionnaireBOTypeGestionnaireBOTypeGestionnaireBOTypeGestionnaireBOTypeGestionnaireBO");
    pilotage.setCaracteristique(infoPilotage);
    return pilotage;
  }

  private TmpObject2 createTmpObject2(boolean htp, boolean adressecg) {
    TmpObject2 tmpObject2 = new TmpObject2();
    tmpObject2.setCodeEtat("V");
    if (htp) {
      tmpObject2.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);
    } else {
      tmpObject2.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONTDB);
    }

    BeneficiaireAlmerys benef = new BeneficiaireAlmerys();
    benef.setRefExternePersonne("RefExternePersonne");
    benef.setNumeroPersonne("NumeroPersonne");
    AdresseAlmerys adresse = new AdresseAlmerys();
    TypeAdresse typeAdresse = new TypeAdresse();
    if (adressecg) {
      typeAdresse.setType("GE");
    } else {
      typeAdresse.setType("AD");
    }

    adresse.setLigne1(
        "Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1");
    adresse.setLigne2(
        "Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2");
    adresse.setLigne3(
        "Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3");
    adresse.setLigne4(
        "Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4");
    adresse.setLigne5(
        "Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5");
    adresse.setLigne6(
        "Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6");
    adresse.setLigne7(
        "Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7");
    adresse.setEmail("toto@gmail.com");
    adresse.setTelephone("0761094698");
    adresse.setFixe("0561094698");
    adresse.setTypeAdresse(typeAdresse);
    adresse.setCodeInterne(
        "CodeInterneCodeInterneCodeInterneCodeInterneCodeInterneCodeInterneCodeInterneCodeInterneCodeInterneCodeInterne");
    adresse.setNomCommercial(
        "NomCommercialNomCommercialNomCommercialNomCommercialNomCommercialNomCommercialNomCommercialNomCommercialNomCommercial");
    adresse.setCodePostal("12345");
    benef.setAdresses(List.of(adresse));
    Affiliation aff = new Affiliation();
    aff.setNomPatronymique("Patronymique");
    aff.setNom("Nom");
    aff.setPrenom("Prenom");
    aff.setQualite("A");
    aff.setIsTeleTransmission(true);
    aff.setTypeAssure("A");
    benef.setAffiliation(aff);
    benef.setRangNaissance("1");
    benef.setDateNaissance("20000101");

    List<String> infosCarteTP = new ArrayList<>();
    infosCarteTP.add(
        "infosCarteTP1infosCarteTP1infosCarteTP1infosCarteTP1infosCarteTP1infosCarteTP1");
    infosCarteTP.add(
        "infosCarteTP2infosCarteTP2infosCarteTP2infosCarteTP2infosCarteTP2infosCarteTP2");
    infosCarteTP.add(
        "infosCarteTP3infosCarteTP3infosCarteTP3infosCarteTP3infosCarteTP3infosCarteTP3");
    infosCarteTP.add(
        "infosCarteTP4infosCarteTP4infosCarteTP4infosCarteTP4infosCarteTP4infosCarteTP4");
    infosCarteTP.add(
        "infosCarteTP5infosCarteTP5infosCarteTP5infosCarteTP5infosCarteTP5infosCarteTP5");
    infosCarteTP.add("");
    tmpObject2.setInfosCarteTP(infosCarteTP);

    tmpObject2.setBeneficiaire(benef);

    Contrat contrat = new Contrat();
    contrat.setNumero("NumeroContrat");
    contrat.setNumeroAdherent("NumeroAdherent");
    contrat.setDestinataire("AD");
    contrat.setGestionnaire(
        "GestionnaireGestionnaireGestionnaireGestionnaireGestionnaireGestionnaire");
    tmpObject2.setContrat(contrat);

    DomaineDroitAlmerys dom = new DomaineDroitAlmerys();
    dom.setCodeExterneProduit("PH");
    PeriodeDroit periode = new PeriodeDroit();
    periode.setPeriodeDebut("2025/01/01");
    periode.setPeriodeFin("2025/12/31");
    periode.setModeObtention("O");
    dom.setPeriodeDroit(periode);
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    prioriteDroit.setCode("03");
    dom.setPrioriteDroit(prioriteDroit);
    tmpObject2.setDomaineDroit(dom);

    tmpObject2.setIdDeclarations(List.of("IdDeclaration"));

    return tmpObject2;
  }
}
