package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class FluxRdoServicePrestationService implements ApplicationContextAware {

  public static final String RDO_SP = "RDO_SP_";
  private final Logger logger = LoggerFactory.getLogger(FluxRdoServicePrestationService.class);

  private static final int NB_ASSURE_MIN = 2;
  private static final int NB_ASSURE_MAX = 5;

  private int offsetNumeroAssure = 0;

  private ApplicationContext context;

  @Autowired MongoTemplate mongoTemplate;

  @Override
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    this.context = ctx;
  }

  private String getRandomNumber(int start, int end) {
    double random = 0;
    if (start != end) {
      if (start > end) {
        random = end + (Math.random() * (start - end));
      } else {
        random = start + (Math.random() * (end - start));
      }
    }
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);
    nf.setGroupingUsed(false);
    return nf.format(random);
  }

  private int getRandomNumberAsInt(int start, int end) {
    return Integer.parseInt(getRandomNumber(start, end));
  }

  private String getCleNir(String nir) {
    long base = Long.parseLong(nir.substring(0, 13).replaceAll("[AB]", "0"));
    long cle = 97 - base % 97;
    return String.valueOf(cle);
  }

  private ContratAIV6 getContrat(String idDeclarant, int indiceContrat) {
    SecureRandom rd = new SecureRandom();
    ContratAIV6 contrat = new ContratAIV6();
    LocalDate currentdate = LocalDate.now();
    String firstDayOfYear = currentdate.getYear() + "-01-01";
    String lastDayOfYear = currentdate.getYear() + "-12-31";

    String firstDayOfMonth = String.valueOf(currentdate.withDayOfMonth(1));
    String lastDayOfMonth = String.valueOf(currentdate.withDayOfMonth(currentdate.lengthOfMonth()));
    String lastDayOfPreviousMonth =
        String.valueOf(
            currentdate
                .minusMonths(1L)
                .withDayOfMonth(currentdate.minusMonths(1L).lengthOfMonth()));

    Periode periodeCurrentYear = new Periode(firstDayOfYear, null);
    List<Periode> periodesCurrentYear = new ArrayList<>();
    periodesCurrentYear.add(new Periode(firstDayOfYear, null));

    contrat.setIdDeclarant(idDeclarant);
    contrat.setSocieteEmettrice("Test TEAM BLUE - RDO");
    contrat.setNumero(getNumeroIncremental(indiceContrat));
    contrat.setNumeroExterne(getNumeroIncremental(indiceContrat) + "_EXT");
    contrat.setNumeroAdherent("ADH_" + getNumeroIncremental(indiceContrat));
    contrat.setNumeroAdherentComplet("ADH_" + getNumeroIncremental(indiceContrat) + "COMPLET");
    contrat.setDateSouscription(firstDayOfYear);
    contrat.setApporteurAffaire("AF_BLUE_RDO");
    contrat.setPeriodesContratResponsableOuvert(periodesCurrentYear);
    contrat.setPeriodesContratCMUOuvert(getPeriodesCmu(firstDayOfYear, lastDayOfYear));
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("GEST_BLUE");
    contrat.setQualification("B");
    contrat.setOrdrePriorisation("1");

    int randomContexteTP = rd.nextInt(10);
    if (randomContexteTP >= 5) {
      ContexteTPV6 contexteTPV6 = new ContexteTPV6();
      contexteTPV6.setDateRestitutionCarte(firstDayOfMonth);
      contexteTPV6.setIsCarteDematerialisee(true);
      contexteTPV6.setIsCartePapier(true);
      contexteTPV6.setIsCartePapierAEditer(true);
      PeriodesDroitsCarte periodeDroitsCarte = new PeriodesDroitsCarte();
      periodeDroitsCarte.setDebut(firstDayOfYear);
      periodeDroitsCarte.setFin(lastDayOfYear);
      contexteTPV6.setPeriodesDroitsCarte(periodeDroitsCarte);
      contrat.setContexteTiersPayant(contexteTPV6);
    }

    int randomContratColl = rd.nextInt(10);
    if (randomContratColl >= 5) {
      ContratCollectifV6 contratCollectifV6 = new ContratCollectifV6();
      contratCollectifV6.setNumero(RandomStringUtils.randomAlphanumeric(3, 8).toUpperCase());
      contratCollectifV6.setNumeroExterne(RandomStringUtils.randomAlphanumeric(3, 8).toUpperCase());
      contratCollectifV6.setRaisonSociale(RandomStringUtils.randomAlphabetic(3, 8).toUpperCase());
      contratCollectifV6.setSiret(RandomStringUtils.randomAlphabetic(3, 8).toUpperCase());
      contratCollectifV6.setGroupePopulation(
          RandomStringUtils.randomAlphabetic(3, 8).toUpperCase());
      contratCollectifV6.setIdentifiantCollectivite(
          StringUtils.leftPad(String.valueOf(getRandomNumber(1, 999999999)), 9, "0"));
      contrat.setContratCollectif(contratCollectifV6);
    }

    processSuspension(rd, contrat, firstDayOfMonth, lastDayOfMonth, lastDayOfPreviousMonth);

    List<Assure> assures = new ArrayList<>();
    int nbAssures = rd.nextInt(NB_ASSURE_MAX - NB_ASSURE_MIN + 1) + NB_ASSURE_MIN;
    // Adresse assure - Identique pour tous les assures du contrat
    String codePostal =
        StringUtils.leftPad(String.valueOf(getRandomNumber(1, 99)), 2, "0")
            + StringUtils.leftPad(String.valueOf(getRandomNumber(1, 999)), 3, "0");
    String ligne1Adresse =
        String.valueOf(getRandomNumber(1, 9999))
            + " RUE DE "
            + RandomStringUtils.randomAlphabetic(5, 15).toUpperCase();
    String ligne6Adresse =
        codePostal + " " + RandomStringUtils.randomAlphabetic(5, 15).toUpperCase();
    AdresseAssure adresseAssure = new AdresseAssure();
    adresseAssure.setCodePostal(codePostal);
    adresseAssure.setLigne1(ligne1Adresse);
    adresseAssure.setLigne6(ligne6Adresse);

    // Destinataires de paiements identique pour tous les assures du contrat
    List<DestinatairePrestations> destinatairesPaiements = new ArrayList<>();
    DestinatairePrestations destPaiement = new DestinatairePrestations();
    Adresse adresse = new Adresse();
    adresse.setLigne1(ligne1Adresse);
    adresse.setLigne6(ligne6Adresse);
    adresse.setCodePostal(codePostal);
    destPaiement.setAdresse(adresse);
    destPaiement.setIdDestinatairePaiements(String.valueOf(indiceContrat));
    destPaiement.setModePaiementPrestations(new ModePaiement("VIR", "Virement", "EUR"));
    NomDestinataire nomDest = new NomDestinataire();
    nomDest.setNomFamille("ASSURE PRINCIPAL " + indiceContrat);
    nomDest.setPrenom("1");
    nomDest.setCivilite("MR");
    destPaiement.setNom(nomDest);
    destPaiement.setPeriode(
        new PeriodeDestinataire(periodeCurrentYear.getDebut(), periodeCurrentYear.getFin()));
    RibAssure rib = new RibAssure();
    rib.setIban("FR9810096000504861538231U73");
    rib.setBic("AGRIFRPP831");
    destPaiement.setRib(rib);
    destinatairesPaiements.add(destPaiement);
    List<DestinataireRelevePrestations> destinatairesRelevePrestations = new ArrayList<>();
    DestinataireRelevePrestations destRelevePrest = new DestinataireRelevePrestations();
    destRelevePrest.setAdresse(adresseAssure);
    destRelevePrest.setIdDestinataireRelevePrestations(String.valueOf(indiceContrat));
    destRelevePrest.setNom(nomDest);
    destRelevePrest.setPeriode(
        new PeriodeDestinataire(periodeCurrentYear.getDebut(), periodeCurrentYear.getFin()));
    Dematerialisation dematerialisation = new Dematerialisation();
    dematerialisation.setEmail(
        RandomStringUtils.randomAlphabetic(5, 10).toLowerCase()
            + "@"
            + RandomStringUtils.randomAlphabetic(3, 8).toLowerCase()
            + ".fr");
    dematerialisation.setIsDematerialise(true);
    dematerialisation.setMobile(
        "07"
            + getRandomNumber(0, 9)
            + getRandomNumber(0, 9)
            + getRandomNumber(0, 9)
            + getRandomNumber(0, 9)
            + getRandomNumber(0, 9)
            + getRandomNumber(0, 9)
            + getRandomNumber(0, 9)
            + getRandomNumber(0, 9));
    destRelevePrest.setDematerialisation(dematerialisation);
    destinatairesRelevePrestations.add(destRelevePrest);

    String anneeNaissanceASP = String.valueOf(getRandomNumber(51, 99));
    String moisNaissanceASP = StringUtils.leftPad(String.valueOf(getRandomNumber(1, 12)), 2, "0");
    String codeNirASP =
        "1"
            + anneeNaissanceASP
            + moisNaissanceASP
            + StringUtils.leftPad(getRandomNumber(1, 99999999), 8, "0");
    String cleNirASP = getCleNir(codeNirASP);
    Nir nirASP = new Nir(codeNirASP, cleNirASP);
    String anneeNaissanceCJT = String.valueOf(getRandomNumber(51, 99));
    String moisNaissanceCJT = StringUtils.leftPad(String.valueOf(getRandomNumber(1, 12)), 2, "0");
    String codeNirCJT =
        "2"
            + anneeNaissanceCJT
            + moisNaissanceCJT
            + StringUtils.leftPad(getRandomNumber(1, 99999999), 8, "0");
    String cleNirCJT = getCleNir(codeNirCJT);
    Nir nirCJT = new Nir(codeNirCJT, cleNirCJT);

    List<DroitAssure> droits = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    droit.setCode("KC_PlatineBase");
    droit.setCodeAssureur("KLESIA_CARCEPT");
    droit.setLibelle("PlatineBase");
    droit.setOrdrePriorisation("1");
    droit.setType("B");
    droit.setPeriode(periodeCurrentYear);
    droit.setDateAncienneteGarantie(firstDayOfYear);
    droits.add(droit);

    droit = new DroitAssure();
    droit.setCode("KC_PlatineComp");
    droit.setCodeAssureur("KLESIA_CARCEPT");
    droit.setLibelle("PlatineComp");
    droit.setOrdrePriorisation("2");
    droit.setType("O");
    droit.setPeriode(periodeCurrentYear);
    droit.setDateAncienneteGarantie(firstDayOfYear);
    droits.add(droit);

    for (int nbAssure = 0; nbAssure < nbAssures; nbAssure++) {
      Assure assure = new Assure();

      IdentiteContrat identite = new IdentiteContrat();
      DataAssure data = new DataAssure();
      if (nbAssure == 0) {
        assure.setIsSouscripteur(true);
        QualiteAssure qualite = new QualiteAssure("A", "Assuré principal");
        assure.setQualite(qualite);

        identite.setNir(nirASP);
        identite.setDateNaissance(
            "19"
                + anneeNaissanceASP
                + moisNaissanceASP
                + StringUtils.leftPad(getRandomNumber(1, 30), 2, "0"));
        identite.setRangNaissance("1");
        identite.setNumeroPersonne(getNumeroIncremental(offsetNumeroAssure));
        List<NirRattachementRO> lstNirRo = new ArrayList<>();
        RattachementRO rattRO = new RattachementRO("01", "624", "1253");
        NirRattachementRO nirRattRo = new NirRattachementRO();
        nirRattRo.setNir(nirASP);
        nirRattRo.setRattachementRO(rattRO);
        nirRattRo.setPeriode(periodeCurrentYear);
        lstNirRo.add(nirRattRo);
        identite.setAffiliationsRO(lstNirRo);

        //
        // Data
        //
        NomAssure nom =
            new NomAssure(
                RDO_SP + indiceContrat,
                null,
                "ASSURE PRINCIPAL" + Integer.toString(nbAssure + 1),
                "MR");
        data.setNom(nom);
        Contact contact =
            new Contact(
                "ap" + StringUtils.leftPad(getRandomNumber(1, 99999999), 8, "0") + "@gmail.coum",
                StringUtils.leftPad(getRandomNumber(1, 999999999), 10, "0"),
                StringUtils.leftPad(getRandomNumber(1, 999999999), 10, "0"));
        data.setContact(contact);
      } else {
        assure.setIsSouscripteur(false);
        String anneeNaissance = null;
        String moisNaissance = StringUtils.leftPad(String.valueOf(getRandomNumber(1, 12)), 2, "0");
        if (nbAssure == 1) {
          QualiteAssure qualite = new QualiteAssure("C", "Conjoint");
          assure.setQualite(qualite);
          anneeNaissance = String.valueOf(getRandomNumber(51, 99));
          identite.setDateNaissance(
              "19"
                  + anneeNaissance
                  + moisNaissance
                  + StringUtils.leftPad(getRandomNumber(1, 30), 2, "0"));
          NomAssure nom =
              new NomAssure(
                  RDO_SP + indiceContrat, null, "CONJOINT" + Integer.toString(nbAssure + 1), "MME");
          data.setNom(nom);
        } else {
          QualiteAssure qualite = new QualiteAssure("E", "Enfant");
          assure.setQualite(qualite);
          anneeNaissance = StringUtils.leftPad(String.valueOf(getRandomNumber(1, 20)), 2, "0");
          identite.setDateNaissance(
              "20"
                  + anneeNaissance
                  + moisNaissance
                  + StringUtils.leftPad(getRandomNumber(1, 30), 2, "0"));
          NomAssure nom =
              new NomAssure(
                  RDO_SP + indiceContrat,
                  null,
                  RandomStringUtils.randomAlphabetic(5, 15).toUpperCase()
                      + Integer.toString(nbAssure + 1),
                  "MR");
          data.setNom(nom);
        }
        identite.setNir(nirCJT);
        identite.setRangNaissance("1");
        identite.setNumeroPersonne(getNumeroIncremental(offsetNumeroAssure));
        List<NirRattachementRO> lstNirRo = new ArrayList<>();
        RattachementRO rattRO = new RattachementRO("01", "624", "1253");
        NirRattachementRO nirRattRo = new NirRattachementRO();
        nirRattRo.setNir(nirCJT);
        nirRattRo.setRattachementRO(rattRO);
        nirRattRo.setPeriode(periodeCurrentYear);
        lstNirRo.add(nirRattRo);
        identite.setAffiliationsRO(lstNirRo);

        Contact contact =
            new Contact(
                "ap" + StringUtils.leftPad(getRandomNumber(1, 99999999), 8, "0") + "@gmail.coum",
                StringUtils.leftPad(getRandomNumber(1, 999999999), 10, "0"),
                StringUtils.leftPad(getRandomNumber(1, 999999999), 10, "0"));
        data.setContact(contact);
      }
      assure.setIdentite(identite);
      data.setAdresse(adresseAssure);
      data.setDestinatairesPaiements(destinatairesPaiements);
      data.setDestinatairesRelevePrestations(destinatairesRelevePrestations);
      assure.setData(data);

      assure.setRangAdministratif(String.valueOf(nbAssure + 1));

      assure.setDateAdhesionMutuelle(firstDayOfYear);
      assure.setDateDebutAdhesionIndividuelle(firstDayOfYear);
      assure.setNumeroAdhesionIndividuelle(
          StringUtils.leftPad(String.valueOf(indiceContrat), 8, "0")
              + StringUtils.leftPad(String.valueOf(nbAssure), 8, "0"));
      assure.setPeriodes(periodesCurrentYear);
      assure.setPeriodesMedecinTraitantOuvert(periodesCurrentYear);

      DigitRelation digit = new DigitRelation();
      Dematerialisation demat = new Dematerialisation();
      demat.setIsDematerialise(false);
      digit.setDematerialisation(demat);
      List<Teletransmission> teletransmissions = new ArrayList<>();
      teletransmissions.add(new Teletransmission(periodeCurrentYear, true));
      digit.setTeletransmissions(teletransmissions);
      assure.setDigitRelation(digit);
      assure.setDroits(droits);
      assures.add(assure);

      offsetNumeroAssure++;
    }
    contrat.setAssures(assures);
    contrat.setVersion(null);
    return contrat;
  }

  private static void processSuspension(
      Random rd,
      ContratAIV6 contrat,
      String firstDayOfMonth,
      String lastDayOfMonth,
      String lastDayOfPreviousMonth) {
    int suspension = rd.nextInt(10);
    if (suspension >= 8) {
      List<PeriodeSuspension> periodesSuspension = new ArrayList<>();
      PeriodeSuspension periodeSuspension = new PeriodeSuspension();
      int suspensionFermee = rd.nextInt(10);
      periodeSuspension.setMotifSuspension("Non paiement");
      periodeSuspension.setTypeSuspension("Provisoire");
      if (suspensionFermee <= 6) {
        periodeSuspension.setPeriode(new Periode(firstDayOfMonth, lastDayOfPreviousMonth));
        periodeSuspension.setMotifLeveeSuspension("Régularisation/Annulation de la suspension");
      } else if (suspensionFermee <= 8) {
        periodeSuspension.setPeriode(new Periode(firstDayOfMonth, lastDayOfMonth));
        periodeSuspension.setMotifLeveeSuspension("Régularisation : suspension d'un mois");
      } else {
        periodeSuspension.setPeriode(new Periode(firstDayOfMonth, null));
      }
      periodesSuspension.add(periodeSuspension);
      contrat.setPeriodesSuspension(periodesSuspension);
    }
  }

  private List<PeriodeContratCMUOuvert> getPeriodesCmu(
      String firstDayOfYear, String lastDayOfYear) {
    List<PeriodeContratCMUOuvert> periodesCmu = new ArrayList<>();
    int nbPeriodes = getRandomNumberAsInt(0, 1);

    for (int i = 0; i < nbPeriodes; i++) {
      // Create a periodeContratCmu
      PeriodeContratCMUOuvert periodeCmu = new PeriodeContratCMUOuvert();
      periodeCmu.setCode("CMU");
      periodeCmu.setPeriode(new Periode(firstDayOfYear, lastDayOfYear));

      periodesCmu.add(periodeCmu);
    }

    return periodesCmu;
  }

  private String getNumeroIncremental(int offsetNumeroAssure) {
    LocalDate currentdate = LocalDate.now();
    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
    builder.appendPattern("yyMMdd");
    String numeroPersonneBase = currentdate.format(builder.toFormatter());

    return numeroPersonneBase + StringUtils.leftPad(Integer.toString(offsetNumeroAssure), 8, "0");
  }

  public int createFile(int nbContrat, String idDeclarant) throws IOException {
    String fileName = "RDO_" + idDeclarant + "_" + nbContrat + ".json";
    File directory = new File("C:\\tmp\\_cuc\\");
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directory.getAbsolutePath() + File.separator + fileName;
    String startFile = "[\n";
    String endFile = "]\n";
    Files.deleteIfExists(Paths.get(filePath));
    Files.write(
        Paths.get(filePath),
        startFile.getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    StringBuilder lineBuilder = new StringBuilder();
    for (int i = 1; i <= nbContrat; i++) {
      if (i % 50000 == 0) {
        logger.info("Nb contrats traités : {}", i);
      }

      lineBuilder.append(mapper.writeValueAsString(getContrat(idDeclarant, i)));
      if (i < nbContrat) {
        lineBuilder.append(",\n");
      } else {
        lineBuilder.append("\n");
      }
      Files.write(
          Paths.get(filePath),
          lineBuilder.toString().getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);
    }
    Files.write(
        Paths.get(filePath),
        endFile.getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    if (context != null) {
      ((ConfigurableApplicationContext) context).close();
    }
    return 0;
  }
}
