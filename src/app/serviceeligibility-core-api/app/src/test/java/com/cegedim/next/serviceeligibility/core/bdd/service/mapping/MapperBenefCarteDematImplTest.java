package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BenefCarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BeneficiaireCouvertureDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat.MapperBenefCarteDematImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
class MapperBenefCarteDematImplTest {
  @Autowired private MapperBenefCarteDematImpl mapperCarte;

  private BenefCarteDematDto benefCarteDematDto;
  private BenefCarteDemat benefCarteDemat;

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    benefCarteDemat = new BenefCarteDemat();
    Beneficiaire beneficiaire = new Beneficiaire();
    beneficiaire.setRefExternePersonne("8343484392E");
    Affiliation affiliation = new Affiliation();
    affiliation.setNom("Nomb");
    affiliation.setNomPatronymique("Nomp");
    affiliation.setNomMarital("Nomm");
    affiliation.setPrenom("Prenom");
    affiliation.setQualite("Q");
    affiliation.setTypeAssure("P");
    affiliation.setRegimeOD1("OD1");
    affiliation.setCaisseOD1("COD1");
    affiliation.setCentreOD1("CeOD1");
    affiliation.setRegimeOD2("OD2");
    affiliation.setCaisseOD2("COD2");
    affiliation.setCentreOD2("CeOD2");
    affiliation.setHasMedecinTraitant(true);
    affiliation.setRegimeParticulier("CMU");
    affiliation.setIsBeneficiaireACS(true);
    affiliation.setIsTeleTransmission(true);
    affiliation.setPeriodeDebut("2023/01/01");
    beneficiaire.setAffiliation(affiliation);

    beneficiaire.setNirOd1("1791062498048");
    beneficiaire.setCleNirOd1("12");
    beneficiaire.setNirOd2("1791062498048");
    beneficiaire.setCleNirOd2("14");
    beneficiaire.setNirBeneficiaire("1791062498048");
    beneficiaire.setCleNirBeneficiaire("12");
    beneficiaire.setDateNaissance("1979-05-12");
    beneficiaire.setRangNaissance("1");
    beneficiaire.setNumeroPersonne("8343484392");
    benefCarteDemat.setBeneficiaire(beneficiaire);

    LienContrat lienContrat = new LienContrat();
    lienContrat.setLienFamilial("F");
    lienContrat.setRangAdministratif("1");
    lienContrat.setModePaiementPrestations("VIR");
    benefCarteDemat.setLienContrat(lienContrat);
    DomaineDroit domaineDroit = new DomaineDroit();
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2023/01/01");
    domaineDroit.setPeriodeDroit(periodeDroit);
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    prioriteDroit.setCode("01");
    domaineDroit.setPrioriteDroit(prioriteDroit);
    domaineDroit.setReferenceCouverture("refCouv");
    benefCarteDemat.setDomainesCouverture(List.of(domaineDroit));

