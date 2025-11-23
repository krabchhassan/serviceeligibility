package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.RibAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.PeriodeDestinataire;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.pojo.DataForEventRibModification;
import com.cegedim.next.serviceeligibility.core.utils.DestinatairePrestationsEventUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContratAivServiceV6Test {

  @Autowired ContratAivService contratAivService;

  private static final String IDDESTINATAIRE = "IDDESTINATAIRE";
  private static final String IDDESTINATAIRE2 = "IDDESTINATAIRE2";

  private static final String IBAN = "FR3330002005500000157841Z25";
  private static final String IBAN2 = "FR33300020055000001D7841Z25";

  private static ContratAIV6 createContract(String mode, String iban, String debut, String fin) {
    ContratAIV6 newContract = new ContratAIV6();
    Assure assure = new Assure();
    IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNumeroPersonne("12345");
    assure.setIdentite(identiteContrat);
    DataAssure dataAssureV5 = new DataAssure();
    List<DestinatairePrestations> destinatairePrestationsList = new ArrayList<>();
    destinatairePrestationsList.add(
        createDestinatairePaiement(debut, fin, IDDESTINATAIRE, mode, iban));
    dataAssureV5.setDestinatairesPaiements(destinatairePrestationsList);
    assure.setData(dataAssureV5);
    List<Assure> assures = List.of(assure);
    newContract.setAssures(assures);
    return newContract;
  }

  private static DestinatairePrestations createDestinatairePaiement(
      String debut, String fin, String idDestinataire, String mode, String iban) {
    DestinatairePrestations destinatairePrestations = new DestinatairePrestations();
    destinatairePrestations.setIdDestinatairePaiements(idDestinataire);
    destinatairePrestations.setIdBeyondDestinatairePaiements("BEYOND-" + idDestinataire);
    PeriodeDestinataire periode = new PeriodeDestinataire();
    periode.setDebut(debut);
    periode.setFin(fin);
    destinatairePrestations.setPeriode(periode);
    RibAssure rib = new RibAssure();
    rib.setIban(iban);
    destinatairePrestations.setRib(rib);
    ModePaiement modePaiement = new ModePaiement();
    modePaiement.setCode(mode);
    destinatairePrestations.setModePaiementPrestations(modePaiement);
    return destinatairePrestations;
  }

  @Test
  void iBanModificationOnKnownRecipientModification() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2020-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2020-01-01", null, IDDESTINATAIRE, "VIR", IBAN2);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    ArrayList<DestinatairePrestations> newDestinatairePrestationV4List = new ArrayList<>();
    newDestinatairePrestationV4List.add(newDestinatairePrestations);
    DestinatairePrestationsEventUtil.manageRecipientEventChange(
        newDestinatairePrestationV4List,
        oldDestinatairePrestationV4List,
        dataForEventRibModificationList,
        newContract.getIdDeclarant(),
        numeroPersonne);
    Assertions.assertFalse(dataForEventRibModificationList.isEmpty());
  }

  @Test
  void iBanModificationOnKnownRecipientNoModification() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2020-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2020-01-01", null, IDDESTINATAIRE, "VIR", IBAN);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    ArrayList<DestinatairePrestations> newDestinatairePrestationV4List = new ArrayList<>();
    newDestinatairePrestationV4List.add(newDestinatairePrestations);
    DestinatairePrestationsEventUtil.manageRecipientEventChange(
        newDestinatairePrestationV4List,
        oldDestinatairePrestationV4List,
        dataForEventRibModificationList,
        newContract.getIdDeclarant(),
        numeroPersonne);
    Assertions.assertTrue(dataForEventRibModificationList.isEmpty());
  }

  @Test
  void modePaymentModificationOnKnownRecipientModification() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2020-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2020-01-01", null, IDDESTINATAIRE, "VAR", IBAN);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    ArrayList<DestinatairePrestations> newDestinatairePrestationV4List = new ArrayList<>();
    newDestinatairePrestationV4List.add(newDestinatairePrestations);
    DestinatairePrestationsEventUtil.manageRecipientEventChange(
        newDestinatairePrestationV4List,
        oldDestinatairePrestationV4List,
        dataForEventRibModificationList,
        newContract.getIdDeclarant(),
        numeroPersonne);
    Assertions.assertFalse(dataForEventRibModificationList.isEmpty());
  }

  @Test
  void newRecipientWithDifferentIbanNoModification() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2020-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2020-01-01", null, IDDESTINATAIRE2, "VAR", IBAN);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    contratAivService.newRecipientWithDifferentIban(
        newContract,
        dataForEventRibModificationList,
        numeroPersonne,
        oldDestinatairePrestationV4List,
        newDestinatairePrestations);
    Assertions.assertTrue(dataForEventRibModificationList.isEmpty());
  }

  @Test
  void newRecipientWithDifferentIbanModification() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2020-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2020-01-01", null, IDDESTINATAIRE2, "VIR", IBAN2);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    contratAivService.newRecipientWithDifferentIban(
        newContract,
        dataForEventRibModificationList,
        numeroPersonne,
        oldDestinatairePrestationV4List,
        newDestinatairePrestations);
    Assertions.assertFalse(dataForEventRibModificationList.isEmpty());
  }

  void periodmodification(String debut, String fin, String destFin) {
    ContratAIV6 newContract = createContract("VIR", IBAN, debut, fin);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2020-01-01", destFin, IDDESTINATAIRE, "VAR", IBAN);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    ArrayList<DestinatairePrestations> newDestinatairePrestationV4List = new ArrayList<>();
    newDestinatairePrestationV4List.add(newDestinatairePrestations);
    DestinatairePrestationsEventUtil.manageRecipientEventChange(
        newDestinatairePrestationV4List,
        oldDestinatairePrestationV4List,
        dataForEventRibModificationList,
        newContract.getIdDeclarant(),
        numeroPersonne);
    Assertions.assertFalse(dataForEventRibModificationList.isEmpty());
  }

  @Test
  void periodModificationOnKnownRecipientModification() {
    periodmodification("2020-01-01", "2023-01-01", null);
  }

  @Test
  void periodModificationOnKnownRecipientModification2() {
    periodmodification("2020-01-01", null, "2023-01-01");
  }

  @Test
  void periodModificationOnKnownRecipientModification3() {
    periodmodification("2021-01-01", null, null);
  }

  @Test
  void periodModificationOnKnownRecipientModification4() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2022-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    DestinatairePrestations newDestinatairePrestations_2 =
        createDestinatairePaiement("2020-01-01", "2021-12-31", IDDESTINATAIRE2, "VIR", IBAN2);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2020-01-01", "2021-12-31", IDDESTINATAIRE, "VIR", IBAN);
    DestinatairePrestations oldDestinatairePrestationV4_2 =
        createDestinatairePaiement("2022-01-01", null, IDDESTINATAIRE2, "VIR", IBAN2);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4_2);
    ArrayList<DestinatairePrestations> newDestinatairePrestationV4List = new ArrayList<>();
    newDestinatairePrestationV4List.add(newDestinatairePrestations);
    newDestinatairePrestationV4List.add(newDestinatairePrestations_2);
    DestinatairePrestationsEventUtil.manageRecipientEventChange(
        newDestinatairePrestationV4List,
        oldDestinatairePrestationV4List,
        dataForEventRibModificationList,
        newContract.getIdDeclarant(),
        numeroPersonne);
    Assertions.assertFalse(dataForEventRibModificationList.isEmpty());
    Assertions.assertEquals(
        IDDESTINATAIRE,
        dataForEventRibModificationList.get(0).getNewDestinataire().getIdDestinatairePaiements());
    Assertions.assertEquals(
        IDDESTINATAIRE2,
        dataForEventRibModificationList.get(0).getOldDestinataire().getIdDestinatairePaiements());
    Assertions.assertEquals(
        IBAN, dataForEventRibModificationList.get(0).getNewDestinataire().getRib().getIban());
    Assertions.assertEquals(
        IBAN2, dataForEventRibModificationList.get(0).getOldDestinataire().getRib().getIban());
  }

  @Test
  void periodModificationOnKnownRecipientModification5() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2023-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    DestinatairePrestations newDestinatairePrestations_2 =
        createDestinatairePaiement("2022-01-01", "2023-01-01", IDDESTINATAIRE2, "VIR", IBAN2);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2022-01-01", "2023-01-01", IDDESTINATAIRE, "VIR", IBAN);
    DestinatairePrestations oldDestinatairePrestationV4_2 =
        createDestinatairePaiement("2023-01-01", null, IDDESTINATAIRE2, "VIR", IBAN2);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4_2);
    ArrayList<DestinatairePrestations> newDestinatairePrestationV4List = new ArrayList<>();
    newDestinatairePrestationV4List.add(newDestinatairePrestations);
    newDestinatairePrestationV4List.add(newDestinatairePrestations_2);
    DestinatairePrestationsEventUtil.manageRecipientEventChange(
        newDestinatairePrestationV4List,
        oldDestinatairePrestationV4List,
        dataForEventRibModificationList,
        newContract.getIdDeclarant(),
        numeroPersonne);
    Assertions.assertFalse(dataForEventRibModificationList.isEmpty());
    Assertions.assertEquals(
        IDDESTINATAIRE,
        dataForEventRibModificationList.get(0).getNewDestinataire().getIdDestinatairePaiements());
    Assertions.assertEquals(
        IDDESTINATAIRE2,
        dataForEventRibModificationList.get(0).getOldDestinataire().getIdDestinatairePaiements());
    Assertions.assertEquals(
        IBAN, dataForEventRibModificationList.get(0).getNewDestinataire().getRib().getIban());
    Assertions.assertEquals(
        IBAN2, dataForEventRibModificationList.get(0).getOldDestinataire().getRib().getIban());
  }

  @Test
  void iBanModificationOnOldKnownRecipient() {
    ContratAIV6 newContract = createContract("VIR", IBAN, "2023-01-01", null);
    List<DataForEventRibModification> dataForEventRibModificationList = new ArrayList<>();
    DestinatairePrestations newDestinatairePrestations =
        newContract.getAssures().get(0).getData().getDestinatairesPaiements().get(0);
    DestinatairePrestations newDestinatairePrestations_2 =
        createDestinatairePaiement("2022-01-01", "2023-01-01", IDDESTINATAIRE2, "VIR", IBAN2);
    String numeroPersonne = newContract.getAssures().get(0).getIdentite().getNumeroPersonne();
    DestinatairePrestations oldDestinatairePrestationV4 =
        createDestinatairePaiement("2023-01-01", null, IDDESTINATAIRE, "VIR", IBAN);
    DestinatairePrestations oldDestinatairePrestationV4_2 =
        createDestinatairePaiement("2022-01-01", "2023-01-01", IDDESTINATAIRE2, "VIR", IBAN);
    ArrayList<DestinatairePrestations> oldDestinatairePrestationV4List = new ArrayList<>();
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4);
    oldDestinatairePrestationV4List.add(oldDestinatairePrestationV4_2);
    ArrayList<DestinatairePrestations> newDestinatairePrestationV4List = new ArrayList<>();
    newDestinatairePrestationV4List.add(newDestinatairePrestations);
    newDestinatairePrestationV4List.add(newDestinatairePrestations_2);
    DestinatairePrestationsEventUtil.manageRecipientEventChange(
        newDestinatairePrestationV4List,
        oldDestinatairePrestationV4List,
        dataForEventRibModificationList,
        newContract.getIdDeclarant(),
        numeroPersonne);
    Assertions.assertFalse(dataForEventRibModificationList.isEmpty());
    Assertions.assertEquals(
        IDDESTINATAIRE2,
        dataForEventRibModificationList.get(0).getNewDestinataire().getIdDestinatairePaiements());
    Assertions.assertEquals(
        IDDESTINATAIRE2,
        dataForEventRibModificationList.get(0).getOldDestinataire().getIdDestinatairePaiements());
    Assertions.assertEquals(
        IBAN2, dataForEventRibModificationList.get(0).getNewDestinataire().getRib().getIban());
    Assertions.assertEquals(
        IBAN, dataForEventRibModificationList.get(0).getOldDestinataire().getRib().getIban());
  }
}
