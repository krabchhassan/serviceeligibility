package com.cegedim.next.serviceeligibility.core.bdd.webservice.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VisioDroitsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(VisioDroitsService.class);

  private static final String DEF_CODE = "DEF";

  @Autowired private ParametreBddService paramService;

  Map<String, List<ParametresPrestationDto>> parametres = new HashMap<>();

  public void emptyCache() {
    parametres.clear();
  }

  private List<PrestationDto> generatePrestations(
      List<ParametresPrestationDto> params, PrestationDto defPrestation) {
    List<PrestationDto> list = new ArrayList<>();
    for (ParametresDto param : params) {
      PrestationDto p = new PrestationDto();
      p.setCode(param.getCode());
      p.setCodeRegroupement(defPrestation.getCodeRegroupement());
      p.setDateEffet(defPrestation.getDateEffet());
      p.setFormule(defPrestation.getFormule());
      p.setFormuleMetier(defPrestation.getFormuleMetier());
      p.setIsEditionRisqueCarte(defPrestation.getIsEditionRisqueCarte());
      p.setLibelle(param.getLibelle());
      list.add(p);
    }
    return list;
  }

  private List<ParametresPrestationDto> getParams(String code) {
    if (!parametres.containsKey(code)) {
      List<ParametresPrestationDto> list =
          paramService.findPrestationsByDomaine(UriConstants.PRESTATIONS, code);
      if (CollectionUtils.isEmpty(list)) {
        LOGGER.warn("No parametrage found for {}", code);
      } else {
        parametres.put(code, list);
        return list;
      }
    } else {
      return parametres.get(code);
    }
    return new ArrayList<>();
  }

  /**
   * Filtre la liste des declarationDtos en ne gardant que les dernieres declarations Base et Surco.
   * Cette methode suppose que la liste en parametre soit deja triee par <b>priorite droit</b>
   * {@code asc}, <b>numero contrat</b> {@code asc}, <b>priorite conventionnement</b> {@code asc} et
   * <b>date debut effet declaration</b> {@code asc}. Dans le cas de <b>recherche segments
   * multiple</b>, on corrige le code domaine en <b>XXX</b>. Pour la version 1 du WS consultation,
   * on ne montre pas le tag <b>reference_couverture</b> dans les domaines.
   *
   * @param declarationDtos la liste des declarations dto.
   * @param typeRechercheSegment type recherche segment
   */
  @ContinueSpan(log = "checkBaseSurco")
  public void checkBaseSurco(
      List<DeclarationDto> declarationDtos,
      TypeRechercheSegmentService typeRechercheSegment,
      List<String> listSegmentRecherche) {

    Map<String, DeclarationDto> selectedDeclarationDtos = new HashMap<>();
    for (DeclarationDto declarationDto : declarationDtos) {
      String cle = getDeclarationKey(declarationDto);
      // Si nombre des selectedDeclarationDtos ne change pas
      // apres selectedDeclarationDtos.put(...
      // alors on ecrase la declaration(newKey).
      // Dans le cas de recherche segments multiple
      // on corrige le code domaine en "XXX"
      // et preserve la liste des prestations pour la cle
      int nbrDeclarationAvant = selectedDeclarationDtos.size();
      DeclarationDto declarationCle = selectedDeclarationDtos.get(cle);
      selectedDeclarationDtos.put(cle, declarationDto);
      int nbrDeclarationApres = selectedDeclarationDtos.size();
      DomaineDroitDto domaine = declarationDto.getDomaineDroits().get(0);
      replaceDefPrestations(domaine);
      // Si type segment recherche est LISTE_SEGMENT
      if (shouldCompareCodePrestation(
          typeRechercheSegment, listSegmentRecherche, nbrDeclarationAvant, nbrDeclarationApres)) {
        domaine.setCode(Constants.CODE_DOMAINE_MULTIPLE);

        for (DomaineDroitDto dom : declarationCle.getDomaineDroits()) {

          // Preserve la liste des prestations pour la cle
          for (PrestationDto prestation : dom.getPrestations()) {
            String code = prestation.getCode();
            // Si la prestation ne se trouve pas dans le domaine
            if (!hasCodePrestation(code, domaine)) {
              domaine.getPrestations().add(prestation);
            }
          }
        }
      }
    }

    declarationDtos.clear();
    declarationDtos.addAll(new ArrayList<>(selectedDeclarationDtos.values()));
  }

  private boolean shouldCompareCodePrestation(
      TypeRechercheSegmentService typeRechercheSegment,
      List<String> listSegmentRecherche,
      int nbrDeclarationAvant,
      int nbrDeclarationApres) {
    return TypeRechercheSegmentService.LISTE_SEGMENT.equals(typeRechercheSegment)
        && listSegmentRecherche.size() > 1
        && nbrDeclarationAvant == nbrDeclarationApres;
  }

  private void replaceDefPrestations(DomaineDroitDto dom) {
    PrestationDto defPrestation = null;
    List<PrestationDto> prestations = new ArrayList<>();
    // Preserve la liste des prestations pour la cle
    for (PrestationDto prestation : dom.getPrestations()) {
      String code = prestation.getCode();
      if (DEF_CODE.equalsIgnoreCase(code)) {
        defPrestation = prestation;
      } else {
        prestations.add(prestation);
      }
    }
    if (defPrestation != null) {
      List<ParametresPrestationDto> params = getParams(dom.getCode());
      if (params != null && !params.isEmpty()) {
        List<PrestationDto> generatedPrestations = generatePrestations(params, defPrestation);
        prestations.addAll(generatedPrestations);
      } else {
        prestations.add(defPrestation);
      }
    }
    dom.setPrestations(prestations);
  }

  /**
   * Trouve la cle prioriteDroit-numContrat-prioriteConv
   *
   * @param declarationDto declaration
   * @return la cle prioriteDroit-numContrat-prioriteConv
   */
  private static String getDeclarationKey(DeclarationDto declarationDto) {

    String prioriteConventionnement;
    String prioriteDroit = declarationDto.getDomaineDroits().get(0).getPrioriteDroit().getCode();
    String numeroContrat = declarationDto.getContrat().getNumero();
    if (declarationDto.getDomaineDroits().get(0).getConventionnements() == null
        || declarationDto.getDomaineDroits().get(0).getConventionnements().isEmpty()) {
      prioriteConventionnement = "0";
    } else {
      prioriteConventionnement =
          String.valueOf(
              declarationDto.getDomaineDroits().get(0).getConventionnements().get(0).getPriorite());
    }
    return prioriteDroit + "-" + numeroContrat + "-" + prioriteConventionnement;
  }

  /**
   * Trouve si le code prestation existe dans le domaine
   *
   * @param codePrestation code prestation à chercher
   * @param domaineDto domaine à verifier
   * @return true si codePrestation existe dans le domaine
   */
  private static boolean hasCodePrestation(String codePrestation, DomaineDroitDto domaineDto) {
    boolean hasPrestation = false;
    for (PrestationDto prestation : domaineDto.getPrestations()) {
      if (prestation.getCode().equals(codePrestation)) {
        hasPrestation = true;
        break;
      }
    }
    return hasPrestation;
  }
}
