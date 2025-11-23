package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.common.base.pefb.services.MetaService;
import com.cegedim.next.serviceeligibility.almerys608.model.Contrat;
import com.cegedim.next.serviceeligibility.almerys608.utils.Constants608;
import com.cegedim.next.serviceeligibility.almerys608.utils.EnumTempTable;
import com.cegedim.next.serviceeligibility.almerys608.utils.XmlUtils;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.MarshalException;
import jakarta.xml.bind.Marshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import javax.xml.transform.TransformerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class CreationFileService {

  private final RetrieveTmpObjectsService retrieveTmpObjectsService;
  private final String outputPath;

  private final Map<String, String> FILE_PILOTAGES = new HashMap<>();

  public String fillFile(
      String idDeclarant, Pilotage pilotage, Date date, Map<String, String> tempTables)
      throws Exception {
    String fileName;
    String filePath = null;
    String metaFilePath = null;
    try {
      Files.createDirectories(Paths.get(outputPath));
      filePath = getFilePath(idDeclarant, pilotage, date);
      metaFilePath = filePath + Constants608.META_EXTENSION;
      fileName = new File(filePath).getName();
      // CRITERE_REGROUPEMENT
      writeCentreGestions(filePath, tempTables.get(EnumTempTable.ADRESSE_GE.name()));
      writeEntreprises(filePath, tempTables.get(EnumTempTable.ENTREPRISE.name()));
      XmlUtils.writeInFile(filePath, "</CRITERE_REGROUPEMENT>");

      // CONTRAT
      boolean empty = writeContrat(filePath, tempTables);

      String end =
          """
                </PERIMETRE_SERVICE>
            </OFFREUR_SERVICE>
        </FICHIER>
        """;
      XmlUtils.writeInFile(filePath, end);
      MetaService.generateMetaData(fileName, outputPath, "batch-608");
      if (empty) {
        // Supprime le fichier si il n a aucun contrat
        deleteFile(filePath);
        deleteFile(metaFilePath);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      deleteFile(filePath);
      deleteFile(metaFilePath);
      throw e;
    }
    return fileName;
  }

  public String testJaxb(
      String idDeclarant, Pilotage pilotage, Date date, Map<String, String> tempTables)
      throws Exception {
    FICHIER fichier = new FICHIER();
    InfoEntete entete = new InfoEntete();
    entete.setNUMFICHIER(UtilService.resizeNumericField(1, 6));
    entete.setDATECREATION(new Date());
    entete.setVERSIONNORME("V3");
    entete.setNUMOSEMETTEUR(
        UtilService.resizeNumericField(
            StringUtils.isNotEmpty(pilotage.getCaracteristique().getNumEmetteur())
                ? Integer.valueOf(pilotage.getCaracteristique().getNumEmetteur())
                : null,
            10));

    fichier.setENTETE(entete);
    try {
      // CRITERE_REGROUPEMENT
      Collection<InfoCentreGestion> infoCentreGestions =
          getCentresGestion(tempTables.get(EnumTempTable.ADRESSE_GE.name()));
      Collection<InfoEntreprise> infoEntreprises =
          getEntreprise(tempTables.get(EnumTempTable.ENTREPRISE.name()));
      InfoOS infoOS = new InfoOS();
      List<InfoPerimetre> infoPerimetreList = new ArrayList<>();
      InfoPerimetre infoPerimetre = new InfoPerimetre();
      infoPerimetre.setCODEPERIMETRE(pilotage.getCaracteristique().getCodePerimetre());
      infoPerimetre.setLIBELLEPERIMETRE(pilotage.getCaracteristique().getNomPerimetre());
      InfoCritere infoCritere = new InfoCritere();
      infoCritere.getCENTREGESTIONS().addAll(infoCentreGestions);
      infoCritere.getENTREPRISES().addAll(infoEntreprises);
      infoPerimetre.setCRITEREREGROUPEMENT(infoCritere);
      infoPerimetre.getCONTRATS().addAll(getInfoContrat(tempTables));
      infoPerimetreList.add(infoPerimetre);
      infoOS.getPERIMETRESERVICES().addAll(infoPerimetreList);
      infoOS.setNUMOS(pilotage.getCaracteristique().getNumEmetteur());
      infoOS.setLIBELLEOS(pilotage.getCaracteristique().getNomClient());
      fichier.setOFFREURSERVICE(infoOS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }

    Files.createDirectories(Paths.get(outputPath));
    String filePath = getFilePath(idDeclarant, pilotage, date);
    String fileName = new File(filePath).getName();
    OutputStream os = new FileOutputStream(filePath);
    os.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\" ?>\n".getBytes());

    JAXBContext jaxbContext =
        JAXBContext.newInstance("com.cegedim.next.serviceeligibility.batch.almerys.normev3");
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(fichier, os);
    os.close();

    return fileName;
  }

  private void deleteFile(String filePath) throws IOException {
    if (filePath != null) {
      Path path = Paths.get(filePath);
      Files.deleteIfExists(path);
    }
  }

  private boolean writeContrat(String filePath, Map<String, String> tempTables)
      throws ExceptionTechnique {
    AtomicBoolean empty = new AtomicBoolean(true);
    Stream<Contrat> contrats =
        retrieveTmpObjectsService.getAll(
            tempTables.get(EnumTempTable.CONTRAT.name()), Contrat.class);
    contrats.forEach(
        contrat -> {
          if (!retrieveTmpObjectsService.isRejected(
              contrat.getNumeroContrat(), tempTables.get(EnumTempTable.REJET.name()))) {
            InfoContrat infoContrat = new InfoContrat();
            infoContrat.setNUMCONTRAT(
                UtilService.resizeField(contrat.getNumeroContratFichier(), 30));
            infoContrat.setNUMCONTRATCOLLECTIF(
                UtilService.resizeField(contrat.getNumeroContratCollectif(), 30));
            infoContrat.setDATEIMMAT(contrat.getDateImmatriculation());
            infoContrat.setETATCONTRAT(CodeContrat.fromValue(contrat.getEtatContrat()));
            infoContrat.setREFENTREPRISE(UtilService.resizeField(contrat.getRefEntreprise(), 15));
            infoContrat
                .getREFADHCONTRATS()
                .add(UtilService.resizeField(contrat.getRefAdhContrat(), 20));

            String refCg =
                retrieveTmpObjectsService.getRefCG(
                    contrat.getRefInterneCG(), tempTables.get(EnumTempTable.ADRESSE_GE.name()));
            infoContrat.setREFINTERNECG(UtilService.resizeField(refCg, 15));

            String refSite =
                retrieveTmpObjectsService.getRefSite(
                    contrat.getRefEntreprise(),
                    contrat.getRefSite(),
                    tempTables.get(EnumTempTable.ENTREPRISE.name()));
            infoContrat.setREFSITE(UtilService.resizeField(refSite, 15));

            List<InfoContrat.RATTACHEMENT> rattachements =
                retrieveTmpObjectsService.getRattachements(
                    contrat.getNumeroContrat(), tempTables.get(EnumTempTable.RATTACHEMENT.name()));
            infoContrat.getRATTACHEMENTS().addAll(rattachements);

            // MEMBRE_CONTRAT
            List<InfoMembreContrat> membres =
                retrieveTmpObjectsService.getMembres(
                    contrat.getNumeroContrat(),
                    tempTables.get(EnumTempTable.MEMBRE_CONTRAT.name()),
                    tempTables.get(EnumTempTable.ADRESSE_AD.name()));
            infoContrat.getMEMBRECONTRATS().addAll(checkMembreContrat(membres));

            // SERVICE_TP
            InfoContrat.SERVICE service = getService(tempTables, contrat.getNumeroContrat());
            infoContrat.setSERVICE(service);

            updateRefInterneOsBenef(service.getSERVICETPPECS());

            updateTypeBenef(infoContrat, service.getSERVICETPPECS());
            log.debug("Avant écriture dans le fichier {}", infoContrat.getNUMCONTRAT());
            try {
              XmlUtils.writeInFile(filePath, infoContrat, "CONTRAT");
            } catch (MarshalException e) {
              log.error(e.getMessage(), e);
            } catch (JAXBException | IOException | TransformerException e) {
              log.error(e.getMessage(), e);
              throw new ExceptionTechnique(e.getMessage(), e);
            }
            empty.set(false);
          }
        });

    return empty.get();
  }

  private List<InfoContrat> getInfoContrat(Map<String, String> tempTables) {
    List<InfoContrat> infoContrats = new ArrayList<>();
    Stream<Contrat> contrats =
        retrieveTmpObjectsService.getAll(
            tempTables.get(EnumTempTable.CONTRAT.name()), Contrat.class);
    contrats.forEach(
        contrat -> {
          if (!retrieveTmpObjectsService.isRejected(
              contrat.getNumeroContrat(), tempTables.get(EnumTempTable.REJET.name()))) {
            InfoContrat infoContrat = new InfoContrat();
            infoContrat.setNUMCONTRAT(
                UtilService.resizeField(contrat.getNumeroContratFichier(), 30));
            infoContrat.setNUMCONTRATCOLLECTIF(
                UtilService.resizeField(contrat.getNumeroContratCollectif(), 30));
            infoContrat.setDATEIMMAT(contrat.getDateImmatriculation());
            infoContrat.setETATCONTRAT(CodeContrat.fromValue(contrat.getEtatContrat()));
            infoContrat.setREFENTREPRISE(UtilService.resizeField(contrat.getRefEntreprise(), 30));
            infoContrat
                .getREFADHCONTRATS()
                .add(UtilService.resizeField(contrat.getRefAdhContrat(), 20));

            String refCg =
                retrieveTmpObjectsService.getRefCG(
                    contrat.getRefInterneCG(), tempTables.get(EnumTempTable.ADRESSE_GE.name()));
            infoContrat.setREFINTERNECG(UtilService.resizeField(refCg, 15));

            String refSite =
                retrieveTmpObjectsService.getRefSite(
                    contrat.getRefEntreprise(),
                    contrat.getRefSite(),
                    tempTables.get(EnumTempTable.ENTREPRISE.name()));
            infoContrat.setREFSITE(UtilService.resizeField(refSite, 15));

            List<InfoContrat.RATTACHEMENT> rattachements =
                retrieveTmpObjectsService.getRattachements(
                    contrat.getNumeroContrat(), tempTables.get(EnumTempTable.RATTACHEMENT.name()));
            infoContrat.getRATTACHEMENTS().addAll(rattachements);

            // MEMBRE_CONTRAT
            List<InfoMembreContrat> membres =
                retrieveTmpObjectsService.getMembres(
                    contrat.getNumeroContrat(),
                    tempTables.get(EnumTempTable.MEMBRE_CONTRAT.name()),
                    tempTables.get(EnumTempTable.ADRESSE_AD.name()));
            infoContrat.getMEMBRECONTRATS().addAll(checkMembreContrat(membres));

            // SERVICE_TP
            InfoContrat.SERVICE service = getService(tempTables, contrat.getNumeroContrat());
            infoContrat.setSERVICE(service);

            updateRefInterneOsBenef(service.getSERVICETPPECS());

            updateTypeBenef(infoContrat, service.getSERVICETPPECS());
            infoContrats.add(infoContrat);
          }
        });
    return infoContrats;
  }

  private InfoContrat.SERVICE getService(Map<String, String> tempTables, String numContrat) {
    InfoContrat.SERVICE service = new InfoContrat.SERVICE();
    for (InfoBenef benef :
        retrieveTmpObjectsService.getBenef(
            tempTables.get(EnumTempTable.BENEFICIAIRE.name()), numContrat)) {
      List<InfoProduit> produits =
          retrieveTmpObjectsService.getProduits(
              tempTables.get(EnumTempTable.PRODUIT.name()),
              tempTables.get(EnumTempTable.CARENCE.name()),
              numContrat,
              benef.getREFINTERNEOS());
      if (produits.isEmpty()) {
        continue;
      }
      benef.getPRODUITS().addAll(produits);

      InfoServiceTP infoServiceTP =
          retrieveTmpObjectsService.getInfoServiceTP(
              tempTables.get(EnumTempTable.SERVICE_TP.name()), numContrat, benef.getREFINTERNEOS());

      InfoTPPEC infoTPPEC = new InfoTPPEC();
      benef.setREFINTERNEOS(UtilService.resizeField(benef.getREFINTERNEOS(), 30));
      infoTPPEC.setBENEFICIAIRE(benef);
      infoTPPEC.setSERVICETP(infoServiceTP);
      service.getSERVICETPPECS().add(infoTPPEC);
    }

    return service;
  }

  private List<InfoMembreContrat> checkMembreContrat(List<InfoMembreContrat> membreContrats) {
    int subscribersNb = countSubscribers(membreContrats);
    if (subscribersNb == 1) {
      for (InfoMembreContrat infoMembreContrat : membreContrats) {
        infoMembreContrat
            .getINDIVIDU()
            .setREFINTERNEOS(
                UtilService.resizeField(infoMembreContrat.getINDIVIDU().getREFINTERNEOS(), 30));
      }
      return membreContrats;
    }

    List<InfoMembreContrat> checkedMembreContrat = new ArrayList<>();
    for (InfoMembreContrat infoMembreContrat : membreContrats) {
      if (Boolean.TRUE.equals(infoMembreContrat.isSOUSCRIPTEUR())
          && StringUtils.isNotBlank(infoMembreContrat.getDATERADIATION())) {
        log.debug("Inversion de liens ");
        infoMembreContrat.setSOUSCRIPTEUR(false);
        infoMembreContrat.setPOSITION("02");
      }
      infoMembreContrat
          .getINDIVIDU()
          .setREFINTERNEOS(
              UtilService.resizeField(infoMembreContrat.getINDIVIDU().getREFINTERNEOS(), 30));
      checkedMembreContrat.add(infoMembreContrat);
    }
    return checkedMembreContrat;
  }

  private int countSubscribers(List<InfoMembreContrat> membreContrats) {
    int count = 0;
    for (InfoMembreContrat infoMembreContrat : membreContrats) {
      if (Boolean.TRUE.equals(infoMembreContrat.isSOUSCRIPTEUR())) {
        count++;
      }
    }
    return count;
  }

  private void updateTypeBenef(InfoContrat infoContrat, List<InfoTPPEC> infos) {
    List<InfoMembreContrat> membreContrats = infoContrat.getMEMBRECONTRATS();
    for (InfoMembreContrat membre_contrat : membreContrats) {
      if (!Boolean.TRUE.equals(membre_contrat.isSOUSCRIPTEUR())
          && membre_contrat.getINDIVIDU() != null) {
        String ref = membre_contrat.getINDIVIDU().getREFINTERNEOS();
        for (InfoTPPEC info : infos) {
          if (info.getBENEFICIAIRE().getREFINTERNEOS().equals(ref)
              && CodeBenef.AS.equals(info.getBENEFICIAIRE().getTYPEBENEF())) {
            info.getBENEFICIAIRE().setTYPEBENEF(CodeBenef.CJ);
            break;
          }
        }
      }
    }
  }

  private void updateRefInterneOsBenef(List<InfoTPPEC> infos) {
    for (InfoTPPEC info : infos) {
      info.getBENEFICIAIRE()
          .setREFINTERNEOS(UtilService.resizeField(info.getBENEFICIAIRE().getREFINTERNEOS(), 30));
    }
  }

  private void writeCentreGestions(String filePath, String collection) throws ExceptionTechnique {
    Stream<InfoCentreGestion> allInfoCentreGestion =
        retrieveTmpObjectsService.getAllInfoCentreGestion(collection);
    allInfoCentreGestion.forEach(
        infoCentreGestion -> {
          try {
            XmlUtils.writeInFile(filePath, infoCentreGestion, "CENTRE_GESTION");
          } catch (JAXBException | IOException | TransformerException e) {
            log.error(e.getMessage(), e);
            throw new ExceptionTechnique(e.getMessage(), e);
          }
        });
  }

  private Collection<InfoCentreGestion> getCentresGestion(String collection) {
    return retrieveTmpObjectsService.getInfoCentreGestion(collection);
  }

  private Collection<InfoEntreprise> getEntreprise(String collection) {
    return retrieveTmpObjectsService.getInfoEntreprise(collection);
  }

  private void writeEntreprises(String filePath, String collection) throws ExceptionTechnique {
    Stream<InfoEntreprise> allInfoEntreprise =
        retrieveTmpObjectsService.getAllInfoEntreprise(collection);
    allInfoEntreprise.forEach(
        infoEntreprise -> {
          try {
            XmlUtils.writeInFile(filePath, infoEntreprise, "ENTREPRISE");
          } catch (JAXBException | IOException | TransformerException e) {
            log.error(e.getMessage(), e);
            throw new ExceptionTechnique(e.getMessage(), e);
          }
        });
  }

  private String getFilePath(String idDeclarant, Pilotage pilotage, Date date)
      throws IOException, JAXBException, TransformerException {
    String key =
        pilotage.getCaracteristique().getNumEmetteur()
            + pilotage.getCritereRegroupement()
            + pilotage.getCritereRegroupementDetaille();
    if (!FILE_PILOTAGES.containsKey(key)) {
      int nbFile = FILE_PILOTAGES.size() + 1; // TODO voir si on ne pas avoir 1 tout le temps.
      SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.BATCH_FILE_NAME_FORMAT_DATE);
      String fileName =
          new StringJoiner("_")
              .add(Constants.NUMERO_BATCH_608)
              .add("Almerys")
              .add(pilotage.getCaracteristique().getCodeClient())
              .add(idDeclarant)
              .add(formatter.format(date))
              .add(nbFile + "")
              .toString();
      String path = outputPath + fileName + XmlUtils.XML;

      XmlUtils.writeInFile(path, Constants608.TOP_OF_FILE);

      InfoEntete entete = new InfoEntete();
      entete.setNUMFICHIER(UtilService.resizeNumericField(nbFile, 6));
      entete.setDATECREATION(new Date());
      entete.setVERSIONNORME("V3");
      entete.setNUMOSEMETTEUR(
          UtilService.resizeNumericField(
              StringUtils.isNotEmpty(pilotage.getCaracteristique().getNumEmetteur())
                  ? Integer.valueOf(pilotage.getCaracteristique().getNumEmetteur())
                  : null,
              10));
      XmlUtils.writeInFile(path, entete, "ENTETE");
      String debOff =
          String.format(
              """
                        <OFFREUR_SERVICE>
                        <LIBELLE_OS>%s</LIBELLE_OS>
                        <NUM_OS>%s</NUM_OS>
                        <PERIMETRE_SERVICE>
                            <CODE_PERIMETRE>%s</CODE_PERIMETRE>
                            <LIBELLE_PERIMETRE>%s</LIBELLE_PERIMETRE>
                            <CRITERE_REGROUPEMENT>
                """,
              pilotage.getCaracteristique().getNomClient(),
              pilotage.getCaracteristique().getNumEmetteur(),
              pilotage.getCaracteristique().getCodePerimetre(),
              pilotage.getCaracteristique().getNomPerimetre());
      XmlUtils.writeInFile(path, debOff);
      FILE_PILOTAGES.put(key, path);
      log.debug("{} created", path);
    }

    return FILE_PILOTAGES.get(key);
  }
}
