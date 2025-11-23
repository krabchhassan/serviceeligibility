package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduPurgeRdo implements CompteRenduGeneric {
  // Benef
  private long tracesInsuredPurges;
  private long tracesBenefPurges;
  private long benefsPurges;

  // HTP
  private long tracesServicePrestationPurges;
  private long servicePrestationPurges;
  private long benefsModifiesHTP;

  // TP
  private long triggersPurges;
  private long triggeredBeneficiariesPurges;
  private long contratsModifies;
  private long contratsPurges;
  private long tracesDeclarationPurges;
  private long declarationsPurges;
  private long benefsModifiesTP;

  private long sasContratsPurges;
  private long parametrageCarteTPPurges;
  private long referentielParametrageCarteTPPurges;

  private long rdoServicePrestationPurges;

  private long declarationConsolidePurges;
  private long tracesPurges;
  private long carteDematPurges;
  private long cartePapierPurges;
  private long traceFluxPurges;
  private long historiqueExecutionPurges;
  private long historiqueExecutionRenouvellementPurges;

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("tracesInsuredPurges", ParameterValue.valueOf((int) this.tracesInsuredPurges));
    map.put("tracesBenefPurges", ParameterValue.valueOf((int) this.tracesBenefPurges));
    map.put("benefsPurges", ParameterValue.valueOf((int) this.benefsPurges));
    map.put(
        "tracesServicePrestationPurges",
        ParameterValue.valueOf((int) this.tracesServicePrestationPurges));
    map.put("servicePrestationPurges", ParameterValue.valueOf((int) this.servicePrestationPurges));
    map.put("benefsModifiesHTP", ParameterValue.valueOf((int) this.benefsModifiesHTP));
    map.put("triggersPurges", ParameterValue.valueOf((int) this.triggersPurges));
    map.put(
        "triggeredBeneficiariesPurges",
        ParameterValue.valueOf((int) this.triggeredBeneficiariesPurges));
    map.put("contratsModifies", ParameterValue.valueOf((int) this.contratsModifies));
    map.put("contratsPurges", ParameterValue.valueOf((int) this.contratsPurges));
    map.put("tracesDeclarationPurges", ParameterValue.valueOf((int) this.tracesDeclarationPurges));
    map.put("declarationsPurges", ParameterValue.valueOf((int) this.declarationsPurges));
    map.put("benefsModifiesTP", ParameterValue.valueOf((int) this.benefsModifiesTP));
    map.put("sasContratsPurges", ParameterValue.valueOf((int) this.sasContratsPurges));
    map.put(
        "parametrageCarteTPPurges", ParameterValue.valueOf((int) this.parametrageCarteTPPurges));
    map.put(
        "referentielParametrageCarteTPPurges",
        ParameterValue.valueOf((int) this.referentielParametrageCarteTPPurges));
    map.put(
        "rdoServicePrestationPurges",
        ParameterValue.valueOf((int) this.rdoServicePrestationPurges));
    map.put(
        "declarationConsolidePruges",
        ParameterValue.valueOf((int) this.declarationConsolidePurges));
    map.put("tracesPurges", ParameterValue.valueOf((int) this.tracesPurges));
    map.put("carteDematPurges", ParameterValue.valueOf((int) this.carteDematPurges));
    map.put("cartePapierPurges", ParameterValue.valueOf((int) this.cartePapierPurges));
    map.put("traceFluxPurges", ParameterValue.valueOf((int) this.traceFluxPurges));
    map.put(
        "historiqueExecutionPurges", ParameterValue.valueOf((int) this.historiqueExecutionPurges));
    map.put(
        "historiqueExecutionRenouvellementPurges",
        ParameterValue.valueOf((int) this.historiqueExecutionRenouvellementPurges));
    return map;
  }

  public void addTracesInsuredPurges(long tracesInsuredPurges) {
    this.tracesInsuredPurges += tracesInsuredPurges;
  }

  public void addTracesBenefPurges(long tracesBenefPurges) {
    this.tracesBenefPurges += tracesBenefPurges;
  }

  public void addBenefsPurges(long benefsPurges) {
    this.benefsPurges += benefsPurges;
  }

  public void addTracesServicePrestationPurges(long tracesServicePrestationPurges) {
    this.tracesServicePrestationPurges += tracesServicePrestationPurges;
  }

  public void addServicePrestationPurges(long servicePrestationPurges) {
    this.servicePrestationPurges += servicePrestationPurges;
  }

  public void addBenefsModifiesHTP(long benefsModifiesHTP) {
    this.benefsModifiesHTP += benefsModifiesHTP;
  }

  public void addTriggersPurges(long triggersPurges) {
    this.triggersPurges += triggersPurges;
  }

  public void addTriggeredBeneficiariesPurges(long triggeredBeneficiariesPurges) {
    this.triggeredBeneficiariesPurges += triggeredBeneficiariesPurges;
  }

  public void addContratsPurges(long contratsPurges) {
    this.contratsPurges += contratsPurges;
  }

  public void addContratsModifies(long contratsModifies) {
    this.contratsModifies += contratsModifies;
  }

  public void addTracesDeclarationPurges(long tracesDeclarationPurges) {
    this.tracesDeclarationPurges += tracesDeclarationPurges;
  }

  public void addDeclarationsPurges(long declarationsPurges) {
    this.declarationsPurges += declarationsPurges;
  }

  public void addBenefsModifiesTP(long benefsModifiesTP) {
    this.benefsModifiesTP += benefsModifiesTP;
  }

  public void addSasContratsPurges(long sasContratsPurges) {
    this.sasContratsPurges += sasContratsPurges;
  }

  public void addParametrageCarteTPPurges(long parametrageCarteTPPurges) {
    this.parametrageCarteTPPurges += parametrageCarteTPPurges;
  }

  public void addReferentielParametrageCarteTPPurges(long referentielParametrageCarteTPPurges) {
    this.referentielParametrageCarteTPPurges += referentielParametrageCarteTPPurges;
  }

  public void addRDOServicePrestationPurges(long rdoServicePrestationPurges) {
    this.rdoServicePrestationPurges = rdoServicePrestationPurges;
  }

  public void addDeclarationConsolidePurges(long count) {
    this.declarationConsolidePurges += count;
  }

  public void addTracesPurges(long count) {
    this.tracesPurges += count;
  }

  public void addCarteDematPurges(long count) {
    this.carteDematPurges += count;
  }

  public void addCartePapierPurges(long count) {
    this.cartePapierPurges += count;
  }

  public void addTraceFluxPurges(long count) {
    this.traceFluxPurges += count;
  }

  public void addHistoriqueExecutionPurges(long count) {
    this.historiqueExecutionPurges += count;
  }

  public void addHistoriqueExecutionRenouvellementPurges(long count) {
    this.historiqueExecutionRenouvellementPurges += count;
  }
}
