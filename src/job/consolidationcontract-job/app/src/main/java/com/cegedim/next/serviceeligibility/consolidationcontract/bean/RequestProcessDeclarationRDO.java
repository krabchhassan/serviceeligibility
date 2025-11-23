package com.cegedim.next.serviceeligibility.consolidationcontract.bean;

import com.cegedim.next.serviceeligibility.consolidationcontract.services.ContractHistoService;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions634;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public record RequestProcessDeclarationRDO(
    ProcessorPartition partition,
    Declaration declaration,
    AtomicBoolean skippingContrat,
    AtomicReference<ContractTP> saveContract,
    AtomicReference<ContractTP> currentContract,
    HistoriqueExecutions634 result,
    AtomicReference<ContractHistoService.Buffer> histoBuffer,
    AtomicReference<ContractTP> nextContract,
    AtomicReference<ContractKey> lastSkippedContract) {}
