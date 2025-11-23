package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Source;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class BeneficiaryServiceTest {

  private final Logger logger = LoggerFactory.getLogger(BeneficiaryServiceTest.class);

  @Autowired private BeneficiaryService service;

  @Autowired private MongoTemplate mongoTemplate;

  @BeforeEach
  public void initializeMocks() {
    Mockito.when(mongoTemplate.save(Mockito.any(BenefAIV5.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });
  }

  @Test
  void stage1_should_create_benef() {
    Declaration decl = getDeclaration();
    Mockito.when(mongoTemplate.save(Mockito.any(BenefAIV5.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });
    BenefAIV5 benefProcessed =
        service.process(decl, "5edf6eb65a05a957292faaa6", true, Source.TDB_DECLARATION);
    // RAZ de données générée
    benefProcessed.getIdentite().setHistoriqueDateRangNaissanceToNull();
    benefProcessed.setIdToNull();
    benefProcessed.getAudit().setDateEmissionToNull();
    benefProcessed.setTraceIdToNull();

    BenefAIV5 benef = getNewBenef();
    Assertions.assertEquals(benefProcessed, benef);
  }

  @Test
  void stage2_should_update_benef() {
    Declaration decl = getDeclaration();
    decl.getBeneficiaire().setNirBeneficiaire("2501060157181");
    decl.getBeneficiaire().setCleNirBeneficiaire("23");
    decl.getBeneficiaire().setNirOd1("2501060157181");
    decl.getBeneficiaire().setCleNirOd1("23");
    decl.getBeneficiaire().setNirOd2("2501060157182");
    decl.getBeneficiaire().setCleNirOd2("22");
    decl.getBeneficiaire().getAffiliation().setCaisseOD2("601");
    decl.getBeneficiaire().getAffiliation().setRegimeOD2("01");

    Mockito.when(mongoTemplate.save(Mockito.any(BenefAIV5.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });
    BenefAIV5 benefProcessed =
        service.process(decl, "5edf6eb65a05a957292faaa6", true, Source.TDB_DECLARATION);
    // RAZ de données générée
    benefProcessed.getIdentite().setHistoriqueDateRangNaissanceToNull();
    benefProcessed.setId(null);
    benefProcessed.getAudit().setDateEmission(null);
    benefProcessed.setTraceId(null);

    BenefAIV5 benef = getNewBenef();
    benef.getIdentite().getNir().setCode("2501060157181");
    benef.getIdentite().getNir().setCle("23");

    benef.getIdentite().getAffiliationsRO().remove(0);
    NirRattachementRO nirRattRO = new NirRattachementRO();
    Nir nirRo = new Nir("2501060157181", "23");
    nirRattRO.setNir(nirRo);
    RattachementRO rattRO = new RattachementRO("01", "601", null);
    nirRattRO.setRattachementRO(rattRO);
    nirRattRO.setPeriode(new Periode("2007-01-01", null));
    benef.getIdentite().getAffiliationsRO().add(nirRattRO);
    nirRattRO = new NirRattachementRO();
    nirRo = new Nir("2501060157182", "22");
    nirRattRO.setNir(nirRo);
    rattRO = new RattachementRO("01", "601", null);
    nirRattRO.setRattachementRO(rattRO);
    nirRattRO.setPeriode(new Periode("2007-01-01", null));
    benef.getIdentite().getAffiliationsRO().add(nirRattRO);
    logger.info(benefProcessed.toString());
    logger.info(benef.toString());
    Assertions.assertEquals(benefProcessed.getIdentite(), benef.getIdentite());
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(),
                Mockito.eq(BenefAIV5.class),
                Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME)))
        .thenReturn(benefProcessed);

    var b2 =
        service.returnCreatedOrUpdatedBeneficiary(
            decl, "5edf6eb65a05a957292faaa6", Source.TDB_DECLARATION);
    Assertions.assertEquals("32103206-00012867", b2.getKey());
  }

  @Test
  void stage2_should_calculate_key() {
    IdentiteContrat identite = new IdentiteContrat();
    identite.setNumeroPersonne("12356790");
    String key = service.calculateKey("3625147586", identite);
    Assertions.assertEquals("3625147586-12356790", key);
  }

  private Declaration getDeclaration() {
    Declaration decl = new Declaration();
    decl.setReferenceExterne("32103213");
    decl.setCodeEtat("V");
    decl.setEffetDebut(new Date());
    decl.setIdDeclarant("32103206");
    decl.setIsCarteTPaEditer(false);
    decl.setNomFichierOrigine("TESTJUNIT.TXT");
    decl.setVersionDeclaration("01");
    decl.setDateCreation(new Date());
    decl.setUserCreation("TEST");
    // Beneficiaire
    BeneficiaireV2 benef = new BeneficiaireV2();
    benef.setIdClientBO("test");
    benef.setDateNaissance("19501017");
    benef.setRangNaissance("1");
    benef.setNirBeneficiaire("2501060157180");
    benef.setCleNirBeneficiaire("24");
    benef.setNirOd1("2501060157180");
    benef.setCleNirOd1("24");
    benef.setNumeroPersonne("00012867");
    benef.setRefExternePersonne("00012867");
    Affiliation aff = new Affiliation();
    aff.setNom("BOWMAN FEDTKE");
    aff.setNomPatronymique("BOWMAN FEDTKE");
    aff.setNomMarital("BOWMAN FEDTKE");
    aff.setPrenom("Irene");
    aff.setPeriodeDebut("2007/01/01");
    aff.setQualite("C");
    aff.setRegimeOD1("01");
    aff.setCaisseOD1("601");
    aff.setHasMedecinTraitant(false);
    aff.setIsBeneficiaireACS(false);
    aff.setIsTeleTransmission(true);
    aff.setTypeAssure("CONJOI");
    benef.setAffiliation(aff);
    List<AdresseAvecFixe> adresses = new ArrayList<>();
    AdresseAvecFixe adr = new AdresseAvecFixe();
    adr.setCodePostal("59250");
    adr.setEmail("test@freex.fr");
    adr.setLigne1("MME BOWMAN FEDTKE");
    adr.setLigne3("RES DU BOIS ST ALON");
    adr.setLigne4("23 RUE OLYMPE DE GOUGES");
    adr.setLigne6("59250 HALLUIN");
    adr.setTelephone("0320508088");
    TypeAdresse typeAdr = new TypeAdresse();
    typeAdr.setType("AD");
    typeAdr.setLibelle("Ayant Droit");
    adr.setTypeAdresse(typeAdr);
    adresses.add(adr);
    benef.setAdresses(adresses);
    decl.setBeneficiaire(benef);

    // Contrat
    Contrat contrat = new Contrat();
    contrat.setNumero("000004060");
    contrat.setDateSouscription("2008/07/01");
    contrat.setNomPorteur("BOWMAN");
    contrat.setPrenomPorteur("Christian");
    contrat.setCivilitePorteur("M");
    contrat.setNumeroAdherent("00004060");
    contrat.setQualification("B");
    contrat.setRangAdministratif("02");
    contrat.setIsContratResponsable(false);
    contrat.setIsContratCMU(false);
    contrat.setDestinataire("AD");
    contrat.setIndividuelOuCollectif("1");
    contrat.setLienFamilial("C");
    contrat.setTypeConvention("MU");
    contrat.setModePaiementPrestations("V");
    contrat.setGestionnaire("CCMO");
    contrat.setNumeroCarte("000000000277855");
    contrat.setNumAMCEchange("0060005615");
    decl.setContrat(contrat);

    // Domaines de droits
    List<DomaineDroit> domaineDroits = new ArrayList<>();
    DomaineDroit dd1 = new DomaineDroit();
    dd1.setCode("PHAR");
    dd1.setCodeOptionMutualiste("0.ENT0203");
    dd1.setCodeProduit("BIEACC");
    dd1.setLibelleGarantie("Bien etre Acc");
    dd1.setCodeGarantie("0.ENT0203");
    dd1.setLibelleGarantie("Bie O AO 70 ans");
    dd1.setCodeProfil("PHAR13");
    dd1.setTauxRemboursement("100");
    dd1.setIsEditable(true);
    dd1.setIsSuspension(false);
    dd1.setDateAdhesionCouverture("2008/07/01");
    dd1.setUniteTauxRemboursement("XX");
    dd1.setNoOrdreDroit(1);
    dd1.setCategorie("PHAR");
    PrioriteDroit prioDroit = new PrioriteDroit();
    prioDroit.setCode("01");
    prioDroit.setLibelle("01");
    prioDroit.setPrioriteBO("01");
    prioDroit.setTypeDroit("01");
    dd1.setPrioriteDroit(prioDroit);
    domaineDroits.add(dd1);
    decl.setDomaineDroits(domaineDroits);
    return decl;
  }

  private BenefAIV5 getNewBenef() {
    BenefAIV5 benef = new BenefAIV5();
    Amc amc = new Amc();
    amc.setIdDeclarant("32103206");
    List<String> services = new ArrayList<>();
    services.add(Constants.SERVICE_TP);
    benef.setServices(services);
    benef.setAmc(amc);
    // amc - N° personne
    benef.setKey("32103206-00012867");
    benef.setNumeroAdherent("00004060");
    // contrats
    List<ContratV5> contrats = new ArrayList<>();
    ContratV5 contrat = new ContratV5();
    contrat.setCodeEtat("V");
    contrat.setNumeroContrat("000004060");
    DataAssure data = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setNomFamille("BOWMAN FEDTKE");
    nom.setNomUsage("BOWMAN FEDTKE");
    nom.setPrenom("Irene");
    data.setNom(nom);
    AdresseAssure adresse = new AdresseAssure();
    adresse.setCodePostal("59250");
    adresse.setLigne1("MME BOWMAN FEDTKE");
    adresse.setLigne3("RES DU BOIS ST ALON");
    adresse.setLigne4("23 RUE OLYMPE DE GOUGES");
    adresse.setLigne6("59250 HALLUIN");
    data.setAdresse(adresse);
    Contact contact = new Contact();
    contact.setEmail("test@freex.fr");
    contact.setFixe("0320508088");
    contact.setMobile("");
    data.setContact(contact);
    contrat.setData(data);
    contrat.setNumeroAdherent("00004060");
    contrat.setSocieteEmettrice("CCMO");
    contrat.setNumeroAMCEchange("0060005615");
    contrats.add(contrat);
    benef.setContrats(contrats);
    // identite
    IdentiteContrat identite = new IdentiteContrat();
    Nir nir = new Nir("2501060157180", "24");
    identite.setNir(nir);
    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nirRattRO = new NirRattachementRO();
    Nir nirRo = new Nir("2501060157180", "24");
    nirRattRO.setNir(nirRo);
    RattachementRO rattRO = new RattachementRO("01", "601", null);
    nirRattRO.setRattachementRO(rattRO);
    nirRattRO.setPeriode(new Periode("2007-01-01", null));
    affiliationsRO.add(nirRattRO);
    identite.setAffiliationsRO(affiliationsRO);
    identite.setDateNaissance("19501017");
    identite.setRangNaissance("1");
    identite.setNumeroPersonne("00012867");
    benef.setIdentite(identite);
    // Audit
    Audit audit = new Audit();
    audit.setDateEmission(null);
    benef.setAudit(audit);
    // traceId
    benef.setTraceId(null);

    return benef;
  }

  @Test
  void should_update_benef_with_different_birth_dates() {
    Declaration decl = getDeclaration();

    decl.getBeneficiaire().setDateNaissance("19501012");
    decl.getBeneficiaire().setRangNaissance("2");
    BenefAIV5 benef = service.process(decl, "hff", false, Source.TDB_DECLARATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(benef);
    Assertions.assertEquals(1, benef.getIdentite().getHistoriqueDateRangNaissance().size());

    decl.getBeneficiaire().setDateNaissance("19991012");
    BenefAIV5 newBenef = service.process(decl, "hff", false, Source.TDB_DECLARATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef);
    Assertions.assertEquals(2, newBenef.getIdentite().getHistoriqueDateRangNaissance().size());

    decl.getBeneficiaire().setDateNaissance("20001012");
    BenefAIV5 newBenef2 = service.process(decl, "hff", false, Source.TDB_DECLARATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef2);
    Assertions.assertEquals(3, newBenef2.getIdentite().getHistoriqueDateRangNaissance().size());
  }

  @Test
  void should_update_benef_with_same_birth_dates() {
    Declaration decl = getDeclaration();

    decl.getBeneficiaire().setDateNaissance("19501012");
    decl.getBeneficiaire().setRangNaissance("2");
    BenefAIV5 benef = service.process(decl, "hff", false, Source.TDB_DECLARATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(benef);
    Assertions.assertEquals(1, benef.getIdentite().getHistoriqueDateRangNaissance().size());

    decl.getBeneficiaire().setDateNaissance("19991012");
    BenefAIV5 newBenef = service.process(decl, "hff", false, Source.TDB_DECLARATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef);
    Assertions.assertEquals(2, newBenef.getIdentite().getHistoriqueDateRangNaissance().size());

    decl.getBeneficiaire().setDateNaissance("19501012");
    BenefAIV5 newBenef2 = service.process(decl, "hff", false, Source.TDB_DECLARATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef2);
    Assertions.assertEquals(2, newBenef2.getIdentite().getHistoriqueDateRangNaissance().size());

    decl.getBeneficiaire().setDateNaissance("19551012");
    BenefAIV5 newBenef3 = service.process(decl, "hff", false, Source.TDB_DECLARATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef3);
    Assertions.assertEquals(3, newBenef3.getIdentite().getHistoriqueDateRangNaissance().size());
  }
}
