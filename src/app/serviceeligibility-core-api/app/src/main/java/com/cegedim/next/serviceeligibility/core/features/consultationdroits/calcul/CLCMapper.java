package com.cegedim.next.serviceeligibility.core.features.consultationdroits.calcul;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.IDBCLCStructMapper;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.clc.*;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeBeneficiaire;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CLCMapper {

  private final IDBCLCStructMapper mapper;

  private final I18NService i18NService = new I18NService();

  private static final String DEF_CODE = "DEF";

  @Autowired private ParametreBddService paramService;

  Map<String, List<ParametresPrestationDto>> parametres = new HashMap<>();

  public void emptyCache() {
    parametres.clear();
  }

  public TypeCodeReponse createCodeReponseOK() {
    TypeCodeReponse typeCodeReponse = new TypeCodeReponse();
    typeCodeReponse.setCode(CodeReponse.OK.getCode());
    typeCodeReponse.setLibelle(i18NService.getMessage(CodeReponse.OK.getCode()));
    return typeCodeReponse;
  }

  public void completeResponseCLC(
      List<ContractDto> contrats, CLCCompleteResponse completeResponse) {
    List<CLCResponse> responses = new ArrayList<>();
    for (ContractDto contrat : contrats) {
      CLCResponse response = new CLCResponse();
      response.setBeneficiaire(mapBeneficiaire(contrat.getBeneficiaire()));
      response.setDeclarantAmc(mapper.declarantDtoToDeclarantAmc(contrat.getDeclarantAmc()));
      response.setContrat(mapper.contratDtoToTypeContrat(contrat.getContrat()));
      response.setDomainesDroits(mapDomaineDroitCLC(contrat.getDomaineDroits()));

      responses.add(response);
    }
    completeResponse.setDroits(responses);
  }

  private TypeBeneficiaire mapBeneficiaire(BeneficiaireDto beneficiaireDto) {
    TypeBeneficiaire typeBeneficiaire = mapper.beneficiaireDtoToTypeBeneficiaire(beneficiaireDto);
    typeBeneficiaire
        .getHistoriqueAffiliations()
        .add(mapper.affiliationDtoToTypeHistoriqueAffiliation(beneficiaireDto.getAffiliation()));
    return typeBeneficiaire;
  }

  private List<DomaineDroitContratCLC> mapDomaineDroitCLC(List<DomaineDroitContratDto> domains) {
    List<DomaineDroitContratCLC> domaineDroitContratCLCS = new ArrayList<>();
    for (DomaineDroitContratDto domain : domains) {
      DomaineDroitContratCLC domaineDroitContratCLC = new DomaineDroitContratCLC();
      domaineDroitContratCLC.setCode(domain.getCode());
      domaineDroitContratCLC.setCodeExterne(domain.getCodeExterne());
      domaineDroitContratCLC.setGaranties(mapGaranties(domain.getGaranties(), domain.getCode()));

      domaineDroitContratCLCS.add(domaineDroitContratCLC);
    }
    return domaineDroitContratCLCS;
  }

  private List<GarantieCLC> mapGaranties(List<GarantieDto> garanties, String codeDomaine) {
    List<GarantieCLC> garantieCLCS = new ArrayList<>();
    for (GarantieDto garantie : garanties) {
      GarantieCLC garantieCLC = new GarantieCLC();
      garantieCLC.setCodeGarantie(garantie.getCodeGarantie());
      garantieCLC.setProduits(mapProduit(garantie.getProduits(), codeDomaine));

      garantieCLCS.add(garantieCLC);
    }
    return garantieCLCS;
  }

  private List<ProduitCLC> mapProduit(List<ProduitDto> produits, String codeDomaine) {
    List<ProduitCLC> produitCLCS = new ArrayList<>();
    for (ProduitDto produit : produits) {
      ProduitCLC produitCLC = new ProduitCLC();
      produitCLC.setCodeProduit(produit.getCodeProduit());
      produitCLC.setReferencesCouverture(
          mapRefCouverture(produit.getReferencesCouverture(), codeDomaine));

      produitCLCS.add(produitCLC);
    }
    return produitCLCS;
  }

  private List<ReferenceCouvertureCLC> mapRefCouverture(
      List<ReferenceCouvertureDto> referencesCouverture, String codeDomaine) {
    List<ReferenceCouvertureCLC> referenceCouvertureCLCS = new ArrayList<>();
    for (ReferenceCouvertureDto referenceCouvertureDto : referencesCouverture) {
      ReferenceCouvertureCLC referenceCouvertureCLC = new ReferenceCouvertureCLC();
      referenceCouvertureCLC.setReferenceCouverture(
          referenceCouvertureDto.getReferenceCouverture());

      List<NaturePrestationDto> natures = referenceCouvertureDto.getNaturesPrestation();

      // #BLUE-3518 Start
      replaceLPPPrestations(natures);
      // #BLUE-3518 End
      replaceDefPrestations(codeDomaine, natures);

      referenceCouvertureCLC.setRemboursements(
          mapMergeable(
              natures, NaturePrestationDto::getRemboursements, RemboursementContrat::getPeriodes));
      referenceCouvertureCLC.setConventionnements(
          mapMergeable(
              natures,
              NaturePrestationDto::getConventionnements,
              ConventionnementContrat::getPeriodes));
      referenceCouvertureCLC.setPrestations(
          mapMergeable(
              natures, NaturePrestationDto::getPrestations, PrestationContrat::getPeriodes));
      referenceCouvertureCLC.setPrioritesDroit(
          mapMergeable(
              natures, NaturePrestationDto::getPrioritesDroit, PrioriteDroitContrat::getPeriodes));
      referenceCouvertureCLC.setPeriodesDroit(mapPeriodesCLC(natures));

      referenceCouvertureCLCS.add(referenceCouvertureCLC);
    }

    return referenceCouvertureCLCS;
  }

  private void replaceDefPrestations(String codeDomaine, List<NaturePrestationDto> natures) {
    PrestationContrat defPrestation = null;
    List<PrestationContrat> prestations = new ArrayList<>();
    for (NaturePrestationDto nature : natures) {
      for (PrestationContrat prestation : nature.getPrestations()) {
        String code = prestation.getCode();
        if (DEF_CODE.equalsIgnoreCase(code)) {
          defPrestation = prestation;
        } else {
          prestations.add(prestation);
        }
      }
      if (defPrestation != null) {
        List<ParametresPrestationDto> params = getParams(codeDomaine);
        if (params != null && !params.isEmpty()) {
          List<PrestationContrat> generatedPrestations = generatePrestations(params, defPrestation);
          prestations.addAll(generatedPrestations);
        } else {
          prestations.add(defPrestation);
        }
      }
      nature.setPrestations(prestations);
    }
  }

  private List<ParametresPrestationDto> getParams(String code) {
    if (!parametres.containsKey(code)) {
      List<ParametresPrestationDto> list =
          paramService.findPrestationsByDomaine(UriConstants.PRESTATIONS, code);
      if (CollectionUtils.isEmpty(list)) {
        log.warn("No parametrage found for {}", code);
      } else {
        parametres.put(code, list);
        return list;
      }
    } else {
      return parametres.get(code);
    }
    return new ArrayList<>();
  }

  private List<PrestationContrat> generatePrestations(
      List<ParametresPrestationDto> params, PrestationContrat defPrestation) {
    List<PrestationContrat> prestationContratList = new ArrayList<>();
    for (ParametresDto param : params) {
      PrestationContrat prestationContrat = new PrestationContrat();
      prestationContrat.setCode(param.getCode());
      prestationContrat.setCodeRegroupement(defPrestation.getCodeRegroupement());
      prestationContrat.setDateEffet(defPrestation.getDateEffet());
      prestationContrat.setFormule(defPrestation.getFormule());
      prestationContrat.setFormuleMetier(defPrestation.getFormuleMetier());
      prestationContrat.setIsEditionRisqueCarte(defPrestation.getIsEditionRisqueCarte());
      prestationContrat.setLibelle(param.getLibelle());
      prestationContratList.add(prestationContrat);
    }
    return prestationContratList;
  }

  private <T extends Mergeable> List<T> mapMergeable(
      List<NaturePrestationDto> natures,
      Function<NaturePrestationDto, List<T>> getToMerge,
      Function<T, List<Periode>> getPeriodes) {
    List<T> merged = new ArrayList<>();
    Map<String, List<T>> groupedByMergeKey =
        natures.stream()
            .flatMap(nature -> getToMerge.apply(nature).stream())
            .collect(Collectors.groupingBy(T::mergeKey));

    for (List<T> value : groupedByMergeKey.values()) {
      List<Periode> mainPeriodes = null;
      for (T nature : value) {
        if (mainPeriodes == null) {
          mainPeriodes = formatPeriodes(getPeriodes.apply(nature));
          merged.add(nature);
        } else {
          mainPeriodes.addAll(formatPeriodes(getPeriodes.apply(nature)));
        }
      }
    }
    return merged;
  }

  private List<PeriodeDroitContratCLC> mapPeriodesCLC(List<NaturePrestationDto> natures) {
    List<PeriodeDroitContratCLC> periodeDroitContratCLCS = new ArrayList<>();
    for (NaturePrestationDto nature : natures) {
      for (PeriodeDroitContratDto periodeDroit : nature.getPeriodesDroit()) {
        PeriodeDroitContratCLC periodeDroitContratCLC = new PeriodeDroitContratCLC();
        periodeDroitContratCLC.setPeriodeDebut(
            DateUtils.formatDate(periodeDroit.getPeriodeDebut()));
        periodeDroitContratCLC.setPeriodeFin(DateUtils.formatDate(periodeDroit.getPeriodeFin()));
        periodeDroitContratCLC.setPeriodeFinFermeture(
            DateUtils.formatDate(periodeDroit.getPeriodeFinFermeture()));
        periodeDroitContratCLC.setTypePeriode(periodeDroit.getTypePeriode());
        periodeDroitContratCLC.setModeObtention(periodeDroit.getModeObtention());
        periodeDroitContratCLC.setLibelleEvenement(periodeDroit.getLibelleEvenement());
        periodeDroitContratCLC.setMotifEvenement(periodeDroit.getMotifEvenement());

        periodeDroitContratCLCS.add(periodeDroitContratCLC);
      }
    }
    return periodeDroitContratCLCS;
  }

  private List<Periode> formatPeriodes(List<Periode> periodesToFormat) {
    periodesToFormat.forEach(
        periode -> {
          periode.setDebut(DateUtils.formatDate(periode.getDebut()));
          periode.setFin(DateUtils.formatDate(periode.getFin()));
        });
    return periodesToFormat;
  }

  public CLCCompleteResponse creerReponseVide() {
    CLCCompleteResponse clcCompleteResponse = new CLCCompleteResponse();
    clcCompleteResponse.setCodeReponse(new TypeCodeReponse());
    return clcCompleteResponse;
  }

  private void replaceLPPPrestations(List<NaturePrestationDto> natures) {
    List<ParametresDto> paramLPP = paramService.findByType("codesLPP");
    for (NaturePrestationDto nature : natures) {
      List<PrestationContrat> prestations = new ArrayList<>();
      for (PrestationContrat prestation : nature.getPrestations()) {
        if (Constants.CODE_LPP.equals(prestation.getCode())
            && CollectionUtils.isNotEmpty(paramLPP)) {
          for (ParametresDto param : paramLPP) {
            PrestationContrat prestationContrat = new PrestationContrat(prestation);
            prestationContrat.setCode(param.getCode());
            prestations.add(prestationContrat);
          }
        } else {
          prestations.add(prestation);
        }
      }
      nature.setPrestations(prestations);
    }
  }
}
