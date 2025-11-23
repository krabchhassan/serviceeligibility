package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BenefCarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BeneficiaireCouvertureDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat.MapperCarteDematImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Formule;
import com.cegedim.next.serviceeligibility.core.model.domain.FormuleMetier;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
public class MapperCarteDematImplTest {

  private static final Logger logger = LoggerFactory.getLogger(MapperCarteDematImplTest.class);
  @Autowired private MapperCarteDematImpl mapper;

  private CarteDemat card;
  private CarteDematDto carteDematDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    logger.info("prepare data for unit test");
    prepareData();
  }

  private void prepareData() {
    BenefCarteDemat benefCarteDemat = new BenefCarteDemat();
    Beneficiaire beneficiaire = new Beneficiaire();
    Affiliation affiliation = new Affiliation();
    LienContrat lienContrat = new LienContrat();
    Contrat contrat = new Contrat();
    List<DomaineDroit> domainDroits = new ArrayList<>();

    card = new CarteDemat();
    card.setPeriodeDebut("2018/01/01");
    card.setPeriodeFin("2019/01/01");
    card.setIdDeclarant("declarantId");

    contrat.setDestinataire("destinataire");

    card.setContrat(contrat);
    List<BenefCarteDemat> beneficiaires = new ArrayList<>();
    beneficiaires.add(benefCarteDemat);
    card.setBeneficiaires(beneficiaires);

    affiliation.setPeriodeDebut("2011/02/02");
    affiliation.setPrenom("bob");

    lienContrat.setLienFamilial("lien familial");

    List<Conventionnement> conventionnements = new ArrayList<>();
    TypeConventionnement typeConventionnement = new TypeConventionnement();
    typeConventionnement.setCode("convetion code");
    typeConventionnement.setLibelle("convention label");

    Conventionnement conv = new Conventionnement();
    conv.setPriorite(1);
    conv.setTypeConventionnement(typeConventionnement);

    DomaineDroit droit = new DomaineDroit();
    droit.setCategorie("category");
    droit.setConventionnements(conventionnements);

    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setDateEvenement("2011/01/01");
    periodeDroit.setLibelleEvenement("event label");
    droit.setPeriodeDroit(periodeDroit);

    PrioriteDroit prioriteDroit = new PrioriteDroit();
    prioriteDroit.setCode("priority code");
    prioriteDroit.setLibelle("priority label");
    droit.setPrioriteDroit(prioriteDroit);

    List<Prestation> prestations = new ArrayList<>();
    Prestation prestation = new Prestation();
    prestation.setCode("prestation code");
    Formule formule = new Formule();
    formule.setLibelle("formule libelle");

    prestation.setFormule(formule);

    FormuleMetier formuleMetier = new FormuleMetier();
    formuleMetier.setLibelle("formule metier label");
    prestation.setFormuleMetier(formuleMetier);

    prestations.add(prestation);

    droit.setPrestations(prestations);
    domainDroits.add(droit);

    beneficiaire.setAffiliation(affiliation);
    beneficiaire.setCleNirBeneficiaire("cleNirBeneficiaire");
    beneficiaire.setCleNirOd1("cleNirOd1");

    benefCarteDemat.setBeneficiaire(beneficiaire);
    benefCarteDemat.setLienContrat(lienContrat);
    benefCarteDemat.setDomainesCouverture(domainDroits);

    carteDematDto = new CarteDematDto();
    carteDematDto.setAdresse(new AdresseDto());
    carteDematDto.setBenefCarteDematDtos(new ArrayList<>());
    carteDematDto.setNomAmc("TEST BLUE");
    carteDematDto.setContrat(new ContratDto());
    carteDematDto.setDomaineConventionDtos(new ArrayList<>());
    carteDematDto.setLibelleAmc("TEST BLUE");
    carteDematDto.setNumeroAmc("0032165199");
    carteDematDto.setPeriodeDebut(
        DateUtils.stringToXMLGregorianCalendar("2023/01/01", DateUtils.FORMATTERSLASHED));
    carteDematDto.setPeriodeFin(
        DateUtils.stringToXMLGregorianCalendar("2023/12/31", DateUtils.FORMATTERSLASHED));
  }

  @Test
  void should_create_dto_from_entity() {
    final CarteDematDto cardDto = mapper.entityToDto(card, null, false, false, null);

    Assertions.assertNotNull(cardDto);
    BenefCarteDematDto transDto = cardDto.getBenefCarteDematDtos().get(0);
    Assertions.assertEquals("cleNirOd1", transDto.getCleNirOd1());
    Assertions.assertEquals("cleNirBeneficiaire", transDto.getCleNirBeneficiaire());
    Assertions.assertEquals(2, transDto.getDebutAffiliation().getDay());
    Assertions.assertEquals(2, transDto.getDebutAffiliation().getMonth());
    Assertions.assertEquals("bob", transDto.getPrenom());
    Assertions.assertEquals("lien familial", transDto.getLienFamilial());
    List<BeneficiaireCouvertureDto> couvertureList = transDto.getCouverture();
    Assertions.assertNotNull(couvertureList);
    Assertions.assertEquals(1, couvertureList.size());
    BeneficiaireCouvertureDto benefDto = couvertureList.get(0);
    Assertions.assertNotNull(benefDto);
    PrestationDto prestation = benefDto.getPrestationDtos().get(0);
    Assertions.assertNotNull(prestation);
    Assertions.assertEquals("prestation code", prestation.getCode());
    Assertions.assertEquals("formule libelle", prestation.getFormule().getLibelle());
    Assertions.assertEquals("formule metier label", prestation.getFormuleMetier().getLibelle());
  }

  @Test
  void should_create_entity_from_dto() {
    final CarteDemat carteDemat = mapper.dtoToEntity(carteDematDto);

    Assertions.assertNotNull(carteDemat);
    Assertions.assertNotNull(carteDemat.getAdresse());
    Assertions.assertNotNull(carteDemat.getDomainesConventions());
    Assertions.assertNotNull(carteDemat.getBeneficiaires());
    Assertions.assertNotNull(carteDemat.getContrat());
  }
}
