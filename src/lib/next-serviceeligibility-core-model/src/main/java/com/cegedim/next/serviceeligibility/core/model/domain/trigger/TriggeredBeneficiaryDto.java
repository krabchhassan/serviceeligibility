package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TriggeredBeneficiaryDto {
  private String numeroContrat;
  private String nir;
  private String dateNaissance;
  private String offresProduits;
  private String collectivite;
  private String college;
  private String critereSecondaireDetaille;
  private String motifAnomalie;
  private List<TriggeredBeneficiaryStatus> historiqueStatuts = new ArrayList<>();
  private ServicePrestationTriggerBenef newContract;
}
