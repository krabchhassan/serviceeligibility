package com.cegedim.next.serviceeligibility.almerysacl.batch.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class SftpServiceTest {

  SftpService sftp;

  public void init(ByteArrayOutputStream out) {
    sftp = new SftpService();
    sftp.setUnitTest(true);
    ReflectionTestUtils.setField(sftp, "inputFolder", "src/test/resources/");
    ReflectionTestUtils.setField(sftp, "issuer", "98549603");
    OutputStreamWriter writer = new OutputStreamWriter(out);
    sftp.setUnitTestWriter(writer);
  }

  @Test
  void should_process_file() throws IOException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    File file = new File("src/test/resources/almer_short.xml");
    File expectedFile = new File("src/test/resources/almer_short_expected.xml");
    Files.readString(file.toPath(), StandardCharsets.US_ASCII);
    init(stream);

    List<String> files = this.sftp.listFiles();
    this.sftp.processFolder(files);
    String result = stream.toString();
    String[] resultLines = result.split(System.lineSeparator());
    String[] expectedLines =
        Files.readString(expectedFile.toPath(), StandardCharsets.US_ASCII).split("\n");
    for (int i = 0; i < resultLines.length; i++) {
      Assertions.assertEquals(resultLines[i], expectedLines[i]);
    }
  }

  @Test
  void should_process_with_wrong_order_file() throws IOException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    File file = new File("src/test/resources/almer_short_wrong_order.xml");
    File expectedFile = new File("src/test/resources/almer_short_expected.xml");
    Files.readString(file.toPath(), StandardCharsets.US_ASCII);
    init(stream);

    List<String> files = this.sftp.listFiles();
    this.sftp.processFolder(files);
    String result = stream.toString();
    String[] resultLines = result.split(System.lineSeparator());
    String[] expectedLines =
        Files.readString(expectedFile.toPath(), StandardCharsets.US_ASCII).split("\n");
    for (int i = 0; i < resultLines.length; i++) {
      Assertions.assertEquals(resultLines[i], expectedLines[i]);
    }
  }

  @Test
  void should_return_product() throws IOException {
    String test = // "<PRODUIT>\n" +
        "<ORDRE>1</ORDRE>\n"
            + "<REFERENCE_PRODUIT>REFVIA008ITELIS</REFERENCE_PRODUIT>\n"
            + "<DATE_ENTREE_PRODUIT>2018-12-01</DATE_ENTREE_PRODUIT>\n"
            + "<DATE_SORTIE_PRODUIT>2019-11-30</DATE_SORTIE_PRODUIT>\n"
            + "</PRODUIT>\n"
            + "<PRODUIT>\n"
            + "<ORDRE>2</ORDRE>\n"
            + "<REFERENCE_PRODUIT>REFVIAPH2</REFERENCE_PRODUIT>\n"
            + "<DATE_ENTREE_PRODUIT>2019-12-31</DATE_ENTREE_PRODUIT>\n"
            + "<DATE_SORTIE_PRODUIT>2020-12-30</DATE_SORTIE_PRODUIT>\n"
            + "</PRODUIT>\n"
            + "NEWLINE_OF_FLUX";
    Reader inputString = new StringReader(test);
    BufferedReader reader = new BufferedReader(inputString);
    sftp = new SftpService();
    sftp.setUnitTest(true);
    String s = sftp.getLastProduct("<PRODUIT>\n", reader, 0);
    Assertions.assertEquals(2, StringUtils.countMatches(s, "</PRODUIT>"));
  }

  @Test
  void should_return_one_product() throws IOException {
    String test = // "<PRODUIT>\n" +
        "<ORDRE>1</ORDRE>\n"
            + "<REFERENCE_PRODUIT>REFVIA008ITELIS</REFERENCE_PRODUIT>\n"
            + "<DATE_ENTREE_PRODUIT>2018-12-01</DATE_ENTREE_PRODUIT>\n"
            + "<DATE_SORTIE_PRODUIT>2019-11-30</DATE_SORTIE_PRODUIT>\n"
            + "</PRODUIT>\n"
            + "<PRODUIT>\n"
            + "<ORDRE>2</ORDRE>\n"
            + "<REFERENCE_PRODUIT>REFVIAPH2</REFERENCE_PRODUIT>\n"
            + "<DATE_ENTREE_PRODUIT>2018-12-31</DATE_ENTREE_PRODUIT>\n"
            + "<DATE_SORTIE_PRODUIT>2019-12-30</DATE_SORTIE_PRODUIT>\n"
            + "</PRODUIT>\n"
            + "NEWLINE_OF_FLUX";
    Reader inputString = new StringReader(test);
    BufferedReader reader = new BufferedReader(inputString);
    sftp = new SftpService();
    sftp.setUnitTest(true);
    String s = sftp.getLastProduct("<PRODUIT>\n", reader, 0);
    Assertions.assertEquals(1, StringUtils.countMatches(s, "</PRODUIT>"));
  }

  @Test
  void should_return_two_product() throws IOException {
    String test = // "<PRODUIT>\n" +
        "<ORDRE>1</ORDRE>\n"
            + "<REFERENCE_PRODUIT>REFVIA008ITELIS</REFERENCE_PRODUIT>\n"
            + "<DATE_ENTREE_PRODUIT>2018-12-01</DATE_ENTREE_PRODUIT>\n"
            + "<DATE_SORTIE_PRODUIT>2019-11-30</DATE_SORTIE_PRODUIT>\n"
            + "</PRODUIT>\n"
            + "<PRODUIT>\n"
            + "<ORDRE>1</ORDRE>\n"
            + "<REFERENCE_PRODUIT>REFVIAPH2</REFERENCE_PRODUIT>\n"
            + "<DATE_ENTREE_PRODUIT>2018-12-31</DATE_ENTREE_PRODUIT>\n"
            + "<DATE_SORTIE_PRODUIT>2019-12-30</DATE_SORTIE_PRODUIT>\n"
            + "</PRODUIT>\n"
            + "NEWLINE_OF_FLUX";
    Reader inputString = new StringReader(test);
    BufferedReader reader = new BufferedReader(inputString);
    sftp = new SftpService();
    sftp.setUnitTest(true);
    String s = sftp.getLastProduct("<PRODUIT>\n", reader, 0);
    Assertions.assertEquals(2, StringUtils.countMatches(s, "</PRODUIT>"));
  }

  @Test
  void should_ignore_empty_file() throws IOException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    File file = new File("src/test/resources/empty_file.xml");
    Files.readString(file.toPath(), StandardCharsets.US_ASCII);
    init(stream);
    boolean foundIssue =
        this.sftp.processFile("src/test/resources/empty_file.xml", "empty_file.xml");
    Assertions.assertFalse(foundIssue);
  }
}
