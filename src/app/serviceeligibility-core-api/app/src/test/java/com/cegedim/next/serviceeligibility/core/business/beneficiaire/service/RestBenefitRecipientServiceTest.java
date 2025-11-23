package com.cegedim.next.serviceeligibility.core.business.beneficiaire.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients.BenefitRecipientsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients.RequestBeneficitRecipientsDto;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
public class RestBenefitRecipientServiceTest {

  public static final String ID_DECLARANT = "1234567890";
  public static final String NUMERO_PERSONNE = "321321032";
  public static final String DEBUT = "2020-01-01";

  @Test
  void mapBenefitRecipientsFromBenefTest() {
    RequestBeneficitRecipientsDto request =
        new RequestBeneficitRecipientsDto(
            ID_DECLARANT + "-" + NUMERO_PERSONNE, "11223344", "32103206664", ID_DECLARANT);
    BenefAIV5 benef = getBenef();

    RestBeneficitRecipientsService service = new RestBeneficitRecipientsService();

    List<BenefitRecipientsDto> benefitRecipientsDtoList =
        service.getBenefitRecipients(request, benef);
    Assertions.assertEquals(2, benefitRecipientsDtoList.size());
    Assertions.assertEquals(
        ID_DECLARANT + "-" + NUMERO_PERSONNE + "-TEST2",
        benefitRecipientsDtoList.get(0).getIdBeyond());
    Assertions.assertEquals("2022-01-01", benefitRecipientsDtoList.get(0).getValidityStartDate());
    Assertions.assertEquals(
        ID_DECLARANT + "-" + NUMERO_PERSONNE + "-TEST",
        benefitRecipientsDtoList.get(1).getIdBeyond());
    Assertions.assertEquals(DEBUT, benefitRecipientsDtoList.get(1).getValidityStartDate());
    Assertions.assertEquals("2021-12-31", benefitRecipientsDtoList.get(1).getValidityEndDate());
  }

  private BenefAIV5 getBenef() {
    BenefAIV5 benef = new BenefAIV5();
    PeriodeDestinataire periode = new PeriodeDestinataire(DEBUT, "2021-12-31");

    String traceId = "5ecf664a4b56700001c54fda";
    benef.setTraceId(traceId);
    benef.setIdClientBO("test@cegedim.com");
    benef.setNumeroAdherent("11223344");

    Amc amc = new Amc();
    amc.setIdDeclarant(ID_DECLARANT);
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
    rattRo.setPeriode(new Periode(DEBUT, "2021-12-31"));
    rattRo.setRattachementRO(new RattachementRO("01", "624", "1236"));
    affiliationsRO.add(rattRo);
    identite.setAffiliationsRO(affiliationsRO);
    identite.setDateNaissance("19791024");
    identite.setRangNaissance("1");
    identite.setNumeroPersonne(NUMERO_PERSONNE);
    identite.setRefExternePersonne("13032066654465");
    benef.setIdentite(identite);

    Audit audit = new Audit();
    audit.setDateEmission("2020-01-01");
    benef.setAudit(audit);

    List<ContratV5> contrats = new ArrayList<>();
    contrats.add(getContratV5("32103206664", periode));
    contrats.add(getContratV5("32103206665", periode));
    benef.setContrats(contrats);
    List<String> services = new ArrayList<>();
    services.add("ServicePrestation");
    benef.setServices(services);
    benef.setKey(benef.getAmc().getIdDeclarant() + "-" + benef.getIdentite().getNumeroPersonne());

    return benef;
  }

  private ContratV5 getContratV5(String numeroContrat, PeriodeDestinataire periode) {
    ContratV5 contrat = new ContratV5();
    contrat.setCodeEtat("V");
    contrat.setNumeroContrat(numeroContrat);
    DataAssure dataAssure = getDataAssure(periode);
    contrat.setData(dataAssure);
    return contrat;
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

    NomDestinataire nomDest = getNomDest("TEST");
    NomDestinataire nomDest2 = getNomDest("TEST2");
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
    destinatairesRelevePrestations.add(
        getDestinataireRelevePrestationsV5(periode, nomDest, adresse));
    PeriodeDestinataire periode2 = new PeriodeDestinataire("2022-01-01", null);
    destinatairesRelevePrestations.add(
        getDestinataireRelevePrestationsV5(periode2, nomDest2, adresse));
    dataAssure.setDestinatairesRelevePrestations(destinatairesRelevePrestations);
    return dataAssure;
  }

  private static NomDestinataire getNomDest(String nom) {
    NomDestinataire nomDest = new NomDestinataire();
    nomDest.setCivilite("MR");
    nomDest.setNomFamille(nom);
    nomDest.setNomUsage("JUNIT");
    nomDest.setPrenom("UNITAIRE");
    return nomDest;
  }

  private static DestinataireRelevePrestations getDestinataireRelevePrestationsV5(
      PeriodeDestinataire periode, NomDestinataire nomDest, AdresseAssure adresse) {
    DestinataireRelevePrestations destRelevePrest = new DestinataireRelevePrestations();
    destRelevePrest.setIdBeyondDestinataireRelevePrestations(
        ID_DECLARANT + "-" + NUMERO_PERSONNE + "-" + nomDest.getNomFamille());
    destRelevePrest.setNom(nomDest);
    destRelevePrest.setAdresse(adresse);
    destRelevePrest.setPeriode(periode);
    Dematerialisation demat = new Dematerialisation();
    demat.setIsDematerialise(true);
    demat.setEmail("test@test.com");
    destRelevePrest.setDematerialisation(demat);
    return destRelevePrest;
  }
}
