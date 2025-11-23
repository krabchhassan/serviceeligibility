package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperCartePapier;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CartePapier;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class MapperCartePapierTest {

  @Autowired MapperCartePapier mapperCartePapier;

  private static final String CARTE_DEMAT_PATH = "src/test/resources/620-CarteDemat/";

  @Test
  void testMapperCartePapier() throws IOException {
    CarteDemat carteDemat =
        UtilsForTesting.createTFromJson(
            CARTE_DEMAT_PATH + "carteDematCas4-1.json", CarteDemat.class);
    Declarant declarant = new Declarant();
    declarant.setNom("nomDeclarant");
    declarant.setLibelle("libelleDeclarant");
    CartePapier cartePapier =
        mapperCartePapier.mapCartePapier(carteDemat, declarant, Constants.ANNUEL, null);

    Assertions.assertEquals("nomDeclarant", cartePapier.getNomAMC());
    Assertions.assertEquals("libelleDeclarant", cartePapier.getLibelleAMC());
    Assertions.assertEquals("0097810998", cartePapier.getNumeroAMC());
    Assertions.assertEquals("2024/01/01", cartePapier.getPeriodeDebut());
    Assertions.assertEquals("2024/12/31", cartePapier.getPeriodeFin());
    Assertions.assertEquals("IGestion", cartePapier.getSocieteEmettrice());
    Assertions.assertEquals("code", cartePapier.getCodeRenvoi());
    Assertions.assertEquals("libelle", cartePapier.getLibelleRenvoi());
    Assertions.assertEquals("IS", cartePapier.getCodeConvention());
    Assertions.assertEquals("img_carte_TP", cartePapier.getFondCarte());
    Assertions.assertEquals("annexe1A", cartePapier.getAnnexe1Carte());
    Assertions.assertEquals("annexe2A", cartePapier.getAnnexe2Carte());
    Assertions.assertEquals("123456789", cartePapier.getNumAMCEchange());
    Assertions.assertEquals("IS", cartePapier.getNumOperateur());
    Assertions.assertEquals(Constants.ANNUEL, cartePapier.getContexte());
    Assertions.assertEquals("2024-01-23", cartePapier.getDateTraitement());

    Assertions.assertEquals("CONSOW0", cartePapier.getContrat().getNumero());
    Assertions.assertEquals("CONSOW0", cartePapier.getContrat().getNumeroAdherent());
    Assertions.assertEquals("CONSOW0-COMPLET", cartePapier.getContrat().getNumeroAdherentComplet());
    Assertions.assertEquals("POPI", cartePapier.getContrat().getNomPorteur());
    Assertions.assertEquals("JEAN PIERRE", cartePapier.getContrat().getPrenomPorteur());
    Assertions.assertEquals("Mr", cartePapier.getContrat().getCivilitePorteur());
    Assertions.assertEquals("03094493", cartePapier.getContrat().getNumeroContratCollectif());
    Assertions.assertEquals(
        "CONSOW0", cartePapier.getContrat().getNumeroExterneContratIndividuel());
    Assertions.assertEquals(
        "DKMMMS09049304930", cartePapier.getContrat().getNumeroExterneContratCollectif());
    Assertions.assertFalse(cartePapier.getContrat().getIsContratResponsable());
    Assertions.assertFalse(cartePapier.getContrat().getIsContratCMU());
    Assertions.assertEquals("0", cartePapier.getContrat().getContratCMUC2S());
    Assertions.assertEquals("1", cartePapier.getContrat().getIndividuelOuCollectif());
    Assertions.assertEquals("A", cartePapier.getContrat().getCategorieSociale());
    Assertions.assertEquals("GEO-FRA-OCC", cartePapier.getContrat().getCritereSecondaireDetaille());
    Assertions.assertEquals("Emplyés", cartePapier.getContrat().getCritereSecondaire());
    Assertions.assertEquals("IGestion", cartePapier.getContrat().getGestionnaire());
    Assertions.assertEquals("TB", cartePapier.getContrat().getGroupeAssures());
    Assertions.assertEquals("123", cartePapier.getContrat().getNumeroCarte());
    Assertions.assertEquals("cege", cartePapier.getContrat().getEditeurCarte());
    Assertions.assertEquals("1", cartePapier.getContrat().getOrdrePriorisation());
    Assertions.assertEquals("wa", cartePapier.getContrat().getIdentifiantCollectivite());
    Assertions.assertEquals("EN", cartePapier.getContrat().getRaisonSociale());
    Assertions.assertEquals("949 372 445", cartePapier.getContrat().getSiret());
    Assertions.assertEquals("X", cartePapier.getContrat().getGroupePopulation());
    Assertions.assertEquals("BLABLA_OPTIQUE", cartePapier.getContrat().getCodeItelis());

    Assertions.assertEquals("Marie-Virginie BERGER", cartePapier.getAdresse().getLigne1());
    Assertions.assertEquals("", cartePapier.getAdresse().getLigne2());
    Assertions.assertEquals("", cartePapier.getAdresse().getLigne3());
    Assertions.assertEquals("2 IMPASSE DE MARIE", cartePapier.getAdresse().getLigne4());
    Assertions.assertEquals("", cartePapier.getAdresse().getLigne5());
    Assertions.assertEquals("00155 Meunier-sur-Mer", cartePapier.getAdresse().getLigne6());
    Assertions.assertEquals("", cartePapier.getAdresse().getLigne7());
    Assertions.assertEquals("00155", cartePapier.getAdresse().getCodePostal());

    Assertions.assertEquals(
        "A", cartePapier.getBeneficiaires().get(0).getLienContrat().getLienFamilial());
    Assertions.assertEquals(
        "1", cartePapier.getBeneficiaires().get(0).getLienContrat().getRangAdministratif());
    Assertions.assertEquals(
        "19791006", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getDateNaissance());
    Assertions.assertEquals(
        "1", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getRangNaissance());
    Assertions.assertEquals(
        "1701062498046",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getNirBeneficiaire());
    Assertions.assertEquals(
        "02", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getCleNirBeneficiaire());
    Assertions.assertEquals(
        "1701062498046", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getNirOd1());
    Assertions.assertEquals(
        "02", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getCleNirOd1());
    Assertions.assertEquals(
        "54756235578", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getNirOd2());
    Assertions.assertEquals(
        "11", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getCleNirOd2());
    Assertions.assertEquals(
        "MBA-000", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getNumeroPersonne());
    Assertions.assertEquals(
        "7209738ADF",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getRefExternePersonne());
    Assertions.assertEquals(
        "2021/01/01",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getDateAdhesionMutuelle());
    Assertions.assertEquals(
        "2021/01/01",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getDateDebutAdhesionIndividuelle());
    Assertions.assertEquals(
        "120320020",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getNumeroAdhesionIndividuelle());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getDateRadiation());
    Assertions.assertEquals(
        "POPI", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getNom());
    Assertions.assertEquals(
        "POPI",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getBeneficiaire()
            .getAffiliation()
            .getNomPatronymique());
    Assertions.assertEquals(
        "POPI",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getNomMarital());
    Assertions.assertEquals(
        "JEAN PIERRE",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getPrenom());
    Assertions.assertEquals(
        "Mr",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getCivilite());
    Assertions.assertEquals(
        "2021/01/01",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getPeriodeDebut());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getPeriodeFin());
    Assertions.assertEquals(
        "A", cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getQualite());
    Assertions.assertEquals(
        "AM",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getBeneficiaire()
            .getAffiliation()
            .getRegimeParticulier());
    Assertions.assertFalse(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getBeneficiaire()
            .getAffiliation()
            .getIsBeneficiaireACS());
    Assertions.assertTrue(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getBeneficiaire()
            .getAffiliation()
            .getIsTeleTransmission());
    Assertions.assertEquals(
        "BENEFICIAIRE",
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getTypeAssure());

    Assertions.assertEquals(
        "AMM", cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCode());
    Assertions.assertEquals(
        "PlatineBase",
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCodeProduit());
    Assertions.assertEquals(
        "KC_PlatineBase",
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCodeGarantie());
    Assertions.assertEquals(
        "SilverBase",
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getLibelleGarantie());
    Assertions.assertEquals(
        "100",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getTauxRemboursement());
    Assertions.assertEquals(
        "PO",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getUniteTauxRemboursement());
    Assertions.assertEquals(
        "AMM-010",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getReferenceCouverture());
    Assertions.assertEquals(
        0, cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getNoOrdreDroit());
    Assertions.assertEquals(
        "AMM", cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCategorie());
    Assertions.assertEquals(
        " 100% FR ",
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getFormulaMask());
    Assertions.assertTrue(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getIsEditable());
    Assertions.assertEquals(
        "01",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getCode());
    Assertions.assertEquals(
        "01",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getLibelle());
    Assertions.assertEquals(
        "01",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getTypeDroit());
    Assertions.assertEquals(
        "01",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getPrioriteBO());
    Assertions.assertEquals(
        0,
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getConventionnements()
            .get(0)
            .getPriorite());
    Assertions.assertEquals(
        "IS",
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getConventionnements()
            .get(0)
            .getTypeConventionnement()
            .getCode());

    Assertions.assertEquals("AMM", cartePapier.getDomainesConventions().get(0).getCode());
    Assertions.assertEquals(0, cartePapier.getDomainesConventions().get(0).getRang());
    Assertions.assertEquals(
        "IS", cartePapier.getDomainesConventions().get(0).getConventions().get(0).getCode());
    Assertions.assertEquals(
        0, cartePapier.getDomainesConventions().get(0).getConventions().get(0).getPriorite());

    Assertions.assertNull(cartePapier.getContrat().getDateSouscription());
    Assertions.assertNull(cartePapier.getContrat().getDateResiliation());
    Assertions.assertNull(cartePapier.getContrat().getType());
    Assertions.assertNull(cartePapier.getContrat().getQualification());
    Assertions.assertNull(cartePapier.getContrat().getRangAdministratif());
    Assertions.assertNull(cartePapier.getContrat().getDestinataire());
    Assertions.assertNull(cartePapier.getContrat().getSituationDebut());
    Assertions.assertNull(cartePapier.getContrat().getSituationFin());
    Assertions.assertNull(cartePapier.getContrat().getMotifFinSituation());
    Assertions.assertNull(cartePapier.getContrat().getLienFamilial());
    Assertions.assertNull(cartePapier.getContrat().getSituationParticuliere());
    Assertions.assertNull(cartePapier.getContrat().getTypeConvention());
    Assertions.assertNull(cartePapier.getContrat().getModePaiementPrestations());
    Assertions.assertNull(cartePapier.getContrat().getFondCarte());
    Assertions.assertNull(cartePapier.getContrat().getAnnexe1Carte());
    Assertions.assertNull(cartePapier.getContrat().getAnnexe2Carte());
    Assertions.assertNull(cartePapier.getContrat().getNumAMCEchange());
    Assertions.assertNull(cartePapier.getContrat().getNumOperateur());
    Assertions.assertNull(cartePapier.getContrat().getCodeRenvoi());
    Assertions.assertNull(cartePapier.getContrat().getLibelleCodeRenvoi());

    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getLienContrat().getModePaiementPrestations());
    Assertions.assertNull(cartePapier.getBeneficiaires().get(0).getBeneficiaire().getIdClientBO());
    Assertions.assertNull(cartePapier.getBeneficiaires().get(0).getBeneficiaire().getInsc());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getRegimeOD1());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getCaisseOD1());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getCentreOD1());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getRegimeOD2());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getCaisseOD2());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAffiliation().getCentreOD2());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getBeneficiaire()
            .getAffiliation()
            .getHasMedecinTraitant());
    Assertions.assertNull(cartePapier.getBeneficiaires().get(0).getBeneficiaire().getAdresses());

    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCodeProfil());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getOrigineDroits());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getNaturePrestation());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getNaturePrestationOnline());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getCodeAssureurGarantie());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCodeOffre());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getVersionOffre());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCodeOc());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCodeCarence());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getCodeOrigine());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getCodeAssureurOrigine());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getPrestations());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getPeriodeDroit());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getPeriodeOnline());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPeriodeProductElement());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getPeriodeCarence());

    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getNirPrio1());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getNirPrio2());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getPrioDroitNir1());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getPrioDroitNir2());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getPrioContratNir1());
    Assertions.assertNull(
        cartePapier
            .getBeneficiaires()
            .get(0)
            .getDomainesCouverture()
            .get(0)
            .getPrioriteDroit()
            .getPrioContratNir2());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getModeAssemblage());
    Assertions.assertNull(
        cartePapier.getBeneficiaires().get(0).getDomainesCouverture().get(0).getIsSuspension());
  }

  @Test
  void testMapperBenefCartePapier() {
    BenefCarteDemat benefCarteDemat = new BenefCarteDemat();
    LienContrat lienContrat = new LienContrat();
    lienContrat.setLienFamilial("lienFamilial");
    lienContrat.setRangAdministratif("rangAdministratif");
    benefCarteDemat.setLienContrat(lienContrat);
    Beneficiaire mockBeneficiaire = Mockito.mock(Beneficiaire.class);
    Mockito.when(mockBeneficiaire.getAffiliation()).thenReturn(Mockito.mock(Affiliation.class));
    benefCarteDemat.setBeneficiaire(mockBeneficiaire);
    DomaineDroit domaineDroit1 = getDomaineDroit("LARA", 3);
    DomaineDroit domaineDroit2 = getDomaineDroit("OPAU", 1);
    DomaineDroit domaineDroit3 = getDomaineDroit("MEDE", 2);
    benefCarteDemat.setDomainesCouverture(List.of(domaineDroit1, domaineDroit2, domaineDroit3));
    BenefCarteDemat resultBenefCarteDemat = mapperCartePapier.mapBenefCartePapier(benefCarteDemat);

    Assertions.assertEquals("OPAU", resultBenefCarteDemat.getDomainesCouverture().get(0).getCode());
    Assertions.assertEquals(
        1, resultBenefCarteDemat.getDomainesCouverture().get(0).getNoOrdreDroit());
    Assertions.assertEquals("MEDE", resultBenefCarteDemat.getDomainesCouverture().get(1).getCode());
    Assertions.assertEquals(
        2, resultBenefCarteDemat.getDomainesCouverture().get(1).getNoOrdreDroit());
    Assertions.assertEquals("LARA", resultBenefCarteDemat.getDomainesCouverture().get(2).getCode());
    Assertions.assertEquals(
        3, resultBenefCarteDemat.getDomainesCouverture().get(2).getNoOrdreDroit());
  }

  private static DomaineDroit getDomaineDroit(String code, int noOrdreDroit) {
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode(code);
    domaineDroit.setNoOrdreDroit(noOrdreDroit);
    domaineDroit.setPrioriteDroit(Mockito.mock(PrioriteDroit.class));
    domaineDroit.setConventionnements(List.of(Mockito.mock(Conventionnement.class)));
    return domaineDroit;
  }
}
