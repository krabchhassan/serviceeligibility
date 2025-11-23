package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.beyond.schemas.CombiAjout;
import com.cegedim.beyond.schemas.CombiModif;
import com.cegedim.next.serviceeligibility.core.almerysProductRef.LotAlmerys;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ProductCombination;
import com.cegedim.next.serviceeligibility.core.services.event.EventServiceUtils;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
public class EventServiceUtilsTest {

  @Test
  void formatAlmerysProductCreationTest() {
    List<CombiAjout> result =
        EventServiceUtils.formatCombinationsForCreation(getProductCombinationList(false));
    Assertions.assertEquals(2, result.size());
    CombiAjout combi1 = result.get(0);
    Assertions.assertEquals("1", combi1.getNumero());
    Assertions.assertEquals("2025/01/01", combi1.getDebut());
    Assertions.assertEquals("-", combi1.getFin());
    Assertions.assertNull(combi1.getFinPrecedente());
    Assertions.assertNotNull(combi1.getGtAjoutees());
    Assertions.assertEquals(1, combi1.getGtAjoutees().size());
    Assertions.assertEquals("BALOO - GT1", combi1.getGtAjoutees().get(0));
    Assertions.assertNotNull(combi1.getLotAjoutes());
    Assertions.assertEquals(2, combi1.getLotAjoutes().size());
    Assertions.assertEquals("LOT_100", combi1.getLotAjoutes().get(0));
    Assertions.assertEquals("LOT_200", combi1.getLotAjoutes().get(1));

    CombiAjout combi2 = result.get(1);
    Assertions.assertEquals("2", combi2.getNumero());
    Assertions.assertEquals("2024/01/01", combi2.getDebut());
    Assertions.assertEquals("-", combi2.getFin());
    Assertions.assertNull(combi2.getFinPrecedente());
    Assertions.assertNotNull(combi2.getGtAjoutees());
    Assertions.assertEquals(1, combi2.getGtAjoutees().size());
    Assertions.assertEquals("BALOO - GT2", combi2.getGtAjoutees().get(0));
    Assertions.assertNotNull(combi2.getLotAjoutes());
    Assertions.assertEquals(2, combi2.getLotAjoutes().size());
    Assertions.assertEquals("LOT_200", combi2.getLotAjoutes().get(0));
    Assertions.assertEquals("LOT_300", combi2.getLotAjoutes().get(1));
  }