    benefCarteDematDto = new BenefCarteDematDto();
    benefCarteDematDto.setNomBeneficiaire("Nomb");
    benefCarteDematDto.setNomPatronymique("Nomp");
    benefCarteDematDto.setNomMarital("Nomm");
    benefCarteDematDto.setPrenom("Prenom");
    benefCarteDematDto.setQualite("Q");
    benefCarteDematDto.setTypeAssure("P");
    benefCarteDematDto.setRegimeOD1("OD1");
    benefCarteDematDto.setCaisseOD1("COD1");
    benefCarteDematDto.setCentreOD1("CeOD1");
    benefCarteDematDto.setRegimeOD2("OD2");
    benefCarteDematDto.setCaisseOD2("COD2");
    benefCarteDematDto.setCentreOD2("CeOD2");
    benefCarteDematDto.setHasMedecinTraitant(true);
    benefCarteDematDto.setRegimeParticulier("CMU");
    benefCarteDematDto.setIsBeneficiaireACS(true);
    benefCarteDematDto.setIsTeleTransmission(true);
    benefCarteDematDto.setDebutAffiliation(
        DateUtils.stringToXMLGregorianCalendar("2023/01/01", DateUtils.FORMATTERSLASHED));
    benefCarteDematDto.setLienFamilial("F");
    benefCarteDematDto.setRangAdministratif("1");
    benefCarteDematDto.setModePaiementPrestations("VIR");
    benefCarteDematDto.setNirOd1("1791062498048");
    benefCarteDematDto.setCleNirOd1("12");
    benefCarteDematDto.setNirOd2("1791062498048");
    benefCarteDematDto.setCleNirOd2("14");
    benefCarteDematDto.setNirBeneficiaire("1791062498048");
    benefCarteDematDto.setCleNirBeneficiaire("12");
    benefCarteDematDto.setDateNaissance("1979-05-12");
    benefCarteDematDto.setRangNaissance("1");
    benefCarteDematDto.setNumeroPersonne("8343484392");
    benefCarteDematDto.setRefExternePersonne("8343484392E");
    benefCarteDematDto.setCouverture(List.of(new BeneficiaireCouvertureDto()));
  }

  @Test
  void should_create_dto_from_entity() {
    final BenefCarteDematDto dto =
        mapperCarte.entityToDto(benefCarteDemat, null, false, false, null);

    Assertions.assertNotNull(dto);
    Beneficiaire beneficiaire = benefCarteDemat.getBeneficiaire();
    Assertions.assertNotNull(dto);
    Assertions.assertEquals(beneficiaire.getNirOd1(), benefCarteDematDto.getNirOd1());
    Assertions.assertEquals(beneficiaire.getCleNirOd1(), benefCarteDematDto.getCleNirOd1());
    Assertions.assertEquals(beneficiaire.getNirOd2(), benefCarteDematDto.getNirOd2());
    Assertions.assertEquals(beneficiaire.getCleNirOd2(), benefCarteDematDto.getCleNirOd2());
    Assertions.assertEquals(
        beneficiaire.getNirBeneficiaire(), benefCarteDematDto.getNirBeneficiaire());
    Assertions.assertEquals(
        beneficiaire.getCleNirBeneficiaire(), benefCarteDematDto.getCleNirBeneficiaire());
    Assertions.assertEquals(beneficiaire.getDateNaissance(), benefCarteDematDto.getDateNaissance());
    Assertions.assertEquals(beneficiaire.getRangNaissance(), benefCarteDematDto.getRangNaissance());
    Assertions.assertEquals(
        beneficiaire.getNumeroPersonne(), benefCarteDematDto.getNumeroPersonne());

    checkAffiliation(beneficiaire);
    checkLienContrat();
  }

  private void checkLienContrat() {
    Assertions.assertEquals(
        benefCarteDemat.getLienContrat().getLienFamilial(), benefCarteDematDto.getLienFamilial());
    Assertions.assertEquals(
        benefCarteDemat.getLienContrat().getRangAdministratif(),
        benefCarteDematDto.getRangAdministratif());
    Assertions.assertEquals(
        benefCarteDemat.getLienContrat().getModePaiementPrestations(),
        benefCarteDematDto.getModePaiementPrestations());
  }

  private void checkAffiliation(Beneficiaire beneficiaire) {
    Affiliation affiliation = beneficiaire.getAffiliation();
    Assertions.assertEquals(affiliation.getNom(), benefCarteDematDto.getNomBeneficiaire());
    Assertions.assertEquals(
        affiliation.getNomPatronymique(), benefCarteDematDto.getNomPatronymique());
    Assertions.assertEquals(affiliation.getNomMarital(), benefCarteDematDto.getNomMarital());
    Assertions.assertEquals(affiliation.getPrenom(), benefCarteDematDto.getPrenom());
    Assertions.assertEquals(affiliation.getQualite(), benefCarteDematDto.getQualite());
    Assertions.assertEquals(affiliation.getTypeAssure(), benefCarteDematDto.getTypeAssure());
    Assertions.assertEquals(affiliation.getRegimeOD1(), benefCarteDematDto.getRegimeOD1());
    Assertions.assertEquals(affiliation.getCaisseOD1(), benefCarteDematDto.getCaisseOD1());
    Assertions.assertEquals(affiliation.getCentreOD1(), benefCarteDematDto.getCentreOD1());
    Assertions.assertEquals(affiliation.getRegimeOD2(), benefCarteDematDto.getRegimeOD2());
    Assertions.assertEquals(affiliation.getCaisseOD2(), benefCarteDematDto.getCaisseOD2());
    Assertions.assertEquals(affiliation.getCentreOD2(), benefCarteDematDto.getCentreOD2());
    Assertions.assertTrue(benefCarteDematDto.getHasMedecinTraitant());
    Assertions.assertEquals(
        affiliation.getRegimeParticulier(), benefCarteDematDto.getRegimeParticulier());
    Assertions.assertTrue(benefCarteDematDto.getIsBeneficiaireACS());
    Assertions.assertTrue(benefCarteDematDto.getIsTeleTransmission());
    Assertions.assertEquals(
        DateUtils.stringToXMLGregorianCalendar(
            affiliation.getPeriodeDebut(), DateUtils.FORMATTERSLASHED),
        benefCarteDematDto.getDebutAffiliation());
  }
}
