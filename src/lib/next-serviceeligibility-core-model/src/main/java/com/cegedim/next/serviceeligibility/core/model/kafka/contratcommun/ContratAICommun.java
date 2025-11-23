package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Classe contenant les champs communs à toutes les versions du contrat d'interface pour l'event
 * Contrat
 *
 * @author RHERMEZ
 */
@Document(collection = "servicePrestation")
@Data
@NoArgsConstructor
public abstract class ContratAICommun {
  @Id private String id;
  private String traceId;
  private String idDeclarant;
  private String societeEmettrice;
  private String numero;
  private String numeroExterne;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String dateSouscription;
  private String dateResiliation;
  private String apporteurAffaire;
  private List<Periode> periodesContratResponsableOuvert;
  private String critereSecondaireDetaille;
  private String critereSecondaire;
  private Boolean isContratIndividuel;
  private String gestionnaire;
  private String qualification;
  private String ordrePriorisation;

  public void setSocieteEmettrice(String societeEmettrice) {
    if (societeEmettrice != null) {
      this.societeEmettrice = StringUtils.leftPad(societeEmettrice, 10, "0");
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((idDeclarant == null) ? 0 : idDeclarant.hashCode());
    result = prime * result + ((numero == null) ? 0 : numero.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ContratAICommun other = (ContratAICommun) obj;
    if (idDeclarant == null) {
      if (other.idDeclarant != null) return false;
    } else if (!idDeclarant.equals(other.idDeclarant)) return false;
    if (numero == null) {
      if (other.numero != null) return false;
    } else if (!numero.equals(other.numero)) return false;
    return true;
  }
}
