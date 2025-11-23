package com.cegedim.next.serviceeligibility.core.features.importexport.dto;

import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroits;
import com.cegedim.next.serviceeligibility.core.model.entity.TranscoParametrage;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ParametrageImportExportDto {

  @Valid @Getter @Setter private List<Declarant> declarants = new ArrayList<>();

  @Valid @Getter @Setter private List<Circuit> circuits = new ArrayList<>();

  @Valid @Getter @Setter private List<ServiceDroits> services = new ArrayList<>();

  @Valid @Getter @Setter private List<ParametreBdd> parametres = new ArrayList<>();

  @Valid @Getter @Setter private List<Transcodage> transcodage = new ArrayList<>();

  @Valid @Getter @Setter private List<TranscoParametrage> transcoParametrage = new ArrayList<>();

  @Valid @Getter @Setter private List<HistoriqueExecution> historiqueExecution = new ArrayList<>();
}
