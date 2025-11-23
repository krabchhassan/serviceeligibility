package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.RDO_KEY_SEPARATOR;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.organisation.dto.OrganizationDto;
import com.cegedim.common.organisation.exception.OrganizationIndexInitException;
import com.cegedim.common.organisation.exception.OrganizationNotFoundException;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdo;
import com.cegedim.next.serviceeligibility.core.model.entity.FileFlowMetadata;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ErrorData;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.TraceServicePrestation;
import com.cegedim.next.serviceeligibility.core.model.enumeration.StatutFileFlowMetaData;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.AbstractValidationService;
import com.cegedim.next.serviceeligibility.core.services.FileFlowMetadataService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.services.pojo.ErrorValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.config.TestConfiguration;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class FileServiceTest {
  @Autowired FileService fileService;

  @Autowired ProcessFileTask processFileTask;

  @Autowired MongoTemplate template;

  @Autowired FileFlowMetadataService fileFlowMetadataService;

  @Autowired OrganizationService organizationService;

  @SpyBean TraceDao traceDao;

  @SpyBean private BeyondPropertiesService beyondPropertiesService;

  FileFlowMetadata meta = null;

  @Autowired ValidationRdoService validationRdoService;

  @BeforeEach
  public void before() throws OrganizationIndexInitException, OrganizationNotFoundException {
    template.dropCollection(Constants.BENEFICIAIRE_COLLECTION_NAME);
    template.dropCollection(Constants.BENEF_TRACE);
    template.dropCollection(Constants.SERVICE_PRESTATION);
    template.dropCollection(Constants.CONTRACT_TRACE);
    validationRdoService.setUseReferentialValidation(false);
    validationRdoService.setControleCorrespondanceBobb(false);

    Mockito.doAnswer(
            invocation -> {
              FileFlowMetadata metaData = new FileFlowMetadata();
              metaData.setNomFichier(invocation.getArgument(0));
              metaData.setSource(invocation.getArgument(1));
              metaData.setVersion(invocation.getArgument(2));
              metaData.setDebutTraitement(LocalDateTime.now(ZoneOffset.UTC));
              metaData.setStatut(StatutFileFlowMetaData.Pending);
              return metaData;
            })
        .when(fileFlowMetadataService)
        .createFileFlowMetadata(Mockito.any(), Mockito.any(), Mockito.any());

    Mockito.doAnswer(invocation -> invocation.getArgument(0))
        .when(template)
        .save(Mockito.any(), Mockito.eq(Constants.CONTRACT_TRACE));
    Mockito.doAnswer(invocation -> invocation.getArgument(0)).when(template).save(Mockito.any());
    Mockito.doAnswer(invocation -> meta = invocation.getArgument(0))
        .when(fileFlowMetadataService)
        .updateFileFlowMetadata(Mockito.any());

    Mockito.doReturn("#").when(beyondPropertiesService).getPropertyOrThrowError(RDO_KEY_SEPARATOR);

    OrganizationDto org = new OrganizationDto();
    org.setIsMainType(Boolean.TRUE);
    org.setCode("");
    org.setFullName("");
    org.setCommercialName("");
    Mockito.doReturn(org).when(organizationService).getOrganizationByAmcNumber(Mockito.anyString());
    Mockito.doReturn(true)
        .when(organizationService)
        .isOrgaAttached(Mockito.anyString(), Mockito.anyString());
  }

  @Test
  void test_should_read_folders() {
    init("src/test/resources/1/");
    boolean status = this.fileService.init();
    Assertions.assertTrue(status);
  }

  @Test
  void test_should_process_file() {
    init("src/test/resources/1");
    this.fileService.init();
    List<ContratAIV6> contracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(0, contracts.size());

    List<BenefAIV5> benefs =
        template.findAll(BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
    Assertions.assertEquals(0, benefs.size());

    boolean status = this.fileService.processFolderV5("now", new CompteRenduRdo());
    Assertions.assertFalse(status);
    Assertions.assertEquals(4, meta.getNbLignesLues());
    Assertions.assertEquals(4, meta.getNbLignesIntegrees());
  }

  @Test
  void test_should_process_fileV2() {
    init("src/test/resources/4/");
    this.fileService.init();
    List<ContratAIV6> contracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(0, contracts.size());

    List<BenefAIV5> benefs =
        template.findAll(BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
    Assertions.assertEquals(0, benefs.size());

    boolean status = this.fileService.processFolderV5("now", new CompteRenduRdo());
    Assertions.assertFalse(status);
    Assertions.assertEquals(4, meta.getNbLignesLues());
    Assertions.assertEquals(4, meta.getNbLignesIntegrees());
  }

  @Test
  void test_should_process_fileV6() {
    init("src/test/resources/5/");
    this.fileService.init();
    List<ContratAIV6> contracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(0, contracts.size());

    List<BenefAIV5> benefs =
        template.findAll(BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
    Assertions.assertEquals(0, benefs.size());

    boolean status =
        this.fileService.processFolder("now", new CompteRenduRdo(), Constants.CONTRACT_VERSION_V6);
    Assertions.assertFalse(status);
    Assertions.assertEquals(4, meta.getNbLignesLues());
    Assertions.assertEquals(4, meta.getNbLignesIntegrees());
  }

  @Test
  void test_should_not_fail_when_listing_unknown_folder() {
    init("src/test/resources_notexisting/");
    boolean status = this.fileService.init();

    Assertions.assertFalse(status);
  }

  private void init(String folder1) {
    ReflectionTestUtils.setField(fileService, "rdoSpFolder", folder1);
    ReflectionTestUtils.setField(fileService, "arlFolder", folder1);
    fileService.setIsUnitTest(true);
  }

  @Test
  void test_should_not_fail_when_parsing_incorrect_filestructure() {
    init("src/test/resources/2/");
    this.fileService.init();
    boolean status = this.fileService.processFolderV5("now", new CompteRenduRdo());
    Assertions.assertFalse(status);
  }

  @Test
  void test_should_not_fail_when_parsing_invalid_contract() {
    init("src/test/resources/3/");
    this.fileService.init();
    boolean status = this.fileService.processFolderV5("now", new CompteRenduRdo());
    Assertions.assertFalse(status);
  }

  @Test
  void test_format_errors() {
    List<ErrorValidationBean> errorValidationBeans = new ArrayList<>();
    ErrorValidationBean errorValidationBean =
        new ErrorValidationBean("message1", Constants.ASSURE, "MBA14762");
    ErrorValidationBean errorValidationBean2 =
        new ErrorValidationBean("message2", Constants.CONTRAT, null);
    ErrorValidationBean errorValidationBean3 =
        new ErrorValidationBean("message3", Constants.ASSURE, "MBA14762");
    errorValidationBeans.add(errorValidationBean);
    errorValidationBeans.add(errorValidationBean2);
    errorValidationBeans.add(errorValidationBean3);
    ContractValidationBean contractValidationBean = new ContractValidationBean();
    contractValidationBean.setErrorValidationBeans(errorValidationBeans);
    ValidationContractException exception =
        new ValidationContractException(
            errorValidationBeans,
            AbstractValidationService.getMessageFromValidationBean(contractValidationBean));
    Map<String, List<String>> errors = this.processFileTask.formatErrors(exception);
    Assertions.assertEquals(1, errors.get(Constants.CONTRAT).size());
    Assertions.assertEquals("message2", errors.get(Constants.CONTRAT).get(0));
    Assertions.assertEquals(2, errors.get("MBA14762").size());
    Assertions.assertEquals("message1", errors.get("MBA14762").get(0));
    Assertions.assertEquals("message3", errors.get("MBA14762").get(1));
  }

  @Test
  void test_arl_content() throws IOException, CsvValidationException {
    init("src/test/resources/6/");
    this.fileService.init();
    this.createMockForARL();
    List<String[]> data = this.processFileTask.getArlContent("arl.csv", "0000401166", null, "");
    StringBuilder res = new StringBuilder();
    for (String[] row : data) {
      for (String cell : row) {
        res.append("\t").append(cell);
      }
      res.append("\n");
    }
    Assertions.assertEquals(readCSV(Paths.get("src/test/resources/6/arl.csv")), res.toString());
  }

  private String readCSV(Path filePath) throws IOException, CsvValidationException {
    StringBuilder expected = new StringBuilder();
    try (Reader reader = Files.newBufferedReader(filePath)) {
      CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
      try (CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()) {
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
          for (String cell : nextRecord) {
            expected.append("\t").append(cell);
          }
          expected.append("\n");
        }
      }
    }
    return expected.toString();
  }

  private void createMockForARL() {
    TraceServicePrestation trace = new TraceServicePrestation();
    TraceServicePrestation trace2 = new TraceServicePrestation();
    ErrorData errorData = new ErrorData();
    errorData.setNumeroPersonne("MBA14762");
    errorData.setMessages(List.of("messageA", "messageB"));
    ErrorData errorData1 = new ErrorData();
    errorData1.setNumeroPersonne("MBA14800");
    errorData1.setMessages(List.of("messageC", "messageD"));
    ErrorData errorData2 = new ErrorData();
    errorData2.setMessages(List.of("message"));
    trace.setErrorMessages(List.of(errorData, errorData2));
    trace.setNumeroAdherent("MBA1476");
    trace.setNumeroContrat("MBA1476");
    trace2.setErrorMessages(List.of(errorData1, errorData2));
    trace2.setNumeroAdherent("MBA14769");
    trace2.setNumeroContrat("MBA14769");
    Page<TraceServicePrestation> resFirstCall =
        PageableExecutionUtils.getPage(List.of(trace), PageRequest.of(1, 1), () -> 2);
    Page<TraceServicePrestation> resSecondCall =
        PageableExecutionUtils.getPage(List.of(trace2), PageRequest.of(2, 1), () -> 2);
    Mockito.doReturn(resFirstCall)
        .doReturn(resSecondCall)
        .doReturn(new PageImpl<>(Collections.emptyList()))
        .when(traceDao)
        .getTraceForArl(Mockito.anyString(), Mockito.anyString(), Mockito.any());
  }
}
