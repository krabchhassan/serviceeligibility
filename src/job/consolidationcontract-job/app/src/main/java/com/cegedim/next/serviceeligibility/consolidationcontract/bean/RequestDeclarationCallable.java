package com.cegedim.next.serviceeligibility.consolidationcontract.bean;

import com.cegedim.next.serviceeligibility.consolidationcontract.services.ContractHistoService;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions634;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.MongoTemplate;

public record RequestDeclarationCallable(
    List<ProcessorPartition> partitions,
    List<ProcessorPartition> processedPartitions,
    HistoriqueExecutions634 lastExecution,
    ContractDao contractDao,
    MongoTemplate template,
    ContractTPService contractTPService,
    Map<String, Long> stepsDurations,
    int saveBufferSize,
    int declarationFetchSize,
    int contratFetchSize,
    int contratPartitionSize,
    String contractCollection,
    boolean shouldHistorize,
    ContractHistoService contractHistoService,
    List<String> declarantList) {}
