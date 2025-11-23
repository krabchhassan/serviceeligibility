package com.cegedim.next.serviceeligibility.core.business.carte.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeCarteDemat;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.dao.CarteDematDaoImpl;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class CarteDematServiceTest {

  @Autowired CarteDematDaoImpl carteDematDao;

  @Autowired DeclarantService declarantService;

  @Autowired CarteDematService carteDematService;

  @Test
  void getCarteDematV2Test() {
    DemandeCarteDemat demande = new DemandeCarteDemat("000000001", "01234567", "2022-01-01");
    CarteDemat carteDemat1 = getCarteDemat("000000001", "2022/12/31", "000000002", false);

    CarteDemat carteDemat2 = getCarteDemat("000000001", "2023/12/31", "000000003", true);

    Mockito.when(carteDematDao.findCartesDematByCriteria(Mockito.any()))
        .thenReturn(List.of(carteDemat1, carteDemat2));

    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(getDeclarant());

    List<CarteDematDto> carteDematDtoList = carteDematService.getCartesDemat(demande, true);
    Assertions.assertEquals(2, carteDematDtoList.size());
    Assertions.assertEquals(
        DateUtils.stringToXMLGregorianCalendar("2022/12/31", DateUtils.FORMATTERSLASHED),
        carteDematDtoList.get(0).getPeriodeFin());
    Assertions.assertEquals(
        DateUtils.stringToXMLGregorianCalendar("2023/12/31", DateUtils.FORMATTERSLASHED),
        carteDematDtoList.get(1).getPeriodeFin());
  }

  private static CarteDemat getCarteDemat(
      String amc, String periodeFin, String amcContrat, boolean isV2) {
    CarteDemat carteDemat1 = new CarteDemat();
    carteDemat1.setPeriodeFin(periodeFin);
    carteDemat1.setIsLastCarteDemat(isV2);
    carteDemat1.setAMC_contrat(amcContrat);
    carteDemat1.setIdDeclarant(amc);
    carteDemat1.setPeriodeDebut("2022/01/01");
    return carteDemat1;
  }

  @Test
  void getCarteDematNoV2Test() {
    DemandeCarteDemat demande = new DemandeCarteDemat("000000001", "01234567", "2022-01-01");
    CarteDemat carteDemat1 = getCarteDemat("000000001", "2022/12/31", "000000002", false);

    CarteDemat carteDemat2 = getCarteDemat("000000001", "2023/12/31", "000000003", true);

    Mockito.when(carteDematDao.findCartesDematByCriteria(Mockito.any()))
        .thenReturn(List.of(carteDemat1, carteDemat2));

    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(getDeclarant());

    List<CarteDematDto> carteDematDtoList = carteDematService.getCartesDemat(demande, false);
    Assertions.assertEquals(2, carteDematDtoList.size());
    Assertions.assertEquals(
        DateUtils.stringToXMLGregorianCalendar("2022/12/31", DateUtils.FORMATTERSLASHED),
        carteDematDtoList.get(0).getPeriodeFin());
    Assertions.assertEquals(
        DateUtils.stringToXMLGregorianCalendar("2023/12/31", DateUtils.FORMATTERSLASHED),
        carteDematDtoList.get(1).getPeriodeFin());
  }

  @Test
  void getCarteDematAONTest() {
    DemandeCarteDemat demande =
        new DemandeCarteDemat(Constants.NUMERO_AON, "01234567", "2022-01-01");

    CarteDemat carteDemat1 = getCarteDemat(Constants.NUMERO_AON, "2022/12/31", "000000002", false);

    CarteDemat carteDemat2 = getCarteDemat(Constants.NUMERO_AON, "2023/12/31", "000000003", true);

    Mockito.when(carteDematDao.findCartesDematByAdherent(Mockito.any()))
        .thenReturn(List.of(carteDemat1, carteDemat2));

    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(getDeclarantAon());

    List<CarteDematDto> carteDematDtoList = carteDematService.getCartesDemat(demande, false);
    Assertions.assertEquals(2, carteDematDtoList.size());
    Assertions.assertEquals(
        DateUtils.stringToXMLGregorianCalendar("2022/12/31", DateUtils.FORMATTERSLASHED),
        carteDematDtoList.get(0).getPeriodeFin());
    Assertions.assertEquals(
        DateUtils.stringToXMLGregorianCalendar("2023/12/31", DateUtils.FORMATTERSLASHED),
        carteDematDtoList.get(1).getPeriodeFin());
  }

  private Declarant getDeclarant() {
    Declarant declarant = new Declarant();
    declarant.set_id("000000001");
    declarant.setNom("AMC Test JUnit");
    declarant.setIdClientBO("Unidentified");

    return declarant;
  }

  private Declarant getDeclarantAon() {
    Declarant declarant = new Declarant();
    declarant.set_id("0000401026");
    declarant.setNom("AON Test JUnit");
    declarant.setIdClientBO("Unidentified");

    return declarant;
  }
}
