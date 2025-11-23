package com.cegedim.next.serviceeligibility.almerys608.services;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.cegedim.common.base.pefb.services.MetaService;
import com.cegedim.next.serviceeligibility.almerys608.dao.CommonDao;
import com.cegedim.next.serviceeligibility.almerys608.dao.ProduitsAlmerysExclusDao;
import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.almerys608.model.InfoCentreGestion;
import com.cegedim.next.serviceeligibility.almerys608.model.InfoEntreprise;
import com.cegedim.next.serviceeligibility.almerys608.utils.EnumTempTable;
import com.cegedim.next.serviceeligibility.almerys608.utils.XmlUtils;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscodageDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationsConsolideesAlmerysDao;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDao;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejection;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoPilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.stream.Streams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.util.CloseableIterator;

public abstract class MockUtils {
  @MockBean public DeclarationsConsolideesAlmerysDao declarationsConsolideesAlmerysDao;
  @MockBean TranscodageDao transcodageDao;
  @MockBean ProduitsAlmerysExclusDao produitsAlmerysExclusDao;
  @MockBean CommonDao commonDao;
  @Autowired ProcessorAlmerys608 processorAlmerys608;
  @MockBean HistoriqueExecutionsDao historiqueExecutionsDao;
  @Autowired MongoTemplate mongoTemplate;
  @MockBean RetrieveTmpObjectsService retrieveTmpObjectsService;
  @MockBean ARLService arlService;
  @MockBean DeclarationDao declarationDao;
  private MockedStatic<MetaService> mockMetaService;

  List<InfoCentreGestion> listInfoCentreGestion = new ArrayList<>();
  List<InfoEntreprise> listInfoEntreprise = new ArrayList<>();
  List<Rattachement> listRattachement = new ArrayList<>();
  List<AdresseAdherent> listAdresseAdherent = new ArrayList<>();
  List<Beneficiaire> listBeneficiaire = new ArrayList<>();
  List<Carence> listCarence = new ArrayList<>();
  List<com.cegedim.next.serviceeligibility.almerys608.model.Contrat> listContrat =
      new ArrayList<>();
  List<MembreContrat> listMembreContrat = new ArrayList<>();
  List<Produit> listProduit = new ArrayList<>();
  List<ServiceTP> listServiceTP = new ArrayList<>();
  List<Souscripteur> listSouscripteur = new ArrayList<>();
  List<Rejet> listRejet = new ArrayList<>();

  public StringBuilder sb = new StringBuilder();

