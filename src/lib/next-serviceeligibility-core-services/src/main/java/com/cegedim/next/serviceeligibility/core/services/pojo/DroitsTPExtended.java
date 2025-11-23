package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOfflineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOnlineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import lombok.Data;

@Data
public class DroitsTPExtended {
  // Structure comprenant les éléments pour remplir rightsV4 au début et
  // DomaineDroit de Déclaration (plus bas)

  String codeGarantie;
  String insurerCode;
  String originCode;
  String originInsurerCode;
  String carenceCode;
  String type;
  String ordrePriorisation;
  String dateAncienneteGarantie;
  String codeOffre;
  String versionOffre;
  String codeProduit;
  ModeAssemblage modeAssemblage;
  String dateDebut;
  String dateFin;
  String codeDomaine;
  PAPNatureTags papNatureTags;
  // champs ajoutés pour génération déclarations
  TpOfflineRightsDetails detailsOffline;

  TpOnlineRightsDetails detailsOnline;
  String libelle;
  String codeOc;
  String dateFinOnline;
  Periode periodeProductElement;
  Periode periodePW;
  PeriodeCarence carencePeriode;

  public DroitsTPExtended() {
    /* empty constructor */ }

  public DroitsTPExtended(DroitsTPExtended source) {
    this.codeGarantie = source.getCodeGarantie();
    this.insurerCode = source.getInsurerCode();
    this.originCode = source.getOriginCode();
    this.originInsurerCode = source.getOriginInsurerCode();
    this.carenceCode = source.getCarenceCode();
    this.type = source.getType();
    this.ordrePriorisation = source.getOrdrePriorisation();
    this.dateAncienneteGarantie = source.getDateAncienneteGarantie();
    this.codeOffre = source.getCodeOffre();
    this.versionOffre = source.getVersionOffre();
    this.codeProduit = source.getCodeProduit();
    this.modeAssemblage = source.getModeAssemblage();
    this.dateDebut = source.getDateDebut();
    this.dateFin = source.getDateFin();
    this.codeDomaine = source.getCodeDomaine();
    if (source.getPapNatureTags() != null) {
      this.papNatureTags = new PAPNatureTags(source.getPapNatureTags());
    }
    if (source.getDetailsOffline() != null) {
      this.detailsOffline = new TpOfflineRightsDetails(source.getDetailsOffline());
    }
    if (source.getDetailsOnline() != null) {
      this.detailsOnline = new TpOnlineRightsDetails(source.getDetailsOnline());
    }
    this.libelle = source.getLibelle();
    this.codeOc = source.getCodeOc();
    this.dateFinOnline = source.getDateFinOnline();
    if (source.getPeriodeProductElement() != null) {
      this.periodeProductElement = new Periode(source.getPeriodeProductElement());
    }
    if (source.getPeriodePW() != null) {
      this.periodePW = new Periode(source.getPeriodePW());
    }
    if (source.getCarencePeriode() != null) {
      this.carencePeriode = new PeriodeCarence(source.getCarencePeriode());
    }
  }
}
