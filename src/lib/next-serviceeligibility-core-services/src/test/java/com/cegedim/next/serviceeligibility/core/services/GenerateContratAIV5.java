package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.QualiteAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.RattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.RibAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.TypeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class GenerateContratAIV5 {
  public ContratAIV6 getContrat() {
    final ContratAIV6 contrat = new ContratAIV6();
    final LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    final String firstDayOfYear = currentdate.getYear() + "-01-01";
    final String lastDayOfYear = currentdate.getYear() + "-12-31";

    final Periode periodeCurrentYear = new Periode(firstDayOfYear, null);
    final List<Periode> periodesCurrentYear = new ArrayList<>();
    periodesCurrentYear.add(new Periode(firstDayOfYear, null));

    contrat.setIdDeclarant("0000003664");
    contrat.setSocieteEmettrice("Test TEAM BLUE - RDO");
    contrat.setNumero("1213");
    contrat.setNumeroExterne("1213_EXT");
    contrat.setNumeroAdherent("ADH_1213");
    contrat.setNumeroAdherentComplet("ADH_1213COMPLET");
    contrat.setDateSouscription(firstDayOfYear);
    contrat.setApporteurAffaire("AF_BLUE_RDO");
    contrat.setPeriodesContratResponsableOuvert(periodesCurrentYear);
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("GEST_BLUE");
    contrat.setQualification("QUAL_BLUE");
    contrat.setOrdrePriorisation("1");
    final List<PeriodeSuspension> periodesSuspension = new ArrayList<>();
    final PeriodeSuspension p1 = new PeriodeSuspension();
    p1.setTypeSuspension(TypeSuspension.Provisoire.toString());
    p1.setPeriode(new Periode("2021-03-01", null));
    periodesSuspension.add(p1);
    contrat.setPeriodesSuspension(periodesSuspension);

    final List<Assure> assures = new ArrayList<>();
    // Adresse assure - Identique pour tous les assures du contrat
    final String codePostal = "59250";
    final String ligne1Adresse = "RUE DU TEST";
    final String ligne6Adresse = "59250 HALLUIN";
    final AdresseAssure adresseAssure = new AdresseAssure();
    adresseAssure.setCodePostal(codePostal);
    adresseAssure.setLigne1(ligne1Adresse);
    adresseAssure.setLigne6(ligne6Adresse);

    // Destinataires de paiements identique pour tous les assures du contrat
    final List<DestinatairePrestations> destinatairesPaiements = new ArrayList<>();
    final DestinatairePrestations destPaiement = new DestinatairePrestations();
    final Adresse adresse = new Adresse();
    adresse.setLigne1(ligne1Adresse);
    adresse.setLigne6(ligne6Adresse);
    adresse.setCodePostal(codePostal);
    destPaiement.setAdresse(adresse);
    destPaiement.setIdDestinatairePaiements("1213");
    destPaiement.setModePaiementPrestations(new ModePaiement("VIR", "Virement", "EUR"));
    final NomDestinataire nomDest = new NomDestinataire();
    nomDest.setNomFamille("ASSURE PRINCIPAL");
    nomDest.setPrenom("1");
    nomDest.setCivilite("MR");
    destPaiement.setNom(nomDest);
    destPaiement.setPeriode(new PeriodeDestinataire(firstDayOfYear, null));
    final RibAssure rib = new RibAssure();
    rib.setIban("FR9810096000504861538231U73");
    rib.setBic("AGRIFRPP831");
    destPaiement.setRib(rib);
    destinatairesPaiements.add(destPaiement);
    final List<DestinataireRelevePrestations> destinatairesRelevePrestations = new ArrayList<>();
    final DestinataireRelevePrestations destRelevePrest = new DestinataireRelevePrestations();
    destRelevePrest.setAdresse(adresseAssure);
    destRelevePrest.setIdDestinataireRelevePrestations("1213");
    destRelevePrest.setNom(nomDest);
    destRelevePrest.setPeriode(new PeriodeDestinataire(firstDayOfYear, null));
    final Dematerialisation demat = new Dematerialisation();
    demat.setIsDematerialise(false);
    destRelevePrest.setDematerialisation(demat);
    destinatairesRelevePrestations.add(destRelevePrest);

    final List<DroitAssure> droits = new ArrayList<>();

    final DroitAssure droit = new DroitAssure();
    droit.setCode("DRT");
    droit.setCodeAssureur("DRTASS");
    droit.setLibelle("Droit 2");
    droit.setOrdrePriorisation("1");
    droit.setType("DRT");
    droit.setPeriode(periodeCurrentYear);
    droit.setDateAncienneteGarantie(firstDayOfYear);
    final List<CarenceDroit> carences = new ArrayList<>();
    final CarenceDroit carence = new CarenceDroit();
    carence.setCode("C1");
    carence.setPeriode(new PeriodeCarence(firstDayOfYear, lastDayOfYear));
    final DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("CC1");
    droitRemplacement.setCodeAssureur("CCASS1");
    droitRemplacement.setLibelle("Carence1");
    carence.setDroitRemplacement(droitRemplacement);
    carences.add(carence);
    droit.setCarences(carences);

    droits.add(droit);

    final Assure assure = new Assure();

    final IdentiteContrat identite = new IdentiteContrat();
    final DataAssure data = new DataAssure();
    assure.setIsSouscripteur(true);
    final QualiteAssure qualite = new QualiteAssure("ASP", "Assuré principal");
    assure.setQualite(qualite);

    identite.setNir(new Nir("1791062498047", "45"));
    identite.setDateNaissance("19791006");
    identite.setRangNaissance("1");
    identite.setNumeroPersonne("1213");
    final List<NirRattachementRO> lstNirRo = new ArrayList<>();
    final RattachementRO rattRO = new RattachementRO("01", "624", "1253");
    final NirRattachementRO nirRattRo = new NirRattachementRO();
    nirRattRo.setNir(new Nir("1791062498047", "45"));
    nirRattRo.setRattachementRO(rattRO);
    nirRattRo.setPeriode(periodeCurrentYear);
    lstNirRo.add(nirRattRo);
    identite.setAffiliationsRO(lstNirRo);

    //
    // Data
    //
    final NomAssure nom = new NomAssure("RDO_SP_1", null, "MR", "ASSURE PRINCIPAL1");
    data.setNom(nom);
    final Contact contact = new Contact("test@gmail.coum", "0321542151", "0621548111");
    data.setContact(contact);

    assure.setIdentite(identite);
    data.setAdresse(adresseAssure);
    data.setDestinatairesPaiements(destinatairesPaiements);
    data.setDestinatairesRelevePrestations(destinatairesRelevePrestations);
    assure.setData(data);

    assure.setRangAdministratif("1");

    assure.setDateAdhesionMutuelle(firstDayOfYear);
    assure.setDateDebutAdhesionIndividuelle(firstDayOfYear);
    assure.setNumeroAdhesionIndividuelle("12316");
    assure.setPeriodes(periodesCurrentYear);
    assure.setPeriodesMedecinTraitantOuvert(periodesCurrentYear);

    final DigitRelation digit = new DigitRelation();
    final Dematerialisation dematV1 = new Dematerialisation();
    dematV1.setIsDematerialise(false);
    digit.setDematerialisation(dematV1);
    final List<Teletransmission> teletransmissions = new ArrayList<>();
    teletransmissions.add(new Teletransmission(periodeCurrentYear, true));
    digit.setTeletransmissions(teletransmissions);
    assure.setDigitRelation(digit);
    assure.setDroits(droits);
    assures.add(assure);
    contrat.setAssures(assures);
    contrat.setVersion(null);
    return contrat;
  }
}