  @BeforeEach
  void setUp() {
    listInfoCentreGestion.clear();
    listInfoEntreprise.clear();
    listRattachement.clear();
    listAdresseAdherent.clear();
    listBeneficiaire.clear();
    listCarence.clear();
    listContrat.clear();
    listMembreContrat.clear();
    listProduit.clear();
    listServiceTP.clear();
    listSouscripteur.clear();
    listRejet.clear();
    sb = new StringBuilder();

    doNothing().when(declarationsConsolideesAlmerysDao).createTmpCollection(Mockito.any());
    List<Transcodage> transcodageDomaineDroits = new ArrayList<>();
    Transcodage transcodageDomaineDroit1 = new Transcodage();
    transcodageDomaineDroit1.setCle(List.of(""));
    transcodageDomaineDroit1.setCodeObjetTransco("");
    transcodageDomaineDroits.add(transcodageDomaineDroit1);
    when(transcodageDao.findByCodeObjetTransco("ALMV3", "Domaine_Droits"))
        .thenReturn(transcodageDomaineDroits);

    List<Transcodage> transcodageLienJuridique = new ArrayList<>();
    Transcodage transcodageDomaineDroit2 = new Transcodage();
    transcodageDomaineDroit2.setCle(List.of(""));
    transcodageDomaineDroit2.setCodeObjetTransco("");
    transcodageLienJuridique.add(transcodageDomaineDroit2);
    when(transcodageDao.findByCodeObjetTransco("ALMV3", "Lien_Juridique"))
        .thenReturn(transcodageLienJuridique);

    List<Transcodage> transcodageTypeBeneficiaire = new ArrayList<>();
    Transcodage transcodageDomaineDroit3 = new Transcodage();
    transcodageDomaineDroit3.setCle(List.of("A"));
    transcodageDomaineDroit3.setCodeTransco("AS");
    transcodageTypeBeneficiaire.add(transcodageDomaineDroit3);
    when(transcodageDao.findByCodeObjetTransco("ALMV3", "Type_beneficiaire"))
        .thenReturn(transcodageTypeBeneficiaire);

    List<Transcodage> transcodageCodeMouvement = new ArrayList<>();
    Transcodage transcodageDomaineDroit4 = new Transcodage();
    transcodageDomaineDroit4.setCle(List.of("", "O", "0"));
    transcodageDomaineDroit4.setCodeTransco("MC");
    transcodageCodeMouvement.add(transcodageDomaineDroit4);
    when(transcodageDao.findByCodeObjetTransco("ALMV3", "Code_Mouvement"))
        .thenReturn(transcodageCodeMouvement);

    List<ProduitsAlmerysExclus> produitsAlmerysExclusList = new ArrayList<>();
    when(produitsAlmerysExclusDao.findByKey(Mockito.any(), Mockito.any()))
        .thenReturn(produitsAlmerysExclusList);

    Mockito.doAnswer(
            invocation -> {
              if (invocation.getArgument(1).equals(InfoCentreGestion.class)) {
                listInfoCentreGestion.addAll(invocation.getArgument(0));
              } else if (invocation.getArgument(1).equals(InfoEntreprise.class)) {
                listInfoEntreprise.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(Rattachement.class)) {
                listRattachement.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(AdresseAdherent.class)) {
                listAdresseAdherent.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(Beneficiaire.class)) {
                listBeneficiaire.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(Carence.class)) {
                listCarence.addAll(invocation.getArgument(0));
              }
              if (invocation
                  .getArgument(1)
                  .equals(com.cegedim.next.serviceeligibility.almerys608.model.Contrat.class)) {
                listContrat.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(MembreContrat.class)) {
                listMembreContrat.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(Produit.class)) {
                listProduit.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(ServiceTP.class)) {
                listServiceTP.addAll(invocation.getArgument(0));
              }
              if (invocation.getArgument(1).equals(Souscripteur.class)) {
                listSouscripteur.addAll(invocation.getArgument(0));
                Mockito.doAnswer(
                        invocation2 ->
                            listSouscripteur.stream()
                                .filter(
                                    s -> s.getNumeroContrat().equals(invocation2.getArgument(2)))
                                .findFirst()
                                .orElse(null))
                    .when(commonDao)
                    .get(eq(Souscripteur.class), Mockito.any(), Mockito.any());
              }
              if (invocation.getArgument(1).equals(Rejet.class)) {
                listRejet.addAll(invocation.getArgument(0));
              }

              List<BulkObject> item = invocation.getArgument(0);
              return item.size();
            })
        .when(commonDao)
        .insertAll(Mockito.any(), Mockito.any(), Mockito.anyString());

    Mockito.doAnswer(invocation -> Streams.of(listInfoCentreGestion.iterator()))
        .when(retrieveTmpObjectsService)
        .getAllInfoCentreGestion(Mockito.anyString());

    Mockito.doAnswer(
            invocation ->
                listInfoCentreGestion.stream()
                        .anyMatch(cg -> invocation.getArgument(0).equals(cg.getRefInterneCG()))
                    ? invocation.getArgument(0)
                    : null)
        .when(retrieveTmpObjectsService)
        .getRefCG(Mockito.anyString(), Mockito.anyString());

    Mockito.doAnswer(invocation -> Streams.of(listInfoEntreprise.iterator()))
        .when(retrieveTmpObjectsService)
        .getAllInfoEntreprise(Mockito.anyString());

    Mockito.doAnswer(
            invocation ->
                listRejet.stream()
                    .anyMatch(rejet -> invocation.getArgument(0).equals(rejet.getNumContrat())))
        .when(retrieveTmpObjectsService)
        .isRejected(Mockito.anyString(), Mockito.anyString());

    Mockito.doAnswer(
            invocation ->
                listRattachement.stream()
                    .filter(rat -> invocation.getArgument(0).equals(rat.getNumeroContrat()))
                    .toList())
        .when(retrieveTmpObjectsService)
        .getRattachements(Mockito.anyString(), Mockito.anyString());

    Mockito.doAnswer(
            invocation ->
                listBeneficiaire.stream()
                    .filter(benef -> invocation.getArgument(1).equals(benef.getNumeroContrat()))
                    .map(this::mapInfoBenef)
                    .toList())
        .when(retrieveTmpObjectsService)
        .getBenef(Mockito.anyString(), Mockito.anyString());

    /*Mockito.doAnswer(invocation -> createCloseableIterator(listCarence.iterator()))
    .when(commonDao)
    .getAggregationStream(Mockito.any(), Mockito.anyString(), eq(Carence.class));*/

    Mockito.doAnswer(invocation -> Streams.of(listContrat.iterator()))
        .when(retrieveTmpObjectsService)
        .getAll(Mockito.anyString(), Mockito.any());

    Mockito.doAnswer(
            invocation ->
                listMembreContrat.stream()
                    .filter(mc -> invocation.getArgument(0).equals(mc.getNumeroContrat()))
                    .map(mc -> mapInfoMembreContrat(mc, listAdresseAdherent))
                    .toList())
        .when(retrieveTmpObjectsService)
        .getMembres(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    Mockito.doAnswer(
            invocation ->
                listProduit.stream()
                    .filter(
                        produit ->
                            invocation.getArgument(2).equals(produit.getNumeroContrat())
                                && invocation.getArgument(3).equals(produit.getRefInterneOS()))
                    .map(this::mapInfoProduit)
                    .toList())
        .when(retrieveTmpObjectsService)
        .getProduits(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    Mockito.doAnswer(
            invocation ->
                listServiceTP.stream()
                    .filter(
                        service ->
                            invocation.getArgument(1).equals(service.getNumeroContrat())
                                && invocation.getArgument(2).equals(service.getRefInterneOS()))
                    .map(this::mapInfoServiceTP)
                    .findFirst()
                    .orElse(null))
        .when(retrieveTmpObjectsService)
        .getInfoServiceTP(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    Mockito.doAnswer(
            invocation -> {
              if (invocation
                  .getArgument(0)
                  .toString()
                  .contains(EnumTempTable.REJET_NON_BLOQUANT.getTableName())) {
                return new ArrayList<>();
              } else {
                return listRejet;
              }
            })
        .when(commonDao)
        .getAll(Mockito.anyString(), eq(Rejet.class));

    Mockito.doAnswer(
            invocationOnMock -> {
              return getDeclaration(invocationOnMock.getArgument(0));
            })
        .when(declarationDao)
        .getDeclarationById(Mockito.anyString());
  }

  public <T> CloseableIterator<T> createCloseableIterator(Iterator<T> iterator) {
    return new CloseableIterator<>() {
      @Override
      public void close() {}

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        return iterator.next();
      }
    };
  }

  private InfoMembreContrat mapInfoMembreContrat(
      MembreContrat membreContrat, List<AdresseAdherent> adresses) {
    InfoMembreContrat infoMembreContrat = new InfoMembreContrat();

    infoMembreContrat.getNNIRATTS().add(membreContrat.getNniRatt());

    InfoIndividuOs infoIndividuOs = new InfoIndividuOs();
    infoIndividuOs.setREFINTERNEOS(membreContrat.getRefInterneOs());
    infoIndividuOs.setDATENAISSANCE(membreContrat.getDateNaissance());
    infoIndividuOs.setRANGNAISSANCE(Integer.valueOf(membreContrat.getRangNaissance()));
    infoIndividuOs.setNOMPATRONIMIQUE(membreContrat.getNomPatronimique());
    infoIndividuOs.setNOMUSAGE(membreContrat.getNomUsage());
    infoIndividuOs.setPRENOM(membreContrat.getPrenom());
    infoIndividuOs.setMEDECINTRAITANT(membreContrat.getMedecinTraitant());
    infoMembreContrat.setINDIVIDU(infoIndividuOs);

    InfoAdresseParticulier infoAdresseParticulier = new InfoAdresseParticulier();
    adresses.stream()
        .filter(adresse -> adresse.getRefInterneOS().equals(membreContrat.getRefInterneOs()))
        .forEach(
            adresse -> {
              InfoAdresseAgregeeParticulier infoAdresseAgregeeParticulier =
                  new InfoAdresseAgregeeParticulier();
              infoAdresseAgregeeParticulier.setLIGNE1(adresse.getAdresse().getLigne1());
              infoAdresseAgregeeParticulier.setLIGNE2(adresse.getAdresse().getLigne2());
              infoAdresseAgregeeParticulier.setLIGNE3(adresse.getAdresse().getLigne3());
              infoAdresseAgregeeParticulier.setLIGNE4(adresse.getAdresse().getLigne4());
              infoAdresseAgregeeParticulier.setLIGNE5(adresse.getAdresse().getLigne5());
              infoAdresseAgregeeParticulier.setLIGNE6(adresse.getAdresse().getLigne6());
              infoAdresseAgregeeParticulier.setLIGNE7(adresse.getAdresse().getLigne7());
              infoAdresseParticulier.getADRESSEAGREGEES().add(infoAdresseAgregeeParticulier);

              if (StringUtils.isNotBlank(adresse.getAdresse().getFixe())) {
                InfoJoignabilite infoJoignabilite = new InfoJoignabilite();
                infoJoignabilite.setACTIF(true);
                infoJoignabilite.setMEDIA(CodeMedia.VF);
                infoJoignabilite.setADRESSEMEDIA(adresse.getAdresse().getFixe());
                infoMembreContrat.getJOIGNABILITES().add(infoJoignabilite);
              }

              if (StringUtils.isNotBlank(adresse.getAdresse().getTelephone())) {
                InfoJoignabilite infoJoignabilite = new InfoJoignabilite();
                infoJoignabilite.setACTIF(true);
                infoJoignabilite.setMEDIA(CodeMedia.VB);
                infoJoignabilite.setADRESSEMEDIA(adresse.getAdresse().getTelephone());
                infoMembreContrat.getJOIGNABILITES().add(infoJoignabilite);
              }

              if (StringUtils.isNotBlank(adresse.getAdresse().getEmail())) {
                InfoJoignabilite infoJoignabilite2 = new InfoJoignabilite();
                infoJoignabilite2.setACTIF(true);
                infoJoignabilite2.setMEDIA(CodeMedia.ME);
                infoJoignabilite2.setADRESSEMEDIA(adresse.getAdresse().getEmail());
                infoMembreContrat.getJOIGNABILITES().add(infoJoignabilite2);
              }
            });
    infoMembreContrat.setADRESSEMEMBRE(infoAdresseParticulier);

    infoMembreContrat.setSOUSCRIPTEUR(1 == membreContrat.getSouscripteur());
    infoMembreContrat.setPOSITION(membreContrat.getPosition());
    if (StringUtils.isNotBlank(membreContrat.getTypeRegime())) {
      infoMembreContrat.setTYPEREGIME(CodeRegime.fromValue(membreContrat.getTypeRegime()));
    }
    infoMembreContrat.setDATEENTREE(membreContrat.getDateEntree());
    infoMembreContrat.setAUTONOME(1 == membreContrat.getAutonome());
    if (StringUtils.isNotBlank(membreContrat.getModePaiement())) {
      infoMembreContrat.setMODEPAIEMENT(
          CodeModePaiement.fromValue(membreContrat.getModePaiement()));
    }
    infoMembreContrat.setNNI(membreContrat.getNni());
    infoMembreContrat.getNNIRATTS().add(membreContrat.getNniRatt());
    if (StringUtils.isNotBlank(membreContrat.getRegimeSpecial())) {
      infoMembreContrat.setREGIMESPECIAL(
          CodeRegimeSpecial.fromValue(membreContrat.getRegimeSpecial()));
    }
    infoMembreContrat.setDATERADIATION(membreContrat.getDateRadiation());

    return infoMembreContrat;
  }

  private InfoBenef mapInfoBenef(Beneficiaire benef) {
    InfoBenef infoBenef = new InfoBenef();

    infoBenef.setREFINTERNEOS(benef.getRefInterneOS());
    if (StringUtils.isNotBlank(benef.getCodeMouvementCarte())) {
      infoBenef.setCODEMOUVEMENTCARTE(CodeCarte.fromValue(benef.getCodeMouvementCarte()));
    }
    infoBenef.setCONTRATRESPONSABLE(1 == benef.getContratResponsable());
    if (StringUtils.isNotBlank(benef.getTypeBenef())) {
      infoBenef.setTYPEBENEF(CodeBenef.fromValue(benef.getTypeBenef()));
    }

    return infoBenef;
  }

  private InfoServiceTP mapInfoServiceTP(ServiceTP serviceTP) {
    InfoServiceTP infoServiceTP = new InfoServiceTP();

    if (StringUtils.isNotBlank(serviceTP.getEnvoi())) {
      infoServiceTP.setENVOI(CodeEnvoi.fromValue(serviceTP.getEnvoi()));
    }
    if (StringUtils.isNotBlank(serviceTP.getActivationDesactivation())) {
      infoServiceTP.setACTIVATIONDESACTIVATION(
          CodeActivation.fromValue(serviceTP.getActivationDesactivation()));
    }
    infoServiceTP.setDATEDEBUTSUSPENSION(serviceTP.getDateDebutSuspension());
    infoServiceTP.setDATEFINSUSPENSION(serviceTP.getDateFinSuspension());
    infoServiceTP.setDATEDEBUTVALIDITE(serviceTP.getDateDebutValidite());
    infoServiceTP.setDATEFINVALIDITE(serviceTP.getDateFinValidite());

    return infoServiceTP;
  }

  private InfoProduit mapInfoProduit(Produit produit) {
    InfoProduit infoProduit = new InfoProduit();

    infoProduit.setDATEENTREEPRODUIT(produit.getDateEntreeProduit());
    infoProduit.setDATESORTIEPRODUIT(produit.getDateSortieProduit());
    infoProduit.setREFERENCEPRODUIT(produit.getReferenceProduit());
    infoProduit.setORDRE(produit.getOrdre());

    return infoProduit;
  }

  protected void mockBdd(List<TmpObject2> tmpObject2List) {
    Mockito.when(
            mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TraceExtractionConso.class))
        .thenReturn(mock(BulkOperations.class));

    when(historiqueExecutionsDao.getLastExecution(
            Mockito.any(Criteria.class), eq(HistoriqueExecution608.class)))
        .thenReturn(null);
    when(declarationsConsolideesAlmerysDao.countDeclarationConsolideesAlmerys(
            Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(1);
    when(declarationsConsolideesAlmerysDao.countAll()).thenReturn(1);

    Stream<TmpObject2> iterator1 = Streams.of(tmpObject2List.iterator());
    when(declarationsConsolideesAlmerysDao.getAll(Mockito.any())).thenReturn(iterator1);
    Stream<TmpObject2> iterator2 = Streams.of(tmpObject2List.iterator());
    when(declarationsConsolideesAlmerysDao.getAllForSouscripteur(Mockito.any()))
        .thenReturn(iterator2);
  }

  protected void checkFile(Declarant declarant, String couloirClient, String fileName) {
    try (MockedStatic<XmlUtils> mockStatic =
        Mockito.mockStatic(XmlUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mockStatic
          .when(() -> XmlUtils.writeInFile(Mockito.any(), Mockito.anyString()))
          .thenAnswer(
              (Answer<Void>)
                  invocation -> {
                    sb.append((String) invocation.getArgument(1));
                    return null;
                  });
      processorAlmerys608.buildAlmerysFile(declarant, couloirClient);

      try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
        Assertions.assertNotNull(inputStream);
        String temoin = new String(inputStream.readAllBytes());
        temoin =
            temoin
                .replaceAll(" {4}<DATE_CREATION>.*</DATE_CREATION>", "")
                .replace("\n", "")
                .replace("\r", "");
        temoin =
            temoin
                .replaceAll(" {4}<NUM_FICHIER>.*</NUM_FICHIER>", "")
                .replace("\n", "")
                .replace("\r", "");
        String result =
            sb.toString()
                .replaceAll(" {4}<DATE_CREATION>.*</DATE_CREATION>", "")
                .replace("\n", "")
                .replace("\r", "");

        result =
            result
                .replaceAll(" {4}<NUM_FICHIER>.*</NUM_FICHIER>", "")
                .replace("\n", "")
                .replace("\r", "");

        Assertions.assertEquals(temoin, result);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Attention, cette valeur doit être différente à chaque test si c'est une valeur différente, ça
   * va être le même fichier et le contenu du xml ne va pas être entier
   *
   * @return csd
   */
  public abstract String getCritereSecondaireDetaille();

  public abstract TmpObject2 createTmpObject2();

  public String getCouloirClient() {
    return "*";
  }

  /**
   * a redefinir si un ARL doit être généré
   *
   * @param id
   * @return
   */
  public Declaration getDeclaration(String id) {
    Declaration decl = new Declaration();
    decl.set_id(id);
    return decl;
  }

  public void test(List<TmpObject2> tmpObject2List, String fileName) {
    test(tmpObject2List, fileName, new ArrayList<>());
  }

  public void test(List<TmpObject2> tmpObject2List, String fileName, List<String> listeCodeRejet) {
    mockBdd(tmpObject2List);

    mockMetaFile();

    mockARL();

    checkFile(initDeclarant(), getCouloirClient(), fileName);

    validateARL(listeCodeRejet);

    closeStaticMock();
  }

  protected Declarant initDeclarant() {
    Pilotage pilotage = new Pilotage();
    pilotage.setServiceOuvert(true);
    pilotage.setCodeService("ALMV3");
    pilotage.setCritereRegroupementDetaille(getCritereSecondaireDetaille());
    pilotage.setDateSynchronisation(new Date());
    InfoPilotage infoPilotage = new InfoPilotage();
    infoPilotage.setNomClient("AG2R");
    infoPilotage.setNumEmetteur("98532001");
    infoPilotage.setNomPerimetre("AG2R");
    infoPilotage.setCodePerimetre("PERIM");
    infoPilotage.setNumExterneContratIndividuel(true);
    pilotage.setCaracteristique(infoPilotage);

    Declarant declarant = new Declarant();
    declarant.set_id("123");
    declarant.setPilotages(List.of(pilotage));
    declarant.setNumeroPrefectoral("123");
    return declarant;
  }

  @Captor private ArgumentCaptor<ArrayList<Rejection>> captor;

  private void mockMetaFile() {
    mockMetaService = mockStatic(MetaService.class);
  }

  private void closeStaticMock() {
    mockMetaService.close();
  }

  private void mockARL() {
    doNothing()
        .when(arlService)
        .buildARL(
            captor.capture(),
            Mockito.any(),
            Mockito.any(),
            Mockito.any(),
            Mockito.any(),
            Mockito.any(),
            Mockito.any());
  }

  private void validateARL(List<String> listeCodeRejet) {
    if (!listeCodeRejet.isEmpty()) {
      Assertions.assertEquals(listeCodeRejet.size(), captor.getValue().size());
      for (int i = 0; i < listeCodeRejet.size(); i++) {
        Assertions.assertEquals(listeCodeRejet.get(i), captor.getValue().get(i).getCodeRejet());
      }
    }
  }
}
