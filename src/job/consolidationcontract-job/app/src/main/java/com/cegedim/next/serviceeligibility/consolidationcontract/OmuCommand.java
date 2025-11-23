package com.cegedim.next.serviceeligibility.consolidationcontract;

import static com.cegedim.next.serviceeligibility.consolidationcontract.constants.Constants.BATCH_MODE_NO_RDO;

import java.util.List;
import java.util.concurrent.Callable;
import lombok.Getter;
import picocli.CommandLine;

@CommandLine.Command(description = {"Commande du batch de consolidation contract"})
@Getter
public class OmuCommand implements Callable<Integer> {
  @CommandLine.Unmatched List<String> unmatched;

  @CommandLine.Option(names = {"--LISTE_AMC_STOP"})
  private String listeAmcStop = "";

  @CommandLine.Option(names = {"--LISTE_AMC_REPRISE"})
  private String listeAmcReprise = "";

  @CommandLine.Option(names = {"--COLLECTION_CONTRACT_NAME"})
  private String collectionContractName = "";

  @CommandLine.Option(names = {"--PARALLELISME"})
  private String parallelisme = "4";

  @CommandLine.Option(names = {"--SAVE_BUFFER_SIZE"})
  private String saveBufferSize = "500";

  @CommandLine.Option(names = {"--FROM_DECLARATION"})
  private String fromDeclaration = "0";

  @CommandLine.Option(names = {"--TO_DECLARATION"})
  private String toDeclaration = "0";

  @CommandLine.Option(names = {"--BATCH_ID"})
  private String batchId = "0";

  @CommandLine.Option(names = {"--DECLARATION_FETCH_SIZE"})
  private String declarationFetchSize = "0";

  @CommandLine.Option(names = {"--CONTRAT_PARTITION_SIZE"})
  private String contratPartitionSize = "0";

  @CommandLine.Option(names = {"--CONTRAT_FETCH_SIZE"})
  private String contratFetchSize = "1000";

  @CommandLine.Option(names = {"--PARTITION_SIZE"})
  private String partitionSize = "500000";

  @CommandLine.Option(names = {"--DATE_SYNCHRO"})
  private String dateSynchro = "";

  @CommandLine.Option(
      names = {"--BATCH_MODE"},
      required = false)
  private String batchMode = BATCH_MODE_NO_RDO;

  @CommandLine.Option(names = {"--SHOULD_HISTORIZE"})
  private String shouldHistorize = "";

  @CommandLine.Option(names = {"--DECLARANTS_LIST"})
  private String declarantsList = "";

  @Override
  public Integer call() {
    return 1;
  }
}
