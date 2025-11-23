package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperBeneficiaireImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.List;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
public class MapperBeneficiaireTest {

  @Autowired private MapperBeneficiaireImpl mapperBeneficiaire;

  private BeneficiaireV2 beneficiaire;

  private BeneficiaireDto beneficiaireDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    beneficiaire = new BeneficiaireV2();

    Affiliation affiliation = new Affiliation();
    affiliation.setCivilite("Mme");
    affiliation.setNom("DELMOTTE");
    affiliation.setPrenom("Priscilla");
    affiliation.setNomMarital("DELMOTTE");
    affiliation.setNomPatronymique("DELMOTTE");
    affiliation.setCaisseOD1("01");
    affiliation.setCentreOD1("011");
    affiliation.setRegimeOD1("1111");
    affiliation.setCaisseOD2("02");
    affiliation.setCentreOD2("012");
    affiliation.setRegimeOD2("1222");
    affiliation.setHasMedecinTraitant(true);
    affiliation.setIsBeneficiaireACS(true);
    affiliation.setIsTeleTransmission(true);
    affiliation.setPeriodeDebut("2022/01/01");
    affiliation.setPeriodeFin("2022/12/31");
    affiliation.setQualite("QA");
    affiliation.setRegimeParticulier("AM");
    affiliation.setTypeAssure("TA");
    beneficiaire.setAffiliation(affiliation);
    beneficiaire.setNirBeneficiaire("1701062498046");
    beneficiaire.setCleNirBeneficiaire("02");
    beneficiaire.setNirOd1("1701062498046");
    beneficiaire.setCleNirOd1("02");
    beneficiaire.setNirOd2("1701062498046");
    beneficiaire.setCleNirOd2("02");
    beneficiaire.setInsc("is");
    beneficiaire.setDateNaissance("19791006");
    beneficiaire.setRangNaissance("1");
    beneficiaire.setNumeroPersonne("288939000");
    beneficiaire.setRefExternePersonne("7209738ADF");

    beneficiaireDto = new BeneficiaireDto();

