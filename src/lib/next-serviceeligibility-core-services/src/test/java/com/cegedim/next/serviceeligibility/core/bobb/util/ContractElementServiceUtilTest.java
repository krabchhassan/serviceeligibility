package com.cegedim.next.serviceeligibility.core.bobb.util;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class ContractElementServiceUtilTest {

  @Test
  void testshouldNotGetProductElementLights() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElement.setTo(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement, LocalDateTime.of(2020, 1, 1, 0, 0, 0), null);
    Assertions.assertEquals(0, lightList.size());
  }

  @Test
  void testshouldGetProductElementLights6501_avec_periode_a_lenvers() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(1900, 1, 1, 0, 0, 0));
    productElement.setTo(null);
    productElementList.add(productElement);
    ProductElement productElement2 = new ProductElement();
    productElement2.setCodeProduct("PDT_BASE1");
    productElement2.setCodeOffer("OFFRE");
    productElement2.setCodeAmc("OC");
    productElement2.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement2.setFrom(LocalDateTime.of(2010, 1, 1, 0, 0, 0));
    productElement2.setTo(LocalDateTime.of(2009, 12, 31, 0, 0, 0));
    productElementList.add(productElement2);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement, LocalDateTime.of(2001, 1, 1, 0, 0, 0), null);
    Assertions.assertEquals(1, lightList.size());
  }

  @Test
  void testshouldGetProductElementLights6501_avec_periode_a_lendroit() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(1900, 1, 1, 0, 0, 0));
    productElement.setTo(null);
    productElementList.add(productElement);
    ProductElement productElement2 = new ProductElement();
    productElement2.setCodeProduct("PDT_BASE1");
    productElement2.setCodeOffer("OFFRE");
    productElement2.setCodeAmc("OC");
    productElement2.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement2.setFrom(LocalDateTime.of(2010, 1, 1, 0, 0, 0));
    productElement2.setTo(LocalDateTime.of(2010, 2, 1, 0, 0, 0));
    productElementList.add(productElement2);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement, LocalDateTime.of(2001, 1, 1, 0, 0, 0), null);
    Assertions.assertEquals(2, lightList.size());
  }

  @Test
  void testshouldNotGetProductElementLights2() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElement.setTo(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement, LocalDateTime.of(2019, 1, 1, 0, 0, 0), null);
    Assertions.assertEquals(0, lightList.size());
  }

  @Test
  void testshouldNotGetProductElementLights3() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElement.setTo(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement,
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            LocalDateTime.of(2023, 1, 1, 0, 0, 0));
    Assertions.assertEquals(0, lightList.size());
  }

  @Test
  void testshouldNotGetProductElementLightsIgnored() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(true);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElement.setTo(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement,
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            LocalDateTime.of(2023, 1, 1, 0, 0, 0));
    Assertions.assertEquals(0, lightList.size());
  }

  @Test
  void testGetProductElementLights() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement, LocalDateTime.of(2020, 1, 1, 0, 0, 0), null);
    Assertions.assertEquals(1, lightList.size());
  }

  @Test
  void testGetProductElementLights2() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PDT_BASE1");
    productElement.setCodeOffer("OFFRE");
    productElement.setCodeAmc("OC");
    productElement.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    List<ProductElementLight> lightList =
        ContractElementServiceUtil.getProductElementLights(
            contractElement,
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0, 0));
    Assertions.assertEquals(1, lightList.size());
  }

  @Test
  void testGetProductElementLightsCas1Contigues() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);

    ProductElement productElement1 = new ProductElement();
    productElement1.setCodeProduct("PDT_BASE1");
    productElement1.setCodeOffer("OFFRE");
    productElement1.setCodeAmc("OC");
    productElement1.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement1.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElement1.setTo(LocalDateTime.of(2020, 7, 2, 0, 0, 0));

    ProductElement productElement2 = new ProductElement();
    productElement2.setCodeProduct("PDT_BASE2");
    productElement2.setCodeOffer("OFFRE");
    productElement2.setCodeAmc("OC");
    productElement2.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement2.setFrom(LocalDateTime.of(2020, 7, 3, 0, 0, 0));

    contractElement.setProductElements(List.of(productElement1, productElement2));

    // Debut 01/01 sans fin doit etre couvert par les deux ProductElement
    Assertions.assertEquals(
        2,
        ContractElementServiceUtil.getProductElementLights(
                contractElement, LocalDateTime.of(2020, 1, 1, 0, 0, 0), null)
            .size());

    // Debut 03/07 sans fin doit etre couvert par le deuxieme ProductElement
    Assertions.assertEquals(
        1,
        ContractElementServiceUtil.getProductElementLights(
                contractElement, LocalDateTime.of(2020, 7, 3, 0, 0, 0), null)
            .size());

    // Debut 02/07 sans fin doit etre couvert par les deux ProductElement
    Assertions.assertEquals(
        2,
        ContractElementServiceUtil.getProductElementLights(
                contractElement, LocalDateTime.of(2020, 7, 2, 0, 0, 0), null)
            .size());

    // Debut 01/08 sans fin doit etre couvert par le deuxieme ProductElement
    Assertions.assertEquals(
        1,
        ContractElementServiceUtil.getProductElementLights(
                contractElement, LocalDateTime.of(2020, 8, 1, 0, 0, 0), null)
            .size());
  }

  @Test
  void testGetProductElementLightsCas2NonContigues() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    contractElement.setIgnored(false);

    ProductElement productElement1 = new ProductElement();
    productElement1.setCodeProduct("PDT_BASE1");
    productElement1.setCodeOffer("OFFRE");
    productElement1.setCodeAmc("OC");
    productElement1.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement1.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElement1.setTo(LocalDateTime.of(2020, 7, 2, 0, 0, 0));

    ProductElement productElement2 = new ProductElement();
    productElement2.setCodeProduct("PDT_BASE2");
    productElement2.setCodeOffer("OFFRE");
    productElement2.setCodeAmc("OC");
    productElement2.setCodeBenefitNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    productElement2.setFrom(LocalDateTime.of(2020, 8, 3, 0, 0, 0));

    contractElement.setProductElements(List.of(productElement1, productElement2));

    // Debut 01/01 sans fin ne doit pas etre couvert car ProductElement non
    // contigues sur la periode de droit
    Assertions.assertEquals(
        0,
        ContractElementServiceUtil.getProductElementLights(
                contractElement, LocalDateTime.of(2020, 1, 1, 0, 0, 0), null)
            .size());

    // Droit dans periode 1er ProductElement
    Assertions.assertEquals(
        1,
        ContractElementServiceUtil.getProductElementLights(
                contractElement,
                LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 5, 1, 0, 0, 0))
            .size());

    // Droit dans periode 2nd ProductElement
    Assertions.assertEquals(
        1,
        ContractElementServiceUtil.getProductElementLights(
                contractElement, LocalDateTime.of(2020, 8, 2, 0, 0, 0), null)
            .size());
  }
}
