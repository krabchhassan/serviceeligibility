package com.cegedim.next.serviceeligibility.consolidationcontract.bean;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * Doit correspondre à l'index db.getCollection("declarations").createIndex({ "idDeclarant": 1,
 * "contrat.numero": 1, "contrat.numeroAdherent": 1, "effetDebut": 1 })
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
public class ContractKey extends DocumentEntity {
  private String idDeclarant;

  private String numeroContrat;

  private String numeroAdherent;

  public boolean isSameContract(ContractKey other) {
    return StringUtils.equals(this.idDeclarant, other.getIdDeclarant())
        && StringUtils.equals(this.numeroContrat, other.getNumeroContrat())
        && StringUtils.equals(this.numeroAdherent, other.getNumeroAdherent());
  }

  public boolean isSameContract(ContractTP other) {
    return StringUtils.equals(this.idDeclarant, other.getIdDeclarant())
        && StringUtils.equals(this.numeroContrat, other.getNumeroContrat())
        && StringUtils.equals(this.numeroAdherent, other.getNumeroAdherent());
  }

  public boolean isSameContract(Declaration declaration) {
    return StringUtils.equals(this.idDeclarant, declaration.getIdDeclarant())
        && StringUtils.equals(this.numeroContrat, declaration.getContrat().getNumero())
        && StringUtils.equals(this.numeroAdherent, declaration.getContrat().getNumeroAdherent());
  }
}
