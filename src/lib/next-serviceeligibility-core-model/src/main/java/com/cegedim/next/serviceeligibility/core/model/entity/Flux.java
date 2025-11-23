package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.InfoAMC;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoFichierEmis;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoFichierRecu;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection tracesFlux dans la base de donnees. */
@Document(collection = "tracesFlux")
public class Flux extends DocumentEntity implements GenericDomain<Flux> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  @Getter @Setter private String batch;

  @Getter @Setter private String processus;

  @Getter @Setter private boolean fichierEmis;

  @Getter @Setter private String idDeclarant;

  @Getter @Setter private String typeFichier;

  /* Date d'exécution */

  @Getter @Setter private Date dateExecution;

  @Getter @Setter private InfoFichierEmis infoFichierEmis;

  @Getter @Setter private InfoFichierRecu infoFichierRecu;

  @Getter @Setter private InfoAMC infoAMC;

  @Override
  public int compareTo(Flux flux) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idDeclarant, flux.idDeclarant);
    compareToBuilder.append(this.batch, flux.batch);
    compareToBuilder.append(this.processus, flux.processus);
    compareToBuilder.append(this.infoFichierEmis, flux.fichierEmis);
    compareToBuilder.append(this.typeFichier, flux.typeFichier);
    compareToBuilder.append(this.infoFichierRecu, flux.infoFichierRecu);
    return compareToBuilder.toComparison();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((batch == null) ? 0 : batch.hashCode());
    result = prime * result + ((dateExecution == null) ? 0 : dateExecution.hashCode());
    result = prime * result + (fichierEmis ? 1231 : 1237);
    result = prime * result + ((idDeclarant == null) ? 0 : idDeclarant.hashCode());
    result = prime * result + ((infoAMC == null) ? 0 : infoAMC.hashCode());
    result = prime * result + ((infoFichierEmis == null) ? 0 : infoFichierEmis.hashCode());
    result = prime * result + ((infoFichierRecu == null) ? 0 : infoFichierRecu.hashCode());
    result = prime * result + ((processus == null) ? 0 : processus.hashCode());
    result = prime * result + ((typeFichier == null) ? 0 : typeFichier.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Flux other = (Flux) obj;
    if (batch == null) {
      if (other.batch != null) return false;
    } else if (!batch.equals(other.batch)) return false;
    if (dateExecution == null) {
      if (other.dateExecution != null) return false;
    } else if (!dateExecution.equals(other.dateExecution)) return false;
    if (fichierEmis != other.fichierEmis) return false;
    if (idDeclarant == null) {
      if (other.idDeclarant != null) return false;
    } else if (!idDeclarant.equals(other.idDeclarant)) return false;
    if (infoAMC == null) {
      if (other.infoAMC != null) return false;
    } else if (!infoAMC.equals(other.infoAMC)) return false;
    if (infoFichierEmis == null) {
      if (other.infoFichierEmis != null) return false;
    } else if (!infoFichierEmis.equals(other.infoFichierEmis)) return false;
    if (infoFichierRecu == null) {
      if (other.infoFichierRecu != null) return false;
    } else if (!infoFichierRecu.equals(other.infoFichierRecu)) return false;
    if (processus == null) {
      if (other.processus != null) return false;
    } else if (!processus.equals(other.processus)) return false;
    if (typeFichier == null) {
      if (other.typeFichier != null) return false;
    } else if (!typeFichier.equals(other.typeFichier)) return false;
    return true;
  }
}
