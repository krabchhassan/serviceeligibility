package com.cegedim.next.serviceeligibility.core.services.cartedemat;

import static com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDematExceptionCode.CARTE_DEMAT_NON_TROUVEE;
import static com.cegedim.next.serviceeligibility.core.utils.CardUtils.getRandomCardList;
import static com.cegedim.next.serviceeligibility.core.utils.CardUtils.getValidCardRequest;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CardRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponse;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4.CardResponseBeneficiaryV4;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponsev4.CardResponseContratV4;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.ws.CardServiceV4;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarteDematException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class CardServiceV4Test {
  @Autowired private CardServiceV4 cardServiceV4;

  @Autowired private MongoTemplate mongoTemplate;

  @Test
  void validateRequestTestRequestNull() {
    CarteDematException thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.validateRequest(null));
    Assertions.assertTrue(
        thrown.getCommentaire().contains("Le corps de la requête ne doit pas être vide."));
  }

  @Test
  void validateRequestTestBlankNumeroAmc() {
    CardRequest request = new CardRequest();

    CarteDematException thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.validateRequest(request));
    Assertions.assertTrue(
        thrown.getCommentaire().contains("Le champ 'numeroAmc' est obligatoire."));
  }

  @Test
  void validateRequestTestBlankDateReference() {
    CardRequest request = new CardRequest();
    request.setNumeroAmc("0000401166");

    CarteDematException thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.validateRequest(request));
    Assertions.assertTrue(
        thrown.getCommentaire().contains("Le champ 'dateReference' est obligatoire."));
  }

  @Test
  void validateRequestTestWrongDateReference() {
    CardRequest request = new CardRequest();
    request.setNumeroAmc("0000401166");

    // request.dateReference is badly formatted
    request.setDateReference("2024/01/24"); // Expected format is yyyy-MM-dd
    CarteDematException thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.validateRequest(request));
    Assertions.assertTrue(
        thrown
            .getCommentaire()
            .contains("Le format du champ 'dateReference' doit être 'yyyy-MM-dd'."));

    // request.dateReference is too low
    request.setDateReference("1500-01-01");
    thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.validateRequest(request));
    Assertions.assertTrue(
        thrown
            .getCommentaire()
            .contains("'dateReference' doit être supérieure à la date du jour."));
  }

  @Test
  void validateRequestTestBlankNumeroContrat() {
    CardRequest request = new CardRequest();
    request.setNumeroAmc("0000401166");
    request.setDateReference("3024-01-24");

    CarteDematException thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.validateRequest(request));
    Assertions.assertTrue(
        thrown.getCommentaire().contains("Le champ 'numeroContrat' est obligatoire."));
  }

  @Test
  void validateRequestTest() {
    CardRequest request = getValidCardRequest();

    Assertions.assertDoesNotThrow(() -> cardServiceV4.validateRequest(request));
  }

  @Test
  void buildResponseCardsTestNoCardFound() {
    // Returns an empty list when looking for cards
    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(CarteDemat.class)))
        .thenReturn(new ArrayList<>());

    CardRequest request = getValidCardRequest();

    CarteDematException thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.buildResponse(request));
    Assertions.assertEquals(CARTE_DEMAT_NON_TROUVEE, thrown.getExceptionCode());
  }

  @Test
  void buildResponseCardsSameNumerosAdherentTest() {
    // Returns a random list with fixed length when looking for cards
    int listSize = 4;
    List<CarteDemat> databaseCardList = getRandomCardList(listSize);

    // Set the same numeroAdherent on each contract
    for (CarteDemat databaseCard : databaseCardList) {
      databaseCard.getContrat().setNumeroAdherent("sameNumeroAdherent");
    }

    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(CarteDemat.class)))
        .thenReturn(databaseCardList);

    CardRequest request = getValidCardRequest();
    List<CardResponse> response = cardServiceV4.buildResponse(request);

    Assertions.assertTrue(CollectionUtils.isNotEmpty(response));
    Assertions.assertEquals(listSize, response.size());
  }

  @Test
  void buildResponseCardsDifferentNumerosAdherentTest() {
    // Returns a random list with fixed length when looking for cards
    int listSize = 4;
    List<CarteDemat> databaseCardList = getRandomCardList(listSize);

    // Set different numeroAdherent on each contract
    for (int i = 0; i < databaseCardList.size(); i++) {
      CarteDemat databaseCard = databaseCardList.get(i);
      databaseCard.getContrat().setNumeroAdherent("differentNumeroAdherent" + i);
    }

    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(CarteDemat.class)))
        .thenReturn(databaseCardList);

    CardRequest request = getValidCardRequest();

    CarteDematException thrown =
        Assertions.assertThrows(
            CarteDematException.class, () -> cardServiceV4.buildResponse(request));
    Assertions.assertTrue(
        thrown
            .getCommentaire()
            .contains("Précisez le numéro d'adhérent recherché dans la requête."));
  }

  @Test
  void buildResponseCardsCheckNewFields() {
    int listSize = 1;
    List<CarteDemat> databaseCardList = getRandomCardList(listSize);
    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(CarteDemat.class)))
        .thenReturn(databaseCardList);

    CardRequest request = getValidCardRequest();
    List<CardResponse> response = cardServiceV4.buildResponse(request);

    Assertions.assertTrue(CollectionUtils.isNotEmpty(response));
    Assertions.assertEquals(listSize, response.size());
    Assertions.assertEquals(
        databaseCardList.get(0).getBeneficiaires().size(),
        response.get(0).getBeneficiaires().size());

    CardResponseBeneficiaryV4 benefReceived =
        (CardResponseBeneficiaryV4) response.get(0).getBeneficiaires().get(0);
    Beneficiaire benefExpected =
        databaseCardList.get(0).getBeneficiaires().get(0).getBeneficiaire();
    Assertions.assertEquals(
        benefExpected.getAffiliation().getRegimeOD1(), benefReceived.getRegimeOD1());
    Assertions.assertEquals(
        benefExpected.getAffiliation().getCaisseOD1(), benefReceived.getCaisseOD1());
    Assertions.assertEquals(
        benefExpected.getAffiliation().getCentreOD1(), benefReceived.getCentreOD1());
    Assertions.assertEquals(
        benefExpected.getAffiliation().getRegimeOD2(), benefReceived.getRegimeOD2());
    Assertions.assertEquals(
        benefExpected.getAffiliation().getCaisseOD2(), benefReceived.getCaisseOD2());
    Assertions.assertEquals(
        benefExpected.getAffiliation().getCentreOD2(), benefReceived.getCentreOD2());
    Assertions.assertEquals(
        benefExpected.getAffiliation().getHasMedecinTraitant(),
        benefReceived.getHasMedecinTraitant());

    CardResponseContratV4 contratReceived = (CardResponseContratV4) response.get(0).getContrat();
    Contrat contratExpected = databaseCardList.get(0).getContrat();
    Assertions.assertEquals(
        contratExpected.getNumOperateur(), contratReceived.getNumeroOperateur());
    Assertions.assertEquals(
        contratExpected.getTypeConvention(), contratReceived.getTypeConvention());
    Assertions.assertEquals(
        contratExpected.getDateSouscription(), contratReceived.getDateSouscription());
    Assertions.assertEquals(contratExpected.getQualification(), contratReceived.getQualification());
    Assertions.assertEquals(
        contratExpected.getSituationParticuliere(), contratReceived.getSituationParticuliere());
  }
}
