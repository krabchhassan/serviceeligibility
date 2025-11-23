package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

/** Classe TraceServiceDto, gere les traces pour un service. */
public class TraceServiceDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 1L;

  private String codeService;
  private String typeService;
  private Boolean lastExecutionOk;
  private List<TracePriorisationDto> traitements;
  private List<TraceExtractionDto> extractions;
  private List<TraceConsolidationDto> consolidations;

  public String getCodeService() {
    return codeService;
  }

  public void setCodeService(String codeService) {
    this.codeService = codeService;
  }

  public Boolean getLastExecutionOk() {
    return lastExecutionOk;
  }

  public void setLastExecutionOk(Boolean lastExecutionOk) {
    this.lastExecutionOk = lastExecutionOk;
  }

  public List<TracePriorisationDto> getTraitements() {
    return traitements;
  }

  public void setTraitements(List<TracePriorisationDto> traitements) {
    this.traitements = traitements;
  }

  public List<TraceExtractionDto> getExtractions() {
    return extractions;
  }

  public void setExtractions(List<TraceExtractionDto> extractions) {
    this.extractions = extractions;
  }

  public List<TraceConsolidationDto> getConsolidations() {
    return consolidations;
  }

  public void setConsolidations(List<TraceConsolidationDto> consolidations) {
    this.consolidations = consolidations;
  }

  public String getTypeService() {
    return typeService;
  }

  public void setTypeService(String typeService) {
    this.typeService = typeService;
  }
}
