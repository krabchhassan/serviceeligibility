package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.dao.ProduitsAlmerysExclusDao;
import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.almerys608.utils.Constants608;
import com.cegedim.next.serviceeligibility.almerys608.utils.EnumTempTable;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscodageDao;
import com.cegedim.next.serviceeligibility.core.dao.HistoriqueExecutionsDao;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.Datas;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.services.DeclarationsConsolideesAlmerysService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.AlmerysJobsService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.*;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;

@RequiredArgsConstructor
public class UtilService {
  private final AlmerysJobsService almerysJobsService;
  private final DeclarationsConsolideesAlmerysService declarationsConsolideesAlmerysService;
  private final HistoriqueExecutionsDao historiqueExecutionsDao;
  private final TranscodageDao transcodageDao;
  private final ProduitsAlmerysExclusDao produitsAlmerysExclusDao;

  public Map<String, String> initTempCollectionName(Declarant declarant, Pilotage pilotage) {
    Map<String, String> tempTables = new HashMap<>();
    for (EnumTempTable tempTable : EnumTempTable.values()) {
      tempTables.put(
          tempTable.name(),
          getTempCollectionName(
              tempTable.getTableName(),
              declarant.get_id(),
              pilotage.getCritereRegroupementDetaille()));
    }
    return tempTables;
  }

  public String getTempCollectionName(String collection, String numAmc, String csd) {
    return "ALMV3_" + collection + "_" + numAmc + "_" + csd;
  }

  public Rejet getRejet(TmpObject2 tmpObject2, String numContrat, String codeRejet, String error) {
    Rejet rejet = new Rejet();

    rejet.setNoPersonne(tmpObject2.getBeneficiaire().getNumeroPersonne());
    rejet.setNir(tmpObject2.getBeneficiaire().getNirOd1());
    rejet.setDateNaissance(
        DateUtils.formatDate(
            DateUtils.parseLocalDate(
                tmpObject2.getBeneficiaire().getDateNaissance(), DateUtils.YYYYMMDD),
            DateUtils.FORMATTER)); // yyyy-MM-dd
    rejet.setRangNaissance(tmpObject2.getBeneficiaire().getRangNaissance());
    rejet.setNom(tmpObject2.getBeneficiaire().getAffiliation().getNom());
    rejet.setPrenom(tmpObject2.getBeneficiaire().getAffiliation().getPrenom());
    rejet.setNumContrat(numContrat);
    rejet.setGestionnaireContrat(tmpObject2.getContrat().getGestionnaire());
    rejet.setGroupeAssures(tmpObject2.getContrat().getGroupeAssures());
    rejet.setDroits(tmpObject2.getCodeEtat());
    rejet.setMvt(tmpObject2.getDomaineDroit().getPeriodeDroit().getModeObtention());
    rejet.setDateDeclaration(
        DateUtils.formatDate(
            tmpObject2.getEffetDebut(), DateUtils.YYYY_MM_DD_HH_MM_SS)); // yyyy-MM-dd HH:mm:ss

    rejet.setNoDeclaration(tmpObject2.getIdDeclarations().getFirst());
    rejet.setIdDeclarationConsolidee(tmpObject2.get_id());
    rejet.setCritereSecondaire(tmpObject2.getContrat().getCritereSecondaire());
    rejet.setCritereSecondaireDetaille(tmpObject2.getContrat().getCritereSecondaireDetaille());
    rejet.setOrigineDroits(tmpObject2.getDomaineDroit().getOrigineDroits());

    rejet.setCodeRejetTraces(codeRejet);
    rejet.setError(error);

    rejet.setRefInterneOS(tmpObject2.getBeneficiaire().getNumeroPersonne());
    rejet.setEffetDebut(tmpObject2.getEffetDebut());

    return rejet;
  }

  public void createTmpCollection1(
      Declarant declarant,
      Pilotage pilotage,
      List<String> critSecondaireDetailleToExclude,
      Date dateDerniereExecution,
      String nameTmpCollection1) {
    declarationsConsolideesAlmerysService.createTmpCollection(
        Aggregation608Util.aggregationCollectionTmp1(
            declarant,
            pilotage,
            critSecondaireDetailleToExclude,
            dateDerniereExecution,
            nameTmpCollection1));
  }