    AffiliationDto affiliationDto = new AffiliationDto();
    affiliationDto.setCivilite("Mme");
    affiliationDto.setNom("DELMOTTE");
    affiliationDto.setPrenom("Priscilla");
    affiliationDto.setNomMarital("DELMOTTE");
    affiliationDto.setNomPatronymique("DELMOTTE");
    affiliationDto.setCaisseOD1("01");
    affiliationDto.setCentreOD1("011");
    affiliationDto.setRegimeOD1("1111");
    affiliationDto.setCaisseOD2("02");
    affiliationDto.setCentreOD2("012");
    affiliationDto.setRegimeOD2("1222");
    affiliationDto.setMedecinTraitant(true);
    affiliationDto.setIsBeneficiaireACS(true);
    affiliationDto.setHasTeleTransmission(true);
    affiliationDto.setPeriodeDebut(DateUtils.parseDate("2022/01/01", DateUtils.FORMATTERSLASHED));
    affiliationDto.setPeriodeFin(DateUtils.parseDate("2022/12/31", DateUtils.FORMATTERSLASHED));
    affiliationDto.setQualite("QA");
    affiliationDto.setRegimeParticulier("AM");
    affiliationDto.setTypeAssure("TA");
    AdresseDto adresseDto = new AdresseDto();
    beneficiaireDto.setAdresses(List.of(adresseDto));
    beneficiaireDto.setAffiliation(affiliationDto);
    beneficiaireDto.setNirBeneficiaire("1701062498046");
    beneficiaireDto.setCleNirBeneficiaire("02");
    beneficiaireDto.setNirOd1("1701062498046");
    beneficiaireDto.setCleNirOd1("02");
    beneficiaireDto.setNirOd2("1701062498046");
    beneficiaireDto.setCleNirOd2("02");
    beneficiaireDto.setInsc("is");
    beneficiaireDto.setDateNaissance("19791006");
    beneficiaireDto.setRangNaissance("1");
    beneficiaireDto.setNumeroPersonne("288939000");
    beneficiaireDto.setRefExternePersonne("7209738ADF");
  }

  @Test
  void should_create_dto_from_entity() {
    final BeneficiaireDto beneficiaireDto =
        mapperBeneficiaire.entityToDto(beneficiaire, null, false, false, null);

    Assertions.assertNotNull(beneficiaireDto);
    checkAffiliationDto(beneficiaireDto, beneficiaire);

    Assertions.assertEquals(
        beneficiaireDto.getNirBeneficiaire(), beneficiaire.getNirBeneficiaire());
    Assertions.assertEquals(
        beneficiaireDto.getCleNirBeneficiaire(), beneficiaire.getCleNirBeneficiaire());
    Assertions.assertEquals(beneficiaireDto.getNirOd1(), beneficiaire.getNirOd1());
    Assertions.assertEquals(beneficiaireDto.getCleNirOd1(), beneficiaire.getCleNirOd1());
    Assertions.assertEquals(beneficiaireDto.getNirOd2(), beneficiaire.getNirOd2());
    Assertions.assertEquals(beneficiaireDto.getCleNirOd2(), beneficiaire.getCleNirOd2());
    Assertions.assertEquals(beneficiaireDto.getInsc(), beneficiaire.getInsc());
    Assertions.assertEquals(beneficiaireDto.getDateNaissance(), beneficiaire.getDateNaissance());
    Assertions.assertEquals(beneficiaireDto.getRangNaissance(), beneficiaire.getRangNaissance());
    Assertions.assertEquals(beneficiaireDto.getNumeroPersonne(), beneficiaire.getNumeroPersonne());
    Assertions.assertEquals(
        beneficiaireDto.getRefExternePersonne(), beneficiaire.getRefExternePersonne());
  }

  private void checkAffiliationDto(BeneficiaireDto beneficiaireDto, BeneficiaireV2 beneficiaire) {
    AffiliationDto affiliationDto = beneficiaireDto.getAffiliation();
    Affiliation affiliation = beneficiaire.getAffiliation();
    Assertions.assertEquals(affiliationDto.getCivilite(), affiliation.getCivilite());
    Assertions.assertEquals(affiliationDto.getNom(), affiliation.getNom());
    Assertions.assertEquals(affiliationDto.getNomMarital(), affiliation.getNomMarital());
    Assertions.assertEquals(affiliationDto.getNomPatronymique(), affiliation.getNomPatronymique());
    Assertions.assertEquals(affiliationDto.getPrenom(), affiliation.getPrenom());
    Assertions.assertEquals(affiliationDto.getCaisseOD1(), affiliation.getCaisseOD1());
    Assertions.assertEquals(affiliationDto.getRegimeOD1(), affiliation.getRegimeOD1());
    Assertions.assertEquals(affiliationDto.getCentreOD1(), affiliation.getCentreOD1());
    Assertions.assertEquals(affiliationDto.getCaisseOD2(), affiliation.getCaisseOD2());
    Assertions.assertEquals(affiliationDto.getRegimeOD2(), affiliation.getRegimeOD2());
    Assertions.assertEquals(affiliationDto.getCentreOD2(), affiliation.getCentreOD2());
    Assertions.assertEquals(
        affiliationDto.getIsBeneficiaireACS(), affiliation.getIsBeneficiaireACS());
    Assertions.assertEquals(affiliationDto.getQualite(), affiliation.getQualite());
    Assertions.assertEquals(
        affiliationDto.getMedecinTraitant(), affiliation.getHasMedecinTraitant());
    Assertions.assertEquals(
        affiliationDto.getHasTeleTransmission(), affiliation.getIsTeleTransmission());
    Assertions.assertEquals(
        affiliationDto.getPeriodeDebut(),
        DateUtils.parseDate(affiliation.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        affiliationDto.getPeriodeFin(),
        DateUtils.parseDate(affiliation.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        affiliationDto.getRegimeParticulier(), affiliation.getRegimeParticulier());
    Assertions.assertEquals(affiliationDto.getTypeAssure(), affiliation.getTypeAssure());
  }

  @Test
  void should_create_entity_from_dto() {
    final BeneficiaireV2 beneficiaire = mapperBeneficiaire.dtoToEntity(beneficiaireDto);

    Assertions.assertNotNull(beneficiaire);
    checkAffiliation(beneficiaire);

    Assertions.assertEquals(
        beneficiaireDto.getNirBeneficiaire(), beneficiaire.getNirBeneficiaire());
    Assertions.assertEquals(
        beneficiaireDto.getCleNirBeneficiaire(), beneficiaire.getCleNirBeneficiaire());
    Assertions.assertEquals(beneficiaireDto.getNirOd1(), beneficiaire.getNirOd1());
    Assertions.assertEquals(beneficiaireDto.getCleNirOd1(), beneficiaire.getCleNirOd1());
    Assertions.assertEquals(beneficiaireDto.getNirOd2(), beneficiaire.getNirOd2());
    Assertions.assertEquals(beneficiaireDto.getCleNirOd2(), beneficiaire.getCleNirOd2());
    Assertions.assertEquals(beneficiaireDto.getInsc(), beneficiaire.getInsc());
    Assertions.assertEquals(beneficiaireDto.getDateNaissance(), beneficiaire.getDateNaissance());
    Assertions.assertEquals(beneficiaireDto.getRangNaissance(), beneficiaire.getRangNaissance());
    Assertions.assertEquals(beneficiaireDto.getNumeroPersonne(), beneficiaire.getNumeroPersonne());
    Assertions.assertEquals(
        beneficiaireDto.getRefExternePersonne(), beneficiaire.getRefExternePersonne());
  }

  private void checkAffiliation(BeneficiaireV2 beneficiaire) {
    checkAffiliationDto(beneficiaireDto, beneficiaire);
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
