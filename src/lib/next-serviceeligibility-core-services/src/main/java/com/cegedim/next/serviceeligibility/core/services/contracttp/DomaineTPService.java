package com.cegedim.next.serviceeligibility.core.services.contracttp;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.SLASHED_FORMATTER;
import static java.util.stream.Collectors.groupingBy;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.services.pojo.CasDeclaration;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DomaineTPService {

  private final PeriodeDroitTPService periodeDroitTPService;

  protected HashMap<String, Set<DomaineDroitForConsolidation>> fillDomainMap(
      Declaration declaration) {
    HashMap<String, Set<DomaineDroitForConsolidation>> declMapDomain = new HashMap<>();
    List<DomaineDroit> domaineListInDeclaration = declaration.getDomaineDroits();
    CasDeclaration casDeclaration = getCasDeclaration(declaration);

    for (DomaineDroit domaineInDeclaration : domaineListInDeclaration) {
      String codeDomaineInDomaineDeclaration = domaineInDeclaration.getCode();

      // Offline
      DomaineDroitForConsolidation domaineDroitForConsolidation =
          domaineDroitForConsolidationFromDomaine(
              domaineInDeclaration, TypePeriode.OFFLINE, casDeclaration);

      Set<DomaineDroitForConsolidation> domaineDroitForConsolidationList =
          declMapDomain.get(codeDomaineInDomaineDeclaration);
      if (domaineDroitForConsolidationList == null) {
        domaineDroitForConsolidationList = new TreeSet<>();
        domaineDroitForConsolidationList.add(domaineDroitForConsolidation);
      } else {
        domaineDroitForConsolidationList.add(domaineDroitForConsolidation);
      }

      // Online
      if (domaineInDeclaration.getPeriodeOnline() != null
          && StringUtils.isNotBlank(domaineInDeclaration.getPeriodeOnline().getPeriodeDebut())) {
        DomaineDroitForConsolidation domaineDroitOnline =
            domaineDroitForConsolidationFromDomaine(
                domaineInDeclaration, TypePeriode.ONLINE, casDeclaration);
        domaineDroitForConsolidationList.add(domaineDroitOnline);
      }

      declMapDomain.put(codeDomaineInDomaineDeclaration, domaineDroitForConsolidationList);
    }
    return declMapDomain;
  }

  private CasDeclaration getCasDeclaration(Declaration declaration) {
    boolean isV = Constants.CODE_ETAT_VALIDE.equals(declaration.getCodeEtat());
    if (Constants.ORIGINE_DECLARATIONTDB.equals(TriggerUtils.getOrigineDeclaration(declaration))) {
      return isV ? CasDeclaration.TDB : CasDeclaration.TFD;
    }
    return isV ? CasDeclaration.OUVERTURE : CasDeclaration.FERMETURE;
  }

  private DomaineDroitForConsolidation domaineDroitForConsolidationFromDomaine(
      DomaineDroit domaineInDeclaration, TypePeriode typePeriode, CasDeclaration casDeclaration) {
    DomaineDroitForConsolidation domaineDroitForConsolidation = new DomaineDroitForConsolidation();
    domaineDroitForConsolidation.setCasDeclaration(casDeclaration);

    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setTypePeriode(typePeriode);
    if (TypePeriode.ONLINE.equals(typePeriode) && domaineInDeclaration.getPeriodeOnline() != null) {
      periodeDroitContractTP.setPeriodeDebut(
          domaineInDeclaration.getPeriodeOnline().getPeriodeDebut());
      periodeDroitContractTP.setPeriodeFin(domaineInDeclaration.getPeriodeOnline().getPeriodeFin());
      periodeDroitContractTP.setPeriodeFinFermeture(
          domaineInDeclaration.getPeriodeOnline().getPeriodeFermetureFin());
    } else {
      periodeDroitContractTP.setPeriodeDebut(
          domaineInDeclaration.getPeriodeDroit().getPeriodeDebut());
      periodeDroitContractTP.setPeriodeFin(domaineInDeclaration.getPeriodeDroit().getPeriodeFin());
      periodeDroitContractTP.setPeriodeFinFermeture(
          domaineInDeclaration.getPeriodeDroit().getPeriodeFermetureFin());
    }
    domaineDroitForConsolidation.setPeriodeDroitContractTP(periodeDroitContractTP);

    Garantie garantie = new Garantie();
    garantie.setCodeGarantie(domaineInDeclaration.getCodeGarantie());
    garantie.setLibelleGarantie(domaineInDeclaration.getLibelleGarantie());
    garantie.setCodeAssureurGarantie(domaineInDeclaration.getCodeAssureurGarantie());
    garantie.setCodeCarence(domaineInDeclaration.getCodeCarence());
    garantie.setCodeAssureurOrigine(domaineInDeclaration.getCodeAssureurOrigine());
    garantie.setCodeOrigine(domaineInDeclaration.getCodeOrigine());
    garantie.setDateAdhesionCouverture(domaineInDeclaration.getDateAdhesionCouverture());
    garantie.setProduits(new ArrayList<>());
    domaineDroitForConsolidation.setGarantie(garantie);

    Produit produit = new Produit();
    produit.setCodeProduit(domaineInDeclaration.getCodeProduit());
    produit.setLibelleProduit(domaineInDeclaration.getLibelleProduit());
    produit.setCodeExterneProduit(domaineInDeclaration.getCodeExterneProduit());
    produit.setLibelleExterneProduit(domaineInDeclaration.getLibelleExterne());
    produit.setCodeOc(domaineInDeclaration.getCodeOc());
    produit.setCodeOffre(domaineInDeclaration.getCodeOffre());
    produit.setReferencesCouverture(new ArrayList<>());
    domaineDroitForConsolidation.setProduit(produit);

    NaturePrestation naturePrestation = new NaturePrestation();
    if (TypePeriode.ONLINE.equals(typePeriode)
        && StringUtils.isNotBlank(domaineInDeclaration.getNaturePrestationOnline())) {
      naturePrestation.setNaturePrestation(domaineInDeclaration.getNaturePrestationOnline());
    } else {
      naturePrestation.setNaturePrestation(domaineInDeclaration.getNaturePrestation());
    }
    if (naturePrestation.getNaturePrestation() == null) {
      naturePrestation.setNaturePrestation(Constants.NATURE_PRESTATION_OTP);
    }

    naturePrestation.setConventionnements(new ArrayList<>());
    naturePrestation.setPrioritesDroit(new ArrayList<>());
    naturePrestation.setPrestations(new ArrayList<>());
    naturePrestation.setRemboursements(new ArrayList<>());
    naturePrestation.setPeriodesDroit(new ArrayList<>());
    domaineDroitForConsolidation.setNaturePrestation(naturePrestation);

    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    referenceCouverture.setFormulaMask(domaineInDeclaration.getFormulaMask());
    referenceCouverture.setReferenceCouverture(domaineInDeclaration.getReferenceCouverture());
    referenceCouverture.setNaturesPrestation(new ArrayList<>());
    domaineDroitForConsolidation.setReferenceCouverture(referenceCouverture);

    domaineDroitForConsolidation.setDomaineDroit(new DomaineDroit(domaineInDeclaration));

    return domaineDroitForConsolidation;
  }

  protected DomaineDroitContractTP createDomaineDroit(
      Set<DomaineDroitForConsolidation> declGarantiDomMap) {
    DomaineDroitContractTP newDomaineDroit = new DomaineDroitContractTP();
    SortedSet<DomaineDroitForConsolidation> newDeclGarantiDomMap =
        consolidationDomaineDroitEditable(declGarantiDomMap);
    // On récupère le premier élément de la liste du premier élément de la map
    DomaineDroitForConsolidation domainList = newDeclGarantiDomMap.first();
    DomaineDroit domaineDroit = new DomaineDroit();
    if (domainList != null) {
      domaineDroit = domainList.getDomaineDroit();
    }
    newDomaineDroit.setCode(domaineDroit.getCode());
    newDomaineDroit.setCodeExterne(domaineDroit.getCodeExterne());
    newDomaineDroit.setLibelle(domaineDroit.getLibelle());
    newDomaineDroit.setLibelleExterne(domaineDroit.getLibelleExterne());
    newDomaineDroit.setCodeProfil(domaineDroit.getCodeProfil());
    newDomaineDroit.setGaranties(
        Objects.requireNonNullElse(newDomaineDroit.getGaranties(), new ArrayList<>()));

    periodeDroitTPService.consolidatePeriods(newDomaineDroit, newDeclGarantiDomMap);

    return newDomaineDroit;
  }

  /**
   * contratDomToEdit : domaine droit du contrat en cours de modification declGarantiDomainMapToAdd
   * : map Garanti/Domaine à intégrer si contratDomToEdit null : initialiser mettre à jour
   * contratDomToEdit avec données du 1er élément de declGarantiDomainMapToAdd pour tous les
   * éléments de declGarantiDomainMapToAdd - si déclaration femerture : - chercher la période qui
   * "inclut" la date de fin du domaine à intégrer - modifier sa date de fin - si déclaration
   * ouverture ou modif : - chercher une période au moins contigüe - modifier date debut et/ou fin
   * -- pour les périodes suivantes, modifier la 1ere trouvée et supprimer l'autre
   */
  protected DomaineDroitContractTP updateDomaineDroit(
      DomaineDroitContractTP contratDomToEdit,
      Set<DomaineDroitForConsolidation> declGarantiDomainMapToAdd) {
    SortedSet<DomaineDroitForConsolidation> newDeclGarantiDomMap =
        consolidationDomaineDroitEditable(declGarantiDomainMapToAdd);

    // On récupère le premier élément de la liste du premier élément de la map
    DomaineDroitForConsolidation domainList = newDeclGarantiDomMap.first();
    DomaineDroit newDom = new DomaineDroit();
    if (domainList != null) {
      newDom = domainList.getDomaineDroit();
    }

    if (contratDomToEdit == null) {
      // create domaine droit
      contratDomToEdit = new DomaineDroitContractTP();
      contratDomToEdit.setCode(newDom.getCode());
      contratDomToEdit.setGaranties(new ArrayList<>());
    }

    contratDomToEdit.setCodeExterne(newDom.getCodeExterne());
    contratDomToEdit.setLibelle(newDom.getLibelle());
    contratDomToEdit.setLibelleExterne(newDom.getLibelleExterne());
    contratDomToEdit.setCodeProfil(newDom.getCodeProfil());
    periodeDroitTPService.consolidatePeriods(contratDomToEdit, newDeclGarantiDomMap);
    return contratDomToEdit;
  }

  private static SortedSet<DomaineDroitForConsolidation> consolidationDomaineDroitEditable(
      Set<DomaineDroitForConsolidation> declGarantiDomMap) {
    SortedSet<DomaineDroitForConsolidation> newDeclGarantiDomMap = new TreeSet<>();
    Map<String, List<DomaineDroitForConsolidation>> domaineByKey =
        declGarantiDomMap.stream()
            .collect(
                groupingBy(
                    DomaineDroitForConsolidation::mergeKey,
                    Collectors.toCollection(ArrayList::new)));

    for (Map.Entry<String, List<DomaineDroitForConsolidation>> entry : domaineByKey.entrySet()) {
      List<DomaineDroitForConsolidation> domaineDroitList = entry.getValue();
      // Sort pour les traiter dans l ordre croissant des dates debut
      domaineDroitList.sort(
          Comparator.comparing(DomaineDroitForConsolidation::getDeclPeriodeDebut));

      DomaineDroitForConsolidation domaineDroitForConsolidationToAdd = null;
      DomaineDroit domaineDroit = null;
      PeriodeDroit periodeDroit = null;

      for (DomaineDroitForConsolidation domaineDroitForConsolidation : domaineDroitList) {
        if (periodeDroit == null) {
          domaineDroitForConsolidationToAdd =
              new DomaineDroitForConsolidation(domaineDroitForConsolidation);
          periodeDroit = getPeriodeDroit(domaineDroitForConsolidation);
          domaineDroit = domaineDroitForConsolidation.getDomaineDroit();
        } else {
          boolean addDomaine = true;
          PeriodeDroit periodeDroitEnCours = getPeriodeDroit(domaineDroitForConsolidation);
          String debutEnCoursMoinsUn =
              DateUtils.getStringDatePlusDays(
                  periodeDroitEnCours.getPeriodeDebut(), -1, SLASHED_FORMATTER);

          if (debutEnCoursMoinsUn.equals(periodeDroit.getPeriodeFin())) {
            // Periode debut n°2 et fin n°1 contigues
            addDomaine = false;
            String debut =
                DateUtils.getMinDate(
                    periodeDroitEnCours.getPeriodeDebut(), periodeDroit.getPeriodeDebut());
            String fin =
                DateUtils.getMaxDateOrNull(
                    periodeDroitEnCours.getPeriodeFin(), periodeDroit.getPeriodeFin());

            periodeDroit.setPeriodeDebut(debut);
            periodeDroit.setPeriodeFin(fin);
          }

          if (debutEnCoursMoinsUn.equals(periodeDroit.getPeriodeFermetureFin())) {
            addDomaine = false;
            String newDeb =
                DateUtils.getMinDate(
                    periodeDroitEnCours.getPeriodeDebut(), periodeDroit.getPeriodeDebut());
            periodeDroit.setPeriodeDebut(newDeb);

            boolean prevReverse =
                DateUtils.isReverseDate(
                    periodeDroit.getPeriodeFermetureDebut(), periodeDroit.getPeriodeFermetureFin());
            boolean currReverse =
                DateUtils.isReverseDate(
                    periodeDroitEnCours.getPeriodeFermetureDebut(),
                    periodeDroitEnCours.getPeriodeFermetureFin());
            if (prevReverse) {
              // Si la periode precedente a des date fermetures inverses (a ne pas prendre en
              // compte) on recupere alors toujours les nouvelles
              periodeDroit.setPeriodeFermetureDebut(periodeDroitEnCours.getPeriodeFermetureDebut());
              periodeDroit.setPeriodeFermetureFin(periodeDroitEnCours.getPeriodeFermetureFin());
            } else if (!currReverse) {
              // Sinon si les deux ne sont pas reverse alors on fusionne les periodes
              String minFermetureDeb =
                  DateUtils.getMinDate(
                      periodeDroitEnCours.getPeriodeFermetureDebut(),
                      periodeDroit.getPeriodeFermetureDebut());
              String maxFermetureFin =
                  DateUtils.getMaxDateOrNull(
                      periodeDroitEnCours.getPeriodeFermetureFin(),
                      periodeDroit.getPeriodeFermetureFin());

              periodeDroit.setPeriodeFermetureDebut(minFermetureDeb);
              periodeDroit.setPeriodeFermetureFin(maxFermetureFin);
            }
          }

          if (addDomaine) {
            newDeclGarantiDomMap.add(domaineDroitForConsolidation);
          }
        }

        domaineDroitForConsolidationToAdd.setDomaineDroit(domaineDroit);
      }
      newDeclGarantiDomMap.add(domaineDroitForConsolidationToAdd);
    }
    return newDeclGarantiDomMap;
  }

  private static PeriodeDroit getPeriodeDroit(
      DomaineDroitForConsolidation domaineDroitForConsolidation) {
    DomaineDroit domaineDroit = domaineDroitForConsolidation.getDomaineDroit();
    if (domaineDroitForConsolidation.isOnline() && domaineDroit.getPeriodeOnline() != null) {
      return domaineDroit.getPeriodeOnline();
    }
    return domaineDroit.getPeriodeDroit();
  }
}
