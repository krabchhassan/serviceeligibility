package com.cegedim.next.serviceeligibility.core.cucumber.utils;

import static com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService.OBJECT_MAPPER;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtils {
  private static final Path DEFAULT_TEST_RESOURCES_PATH =
      Paths.get("build").resolve("resources").resolve("serviceTest");
  public static final Path DEFAULT_TEST_RESOURCES_REQUEST_PATH =
      DEFAULT_TEST_RESOURCES_PATH.resolve("request");
  public static final Path DEFAULT_TEST_RESOURCES_RESULT_PATH =
      DEFAULT_TEST_RESOURCES_PATH.resolve("response");

  public static final String JSON = ".json";

  public static <T> T readRequestFile(
      String filePath, Class<T> clazz, TransformUtils.Parser... parsers) {
    return readFile(DEFAULT_TEST_RESOURCES_REQUEST_PATH.resolve(filePath), clazz, parsers);
  }

  public static <T> T readResultFile(
      String filePath, Class<T> clazz, TransformUtils.Parser... parsers) {
    return readFile(DEFAULT_TEST_RESOURCES_RESULT_PATH.resolve(filePath), clazz, parsers);
  }

  public static <T> T readFile(Path filePath, Class<T> clazz, TransformUtils.Parser... parsers) {
    try {
      String json = Files.readString(filePath);
      json = TransformUtils.DEFAULT.parse(json);
      for (TransformUtils.Parser parser : parsers) {
        json = parser.parse(json);
      }
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String readXmlFile(String filePath) throws IOException {
    try {
      return new String(Files.readAllBytes(DEFAULT_TEST_RESOURCES_REQUEST_PATH.resolve(filePath)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static GetInfoBddRequestDto readXmlRequestFile(String filePath)
      throws JAXBException, FileNotFoundException, XMLStreamException {
    XMLInputFactory xif = XMLInputFactory.newFactory();
    XMLStreamReader xsr =
        xif.createXMLStreamReader(
            new FileReader(
                new File(DEFAULT_TEST_RESOURCES_REQUEST_PATH.resolve(filePath).toUri())));
    JAXBContext jaxbContext = JAXBContext.newInstance(GetInfoBddRequestDto.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    JAXBElement<GetInfoBddRequestDto> request =
        jaxbUnmarshaller.unmarshal(xsr, GetInfoBddRequestDto.class);
    return request.getValue();
  }

  public static <T> void writeFile(String filePath, T object) {
    try {
      Files.write(
          DEFAULT_TEST_RESOURCES_RESULT_PATH.resolve(filePath),
          OBJECT_MAPPER.writeValueAsBytes(object));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
