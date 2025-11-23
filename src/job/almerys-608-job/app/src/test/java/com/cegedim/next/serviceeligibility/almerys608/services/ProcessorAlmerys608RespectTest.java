package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import config.Test608Configuration;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = Test608Configuration.class)
@Slf4j
class ProcessorAlmerys608RespectTest extends MockUtils {

  // TODO @Test
  void testPassant() {
    List<TmpObject2> tmpObject2List = new ArrayList<>();
    tmpObject2List.add(createTmpObject2());
    test(tmpObject2List, "generatedXml/testPassantRespect.xml");
  }

  @Override
  public TmpObject2 createTmpObject2() {
    TmpObject2 tmpObject2 = new TmpObject2();
    tmpObject2.setCodeEtat("V");

    BeneficiaireAlmerys benef = new BeneficiaireAlmerys();
    benef.setRefExternePersonne(
        "RefExternePersonneRefExternePersonneRefExternePersonneRefExternePersonne");
    benef.setNumeroPersonne(
        "NumeroPersonneNumeroPersonneNumeroPersonneNumeroPersonneNumeroPersonneNumeroPersonne");
    AdresseAlmerys adresse = new AdresseAlmerys();
    TypeAdresse typeAdresse = new TypeAdresse();
    typeAdresse.setType("AD");
    adresse.setLigne1(
        "Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1Ligne1");
    adresse.setLigne2(
        "Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2Ligne2");
    adresse.setLigne3(
        "Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3Ligne3");
    adresse.setLigne4(
        "Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4Ligne4");
    adresse.setLigne5(
        "Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5Ligne5");
    adresse.setLigne6(
        "Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6Ligne6");
    adresse.setLigne7(
        "Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7Ligne7");
    adresse.setEmail("toto@gmail.com");
    adresse.setTypeAdresse(typeAdresse);
    benef.setAdresses(List.of(adresse));
    Affiliation aff = new Affiliation();
    aff.setNomPatronymique(
        "PatronymiquePatronymiquePatronymiquePatronymiquePatronymiquePatronymiquePatronymiquePatronymiquePatronymique");
    aff.setNom(
        "NomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsagerNomUsager");
    aff.setPrenom("PrenomPrenomPrenomPrenomPrenomPrenomPrenomPrenomPrenomPrenom");
    aff.setQualite("A");
    aff.setIsTeleTransmission(true);
    aff.setTypeAssure("A");
    benef.setAffiliation(aff);
    benef.setRangNaissance("1");
    benef.setDateNaissance("20000101");

    tmpObject2.setBeneficiaire(benef);

    Contrat contrat = new Contrat();
    contrat.setNumero("NumeroContratQuiDepasse30caractères");
    contrat.setDestinataire("AD");
    tmpObject2.setContrat(contrat);

    DomaineDroitAlmerys dom = new DomaineDroitAlmerys();
    dom.setCodeExterneProduit(
        "Lareferenceduproduitnedoitpasdepasser80caracteresLareferenceduproduitnedoitpasdepasser80caracteres");
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

  @Override
  public String getCritereSecondaireDetaille() {
    return "CSDR";
  }
}
