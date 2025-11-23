package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import config.Test608Configuration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = Test608Configuration.class)
@Slf4j
class ProcessorAlmerysRejectedTest extends MockUtils {

  // TODO @Test
  void testRejected() {
    List<TmpObject2> tmpObject2List = new ArrayList<>();
    tmpObject2List.add(createTmpObject2());
    List<String> listeCodeRejet = new ArrayList<>();
    listeCodeRejet.add("A04;;A04;TYPE BENEFICIAIRE INCORRECT;Z;M;B#");

    test(tmpObject2List, "generatedXml/testEmpty.xml", listeCodeRejet);
  }

  @Override
  public TmpObject2 createTmpObject2() {
    TmpObject2 tmpObject2 = new TmpObject2();
    tmpObject2.set_id("A1");
    tmpObject2.setCodeEtat("V");
    LocalDateTime localDateTime = LocalDateTime.now().plusHours(10);
    tmpObject2.setEffetDebut(Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
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
    aff.setQualite("Z");
    aff.setIsTeleTransmission(true);
    aff.setTypeAssure("Z");
    benef.setAffiliation(aff);
    benef.setRangNaissance("1");
    benef.setDateNaissance("20000101");

    tmpObject2.setBeneficiaire(benef);

    Contrat contrat = new Contrat();
    contrat.setNumero("NumeroContrat");
    contrat.setDestinataire("AD");
    contrat.setDateSouscription("2023/04/02");
    tmpObject2.setContrat(contrat);

    DomaineDroitAlmerys dom = new DomaineDroitAlmerys();
    dom.setCodeExterneProduit("PH");
    PeriodeDroit periode = new PeriodeDroit();
    periode.setPeriodeDebut("2025/01/01");
    periode.setPeriodeFin("2025/03/02");
    periode.setModeObtention("O");
    dom.setPeriodeDroit(periode);
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    prioriteDroit.setCode("01");
    dom.setPrioriteDroit(prioriteDroit);
    tmpObject2.setDomaineDroit(dom);

    tmpObject2.setIdDeclarations(List.of("IdDeclaration"));

    return tmpObject2;
  }

  @Override
  public String getCritereSecondaireDetaille() {
    return "CSD6";
  }

  @Override
  public Declaration getDeclaration(String id) {
    Declaration decl = new Declaration();
    decl.set_id(id);
    decl.setIdDeclarant("IdDeclarant");
    Contrat contrat = new Contrat();
    contrat.setNumero("NumeroContrat");
    decl.setContrat(contrat);
    BeneficiaireV2 benef = new BeneficiaireV2();
    benef.setNumeroPersonne("NumeroPersonne");
    decl.setBeneficiaire(benef);
    DomaineDroit domaine = new DomaineDroit();
    domaine.setCode("PHAR");
    PeriodeDroit periode = new PeriodeDroit();
    periode.setModeObtention("O");
    domaine.setPeriodeDroit(periode);
    decl.setDomaineDroits(List.of(domaine));
    return decl;
  }
}
