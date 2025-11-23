package com.cegedim.next.beneficiary.worker.service;

import com.cegedim.next.beneficiary.worker.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Source;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.HistoriqueDateRangNaissance;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@JsonTest
@Import(TestConfiguration.class)
class BenefV5ServiceTest {

  private final Logger logger = LoggerFactory.getLogger(BenefV5ServiceTest.class);

  @Autowired BenefV5Service service;

  @Autowired TraceService traceService;

  @Autowired MongoTemplate mongoTemplate;

  List<BenefAIV5> databaseBenefs = new ArrayList<>();

  @BeforeEach
  public void before() {
    databaseBenefs.clear();
    Trace t = new Trace();
    t.setId("id");

    Mockito.when(mongoTemplate.save(Mockito.any(), Mockito.eq(Constants.BENEF_TRACE)))
        .thenReturn(t);
  }

  @Test
  void processBenefAIV5_SeveralContract() {

    Mockito.doAnswer(
            invocation -> {
              return invocation.getArgument(0);
            })
        .when(mongoTemplate)
        .save(Mockito.any(), Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME));

    BenefAIV5 benef = getBenef();
    BenefAIV5 processedBenef = service.process(benef, true, Source.SERVICE_PRESTATION);
    Assertions.assertEquals(processedBenef.getIdClientBO(), benef.getIdClientBO());
    Assertions.assertEquals(processedBenef.getKey(), benef.getKey());
    Assertions.assertEquals(processedBenef.getNumeroAdherent(), benef.getNumeroAdherent());

    Amc processedAmc = processedBenef.getAmc();
    Amc amc = benef.getAmc();
    Assertions.assertEquals(processedAmc.getIdDeclarant(), amc.getIdDeclarant());
    Assertions.assertEquals(processedAmc.getLibelle(), amc.getLibelle());

    Audit processedAudit = processedBenef.getAudit();
    Audit audit = benef.getAudit();
    Assertions.assertEquals(processedAudit.getDateEmission(), audit.getDateEmission());

