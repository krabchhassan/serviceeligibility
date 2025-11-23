package com.cegedim.next.serviceeligibility.almerys608.utils;

import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoCentreGestion;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoDetailCarte;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoEntreprise;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoMembreContrat;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@UtilityClass
public class XmlUtils {
  public static final String XML = ".xml";

  Map<Class<?>, JAXBContext> jaxbContexts;

  public static <T> Document createDocument(T object, String elemName) throws JAXBException {
    Class<T> clazz = (Class<T>) object.getClass();
    JAXBContext jaxbContext;
    if (jaxbContexts != null && jaxbContexts.containsKey(clazz)) {
      jaxbContext = jaxbContexts.get(clazz);
    } else {
      jaxbContexts = new HashMap<>();
      jaxbContext = JAXBContext.newInstance(clazz);
      jaxbContexts.put(clazz, jaxbContext);
    }

    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

    jaxbMarshaller.setListener(
        new Marshaller.Listener() {
          @Override
          public void beforeMarshal(Object source) {
            if (source instanceof InfoCentreGestion wrapper) {
              marshallInfoCarte(wrapper);
            }

            if (source instanceof InfoMembreContrat wrapper) {
              if (wrapper.getADRESSEMEMBRE() != null
                  && CollectionUtils.isEmpty(wrapper.getADRESSEMEMBRE().getADRESSEAGREGEES())) {
                wrapper.setADRESSEMEMBRE(null);
              }
              wrapper.getNNIRATTS().removeIf(StringUtils::isBlank);
            }

            if (source instanceof InfoDetailCarte wrapper) {
              wrapper.getCODEANNEXES().removeIf(StringUtils::isBlank);
            }

            if (source instanceof InfoEntreprise wrapper) {
              wrapper.getNUMCONTRATCOLLECTIVES().removeIf(StringUtils::isBlank);
            }
          }
        });

    QName qName = new QName(clazz.getPackage().getName(), clazz.getSimpleName());
    JAXBElement<T> root = new JAXBElement<>(qName, clazz, object);

    DOMResult result = new DOMResult();
    jaxbMarshaller.marshal(root, result);
    Document doc = (Document) result.getNode();
    doc.renameNode(doc.getFirstChild(), null, elemName);

    return doc;
  }

  private static void marshallInfoCarte(InfoCentreGestion wrapper) {
    if (wrapper.getINFOCARTE() != null) {
      int allEmpty = 6;
      if (StringUtils.isBlank(wrapper.getINFOCARTE().getLIGNE1())) {
        wrapper.getINFOCARTE().setLIGNE1(null);
        allEmpty--;
      }
      if (StringUtils.isBlank(wrapper.getINFOCARTE().getLIGNE2())) {
        wrapper.getINFOCARTE().setLIGNE2(null);
        allEmpty--;
      }
      if (StringUtils.isBlank(wrapper.getINFOCARTE().getLIGNE3())) {
        wrapper.getINFOCARTE().setLIGNE3(null);
        allEmpty--;
      }
      if (StringUtils.isBlank(wrapper.getINFOCARTE().getLIGNE4())) {
        wrapper.getINFOCARTE().setLIGNE4(null);
        allEmpty--;
      }
      if (StringUtils.isBlank(wrapper.getINFOCARTE().getLIGNE5())) {
        wrapper.getINFOCARTE().setLIGNE5(null);
        allEmpty--;
      }
      if (StringUtils.isBlank(wrapper.getINFOCARTE().getLIGNE6())) {
        wrapper.getINFOCARTE().setLIGNE6(null);
        allEmpty--;
      }
      if (allEmpty == 0) {
        wrapper.setINFOCARTE(null);
      }
    }
  }

  public static String nodeToString(Node node) throws TransformerException {
    TransformerFactory transFactory = TransformerFactory.newDefaultInstance();
    Transformer transformer = transFactory.newTransformer();
    StringWriter buffer = new StringWriter();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.ISO_8859_1.toString());
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    transFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    transformer.transform(new DOMSource(node), new StreamResult(buffer));

    // supprime toutes les balises xmlns
    return buffer.toString().replaceAll(" xmlns(:[a-zA-Z0-9]+)?=\"[^\"]*\"", "");
  }

  public void writeInFile(String path, String content) throws IOException {
    try (FileWriter writer = new FileWriter(path, StandardCharsets.ISO_8859_1, true)) {
      writer.write(content);
    }
  }

  public void writeInFile(String path, Node content) throws TransformerException, IOException {
    writeInFile(path, nodeToString(content));
  }

  public void writeInFile(String path, Object object, String elementName)
      throws TransformerException, IOException, JAXBException {
    Node node = createDocument(object, elementName);
    writeInFile(path, node);
  }
}