  public void createTmpCollection2(
      Declarant declarant, String nameTmpCollection1, String nameTmpCollection2) {
    declarationsConsolideesAlmerysService.createIndexOnTmp2(nameTmpCollection2);
    declarationsConsolideesAlmerysService.createTmpCollection(
        Aggregation608Util.aggregationCollectionTmp2(
            declarant, nameTmpCollection1, nameTmpCollection2));
  }

  public void createIndexOnCollection(Map<String, String> tempTables) {
    for (Map.Entry<String, String> entry : tempTables.entrySet()) {
      if (entry.getValue().contains(EnumTempTable.SERVICE_TP.getTableName())
          || entry.getValue().contains(EnumTempTable.MEMBRE_CONTRAT.getTableName())
          || entry.getValue().contains(EnumTempTable.PRODUIT.getTableName())
          || entry.getValue().contains(EnumTempTable.BENEFICIAIRE.getTableName())
          || entry.getValue().contains(EnumTempTable.CARENCE.getTableName())) {
        declarationsConsolideesAlmerysService.createIndexContratRefInterneOsOnTmpTable(
            entry.getValue());
      }
      if (entry.getValue().contains(EnumTempTable.REJET.getTableName())
          || entry.getValue().contains(EnumTempTable.RATTACHEMENT.getTableName())) {
        declarationsConsolideesAlmerysService.createIndexContratOnTmpTable(entry.getValue());
      }
    }
  }

  public Integer countDeclarationConsolideesAlmerys(
      Declarant declarant,
      Pilotage pilotage,
      Date dateDerniereExecution,
      List<String> critSecondaireDetailleToExclude) {
    return declarationsConsolideesAlmerysService.countDeclarationConsolideesAlmerys(
        declarant, pilotage, dateDerniereExecution, critSecondaireDetailleToExclude);
  }

  public Integer countAllDeclarationConsolideesAlmerys() {
    return declarationsConsolideesAlmerysService.countAllDeclarationConsolideesAlmerys();
  }

  public HistoriqueExecution608 getLastHistoriquePilotage(
      Declarant declarant, Pilotage pilotage, String batchNumber) {
    Criteria criteria =
        almerysJobsService.getLastHistoriquePilotageCriteria(declarant, pilotage, batchNumber);
    return historiqueExecutionsDao.getLastExecution(criteria, HistoriqueExecution608.class);
  }

  public Stream<TmpObject2> getAllForSouscripteur(String nameTmpCollection2) {
    return declarationsConsolideesAlmerysService.getAllForSouscripteur(nameTmpCollection2);
  }

  public Stream<TmpObject2> getAll(String nameTmpCollection2) {
    return declarationsConsolideesAlmerysService.getAll(nameTmpCollection2);
  }

  public HistoriqueExecution608 createHistoriquePilotage(
      Declarant declarant, Pilotage pilotage, Date dateExec, String batchNumber) {
    HistoriqueExecution608 histo = new HistoriqueExecution608();
    histo.setBatch(batchNumber);
    histo.setIdDeclarant(declarant.getNumeroPrefectoral());
    histo.setCodeService(pilotage.getCodeService());
    histo.setTypeConventionnement(pilotage.getTypeConventionnement());
    histo.setCritereSecondaire(pilotage.getCritereRegroupement());
    histo.setCritereSecondaireDetaille(pilotage.getCritereRegroupementDetaille());
    histo.setDateExecution(dateExec);
    histo.setDatas(new Datas());
    return histo;
  }

  public String getNameTmpCollection(String couloirClient, int numero) {
    String nameTmpCollection = Constants608.NAME_TMP_COLLECTION + numero;
    if (StringUtils.isNotBlank(couloirClient)) {
      nameTmpCollection += Constants608.UNDERSCORE + couloirClient;
    }
    return nameTmpCollection;
  }

  @Cacheable(value = "transcodage608", key = "{#codeService,#codeObjetTransco}")
  public List<Transcodage> getTranscodage(String codeService, String codeObjetTransco) {
    return transcodageDao.findByCodeObjetTransco(codeService, codeObjetTransco);
  }

