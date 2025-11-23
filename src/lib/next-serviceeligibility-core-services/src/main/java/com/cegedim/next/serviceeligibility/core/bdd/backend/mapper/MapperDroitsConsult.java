package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.LEVEL_OF_GUARANTEE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDomaineDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Parametre;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapperDroitsConsult {

  private final ParametreBddDaoImpl parametreBddDaoImpl;

  private final ParametreBddServiceImpl parametreBddServiceImpl;

  private final BeyondPropertiesService beyondPropertiesService;

  private static final String CODE_LPP = "LPP";

  public List<String> getCodesLPP() {
    ParametreBdd param = parametreBddDaoImpl.findParametres("codesLPP");
    List<String> listLPP = new ArrayList<>();
    listLPP.add(CODE_LPP);
    if (param != null && param.getListeValeurs() != null) {
      listLPP =
          param.getListeValeurs().stream().map(object -> Objects.toString(object, null)).toList();
    }
    return listLPP;
  }

  /**
   * @param d Transforme la déclaration d en renvoyant les prestations de manière unique (pas de
   *     doublon) et en transformant LPP en la liste de code présent en paramétrage (si absent, on
   *     laisse LPP) tout en tenant compte de l'ordre de priorité des domaines
   */
  public void transformDomainsDto(DeclarationDto d) {
    List<String> listeCodesLPP = getCodesLPP();
    List<DomaineDroitDto> domains = d.getDomaineDroits();
    List<String> listeCodesPrestation = new ArrayList<>();
    List<ParametresDto> listDomaines = parametreBddServiceImpl.findByType(Constants.DOMAINE);
    Map<String, String> domainsPrio;
    if (!listDomaines.isEmpty()) {
      domainsPrio =
          listDomaines.stream()
              .collect(
                  Collectors.toMap(
                      ParametresDto::getCode, item -> ((ParametresDomaineDto) item).getPriorite()));
    } else {
      domainsPrio = new HashMap<>();
    }
    domains.sort(
        (d1, d2) -> {
          int codeLpp =
              (d1.getPrestations().stream().anyMatch(p -> CODE_LPP.equals(p.getCode()))) ? 1 : 0;
          return (Integer.parseInt(
                          domainsPrio.get(d2.getCode()) != null
                              ? domainsPrio.get(d2.getCode())
                              : "0")
                      - Integer.parseInt(
                          domainsPrio.get(d1.getCode()) != null
                              ? domainsPrio.get(d1.getCode())
                              : "0"))
                  * 2
              + ((d2.getPrestations().stream().anyMatch(p -> CODE_LPP.equals(p.getCode())))
                  ? -1
                  : codeLpp);
        });
    domains.forEach(
        it ->
            it.setPrestations(
                performPrestationsDto(it.getPrestations(), listeCodesLPP, listeCodesPrestation)));
  }

  /**
   * @param entry
   * @param listeCodesLPP
   * @param excludes
   * @return l'ensemble des prestations à partir de la list entry, en excluant celle déjà dans
   *     excludes (qui est mis à jour au fur et à mesure) et en traitant "LPP" en dernier
   */
  public List<PrestationDto> performPrestationsDto(
      List<PrestationDto> entry, List<String> listeCodesLPP, List<String> excludes) {
    List<PrestationDto> out = new ArrayList<>();
    entry.sort(this::isCodeLPP);
    for (PrestationDto prest : entry) {
      if (!excludes.contains(prest.getCode())) {
        if (CODE_LPP.equals(prest.getCode())) {
          out.addAll(createListePrestationDtoLPP(prest, listeCodesLPP, excludes));
          excludes.addAll(listeCodesLPP);
        } else {
          out.add(prest);
          excludes.add(prest.getCode());
        }
      }
    }
    return out;
  }

  private int isCodeLPP(PrestationDto d1, PrestationDto d2) {
    int codeLPP = CODE_LPP.equals(d2.getCode()) ? -1 : 0;
    return CODE_LPP.equals(d1.getCode()) ? 1 : codeLPP;
  }

  public void cumulDomainsDeclaration(Declaration decl) {
    List<ParametresDto> listDomaines = parametreBddServiceImpl.findByType(Constants.DOMAINE);
    List<String> domainsCumulAvecPlafond;
    List<String> domainsCumulSansPlafond;
    if (!listDomaines.isEmpty()) {
      domainsCumulAvecPlafond =
          listDomaines.stream()
              .filter(
                  item ->
                      ((ParametresDomaineDto) item).getIsCumulGaranties()
                          && ((ParametresDomaineDto) item).getIsCumulPlafonne())
              .map(ParametresDto::getCode)
              .toList();
      domainsCumulSansPlafond =
          listDomaines.stream()
              .filter(
                  item ->
                      ((ParametresDomaineDto) item).getIsCumulGaranties()
                          && !((ParametresDomaineDto) item).getIsCumulPlafonne())
              .map(ParametresDto::getCode)
              .toList();
    } else {
      domainsCumulAvecPlafond = new ArrayList<>();
      domainsCumulSansPlafond = new ArrayList<>();
    }

    Map<String, List<DomaineDroit>> regroupByPeriod =
        decl.getDomaineDroits().stream()
            .collect(
                Collectors.groupingBy(
                    domaine ->
                        domaine.getPeriodeDroit().getPeriodeDebut()
                            + domaine.getPeriodeDroit().getPeriodeFin(),
                    Collectors.toCollection(ArrayList::new)));
    List<DomaineDroit> cumulDomainsByPeriod = new ArrayList<>();
    for (List<DomaineDroit> groupedByPeriod : regroupByPeriod.values()) {
      cumulDomains(groupedByPeriod, domainsCumulAvecPlafond, true);
      cumulDomains(groupedByPeriod, domainsCumulSansPlafond, false);
      cumulDomainsByPeriod.addAll(groupedByPeriod);
    }
    decl.setDomaineDroits(cumulDomainsByPeriod);
  }

  public void cumulDomains(
      List<DomaineDroit> domains, List<String> cumul, Boolean isCumulPlafonne) {
    if (cumul.isEmpty()) {
      return;
    }

    domains.sort(
        (d1, d2) ->
            d2.getCode().equals(d1.getCode())
                ? compareGuaranteesPriority(d1, d2)
                : d1.compareTo(d2));
    List<DomaineDroit> listRemove = new ArrayList<>();
    for (int i = 0; i < domains.size() - 1; i++) {
      DomaineDroit dom = domains.get(i);
      int j = 1;
      DomaineDroit nexDom = domains.get(i + j);
      while (cumul.contains(dom.getCode())
          && dom.getCode().equals(nexDom.getCode())
          && dom.getUniteTauxRemboursement().equals(nexDom.getUniteTauxRemboursement())
          && ("PO".equals(dom.getUniteTauxRemboursement())
              || "TA".equals(dom.getUniteTauxRemboursement()))) {
        // Cumul
        cumulTwoDomains(dom, nexDom, isCumulPlafonne);
        listRemove.add(nexDom);
        j++;
        if (i + j < domains.size()) {
          nexDom = domains.get(i + j);
        } else {
          // on "saute" le(s) domaine(s) déjà cumulé(s) pour le for
          i += j - 1;
          break;
        }
      }
    }
    domains.removeAll(listRemove);
  }

  private int compareGuaranteesPriority(DomaineDroit d1, DomaineDroit d2) {
    if (d2.getPrioriteDroit().getCode().equals(d1.getPrioriteDroit().getCode())) {
      return Float.compare(
          Float.parseFloat(getFormuleValue(d2)), Float.parseFloat(getFormuleValue(d1)));
    }
    if (Constants.LOW_GUARANTEE_LEVEL.equals(
        beyondPropertiesService.getPropertyOrThrowError(LEVEL_OF_GUARANTEE))) {
      return d2.getPrioriteDroit().getCode().compareTo(d1.getPrioriteDroit().getCode());
    } else {
      return d1.getPrioriteDroit().getCode().compareTo(d2.getPrioriteDroit().getCode());
    }
  }

  private String getFormuleValue(DomaineDroit d2) {
    return (d2.getPrestations().isEmpty()
            || d2.getPrestations().get(0).getFormule().getParametres().isEmpty())
        ? "0"
        : d2.getPrestations().get(0).getFormule().getParametres().get(0).getValeur();
  }

  public void cumulTwoDomains(DomaineDroit domainA, DomaineDroit domainB, Boolean isCumulPlafonne) {
    List<Prestation> listPrestA = domainA.getPrestations();
    List<Prestation> listPrestB = domainB.getPrestations();
    DecimalFormat formatter = new DecimalFormat("000.00");
    if (listPrestA != null && listPrestB != null) {
      for (Prestation prestA : listPrestA) {
        for (Prestation prestB : listPrestB) {
          if (prestA.getCode().equals(prestB.getCode())
              && prestA.getFormule().getNumero().equals(prestB.getFormule().getNumero())) {
            cumulTwoDomainsValues(
                domainA, domainB, listPrestB, formatter, prestA, prestB, isCumulPlafonne);
            break;
          }
        }
      }
      listPrestA.addAll(listPrestB);
      domainA.setPrestations(listPrestA);
    }
  }

  private void cumulTwoDomainsValues(
      DomaineDroit domainA,
      DomaineDroit domainB,
      List<Prestation> listPrestB,
      DecimalFormat formatter,
      Prestation prestA,
      Prestation prestB,
      Boolean isCumulPlafonne) {
    List<Parametre> paramsB = prestB.getFormule().getParametres();
    if (!domainA.getPrioriteDroit().getCode().equals(domainB.getPrioriteDroit().getCode())) {
      int i = 0;
      for (Parametre paramA : prestA.getFormule().getParametres()) {
        Parametre paramB = paramsB.get(i);
        float val = Float.parseFloat(paramA.getValeur()) + Float.parseFloat(paramB.getValeur());
        if (Boolean.TRUE.equals(isCumulPlafonne) && val > 100F) {
          val = 100F;
        }
        paramA.setValeur(formatter.format(val).replace(",", "."));
        i++;
      }
    } else {
      List<Parametre> paramsA = prestA.getFormule().getParametres();
      if (paramsA.isEmpty()
          || Float.parseFloat(paramsA.get(0).getValeur())
              < Float.parseFloat(paramsB.get(0).getValeur())) {
        prestA.getFormule().setParametres(paramsB);
      }
    }
    listPrestB.remove(prestB);
  }

  /**
   * @param lpp
   * @param listeCodesLPP
   * @param excludes
   * @return liste de prestation créées à partir de la prestation lpp et de la liste listeCodesLPP
   *     en excluant ceux dans excludes
   */
  public List<PrestationDto> createListePrestationDtoLPP(
      PrestationDto lpp, List<String> listeCodesLPP, List<String> excludes) {
    List<PrestationDto> out = new ArrayList<>();
    for (String code : listeCodesLPP) {
      if (!excludes.contains(code)) {
        PrestationDto p = new PrestationDto(lpp);
        p.setCode(code);
        out.add(p);
      }
    }

    return out;
  }
}
