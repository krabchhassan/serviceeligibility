package com.cegedim.next.serviceeligibility.consolidationcontract.bean;

import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Doit correspondre à l'index db.getCollection("declarations").createIndex({ "idDeclarant": 1,
 * "contrat.numero": 1, "contrat.numeroAdherent": 1, "effetDebut": 1 })
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeclarationKey extends DocumentEntity {
  private String idDeclarant;

  @Field("contrat.numero")
  private String contratNumero;

  @Field("contrat.numeroAdherent")
  private String numeroAdherent;

  private Date effetDebut;

  public boolean isSameContract(DeclarationKey other) {

    return StringUtils.equals(this.idDeclarant, other.getIdDeclarant())
        && StringUtils.equals(this.contratNumero, other.getContratNumero())
        && StringUtils.equals(this.numeroAdherent, other.getNumeroAdherent());
  }
}
