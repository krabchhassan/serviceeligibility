package com.cegedim.next.consumer.api.controller;

import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import com.cegedim.next.consumer.api.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.services.AbstractValidationContratService;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContractControllerTest {

  @Autowired MongoTemplate mongoTemplate;

  @Autowired AbstractValidationContratService validationService;

  @Autowired private ContractController contractController;

  @MockBean public TraceService traceService;
  @MockBean public OcService ocService;

  @Autowired public AuthenticationFacade authenticationFacade;

  @BeforeEach
  void setUp() {
    validationService.setUseReferentialValidation(false);
    validationService.setControleCorrespondanceBobb(false);
  }

  private String body_with_error =
      "{\"idDeclarant\":\"0032165199\",\"societeEmettrice\":\"Test TEAM BLUE\",\"numero\":\"8343484392\",\"numeroExterne\":\"SLOK4983989\",\"numeroAdherent\":\"001\",\"numeroAdherentComplet\":\"TBT83747438\",\"dateSouscription\":\"2020-01-22\",\"dateResiliation\":null,\"apporteurAffaire\":\"Courtier & Co\",\"periodesContratResponsableOuvert\":[{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}],\"periodesContratCMUOuvert\":[{\"code\":\"CMU\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}}],\"critereSecondaireDetaille\":\"CAD\",\"critereSecondaire\":\"Cadres\",\"isContratIndividuel\":true,\"gestionnaire\":\"IGestion\",\"contratCollectif\":{\"numero\":\"03094493\",\"numeroExterne\":\"DKMMMS09049304930\"},\"qualification\":\"Base\",\"ordrePriorisation\":\"1\",\"contexteTiersPayant\":{\"isCartePapierAEditer\":true,\"periodesDroitsCarte\":{\"debut\":\"2020-01-01\",\"fin\":\"2023-12-31\"},\"college\":\"Cadre\",\"collectivite\":\"362 521 879 00034\",\"isCarteDematerialisee\":true,\"isCartePapier\":false},\"assures\":[{\"isSouscripteur\":true,\"rangAdministratif\":\"1\",\"identite\":{\"nir\":{\"code\":\"1701062498046\",\"cle\":\"02\"},\"affiliationsRO\":[{\"nir\":{\"code\":\"1701062498046\",\"cle\":\"02\"},\"rattachementRO\":{\"codeRegime\":\"01\",\"codeCaisse\":\"624\",\"codeCentre\":\"0156\"},\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}},{\"nir\":{\"code\":\"1701062498046\",\"cle\":\"02\"},\"rattachementRO\":{\"codeRegime\":\"01\",\"codeCaisse\":\"595\",\"codeCentre\":\"0123\"},\"periode\":{\"debut\":\"2019-01-01\",\"fin\":\"2019-12-31\"}}],\"dateNaissance\":\"19791006\",\"rangNaissance\":\"1\",\"numeroPersonne\":\"001\",\"refExternePersonne\":\"7209738ADF\"},\"data\":{\"nom\":{\"nomFamille\":\"DELMOTTE\",\"nomUsage\":null,\"prenom\":\"JEAN PIERRE\",\"civilite\":\"Mr\"},\"adresse\":{\"ligne1\":\"25 RUE DES LAGRADES\",\"ligne2\":\"RESIDENCE DES PLATEAUX\",\"ligne3\":\"59250\",\"ligne4\":\"HALLUIN\",\"ligne5\":\"F\",\"ligne6\":\"FRANCE\",\"ligne7\":\"CEDEX41\",\"codePostal\":\"5920\"},\"contact\":{\"fixe\":\"0321992364\",\"mobile\":\"0735427677\",\"email\":\"jp-delmotte@gagamil.coum\"},\"destinatairesPaiements\":[{\"idDestinatairePaiements\":\"123456\",\"nom\":{\"nomFamille\":\"DELMOTTE\",\"nomUsage\":null,\"prenom\":\"JEAN PIERRE\",\"civilite\":\"Mr\"},\"adresse\":{\"ligne1\":\"25 RUE DES LAGRADES\",\"ligne2\":\"RESIDENCE DES PLATEAUX\",\"ligne3\":\"59250\",\"ligne4\":\"HALLUIN\",\"ligne5\":\"F\",\"ligne6\":\"FRANCE\",\"ligne7\":\"CEDEX41\",\"codePostal\":\"5920\"},\"rib\":{\"bic\":\"MCCFFRP1\",\"iban\":\"FR3330002005500000157841Z25\"},\"modePaiementPrestations\":{\"code\":\"VIR\",\"libelle\":\"Virement\",\"codeMonnaie\":\"EUR\"},\"periode\":{\"debut\":\"2020-01-01\"}}],\"destinatairesRelevePrestations\":[{\"idDestinataireRelevePrestations\":\"123456\",\"nom\":{\"nomFamille\":\"DELMOTTE\",\"nomUsage\":null,\"prenom\":\"JEAN PIERRE\",\"civilite\":\"Mr\"},\"adresse\":{\"ligne1\":\"25 RUE DES LAGRADES\",\"ligne2\":\"RESIDENCE DES PLATEAUX\",\"ligne3\":\"59250\",\"ligne4\":\"HALLUIN\",\"ligne5\":\"F\",\"ligne6\":\"FRANCE\",\"ligne7\":\"CEDEX41\",\"codePostal\":\"5920\"},\"periode\":{\"debut\":\"2020-01-01\"},\"dematerialisation\":{\"isDematerialise\":\"false\",\"email\":\"testContractDelete1-1@consumer.worker\",\"mobile\":\"0600000011\"}}]},\"dateAdhesionMutuelle\":\"2020-01-01\",\"dateDebutAdhesionIndividuelle\":\"2020-01-01\",\"numeroAdhesionIndividuelle\":\"120320020\",\"dateRadiation\":null,\"dateCreation\":\"2018-08-22T07:53:13.460Z\",\"dateModification\":\"2018-08-22T09:53:13.460Z\",\"digitRelation\":{\"dematerialisation\":{\"isDematerialise\":\"true\",\"email\":\"jp-delmotte@gagamil.coum\",\"mobile\":\"0735427677\"},\"teletransmissions\":[{\"periode\":{\"debut\":\"2020-01-01\"},\"isTeletransmission\":true}]},\"periodes\":[{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}],\"periodesMedecinTraitantOuvert\":[{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"},{\"debut\":\"2019-01-01\",\"fin\":\"2019-12-31\"}],\"regimesParticuliers\":[{\"code\":\"AM\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}}],\"situationsParticulieres\":[{\"code\":\"ALD\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}}],\"qualite\":{\"code\":\"B\",\"libelle\":\"BENEFICIAIRE\"},\"droits\":[{\"code\":\"ATRDENT\",\"codeAssureur\":\"DNT\",\"libelle\":\"DENTAIRE\",\"ordrePriorisation\":\"1\",\"type\":\"BASE\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":null},\"dateAncienneteGarantie\":\"2020-01-01\",\"carences\":[{\"code\":\"3M\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"},\"droitRemplacement\":{\"codeAssureur\":\"DENASS\",\"libelle\":\"Dentaire Rempl\",\"code\":\"DNTASS\"}}]}]}]}\n";
  private String body =
      "{\"idDeclarant\":\"0032165199\",\"societeEmettrice\":\"Test TEAM BLUE\",\"numero\":\"8343484392\",\"numeroExterne\":\"SLOK4983989\",\"numeroAdherent\":\"001\",\"numeroAdherentComplet\":\"TBT83747438\",\"dateSouscription\":\"2020-01-22\",\"dateResiliation\":null,\"apporteurAffaire\":\"Courtier & Co\",\"periodesContratResponsableOuvert\":[{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}],\"periodesContratCMUOuvert\":[{\"code\":\"CMU\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}}],\"critereSecondaireDetaille\":\"CAD\",\"critereSecondaire\":\"Cadres\",\"isContratIndividuel\":true,\"gestionnaire\":\"IGestion\",\"contratCollectif\":{\"numero\":\"03094493\",\"numeroExterne\":\"DKMMMS09049304930\"},\"qualification\":\"Base\",\"ordrePriorisation\":\"1\",\"contexteTiersPayant\":{\"isCartePapierAEditer\":true,\"periodesDroitsCarte\":{\"debut\":\"2020-01-01\",\"fin\":\"2023-12-31\"},\"college\":\"Cadre\",\"collectivite\":\"362 521 879 00034\",\"isCarteDematerialisee\":true,\"isCartePapier\":false},\"assures\":[{\"isSouscripteur\":true,\"rangAdministratif\":\"1\",\"identite\":{\"nir\":{\"code\":\"1701062498046\",\"cle\":\"02\"},\"affiliationsRO\":[{\"nir\":{\"code\":\"1701062498046\",\"cle\":\"02\"},\"rattachementRO\":{\"codeRegime\":\"01\",\"codeCaisse\":\"624\",\"codeCentre\":\"0156\"},\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}},{\"nir\":{\"code\":\"1701062498046\",\"cle\":\"02\"},\"rattachementRO\":{\"codeRegime\":\"01\",\"codeCaisse\":\"595\",\"codeCentre\":\"0123\"},\"periode\":{\"debut\":\"2019-01-01\",\"fin\":\"2019-12-31\"}}],\"dateNaissance\":\"19791006\",\"rangNaissance\":\"1\",\"numeroPersonne\":\"001\",\"refExternePersonne\":\"7209738ADF\"},\"data\":{\"nom\":{\"nomFamille\":\"DELMOTTE\",\"nomUsage\":null,\"prenom\":\"JEAN PIERRE\",\"civilite\":\"Mr\"},\"adresse\":{\"ligne1\":\"25 RUE DES LAGRADES\",\"ligne2\":\"RESIDENCE DES PLATEAUX\",\"ligne3\":\"59250\",\"ligne4\":\"HALLUIN\",\"ligne5\":\"F\",\"ligne6\":\"FRANCE\",\"ligne7\":\"CEDEX41\",\"codePostal\":\"5920\"},\"contact\":{\"fixe\":\"0321992364\",\"mobile\":\"0735427677\",\"email\":\"jp-delmotte@gagamil.coum\"},\"destinatairesPaiements\":[{\"idDestinatairePaiements\":\"123456\",\"nom\":{\"nomFamille\":\"DELMOTTE\",\"nomUsage\":null,\"prenom\":\"JEAN PIERRE\",\"civilite\":\"Mr\"},\"adresse\":{\"ligne1\":\"25 RUE DES LAGRADES\",\"ligne2\":\"RESIDENCE DES PLATEAUX\",\"ligne3\":\"59250\",\"ligne4\":\"HALLUIN\",\"ligne5\":\"F\",\"ligne6\":\"FRANCE\",\"ligne7\":\"CEDEX41\",\"codePostal\":\"5920\"},\"rib\":{\"bic\":\"MCCFFRP1\",\"iban\":\"FR3330002005500000157841Z25\"},\"modePaiementPrestations\":{\"code\":\"VIR\",\"libelle\":\"Virement\",\"codeMonnaie\":\"EUR\"},\"periode\":{\"debut\":\"2020-01-01\"}}],\"destinatairesRelevePrestations\":[{\"idDestinataireRelevePrestations\":\"123456\",\"nom\":{\"nomFamille\":\"DELMOTTE\",\"nomUsage\":null,\"prenom\":\"JEAN PIERRE\",\"civilite\":\"Mr\"},\"adresse\":{\"ligne1\":\"25 RUE DES LAGRADES\",\"ligne2\":\"RESIDENCE DES PLATEAUX\",\"ligne3\":\"59250\",\"ligne4\":\"HALLUIN\",\"ligne5\":\"F\",\"ligne6\":\"FRANCE\",\"ligne7\":\"CEDEX41\",\"codePostal\":\"5920\"},\"periode\":{\"debut\":\"2020-01-01\"},\"dematerialisation\":{\"isDematerialise\":\"false\",\"email\":\"testContractDelete1-1@consumer.worker\",\"mobile\":\"0600000011\"}}]},\"dateAdhesionMutuelle\":\"2020-01-01\",\"dateDebutAdhesionIndividuelle\":\"2020-01-01\",\"numeroAdhesionIndividuelle\":\"120320020\",\"dateRadiation\":null,\"digitRelation\":{\"dematerialisation\":{\"isDematerialise\":\"true\",\"email\":\"jp-delmotte@gagamil.coum\",\"mobile\":\"0735427677\"},\"teletransmissions\":[{\"periode\":{\"debut\":\"2020-01-01\"},\"isTeletransmission\":true}]},\"periodes\":[{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}],\"periodesMedecinTraitantOuvert\":[{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"},{\"debut\":\"2019-01-01\",\"fin\":\"2019-12-31\"}],\"regimesParticuliers\":[{\"code\":\"AM\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}}],\"situationsParticulieres\":[{\"code\":\"ALD\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"}}],\"qualite\":{\"code\":\"B\",\"libelle\":\"BENEFICIAIRE\"},\"droits\":[{\"code\":\"ATRDENT\",\"codeAssureur\":\"DNT\",\"libelle\":\"DENTAIRE\",\"ordrePriorisation\":\"1\",\"type\":\"BASE\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":null},\"dateAncienneteGarantie\":\"2020-01-01\",\"carences\":[{\"code\":\"3M\",\"periode\":{\"debut\":\"2020-01-01\",\"fin\":\"2020-12-31\"},\"droitRemplacement\":{\"codeAssureur\":\"DENASS\",\"libelle\":\"Dentaire Rempl\",\"code\":\"DNTASS\"}}]}]}]}\n";
  private String version = "5";
  private String idDeclarant = "0032165199";

  @Test
  void processV5Test_unserialization_fails() {
    Mockito.when(mongoTemplate.save(Mockito.any(Trace.class), Mockito.anyString()))
        .thenReturn(new Trace());
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.anyList(),
            Mockito.anyString());
    ResponseEntity<CreationResponse> response =
        contractController.createContractV2(body_with_error, version, idDeclarant);
    Assertions.assertEquals(400, response.getStatusCodeValue());
    Assertions.assertEquals("ErrorDeserializing", response.getBody().getStatus());
  }

  @Test
  void processV5Test_unknown_amc() {
    Mockito.when(mongoTemplate.save(Mockito.any(Trace.class), Mockito.anyString()))
        .thenReturn(new Trace());
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.anyList(),
            Mockito.anyString());
    ResponseEntity<CreationResponse> response =
        contractController.createContractV2(body, version, idDeclarant);
    Assertions.assertEquals(400, response.getStatusCodeValue());
    Assertions.assertEquals(
        "Le déclarant " + idDeclarant + " n'existe pas !",
        response.getBody().getErrorMessages().get(0));
  }

  @Test
  void processV5Test_no_orga() {
    Mockito.when(authenticationFacade.getAuthenticationUserName()).thenReturn("Unidentified");
    Declarant declarant = new Declarant();
    declarant.setIdClientBO("Unidentified");
    Mockito.when(mongoTemplate.findById(idDeclarant, Declarant.class)).thenReturn(declarant);
    Mockito.when(mongoTemplate.save(Mockito.any(Trace.class), Mockito.anyString()))
        .thenReturn(new Trace());
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.anyList(),
            Mockito.anyString());
    ResponseEntity<CreationResponse> response =
        contractController.createContractV2(body, version, idDeclarant);
    Assertions.assertEquals(400, response.getStatusCodeValue());
    Assertions.assertEquals(
        "Pour le contrat n°8343484392 de l'adhérent n°001 lié à l'AMC 0032165199 : Impossible de déterminer l'organisation liée à l'AMC n°0032165199",
        response.getBody().getErrorMessages().get(0));
  }

  @Test
  void processV5Test() {
    Mockito.when(authenticationFacade.getAuthenticationUserName()).thenReturn("Unidentified");
    Declarant declarant = new Declarant();
    declarant.setIdClientBO("Unidentified");
    Mockito.when(mongoTemplate.findById(idDeclarant, Declarant.class)).thenReturn(declarant);
    Mockito.when(ocService.getOC(idDeclarant)).thenReturn(new Oc());
    Mockito.when(mongoTemplate.save(Mockito.any(Trace.class), Mockito.anyString()))
        .thenReturn(new Trace());
    Mockito.doNothing()
        .when(traceService)
        .updateStatusError(
            Mockito.anyString(),
            Mockito.any(TraceStatus.class),
            Mockito.anyList(),
            Mockito.anyString());

    OrganisationServiceWrapper organisationServiceWrapper =
        Mockito.mock(OrganisationServiceWrapper.class);
    Organisation org = new Organisation();
    org.setMain(true);
    org.setCode("Unknown");
    org.setFullName("s3OrgaFullName");
    org.setCommercialName("s3OrgaCommercialName");
    try {
      Mockito.when(organisationServiceWrapper.getOrganizationByAmcNumber(Mockito.anyString()))
          .thenReturn(org);
      Mockito.when(
              organisationServiceWrapper.isOrgaAttached(Mockito.anyString(), Mockito.anyString()))
          .thenReturn(true);
    } catch (Exception ignore) {
    }

    ReflectionTestUtils.setField(
        validationService, "organisationServiceWrapper", organisationServiceWrapper);
    ReflectionTestUtils.setField(contractController, "validationService", validationService);

    ResponseEntity<CreationResponse> response =
        contractController.createContractV2(body, version, idDeclarant);
    Assertions.assertEquals(200, response.getStatusCodeValue());
  }
}
