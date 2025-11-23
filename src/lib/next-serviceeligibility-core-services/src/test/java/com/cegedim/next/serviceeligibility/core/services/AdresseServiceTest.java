package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeAdresse;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.services.common.batch.AdresseService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class AdresseServiceTest {

  @Autowired AdresseService adresseService;

  private static final String DECLARATION_CONSO_PATH =
      "src/test/resources/620-declarationConsolidees/";

  @Test
  void casAssurePrincipalWithAdresse() throws IOException {
    List<DeclarationConsolide> declarationConsolideList = new ArrayList<>();
    DeclarationConsolide declarationConsolide =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest.json", DeclarationConsolide.class);
    Adresse adresse2 = declarationConsolide.getBeneficiaire().getAdresses().get(0);
    declarationConsolide.getBeneficiaire().setAdresses(List.of(new Adresse(adresse2)));
    declarationConsolide.getBeneficiaire().getAdresses().get(0).setCodePostal("11300");
    declarationConsolideList.add(declarationConsolide);
    declarationConsolideList.add(
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest.json", DeclarationConsolide.class));
    declarationConsolideList.add(
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas4-3.json", DeclarationConsolide.class));
    Adresse expectedAdresse = getExpectedAdresse();
    Adresse adresse = adresseService.getAdresseForCarte(declarationConsolideList);
    Assertions.assertEquals(expectedAdresse, adresse);
  }

  @Test
  void casAssurePrincipalWithoutAdresseAD() throws IOException {
    List<DeclarationConsolide> declarationConsolideList = new ArrayList<>();
    DeclarationConsolide declarationConsolide =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas4-1.json", DeclarationConsolide.class);
    declarationConsolide.getBeneficiaire().getAdresses().get(0).getTypeAdresse().setType("EN");
    declarationConsolideList.add(
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest1.json", DeclarationConsolide.class));
    declarationConsolideList.add(declarationConsolide);
    Adresse adresse = adresseService.getAdresseForCarte(declarationConsolideList);
    Assertions.assertNull(adresse);
  }

  @Test
  void casRangAdministratifWithAdresse() throws IOException {
    List<DeclarationConsolide> declarationConsolideList = new ArrayList<>();
    declarationConsolideList.add(
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest3.json", DeclarationConsolide.class));
    DeclarationConsolide declarationConsolide =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest3.json", DeclarationConsolide.class);
    declarationConsolide.getContrat().setRangAdministratif("2");
    DeclarationConsolide declarationConsolide2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest2.json", DeclarationConsolide.class);
    TypeAdresse typeAdresse = new TypeAdresse();
    typeAdresse.setType("AD");
    declarationConsolide2.getBeneficiaire().getAdresses().get(0).setTypeAdresse(typeAdresse);
    declarationConsolideList.add(declarationConsolide);
    declarationConsolideList.add(declarationConsolide2);
    Adresse adresse = adresseService.getAdresseForCarte(declarationConsolideList);
    Assertions.assertEquals(getExpectedAdresse(), adresse);
  }

  @Test
  void casRangAdministratifWithoutAdresseAD() throws IOException {
    List<DeclarationConsolide> declarationConsolideList = new ArrayList<>();
    declarationConsolideList.add(
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest3.json", DeclarationConsolide.class));
    declarationConsolideList.add(
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConsoAdresseTest2.json", DeclarationConsolide.class));
    Assertions.assertNull(adresseService.getAdresseForCarte(declarationConsolideList));
  }

  private static Adresse getExpectedAdresse() {
    Adresse expectedAdresse = new Adresse();
    expectedAdresse.setLigne1("25 RUE DES LAGRADES");
    expectedAdresse.setLigne2("RESIDENCE DES PLATEAUX");
    expectedAdresse.setLigne3("59250");
    expectedAdresse.setLigne4("HALLUIN");
    expectedAdresse.setLigne5("F");
    expectedAdresse.setLigne6("FRANCE");
    expectedAdresse.setLigne7("CEDEX41");
    expectedAdresse.setCodePostal("5920");
    expectedAdresse.setEmail("5920");
    TypeAdresse type = new TypeAdresse();
    type.setType("AD");
    expectedAdresse.setTypeAdresse(type);
    return expectedAdresse;
  }
}
