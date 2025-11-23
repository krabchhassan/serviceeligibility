package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.NomDestinataire;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import java.util.ArrayList;
import java.util.List;

public final class TestUtils {
  public static final String ID_DECLARANT = "AAA";
  public static final String NUMERO_ADHERENT = "ADH";
  public static final String NUMERO = "12345";
  public static final String DATE_RESILIATION = "2023-01-01";
  public static final String DEBUT = "2020-01-01";
  public static final String FIN = "2020-12-31";

  private TestUtils() {
    // utils
  }

  public static ServicePrestationV6 getServicePrestationV6() {
    ServicePrestationV6 servicePrestationV6 = new ServicePrestationV6();
    servicePrestationV6.setNumero(NUMERO);
    servicePrestationV6.setNumeroExterne(NUMERO);
    servicePrestationV6.setIdDeclarant(ID_DECLARANT);
    servicePrestationV6.setSocieteEmettrice("KLESIA CARCEPT");
    servicePrestationV6.setDateSouscription(DEBUT);
    servicePrestationV6.setDateResiliation(DATE_RESILIATION);
    servicePrestationV6.setNumeroAdherent(NUMERO_ADHERENT);
    servicePrestationV6.setNumeroAdherentComplet(NUMERO_ADHERENT);
    servicePrestationV6.setApporteurAffaire("Courtier & Co");
    List<Periode> periodesContratResponsable = new ArrayList<>();
    Periode periodeContratResponsable = new Periode();
    periodeContratResponsable.setDebut(DEBUT);
    periodeContratResponsable.setFin(FIN);
    periodesContratResponsable.add(periodeContratResponsable);
    servicePrestationV6.setPeriodesContratResponsableOuvert(periodesContratResponsable);
    servicePrestationV6.setCritereSecondaire("Cadres");
    servicePrestationV6.setCritereSecondaireDetaille("CAD");
    servicePrestationV6.setIsContratIndividuel(true);
    servicePrestationV6.setGestionnaire("IGestion");
    ContratCollectifV6 contratCollectif = new ContratCollectifV6();
    contratCollectif.setNumero(NUMERO);
    contratCollectif.setNumeroExterne(NUMERO);
    servicePrestationV6.setContratCollectif(contratCollectif);
    servicePrestationV6.setQualification("BASE");
    servicePrestationV6.setOrdrePriorisation("1");

    Assure assure = new Assure();
    DataAssure dataAssureV5 = new DataAssure();
    List<DestinatairePrestations> destinatairePrestationsList = new ArrayList<>();
    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    destinatairePrestations.setIdDestinatairePaiements("123456");
    destinatairePrestations.setIdBeyondDestinatairePaiements("123456-AAA");
    NomDestinataire nomDestinataire = new NomDestinataire();
    nomDestinataire.setCivilite("Mr");
    nomDestinataire.setNomFamille("NOM");
    nomDestinataire.setNomUsage("NOMU");
    nomDestinataire.setPrenom("PRE");
    nomDestinataire.setRaisonSociale("RAISON");
    destinatairePrestations.setNom(nomDestinataire);
    destinatairePrestationsList.add(destinatairePrestations);
    dataAssureV5.setDestinatairesPaiements(destinatairePrestationsList);
    assure.setData(dataAssureV5);
    servicePrestationV6.setAssure(assure);
    return servicePrestationV6;
  }
}