  public List<ProduitsAlmerysExclus> getProduitsAlmerysExclus(
      String idDeclarant, String critereRegroupementDetaille) {
    return produitsAlmerysExclusDao.findByKey(idDeclarant, critereRegroupementDetaille);
  }

  public Date getDateDernierExecution(Pilotage pilotage, HistoriqueExecution608 lastPilotageHisto) {
    Date lastExecutionDate =
        lastPilotageHisto != null ? lastPilotageHisto.getDateExecution() : null;
    return pilotage.getDateSynchronisation() != null
        ? pilotage.getDateSynchronisation()
        : lastExecutionDate;
  }

  public static String mapNumContratIndividuel(TmpObject2 tmpObject2, Pilotage pilotage) {
    boolean isHTP = isHTP(tmpObject2);
    Contrat contrat = tmpObject2.getContrat();
    String numeroContrat = contrat.getNumero();
    boolean externe =
        Boolean.TRUE.equals(pilotage.getCaracteristique().getNumExterneContratIndividuel());
    if (((isHTP && externe) || (!isHTP && !externe))
        && contrat.getNumeroExterneContratIndividuel() != null) {
      numeroContrat = contrat.getNumeroExterneContratIndividuel();
    }
    return numeroContrat;
  }

  public static String mapNumContratCollectif(TmpObject2 tmpObject2, Pilotage pilotage) {
    boolean isHTP = isHTP(tmpObject2);
    Contrat contrat = tmpObject2.getContrat();
    String refEntreprise = contrat.getNumeroContratCollectif();
    boolean externe =
        Boolean.TRUE.equals(pilotage.getCaracteristique().getNumExterneContratCollectif());
    if (((isHTP && externe) || (!isHTP && !externe))
        && contrat.getNumeroExterneContratCollectif() != null) {
      refEntreprise = contrat.getNumeroExterneContratCollectif();
    }
    return refEntreprise;
  }

  public static boolean isHTP(TmpObject2 tmpObject2) {
    return Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration());
  }

  public static String mapRefNumContrat(TmpObject2 tmpObject2) {
    return tmpObject2.getContrat().getNumero() + "-" + tmpObject2.getContrat().getNumeroAdherent();
  }

  public static String resizeField(String field, int maxLength) {
    if (field == null) {
      return null;
    }
    if (field.length() > maxLength) {
      return field.substring(0, maxLength);
    }

    return field;
  }

  public static Integer resizeNumericField(Integer field, int maxLength) {
    if (field == null) {
      return null;
    }
    if (maxLength >= 10) {
      return field;
    }
    int taille = Integer.parseInt("9".repeat(maxLength));
    if (field > taille) {
      return Integer.valueOf(field.toString().substring(field.toString().length() - maxLength));
    }
    return field;
  }

  public static <T extends Adresse> void fillAdressLine(
      T adresse, AdresseAlmerys adresseBenefAlmerys, int maxLength) {
    adresse.setLigne1(UtilService.resizeField(adresseBenefAlmerys.getLigne1(), maxLength));
    adresse.setLigne2(UtilService.resizeField(adresseBenefAlmerys.getLigne2(), maxLength));
    adresse.setLigne3(UtilService.resizeField(adresseBenefAlmerys.getLigne3(), maxLength));
    adresse.setLigne4(UtilService.resizeField(adresseBenefAlmerys.getLigne4(), maxLength));
    adresse.setLigne5(UtilService.resizeField(adresseBenefAlmerys.getLigne5(), maxLength));
    adresse.setLigne6(UtilService.resizeField(adresseBenefAlmerys.getLigne6(), maxLength));
    adresse.setLigne7(UtilService.resizeField(adresseBenefAlmerys.getLigne7(), maxLength));
    adresse.setEmail(adresseBenefAlmerys.getEmail());
    adresse.setCodePostal(adresseBenefAlmerys.getCodePostal());
    adresse.setTelephone(adresseBenefAlmerys.getTelephone());
  }

  public static <T extends Adresse> void fillAdressLine(
      T adresse, AdresseAlmerys adresseBenefAlmerys) {
    fillAdressLine(adresse, adresseBenefAlmerys, 100);
  }
}