  @Test
  void formatAlmerysProductModificationTest() {
    AlmerysProduct almerysProduct = getAlmerysProduct(false);
    AlmerysProduct almerysProduct2 = getAlmerysProduct(true);
    List<CombiModif> result =
        EventServiceUtils.formatCombinationsForModification(almerysProduct, almerysProduct2);
    Assertions.assertEquals(1, result.size());
    CombiModif combi1 = result.get(0);
    Assertions.assertEquals("1", combi1.getNumero());
    Assertions.assertEquals("2025/01/01", combi1.getDebut());
    Assertions.assertEquals("2025/12/31", combi1.getFin());
    Assertions.assertEquals("-", combi1.getFinPrecedente());
    Assertions.assertNotNull(combi1.getGtAjoutees());
    Assertions.assertEquals(1, combi1.getGtAjoutees().size());
    Assertions.assertEquals("BALOO - GT2", combi1.getGtAjoutees().get(0));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi1.getGtSupprimees()));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi1.getLotAjoutes()));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi1.getLotSupprimes()));
  }

  @Test
  void formatAlmerysProductModificationNoChangeTest() {
    AlmerysProduct almerysProduct = getAlmerysProduct(false);
    AlmerysProduct almerysProduct2 = getAlmerysProduct(false);
    List<CombiModif> result =
        EventServiceUtils.formatCombinationsForModification(almerysProduct, almerysProduct2);
    Assertions.assertTrue(result.isEmpty());
  }

  @NotNull
  public static AlmerysProduct getAlmerysProduct(boolean isModification) {
    AlmerysProduct almerysProduct = new AlmerysProduct();
    almerysProduct.setCode("PDT_ALM1");
    almerysProduct.setDescription("test");
    List<ProductCombination> productCombinations = getProductCombinationList(isModification);
    almerysProduct.setProductCombinations(productCombinations);
    return almerysProduct;
  }

  @NotNull
  private static List<ProductCombination> getProductCombinationList(boolean isModification) {
    List<ProductCombination> productCombinations = new ArrayList<>();
    ProductCombination productCombination1 = new ProductCombination();
    productCombination1.setDateDebut("2025/01/01");
    if (isModification) {
      productCombination1.setDateFin("2025/12/31");
    }
    List<GarantieTechnique> garantieTechniques = new ArrayList<>();
    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT1");
    garantieTechnique1.setCodeAssureur("BALOO");
    garantieTechniques.add(garantieTechnique1);
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT2");
    garantieTechnique2.setCodeAssureur("BALOO");
    if (isModification) {
      garantieTechniques.add(garantieTechnique2);
    }
    productCombination1.setGarantieTechniqueList(garantieTechniques);

    List<LotAlmerys> lotAlmerysList = new ArrayList<>();
    LotAlmerys lotAlmerys1 = new LotAlmerys();
    lotAlmerys1.setCode("LOT_100");
    lotAlmerysList.add(lotAlmerys1);
    LotAlmerys lotAlmerys2 = new LotAlmerys();
    lotAlmerys2.setCode("LOT_200");
    lotAlmerysList.add(lotAlmerys2);
    productCombination1.setLotAlmerysList(lotAlmerysList);
    productCombinations.add(productCombination1);

    ProductCombination productCombination2 = new ProductCombination();
    productCombination2.setDateDebut("2024/01/01");
    List<GarantieTechnique> garantieTechniques2 = new ArrayList<>();
    garantieTechniques2.add(garantieTechnique2);
    productCombination2.setGarantieTechniqueList(garantieTechniques2);

    List<LotAlmerys> lotAlmerysList2 = new ArrayList<>();
    LotAlmerys lotAlmerys3 = new LotAlmerys();
    lotAlmerys3.setCode("LOT_300");
    lotAlmerysList2.add(lotAlmerys2);
    lotAlmerysList2.add(lotAlmerys3);
    productCombination2.setLotAlmerysList(lotAlmerysList2);

    productCombinations.add(productCombination2);
    return productCombinations;
  }

  @Test
  void noPrevList() {
    AlmerysProduct before = getAlmerysProduct(false);
    AlmerysProduct after = getAlmerysProduct(true);

    before
        .getProductCombinations()
        .forEach(
            combi -> {
              combi.setGarantieTechniqueList(null);
              combi.setLotAlmerysList(null);
            });

    List<CombiModif> result = EventServiceUtils.formatCombinationsForModification(before, after);
    Assertions.assertEquals(2, result.size());
    CombiModif combi1 = result.get(0);
    Assertions.assertEquals("1", combi1.getNumero());
    Assertions.assertEquals("2025/01/01", combi1.getDebut());
    Assertions.assertEquals("2025/12/31", combi1.getFin());
    Assertions.assertEquals("-", combi1.getFinPrecedente());
    Assertions.assertNotNull(combi1.getGtAjoutees());
    Assertions.assertEquals(2, combi1.getGtAjoutees().size());
    Assertions.assertEquals("BALOO - GT1", combi1.getGtAjoutees().get(0));
    Assertions.assertEquals("BALOO - GT2", combi1.getGtAjoutees().get(1));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi1.getGtSupprimees()));
    Assertions.assertNotNull(combi1.getLotAjoutes());
    Assertions.assertEquals(2, combi1.getLotAjoutes().size());
    Assertions.assertEquals("LOT_100", combi1.getLotAjoutes().get(0));
    Assertions.assertEquals("LOT_200", combi1.getLotAjoutes().get(1));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi1.getLotSupprimes()));

    CombiModif combi2 = result.get(1);
    Assertions.assertEquals("2", combi2.getNumero());
    Assertions.assertEquals("2024/01/01", combi2.getDebut());
    Assertions.assertEquals("-", combi2.getFin());
    Assertions.assertNull(combi2.getFinPrecedente());
    Assertions.assertNotNull(combi2.getGtAjoutees());
    Assertions.assertEquals(1, combi2.getGtAjoutees().size());
    Assertions.assertEquals("BALOO - GT2", combi2.getGtAjoutees().get(0));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi2.getGtSupprimees()));
    Assertions.assertNotNull(combi2.getLotAjoutes());
    Assertions.assertEquals(2, combi2.getLotAjoutes().size());
    Assertions.assertEquals("LOT_200", combi2.getLotAjoutes().get(0));
    Assertions.assertEquals("LOT_300", combi2.getLotAjoutes().get(1));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi2.getLotSupprimes()));
  }

  @Test
  void noNewList() {
    AlmerysProduct before = getAlmerysProduct(false);
    AlmerysProduct after = getAlmerysProduct(true);

    after
        .getProductCombinations()
        .forEach(
            combi -> {
              combi.setGarantieTechniqueList(null);
              combi.setLotAlmerysList(null);
            });

    List<CombiModif> result = EventServiceUtils.formatCombinationsForModification(before, after);
    Assertions.assertEquals(2, result.size());
    CombiModif combi1 = result.get(0);
    Assertions.assertEquals("1", combi1.getNumero());
    Assertions.assertEquals("2025/01/01", combi1.getDebut());
    Assertions.assertEquals("2025/12/31", combi1.getFin());
    Assertions.assertEquals("-", combi1.getFinPrecedente());
    Assertions.assertNotNull(combi1.getGtSupprimees());
    Assertions.assertEquals(1, combi1.getGtSupprimees().size());
    Assertions.assertEquals("BALOO - GT1", combi1.getGtSupprimees().get(0));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi1.getGtAjoutees()));
    Assertions.assertNotNull(combi1.getLotSupprimes());
    Assertions.assertEquals(2, combi1.getLotSupprimes().size());
    Assertions.assertEquals("LOT_100", combi1.getLotSupprimes().get(0));
    Assertions.assertEquals("LOT_200", combi1.getLotSupprimes().get(1));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi1.getLotAjoutes()));

    CombiModif combi2 = result.get(1);
    Assertions.assertEquals("2", combi2.getNumero());
    Assertions.assertEquals("2024/01/01", combi2.getDebut());
    Assertions.assertEquals("-", combi2.getFin());
    Assertions.assertNull(combi2.getFinPrecedente());
    Assertions.assertNotNull(combi2.getGtSupprimees());
    Assertions.assertEquals(1, combi2.getGtSupprimees().size());
    Assertions.assertEquals("BALOO - GT2", combi2.getGtSupprimees().get(0));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi2.getGtAjoutees()));
    Assertions.assertNotNull(combi2.getLotSupprimes());
    Assertions.assertEquals(2, combi2.getLotSupprimes().size());
    Assertions.assertEquals("LOT_200", combi2.getLotSupprimes().get(0));
    Assertions.assertEquals("LOT_300", combi2.getLotSupprimes().get(1));
    Assertions.assertTrue(CollectionUtils.isEmpty(combi2.getLotAjoutes()));
  }
}