    List<ContratV5> processedContrats = processedBenef.getContrats();
    List<ContratV5> contrats = benef.getContrats();
    Assertions.assertEquals(processedContrats.get(0).getCodeEtat(), contrats.get(0).getCodeEtat());
    Assertions.assertEquals(
        processedContrats.get(0).getNumeroContrat(), contrats.get(0).getNumeroContrat());
    DataAssure processedData = processedContrats.get(0).getData();
    DataAssure data = contrats.get(0).getData();
    AdresseAssure processedAdresse = processedData.getAdresse();
    AdresseAssure adresse = data.getAdresse();
    Assertions.assertEquals(processedAdresse.getLigne4(), adresse.getLigne4());
    Assertions.assertEquals(processedAdresse.getLigne5(), adresse.getLigne5());
    Assertions.assertEquals(processedAdresse.getLigne6(), adresse.getLigne6());
    NomAssure processedNomAssure = processedData.getNom();
    NomAssure nomAssure = data.getNom();
    Assertions.assertEquals(processedNomAssure.getCivilite(), nomAssure.getCivilite());
    Assertions.assertEquals(processedNomAssure.getNomFamille(), nomAssure.getNomFamille());
    Assertions.assertEquals(processedNomAssure.getNomUsage(), nomAssure.getNomUsage());
    Assertions.assertEquals(processedNomAssure.getPrenom(), nomAssure.getPrenom());
    List<DestinatairePrestations> processedDestPrests = processedData.getDestinatairesPaiements();
    List<DestinatairePrestations> destPrests = data.getDestinatairesPaiements();
    ModePaiement processedModePaiement = processedDestPrests.get(0).getModePaiementPrestations();
    ModePaiement modePaiement = destPrests.get(0).getModePaiementPrestations();
    Assertions.assertEquals(processedModePaiement.getCode(), modePaiement.getCode());
    Assertions.assertEquals(processedModePaiement.getCodeMonnaie(), modePaiement.getCodeMonnaie());
    Assertions.assertEquals(processedModePaiement.getLibelle(), modePaiement.getLibelle());
    NomDestinataire processedNomDestPrest = processedDestPrests.get(0).getNom();
    NomDestinataire nomDestPrest = destPrests.get(0).getNom();
    Assertions.assertEquals(processedNomDestPrest.getCivilite(), nomDestPrest.getCivilite());
    Assertions.assertEquals(processedNomDestPrest.getNomFamille(), nomDestPrest.getNomFamille());
    Assertions.assertEquals(processedNomDestPrest.getNomUsage(), nomDestPrest.getNomUsage());
    Assertions.assertEquals(processedNomDestPrest.getPrenom(), nomDestPrest.getPrenom());
    PeriodeDestinataire processedPeriode = processedDestPrests.get(0).getPeriode();
    PeriodeDestinataire periode = destPrests.get(0).getPeriode();
    Assertions.assertEquals(processedPeriode.getDebut(), periode.getDebut());
    Assertions.assertEquals(processedPeriode.getFin(), periode.getFin());
    RibAssure processedRib = processedDestPrests.get(0).getRib();
    RibAssure rib = destPrests.get(0).getRib();
    Assertions.assertEquals(processedRib.getBic(), rib.getBic());
    Assertions.assertEquals(processedRib.getIban(), rib.getIban());
    List<DestinataireRelevePrestations> processedDestRelevePrests =
        processedData.getDestinatairesRelevePrestations();
    List<DestinataireRelevePrestations> destRelevePrests = data.getDestinatairesRelevePrestations();
    processedNomDestPrest = processedDestRelevePrests.get(0).getNom();
    nomDestPrest = destRelevePrests.get(0).getNom();
    Assertions.assertEquals(processedNomDestPrest.getCivilite(), nomDestPrest.getCivilite());
    Assertions.assertEquals(processedNomDestPrest.getNomFamille(), nomDestPrest.getNomFamille());
    Assertions.assertEquals(processedNomDestPrest.getNomUsage(), nomDestPrest.getNomUsage());
    Assertions.assertEquals(processedNomDestPrest.getPrenom(), nomDestPrest.getPrenom());
    Dematerialisation demat = processedDestRelevePrests.get(0).getDematerialisation();
    Assertions.assertNotNull(demat.getIsDematerialise());
    processedAdresse = processedDestRelevePrests.get(0).getAdresse();
    adresse = destRelevePrests.get(0).getAdresse();
    Assertions.assertEquals(processedAdresse.getLigne4(), adresse.getLigne4());
    Assertions.assertEquals(processedAdresse.getLigne5(), adresse.getLigne5());
    Assertions.assertEquals(processedAdresse.getLigne6(), adresse.getLigne6());
    processedPeriode = processedDestRelevePrests.get(0).getPeriode();
    periode = destRelevePrests.get(0).getPeriode();
    Assertions.assertEquals(processedPeriode.getDebut(), periode.getDebut());
    Assertions.assertEquals(processedPeriode.getFin(), periode.getFin());

    // Ajout d'un contrat/nir...
    IdentiteContrat identite = benef.getIdentite();
    List<NirRattachementRO> affiliationsRO = identite.getAffiliationsRO();
    Nir nir = new Nir();
    nir.setCode("1791062498048");
    nir.setCle("44");
    NirRattachementRO rattRo = new NirRattachementRO();
    rattRo.setNir(nir);
    rattRo.setPeriode(new Periode("2020-12-01", null));
    rattRo.setRattachementRO(new RattachementRO("01", "595", "1236"));
    identite.setAffiliationsRO(affiliationsRO);
    benef.setIdentite(identite);

    contrats = benef.getContrats();
    ContratV5 contrat = new ContratV5();
    contrat.setCodeEtat("V");
    contrat.setNumeroContrat("32103206665");
    contrat.setData(contrats.get(0).getData());
    contrats.add(contrat);
    benef.setContrats(contrats);
    processedBenef = service.process(benef, true, Source.SERVICE_PRESTATION);
    Assertions.assertEquals(processedBenef.getIdClientBO(), benef.getIdClientBO());
    Assertions.assertEquals(processedBenef.getKey(), benef.getKey());
    Assertions.assertEquals(processedBenef.getNumeroAdherent(), benef.getNumeroAdherent());

