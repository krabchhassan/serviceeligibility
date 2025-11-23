package com.cegedim.next.serviceeligibility.mergealmerys.services;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.mergealmerys.model.CompteRenduMergeAlmerys;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
@Service
@RequiredArgsConstructor
public class Processor implements ApplicationRunner {

  private final OmuHelper omuHelper;
  private final TraceExtractionConsoService traceExtractionConsoService;
  private final FluxService fluxService;
  private final Tracer tracer;

  @Value("${JOB_SPAN_NAME:default_span}")
  private String spanName;

  @Value("${WORKDIR_INPUT_FOLDER}")
  private String input;

  @Value("${WORKDIR_OUTPUT_FOLDER}")
  private String output;

  @Value("${ACTIVATED:true}")
  private boolean activated;

  private static final Map<String, String> WRITTEN_FILES = new HashMap<>();
  private static final String PERIMETRE_SERVICE = "PERIMETRE_SERVICE";
  public static final int CODE_RETOUR_OK = 0;
  public static final int CODE_RETOUR_UNEXPECTED_EXCEPTION = 3;

  public void runProcess() {
    CompteRenduMergeAlmerys compteRendu = new CompteRenduMergeAlmerys();
    int processReturnCode;
    log.info("Merge activated {}", activated);
    try {
      processReturnCode = processAllAlmerysFiles(compteRendu);
    } catch (Exception e) {
      log.error(e.getLocalizedMessage(), e);
      processReturnCode = CODE_RETOUR_UNEXPECTED_EXCEPTION;
    }
    // In any case, produce a CREX
    finally {
      displayResult(compteRendu);
      omuHelper.produceOutputParameters(compteRendu.asMap());
    }
    System.exit(processReturnCode);
  }

  private int processAllAlmerysFiles(CompteRenduMergeAlmerys compteRendu) {
    // Try to create directory
    try {
      Files.createDirectories(Paths.get(input));
      Files.createDirectories(Paths.get(output));
    } catch (IOException e) {
      log.error("Failed to create directory {}", output, e);
      return CODE_RETOUR_UNEXPECTED_EXCEPTION;
    }

    try {
      List<String> mergeablePaths = getInputFilesPath();
      if (activated) {
        mergeFiles(mergeablePaths);
      } else {
        copyFiles(mergeablePaths);
      }

      // Supprime les fichiers d entree
      log.info("Deleting input files");
      deleteFiles(mergeablePaths);
      if (!activated) {
        mergeablePaths.clear();
      }

      compteRendu.addCreatedFileNames(WRITTEN_FILES.values());
      compteRendu.addMergedFileNames(mergeablePaths);

      return CODE_RETOUR_OK;
    } catch (IOException e) {
      log.error("Error while trying to access file", e);
    } catch (Exception e) {
      log.error("Unexpected exception", e);
    }

    log.info("Deleting created files");
    deleteFiles(WRITTEN_FILES.values());

    return CODE_RETOUR_UNEXPECTED_EXCEPTION;
  }

  private void copyFiles(List<String> mergeablePaths) throws IOException {
    for (String path : mergeablePaths) {
      File toCopyFile = new File(path);
      File destinationFIle = new File(output + toCopyFile.getName());
      FileCopyUtils.copy(toCopyFile, destinationFIle);

      // Stockage temporaire dans le cas d une exception
      WRITTEN_FILES.put(destinationFIle.getName(), destinationFIle.getAbsolutePath());
      log.debug(
          "Copied file {} to {}", toCopyFile.getAbsolutePath(), destinationFIle.getAbsolutePath());
    }

    // Si tout est bien copie alors on vide la liste pour ne pas les retrouver dans le CREX en
    // fichiers crees
    WRITTEN_FILES.clear();
  }

  private void mergeFiles(List<String> mergeablePaths) throws Exception {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = docFactory.newDocumentBuilder();

    List<String> copiedFiles = new ArrayList<>();
    Map<String, List<String>> mergedFiles = new HashMap<>();
    for (int i = 0; i < mergeablePaths.size(); i++) {
      File mergeableFile = new File(mergeablePaths.get(i));

      if (mergeableFile.length() <= 0) {
        // Si un fichier est vide alors on le copie tel quel
        File copyFile = new File(output + mergeableFile.getName());
        FileCopyUtils.copy(mergeableFile, copyFile);
        WRITTEN_FILES.put(mergeableFile.getName(), copyFile.getAbsolutePath());
        copiedFiles.add(copyFile.getAbsolutePath());
        continue;
      }

      Document xml = builder.parse(mergeableFile);
      Node entete = getFirstElement(xml, "ENTETE");
      String os = getFirstElement(entete, "NUM_OS_EMETTEUR").getTextContent();
      if (!WRITTEN_FILES.containsKey(os)) {
        String oldFileName = mergeableFile.getName();
        String newFile = output + getFileNameEmetteur(mergeableFile.getName(), os);
        createNewFile(newFile, entete, xml);
        WRITTEN_FILES.put(os, newFile);
        ArrayList<String> oldFileNames = new ArrayList<>();
        oldFileNames.add(oldFileName);
        mergedFiles.put(newFile, oldFileNames);
      } else {
        mergedFiles.get(WRITTEN_FILES.get(os)).add(mergeableFile.getName());
      }
      fillPerimetreServices(WRITTEN_FILES.get(os), xml);

      log.debug("{}/{} files merged", i + 1, mergeablePaths.size());
    }

    writeEndOfFiles(copiedFiles);

    updateTraceConso(mergedFiles, output);
    updateTraceFlux(mergedFiles, output);
  }

