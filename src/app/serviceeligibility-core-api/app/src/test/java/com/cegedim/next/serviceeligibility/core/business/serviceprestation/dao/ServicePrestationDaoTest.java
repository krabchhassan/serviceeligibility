package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.NomDestinataire;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServicePrestationDaoTest {

  private ServicePrestationV6 createServicePrestation(
      PeriodeDestinataire period1,
      PeriodeDestinataire period2,
      String identifiant1,
      String identifiant2) {
    ServicePrestationV6 servicePrestationVservicePrestationV6 = new ServicePrestationV6();
    servicePrestationVservicePrestationV6.setNumero("12345");
    servicePrestationVservicePrestationV6.setIdDeclarant("AAA");
    servicePrestationVservicePrestationV6.setDateSouscription("2020-01-01");
    servicePrestationVservicePrestationV6.setDateResiliation("2023-01-01");
    servicePrestationVservicePrestationV6.setNumeroAdherent("ADH");
    Assure assure = new Assure();
    DataAssure dataAssureV4 = new DataAssure();
    List<DestinatairePrestations> destinatairePrestationsList = new ArrayList<>();
    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    destinatairePrestations.setIdDestinatairePaiements(identifiant1);
    destinatairePrestations.setIdBeyondDestinatairePaiements("123456-AAA");
    NomDestinataire nomDestinataire = new NomDestinataire();
    nomDestinataire.setCivilite("Mr");
    nomDestinataire.setNomFamille("NOM");
    nomDestinataire.setNomUsage("NOMU");
    nomDestinataire.setPrenom("PRE");
    nomDestinataire.setRaisonSociale("RAISON");
    destinatairePrestations.setNom(nomDestinataire);

    destinatairePrestations.setPeriode(period1);

    DestinatairePrestations destinatairePrestationsV42 = new DestinatairePrestations();
    destinatairePrestationsV42.setIdDestinatairePaiements(identifiant2);
    destinatairePrestationsV42.setIdBeyondDestinatairePaiements("123456-AAA");
    NomDestinataire nomDestinataireV32 = new NomDestinataire();
    nomDestinataireV32.setCivilite("Mr");
    nomDestinataireV32.setNomFamille("NOM");
    nomDestinataireV32.setNomUsage("NOMU");
    nomDestinataireV32.setPrenom("PRE");
    nomDestinataireV32.setRaisonSociale("RAISON");
    destinatairePrestations.setNom(nomDestinataireV32);

    destinatairePrestationsV42.setPeriode(period2);

    destinatairePrestationsList.add(destinatairePrestations);
    destinatairePrestationsList.add(destinatairePrestationsV42);
    dataAssureV4.setDestinatairesPaiements(destinatairePrestationsList);
    assure.setData(dataAssureV4);
    servicePrestationVservicePrestationV6.setAssure(assure);
    return servicePrestationVservicePrestationV6;
  }

  private void shouldReturnXDestPaiements(String dateToTest, String identifiant2, int expected) {
    PeriodeDestinataire period1 = new PeriodeDestinataire();
    period1.setDebut("2020-01-01");
    period1.setFin("2021-01-01");
    PeriodeDestinataire period2 = new PeriodeDestinataire();
    period2.setDebut("2020-06-01");
    period2.setFin("2021-01-01");
    List<ServicePrestationV6> servicePrestationV6s =
        getServicePrestationV6s(
            dateToTest, createServicePrestation(period1, period2, "123456", identifiant2));
    Assertions.assertEquals(
        expected,
        servicePrestationV6s.get(0).getAssure().getData().getDestinatairesPaiements().size());
  }

  @Test
  void shouldReturnZero() {
    shouldReturnXDestPaiements("2022-01-01", "123456", 0);
  }

  @Test
  void shouldReturnOne() {
    shouldReturnXDestPaiements("2020-01-01", "123456", 1);
  }

  @Test
  void shouldReturnTwo() {
    shouldReturnXDestPaiements("2020-07-01", "123455", 2);
  }

  @Test
  void shouldReturnOneBis() {
    PeriodeDestinataire period1 = new PeriodeDestinataire();
    period1.setDebut("2020-01-01");
    PeriodeDestinataire period2 = new PeriodeDestinataire();
    period2.setDebut("2020-06-01");
    List<ServicePrestationV6> servicePrestationV6s =
        getServicePrestationV6s(
            "2020-07-01", createServicePrestation(period1, period2, "123456", "123456"));
    Assertions.assertEquals(
        1, servicePrestationV6s.get(0).getAssure().getData().getDestinatairesPaiements().size());
    Assertions.assertEquals(
        "2020-06-01",
        servicePrestationV6s
            .get(0)
            .getAssure()
            .getData()
            .getDestinatairesPaiements()
            .get(0)
            .getPeriode()
            .getDebut());
  }

  private List<ServicePrestationV6> getServicePrestationV6s(
      String dateToTest, ServicePrestationV6 servicePrestationV6) {
    List<ServicePrestationV6> servicePrestationV6s = new ArrayList<>();
    servicePrestationV6s.add(servicePrestationV6);
    LocalDate localDateToTest = LocalDate.parse(dateToTest, DateUtils.FORMATTER);
    new ServicePrestationDaoImpl(null, null, null)
        .updatePaymentsBeneficiary(localDateToTest, servicePrestationV6s);
    return servicePrestationV6s;
  }
}