    processedAmc = processedBenef.getAmc();
    amc = benef.getAmc();
    Assertions.assertEquals(processedAmc.getIdDeclarant(), amc.getIdDeclarant());
    Assertions.assertEquals(processedAmc.getLibelle(), amc.getLibelle());

    processedAudit = processedBenef.getAudit();
    audit = benef.getAudit();
    Assertions.assertEquals(processedAudit.getDateEmission(), audit.getDateEmission());

    processedContrats = processedBenef.getContrats();
    contrats = benef.getContrats();
    Assertions.assertEquals(processedContrats.get(0).getCodeEtat(), contrats.get(0).getCodeEtat());
    Assertions.assertEquals(
        processedContrats.get(0).getNumeroContrat(), contrats.get(0).getNumeroContrat());
    processedData = processedContrats.get(0).getData();
    data = contrats.get(0).getData();
    processedAdresse = processedData.getAdresse();
    adresse = data.getAdresse();
    Assertions.assertEquals(processedAdresse.getLigne4(), adresse.getLigne4());
    Assertions.assertEquals(processedAdresse.getLigne5(), adresse.getLigne5());
    Assertions.assertEquals(processedAdresse.getLigne6(), adresse.getLigne6());
    processedNomAssure = processedData.getNom();
    nomAssure = data.getNom();
    Assertions.assertEquals(processedNomAssure.getCivilite(), nomAssure.getCivilite());
    Assertions.assertEquals(processedNomAssure.getNomFamille(), nomAssure.getNomFamille());
    Assertions.assertEquals(processedNomAssure.getNomUsage(), nomAssure.getNomUsage());
    Assertions.assertEquals(processedNomAssure.getPrenom(), nomAssure.getPrenom());
    processedDestPrests = processedData.getDestinatairesPaiements();
    destPrests = data.getDestinatairesPaiements();
    processedModePaiement = processedDestPrests.get(0).getModePaiementPrestations();
    modePaiement = destPrests.get(0).getModePaiementPrestations();
    Assertions.assertEquals(processedModePaiement.getCode(), modePaiement.getCode());
    Assertions.assertEquals(processedModePaiement.getCodeMonnaie(), modePaiement.getCodeMonnaie());
    Assertions.assertEquals(processedModePaiement.getLibelle(), modePaiement.getLibelle());
    Assertions.assertEquals(processedNomAssure.getCivilite(), nomAssure.getCivilite());
    Assertions.assertEquals(processedNomAssure.getNomFamille(), nomAssure.getNomFamille());
    Assertions.assertEquals(processedNomAssure.getNomUsage(), nomAssure.getNomUsage());
    Assertions.assertEquals(processedNomAssure.getPrenom(), nomAssure.getPrenom());
    processedPeriode = processedDestPrests.get(0).getPeriode();
    periode = destPrests.get(0).getPeriode();
    Assertions.assertEquals(processedPeriode.getDebut(), periode.getDebut());
    Assertions.assertEquals(processedPeriode.getFin(), periode.getFin());
    processedRib = processedDestPrests.get(0).getRib();
    rib = destPrests.get(0).getRib();
    Assertions.assertEquals(processedRib.getBic(), rib.getBic());
    Assertions.assertEquals(processedRib.getIban(), rib.getIban());
    processedDestRelevePrests = processedData.getDestinatairesRelevePrestations();
    destRelevePrests = data.getDestinatairesRelevePrestations();
    Assertions.assertEquals(processedNomAssure.getCivilite(), nomAssure.getCivilite());
    Assertions.assertEquals(processedNomAssure.getNomFamille(), nomAssure.getNomFamille());
    Assertions.assertEquals(processedNomAssure.getNomUsage(), nomAssure.getNomUsage());
    Assertions.assertEquals(processedNomAssure.getPrenom(), nomAssure.getPrenom());
    demat = processedDestRelevePrests.get(0).getDematerialisation();
    Assertions.assertTrue(demat.getIsDematerialise());
    Assertions.assertNotNull(demat.getEmail());
    processedAdresse = processedDestRelevePrests.get(0).getAdresse();
    adresse = destRelevePrests.get(0).getAdresse();
    Assertions.assertEquals(processedAdresse.getLigne4(), adresse.getLigne4());
    Assertions.assertEquals(processedAdresse.getLigne5(), adresse.getLigne5());
    Assertions.assertEquals(processedAdresse.getLigne6(), adresse.getLigne6());
    processedPeriode = processedDestRelevePrests.get(0).getPeriode();
    periode = destRelevePrests.get(0).getPeriode();
    Assertions.assertEquals(processedPeriode.getDebut(), periode.getDebut());
    Assertions.assertEquals(processedPeriode.getFin(), periode.getFin());
  }

  @Test
  void processBenefAIV5_UpdateExistingContract() {
    Mockito.doAnswer(
            invocation -> {
              return invocation.getArgument(0);
            })
        .when(mongoTemplate)
        .save(Mockito.any(), Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME));

    logger.info("Update existing Benef V4");
    PeriodeDestinataire periode = new PeriodeDestinataire("2020-01-01", null);
    BenefAIV5 benef = getBenef();
    benef.getContrats().get(0).setCodeEtat(null);
    benef.getContrats().get(0).setNumeroContrat("32103206666");
    benef.getContrats().get(0).setData(null);
    AggregationResults res = new AggregationResults(Arrays.asList(), new Document());
    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(),
                Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME),
                Mockito.eq(BenefAI.class)))
        .thenReturn(res);
    BenefAIV5 processedBenef = service.process(benef, true, Source.SERVICE_PRESTATION);

    res = new AggregationResults(Arrays.asList(processedBenef), new Document());
    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(),
                Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME),
                Mockito.eq(BenefAI.class)))
        .thenReturn(res);

    // Ajout d'un nir
    IdentiteContrat identite = benef.getIdentite();
    List<NirRattachementRO> affiliationsRO = identite.getAffiliationsRO();
    Nir nir = new Nir();
    nir.setCode("1791062498048");
    nir.setCle("44");
    NirRattachementRO rattRo = new NirRattachementRO();
    rattRo.setNir(nir);
    rattRo.setPeriode(new Periode("2020-12-01", null));
    rattRo.setRattachementRO(new RattachementRO("01", "595", "1236"));
    identite.setAffiliationsRO(affiliationsRO);
    benef.setIdentite(identite);

    // Maj du contrat
    List<ContratV5> contrats = benef.getContrats();
    ContratV5 contrat = contrats.get(0);
    contrat.setCodeEtat("V");
    benef.getContrats().get(0).setData(getDataAssure(periode));

    processedBenef = service.process(benef, true, Source.SERVICE_PRESTATION);
    Assertions.assertEquals(processedBenef.getIdClientBO(), benef.getIdClientBO());
    Assertions.assertEquals(processedBenef.getKey(), benef.getKey());
    Assertions.assertEquals(processedBenef.getNumeroAdherent(), benef.getNumeroAdherent());

    Amc processedAmc = processedBenef.getAmc();
    Amc amc = benef.getAmc();
    Assertions.assertEquals(processedAmc.getIdDeclarant(), amc.getIdDeclarant());
    Assertions.assertEquals(processedAmc.getLibelle(), amc.getLibelle());

    Audit processedAudit = processedBenef.getAudit();
    Audit audit = benef.getAudit();
    Assertions.assertEquals(processedAudit.getDateEmission(), audit.getDateEmission());

    List<ContratV5> processedContrats = processedBenef.getContrats();
    contrats = benef.getContrats();
    Assertions.assertEquals(processedContrats.get(0).getCodeEtat(), contrats.get(0).getCodeEtat());
    Assertions.assertEquals(
        processedContrats.get(0).getNumeroContrat(), contrats.get(0).getNumeroContrat());
    DataAssure processedData = processedContrats.get(0).getData();
    DataAssure data = contrats.get(0).getData();
    AdresseAssure processedAdresse = processedData.getAdresse();
    AdresseAssure adresse = data.getAdresse();
    Assertions.assertEquals(processedAdresse.getLigne4(), adresse.getLigne4());
    Assertions.assertEquals(processedAdresse.getLigne5(), adresse.getLigne5());
    Assertions.assertEquals(processedAdresse.getLigne6(), adresse.getLigne6());
    NomAssure processedNomAssure = processedData.getNom();
    NomAssure nomAssure = data.getNom();
    Assertions.assertEquals(processedNomAssure.getCivilite(), nomAssure.getCivilite());
    Assertions.assertEquals(processedNomAssure.getNomFamille(), nomAssure.getNomFamille());
    Assertions.assertEquals(processedNomAssure.getNomUsage(), nomAssure.getNomUsage());
    Assertions.assertEquals(processedNomAssure.getPrenom(), nomAssure.getPrenom());
    List<DestinatairePrestations> processedDestPrests = processedData.getDestinatairesPaiements();
    List<DestinatairePrestations> destPrests = data.getDestinatairesPaiements();
    ModePaiement processedModePaiement = processedDestPrests.get(0).getModePaiementPrestations();
    ModePaiement modePaiement = destPrests.get(0).getModePaiementPrestations();
    Assertions.assertEquals(processedModePaiement.getCode(), modePaiement.getCode());
    Assertions.assertEquals(processedModePaiement.getCodeMonnaie(), modePaiement.getCodeMonnaie());
    Assertions.assertEquals(processedModePaiement.getLibelle(), modePaiement.getLibelle());
    NomDestinataire processedNomDestPrest = processedDestPrests.get(0).getNom();
    NomDestinataire nomDestPrest = destPrests.get(0).getNom();
    Assertions.assertEquals(processedNomDestPrest.getCivilite(), nomDestPrest.getCivilite());
    Assertions.assertEquals(processedNomDestPrest.getNomFamille(), nomDestPrest.getNomFamille());
    Assertions.assertEquals(processedNomDestPrest.getNomUsage(), nomDestPrest.getNomUsage());
    Assertions.assertEquals(processedNomDestPrest.getPrenom(), nomDestPrest.getPrenom());
    PeriodeDestinataire processedPeriode = processedDestPrests.get(0).getPeriode();
    PeriodeDestinataire periodeDestinataire = destPrests.get(0).getPeriode();
    Assertions.assertEquals(processedPeriode.getDebut(), periodeDestinataire.getDebut());
    Assertions.assertEquals(processedPeriode.getFin(), periodeDestinataire.getFin());
    RibAssure processedRib = processedDestPrests.get(0).getRib();
    RibAssure rib = destPrests.get(0).getRib();
    Assertions.assertEquals(processedRib.getBic(), rib.getBic());
    Assertions.assertEquals(processedRib.getIban(), rib.getIban());
    List<DestinataireRelevePrestations> processedDestRelevePrests =
        processedData.getDestinatairesRelevePrestations();
    List<DestinataireRelevePrestations> destRelevePrests = data.getDestinatairesRelevePrestations();
    processedNomDestPrest = processedDestRelevePrests.get(0).getNom();
    nomDestPrest = destRelevePrests.get(0).getNom();
    Assertions.assertEquals(processedNomDestPrest.getCivilite(), nomDestPrest.getCivilite());
    Assertions.assertEquals(processedNomDestPrest.getNomFamille(), nomDestPrest.getNomFamille());
    Assertions.assertEquals(processedNomDestPrest.getNomUsage(), nomDestPrest.getNomUsage());
    Assertions.assertEquals(processedNomDestPrest.getPrenom(), nomDestPrest.getPrenom());
    processedAdresse = processedDestRelevePrests.get(0).getAdresse();
    adresse = destRelevePrests.get(0).getAdresse();
    Assertions.assertEquals(processedAdresse.getLigne4(), adresse.getLigne4());
    Assertions.assertEquals(processedAdresse.getLigne5(), adresse.getLigne5());
    Assertions.assertEquals(processedAdresse.getLigne6(), adresse.getLigne6());
    processedPeriode = processedDestRelevePrests.get(0).getPeriode();
    periodeDestinataire = destRelevePrests.get(0).getPeriode();
    Assertions.assertEquals(processedPeriode.getDebut(), periodeDestinataire.getDebut());
    Assertions.assertEquals(processedPeriode.getFin(), periodeDestinataire.getFin());

    Assertions.assertEquals("11223344", processedBenef.getNumeroAdherent());
  }

  private BenefAIV5 getBenef() {
    BenefAIV5 benef = new BenefAIV5();
    Periode periode = new Periode("2020-01-01", null);
    String traceId = "5ecf664a4b56700001c54fda";
    traceService.createTraceForDeclaration("ServicePrestation", traceId, Constants.BENEF_TRACE);
    benef.setTraceId(traceId);
    benef.setIdClientBO("test@cegedim.com");
    benef.setNumeroAdherent("11223344");

    Amc amc = new Amc();
    amc.setIdDeclarant("1234567890");
    amc.setLibelle("Amc de test");
    benef.setAmc(amc);

    IdentiteContrat identite = new IdentiteContrat();
    Nir nir = new Nir();
    nir.setCode("1791062498047");
    nir.setCle("45");
    identite.setNir(nir);
    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO rattRo = new NirRattachementRO();
    rattRo.setNir(nir);
    rattRo.setPeriode(periode);
    rattRo.setRattachementRO(new RattachementRO("01", "624", "1236"));
    affiliationsRO.add(rattRo);
    identite.setAffiliationsRO(affiliationsRO);
    identite.setDateNaissance("19791024");
    identite.setRangNaissance("1");
    identite.setNumeroPersonne("321321032");
    identite.setRefExternePersonne("13032066654465");
    benef.setIdentite(identite);

    Audit audit = new Audit();
    audit.setDateEmission("2020-01-01");
    benef.setAudit(audit);

    List<ContratV5> contrats = new ArrayList<>();
    ContratV5 contrat = new ContratV5();
    contrat.setCodeEtat("V");
    contrat.setNumeroContrat("32103206664");
    DataAssure dataAssure =
        getDataAssure(new PeriodeDestinataire(periode.getDebut(), periode.getFin()));
    contrat.setData(dataAssure);
    contrats.add(contrat);
    benef.setContrats(contrats);
    List<String> services = new ArrayList<>();
    services.add("ServicePrestation");
    benef.setServices(services);
    benef.setKey(benef.getAmc().getIdDeclarant() + "-" + benef.getIdentite().getNumeroPersonne());

    return benef;
  }

  private DataAssure getDataAssure(PeriodeDestinataire periode) {
    DataAssure dataAssure = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setCivilite("MR");
    nom.setNomFamille("TEST");
    nom.setNomUsage("JUNIT");
    nom.setPrenom("UNITAIRE");
    dataAssure.setNom(nom);
    List<DestinatairePrestations> destinatairesPrestations = new ArrayList<>();
    DestinatairePrestations destPrest = new DestinatairePrestations();

    NomDestinataire nomDest = new NomDestinataire();
    nomDest.setCivilite("MR");
    nomDest.setNomFamille("TEST");
    nomDest.setNomUsage("JUNIT");
    nomDest.setPrenom("UNITAIRE");
    destPrest.setNom(nomDest);
    RibAssure rib = new RibAssure();
    rib.setBic("CMCIFRPP");
    rib.setIban("FR1610096000406996324279G56");
    destPrest.setRib(rib);
    ModePaiement modePaiementPrestations = new ModePaiement();
    modePaiementPrestations.setCode("VIR");
    modePaiementPrestations.setCodeMonnaie("EUR");
    modePaiementPrestations.setLibelle("Virement");
    destPrest.setModePaiementPrestations(modePaiementPrestations);
    destPrest.setPeriode(periode);
    destinatairesPrestations.add(destPrest);
    dataAssure.setDestinatairesPaiements(destinatairesPrestations);

    AdresseAssure adresse = new AdresseAssure();
    adresse.setLigne4("325 RUE DU CHAMPS");
    adresse.setLigne5("59130");
    adresse.setLigne6("MAVILLLE");
    dataAssure.setAdresse(adresse);

    List<DestinataireRelevePrestations> destinatairesRelevePrestations = new ArrayList<>();
    DestinataireRelevePrestations destRelevePrest = new DestinataireRelevePrestations();
    destRelevePrest.setNom(nomDest);
    destRelevePrest.setAdresse(adresse);
    destRelevePrest.setPeriode(periode);
    Dematerialisation demat = new Dematerialisation();
    demat.setIsDematerialise(true);
    demat.setEmail("test@test.com");
    destRelevePrest.setDematerialisation(demat);
    destinatairesRelevePrestations.add(destRelevePrest);
    dataAssure.setDestinatairesRelevePrestations(destinatairesRelevePrestations);
    return dataAssure;
  }

  @Test
  void should_work() {
    Mockito.doAnswer(
            invocation -> {
              return invocation.getArgument(0);
            })
        .when(mongoTemplate)
        .save(Mockito.any(), Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME));

    BenefAIV5 benef = null;
    BenefAIV5 newBenef = service.returnUpdatedBeneficiary(benef, Source.SERVICE_PRESTATION);
    Assertions.assertNull(newBenef);
    benef = getBenef();
    benef.setAmc(null);
    newBenef = service.returnUpdatedBeneficiary(benef, Source.SERVICE_PRESTATION);
    Assertions.assertNotNull(newBenef);
    benef = getBenef();
    benef.setServices(Arrays.asList("SERVICE2"));
    service.returnUpdatedBeneficiary(benef, Source.SERVICE_PRESTATION);
  }

  BenefAIV5 copyBenef(BenefAIV5 item) {
    BenefAIV5 newb = new BenefAIV5();
    List<ContratV5> listC = new ArrayList<>();
    newb.setContrats(listC);
    var existingList = item.getContrats();
    for (var a : existingList) {
      ContratV5 c = new ContratV5();
      c.setNumeroContrat(a.getNumeroContrat());
      listC.add(c);
    }
    return newb;
  }

  @Test
  void processBenefAIV5_UpdateExistingContract2() {

    Mockito.doAnswer(
            invocation -> {
              return invocation.getArgument(0);
            })
        .when(mongoTemplate)
        .save(Mockito.any(), Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME));

    Mockito.doAnswer(
            invocation -> {
              return invocation.getArgument(0);
            })
        .when(mongoTemplate)
        .save(Mockito.any(), Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME));

    Mockito.doAnswer(
            invocation -> {
              return invocation.getArgument(0);
            })
        .when(mongoTemplate)
        .save(Mockito.any(), Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME));

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(),
                Mockito.eq(BenefAIV5.class),
                Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME)))
        .thenReturn(null);

    PeriodeDestinataire periode = new PeriodeDestinataire("2020-01-01", null);
    BenefAIV5 benef = getBenef();
    benef.getContrats().get(0).setCodeEtat(null);
    benef.getContrats().get(0).setNumeroContrat("32103206666");
    benef.getContrats().get(0).setData(null);
    BenefAIV5 processedBenef = null;
    var b = service.process(benef, true, Source.SERVICE_PRESTATION);
    processedBenef = copyBenef(b);

    // Ajout d'un nir
    IdentiteContrat identite = benef.getIdentite();
    List<NirRattachementRO> affiliationsRO = identite.getAffiliationsRO();
    Nir nir = new Nir();
    nir.setCode("1791062498048");
    nir.setCle("44");
    NirRattachementRO rattRo = new NirRattachementRO();
    rattRo.setNir(nir);
    rattRo.setPeriode(new Periode("2020-12-01", null));
    rattRo.setRattachementRO(new RattachementRO("01", "595", "1236"));
    identite.setAffiliationsRO(affiliationsRO);
    benef.setIdentite(identite);

    // Maj du contrat
    List<ContratV5> contrats = benef.getContrats();
    ContratV5 contrat = contrats.get(0);
    contrat.setCodeEtat("");
    DataAssure data = contrat.getData();
    contrat.setData(null);
    contrat.setNumeroContrat("32103206665");
    benef.getContrats().get(0).setData(getDataAssure(periode));
    benef.setServices(null);
    benef.setAudit(null);
    benef.setContrats(contrats);

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(),
                Mockito.eq(BenefAIV5.class),
                Mockito.eq(Constants.BENEFICIAIRE_COLLECTION_NAME)))
        .thenReturn(processedBenef);

    service.process(benef, true, Source.SERVICE_PRESTATION);

    contrat.setCodeEtat("");

    DataAssure newData = new DataAssure();
    newData.setDestinatairesRelevePrestations(null);
    contrat.setData(data);
    contrat.setNumeroContrat("32103206665");
    contrats.clear();
    contrats.add(contrat);
    benef.getContrats().get(0).setData(getDataAssure(periode));
    benef.setContrats(contrats);
    processedBenef = service.process(benef, true, Source.SERVICE_PRESTATION);
    Assertions.assertEquals(2, processedBenef.getContrats().size());
  }

  @Test
  void should_update_benef_with_different_birth_dates() {
    BenefAIV5 benef = getBenef();
    benef.getIdentite().setDateNaissance("19501012");
    benef.getIdentite().setRangNaissance("1");
    List<HistoriqueDateRangNaissance> historiqueDateRangNaissances = new ArrayList<>();
    HistoriqueDateRangNaissance historiqueDateRangNaissance = new HistoriqueDateRangNaissance();
    historiqueDateRangNaissance.setDateNaissance("19501012");
    historiqueDateRangNaissance.setRangNaissance("1");
    historiqueDateRangNaissances.add(historiqueDateRangNaissance);
    benef.getIdentite().setHistoriqueDateRangNaissance(historiqueDateRangNaissances);
    service.process(benef, false, Source.SERVICE_PRESTATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(benef);
    Assertions.assertEquals(1, benef.getIdentite().getHistoriqueDateRangNaissance().size());

    BenefAIV5 newBenef = getBenef();
    newBenef.getIdentite().setDateNaissance("19991012");
    service.process(newBenef, false, Source.SERVICE_PRESTATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef);
    Assertions.assertEquals(2, newBenef.getIdentite().getHistoriqueDateRangNaissance().size());

    BenefAIV5 newBenef2 = getBenef();
    newBenef2.getIdentite().setDateNaissance("20001012");
    service.process(newBenef2, false, Source.SERVICE_PRESTATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef2);
    Assertions.assertEquals(3, newBenef2.getIdentite().getHistoriqueDateRangNaissance().size());
  }

  @Test
  void should_update_benef_with_same_birth_dates() {
    BenefAIV5 benef = getBenef();
    benef.getIdentite().setDateNaissance("19501012");
    benef.getIdentite().setRangNaissance("1");
    List<HistoriqueDateRangNaissance> historiqueDateRangNaissances = new ArrayList<>();
    HistoriqueDateRangNaissance historiqueDateRangNaissance = new HistoriqueDateRangNaissance();
    historiqueDateRangNaissance.setDateNaissance("19501012");
    historiqueDateRangNaissance.setRangNaissance("1");
    historiqueDateRangNaissances.add(historiqueDateRangNaissance);
    benef.getIdentite().setHistoriqueDateRangNaissance(historiqueDateRangNaissances);
    service.process(benef, false, Source.SERVICE_PRESTATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(benef);
    Assertions.assertEquals(1, benef.getIdentite().getHistoriqueDateRangNaissance().size());

    BenefAIV5 newBenef = getBenef();
    newBenef.getIdentite().setDateNaissance("19991012");
    service.process(newBenef, false, Source.SERVICE_PRESTATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef);
    Assertions.assertEquals(2, newBenef.getIdentite().getHistoriqueDateRangNaissance().size());

    BenefAIV5 newBenef2 = getBenef();
    newBenef2.getIdentite().setDateNaissance("19501012");
    service.process(newBenef2, false, Source.SERVICE_PRESTATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef2);
    Assertions.assertEquals(2, newBenef2.getIdentite().getHistoriqueDateRangNaissance().size());

    BenefAIV5 newBenef3 = getBenef();
    newBenef3.getIdentite().setDateNaissance("19551012");
    service.process(newBenef3, false, Source.SERVICE_PRESTATION);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(BenefAIV5.class), Mockito.anyString()))
        .thenReturn(newBenef3);
    Assertions.assertEquals(3, newBenef3.getIdentite().getHistoriqueDateRangNaissance().size());
  }
}