  private void updateTraceConso(Map<String, List<String>> mergedFiles, String output) {
    if (!mergedFiles.isEmpty()) {
      mergedFiles.forEach(
          (destination, sources) -> {
            if (!CollectionUtils.isEmpty(sources)) {
              String cleanDestination = destination.replace(output, "");
              traceExtractionConsoService.replaceFileName(sources, cleanDestination);
            }
          });
    }
  }

  private void updateTraceFlux(Map<String, List<String>> mergedFiles, String output) {
    if (!mergedFiles.isEmpty()) {
      mergedFiles.forEach(
          (destination, sources) -> {
            if (!CollectionUtils.isEmpty(sources)) {
              String cleanDestination = destination.replace(output, "");
              fluxService.updateNomFichierEmisFlux(sources, cleanDestination);
            }
          });
    }
  }

  private void deleteFiles(Collection<String> filesPath) {
    for (String file : filesPath) {
      File toDelete = new File(file);
      log.debug("{} deleted {}", file, toDelete.delete());
    }
  }

  private void writeEndOfFiles(List<String> copiedFiles) throws IOException {
    log.debug("Will write end of created files");
    String bottom =
        System.lineSeparator()
            + """
                    </OFFREUR_SERVICE>
                </FICHIER>""";

    for (String writedFile : WRITTEN_FILES.values()) {
      // Ecrit la fin des fichiers merges et pas ceux seulement copies
      if (!copiedFiles.contains(writedFile)) {
        writeInFile(writedFile, bottom);
      }
    }
  }

  private void fillPerimetreServices(String filePath, Document xml)
      throws IOException, TransformerException {
    NodeList perimetres = xml.getElementsByTagName(PERIMETRE_SERVICE);
    for (int i = 0; i < perimetres.getLength(); i++) {
      Node perimetre = perimetres.item(i);
      writeInFile(filePath, perimetre);
    }
  }

  private void createNewFile(String filePath, Node entete, Document xml) throws Exception {
    writeTopOfFile(filePath);
    // Entete
    writeInFile(filePath, entete);

    // Debut offreur
    Node offreur = getFirstElement(xml, "OFFREUR_SERVICE");
    writeInFile(filePath, System.lineSeparator() + "    <OFFREUR_SERVICE>");
    NodeList sousObjets = offreur.getChildNodes();
    for (int i = 0; i < sousObjets.getLength(); i++) {
      Node sousObjet = sousObjets.item(i);
      if (!PERIMETRE_SERVICE.equals(sousObjet.getNodeName())) {
        writeInFile(filePath, sousObjet);
      }
    }
  }

  private void displayResult(CompteRenduMergeAlmerys compteRendu) {
    log.info("=== Resultat de l'aggregation des fichiers Almerys ===");
    log.info("Nombre de fichiers créés : {}", compteRendu.getFichiersCrees().size());
    log.info("Nombre de fichiers aggrégés : {}", compteRendu.getFichiersAggreges().size());
  }

  @Override
  public void run(ApplicationArguments args) {
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      this.runProcess();
    } finally {
      newSpan.end();
    }
  }

  /** Recupere la liste de fichier a merger */
  private List<String> getInputFilesPath() throws IOException {
    List<String> fileSet = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(input))) {
      for (Path path : stream) {
        if (!Files.isDirectory(path) && path.toString().endsWith(".xml")) {
          fileSet.add(path.toString());
        }
      }
    }
    return fileSet;
  }

  private void writeInFile(String path, String content) throws IOException {
    try (FileWriter writer = new FileWriter(path, true)) {
      writer.write(content);
    }
  }

  private void writeInFile(String path, Node content) throws TransformerException, IOException {
    writeInFile(path, nodeToString(content));
  }

  private Node getFirstElement(Node node, String name) throws Exception {
    NodeList list;
    if (node instanceof Document) {
      list = ((Document) node).getElementsByTagName(name);
    } else {
      list = ((Element) node).getElementsByTagName(name);
    }
    if (list.getLength() > 0) {
      return list.item(0);
    }
    throw new Exception("Missing element " + name);
  }

  private void writeTopOfFile(String path) throws IOException {
    String top =
        """
                <?xml version="1.0" encoding="ISO-8859-1" standalone="no" ?>
                <FICHIER xmlns="http://www.almerys.com/NormeV3">
                """;
    writeInFile(path, top);
  }

  private String nodeToString(Node node) throws TransformerException {
    TransformerFactory transFactory = TransformerFactory.newInstance();
    Transformer transformer = transFactory.newTransformer();
    StringWriter buffer = new StringWriter();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.transform(new DOMSource(node), new StreamResult(buffer));
    return buffer.toString();
  }

  private static String getFileNameEmetteur(String current, String emetteur) {
    String[] splitted = current.split("_");
    splitted[3] = emetteur;
    return String.join("_", splitted);
  }
}
