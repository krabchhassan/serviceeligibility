package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.importexport;

import com.cegedim.next.serviceeligibility.core.model.entity.*;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ImportExportDto {

  @Valid @Getter @Setter private List<Declaration> declarations = new ArrayList<>();

  @Valid @Getter @Setter private List<CarteDemat> cards = new ArrayList<>();

  @Valid @Getter @Setter private List<Flux> flux = new ArrayList<>();

  @Valid @Getter @Setter private List<Volumetrie> volumetries = new ArrayList<>();

  @Valid @Getter @Setter private List<Declarant> declarants = new ArrayList<>();

  @Valid @Getter @Setter private List<Circuit> circuits = new ArrayList<>();

  @Valid @Getter @Setter private List<ServiceDroits> services = new ArrayList<>();

  @Valid @Getter @Setter private List<ParametreBdd> parametres = new ArrayList<>();

  @Valid @Getter @Setter private List<Transcodage> transcodage = new ArrayList<>();

  @Valid @Getter @Setter private List<TranscoParametrage> transcoParametrage = new ArrayList<>();

  @Valid @Getter @Setter private List<HistoriqueExecution> historiqueExecution = new ArrayList<>();
}
