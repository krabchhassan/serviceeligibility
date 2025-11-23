package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import config.Test608Configuration;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = Test608Configuration.class)
@Slf4j
class ProcessorAlmerys6943EvtTest extends MockUtils {

  // TODO @Test
  void testPassant6943() {
    List<TmpObject2> tmpObject2List = new ArrayList<>();
    tmpObject2List.add(createTmpObject2());

    test(tmpObject2List, "generatedXml/testPassant6943.xml");
  }

  @Override
  public TmpObject2 createTmpObject2() {
    TmpObject2 tmpObject2 = new TmpObject2();
    tmpObject2.setCodeEtat("V");
    tmpObject2.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONEVT);

    BeneficiaireAlmerys benef = new BeneficiaireAlmerys();
    benef.setRefExternePersonne("RefExternePersonne");
    benef.setNumeroPersonne("NumeroPersonne");
    AdresseAlmerys adresse = new AdresseAlmerys();
    TypeAdresse typeAdresse = new TypeAdresse();
    typeAdresse.setType("AD");
    adresse.setLigne1("Ligne1");
    adresse.setEmail("toto@gmail.com");
    adresse.setTypeAdresse(typeAdresse);
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

    tmpObject2.setBeneficiaire(benef);

    Contrat contrat = new Contrat();
    contrat.setNumero("NumeroContrat");
    contrat.setDestinataire("AD");
    contrat.setDateSouscription("2023/03/02");
    tmpObject2.setContrat(contrat);

    DomaineDroitAlmerys dom = new DomaineDroitAlmerys();
    dom.setCodeExterneProduit("PH");
    PeriodeDroit periode = new PeriodeDroit();
    periode.setPeriodeDebut("2025/01/01");
    periode.setPeriodeFin("2025/03/02");
    periode.setModeObtention("O");
    dom.setPeriodeDroit(periode);
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    prioriteDroit.setCode("02");
    dom.setPrioriteDroit(prioriteDroit);
    tmpObject2.setDomaineDroit(dom);

    tmpObject2.setIdDeclarations(List.of("IdDeclaration"));

    return tmpObject2;
  }

  @Override
  public String getCritereSecondaireDetaille() {
    return "CSD2";
  }
}
