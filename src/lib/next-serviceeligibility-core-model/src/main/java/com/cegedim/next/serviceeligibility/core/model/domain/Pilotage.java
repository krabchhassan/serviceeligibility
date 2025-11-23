package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.annotation.Transient;

@Data
public class Pilotage implements GenericDomain<Pilotage> {

  private static final long serialVersionUID = 1L;

  private String codeService;
  @Transient private int triRestitution;
  private String critereRegroupement;
  private String critereRegroupementDetaille;
  private Boolean serviceOuvert;
  private Boolean isCarteEditable;
  private Date dateOuverture;
  private Date dateSynchronisation;
  private String couloirClient;
  private String typeConventionnement;

  /* DOCUMENTS EMBEDDED */
  private InfoPilotage caracteristique;

  public Pilotage() {
    /* empty constructor */ }

  public Pilotage(Pilotage source) {
    this.codeService = source.getCodeService();
    this.triRestitution = source.getTriRestitution();
    this.critereRegroupement = source.getCritereRegroupement();
    this.critereRegroupementDetaille = source.getCritereRegroupementDetaille();
    this.serviceOuvert = source.getServiceOuvert();
    this.isCarteEditable = source.getIsCarteEditable();
    if (source.getDateOuverture() != null) {
      this.dateOuverture = new Date(source.getDateOuverture().getTime());
    }
    if (source.getDateSynchronisation() != null) {
      this.dateSynchronisation = new Date(source.getDateSynchronisation().getTime());
    }
    this.couloirClient = source.getCouloirClient();
    this.typeConventionnement = source.getTypeConventionnement();
    if (source.getCaracteristique() != null) {
      this.caracteristique = new InfoPilotage(source.getCaracteristique());
    }
  }

  @Override
  public int compareTo(final Pilotage pilotage) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codeService, pilotage.codeService);
    compareToBuilder.append(this.critereRegroupement, pilotage.critereRegroupement);
    compareToBuilder.append(this.critereRegroupementDetaille, pilotage.critereRegroupementDetaille);
    compareToBuilder.append(this.serviceOuvert, pilotage.serviceOuvert);
    compareToBuilder.append(this.isCarteEditable, pilotage.isCarteEditable);
    compareToBuilder.append(this.dateOuverture, pilotage.dateOuverture);
    compareToBuilder.append(this.dateSynchronisation, pilotage.dateSynchronisation);
    compareToBuilder.append(this.couloirClient, pilotage.couloirClient);
    compareToBuilder.append(this.typeConventionnement, pilotage.typeConventionnement);
    compareToBuilder.append(this.caracteristique, pilotage.caracteristique);
    return compareToBuilder.toComparison();
  }
}
