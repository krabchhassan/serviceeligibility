package com.cegedim.next.serviceeligibility.core.services.common.batch;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdresseService {

  private final BeyondPropertiesService beyondPropertiesService;

  public Adresse getAdresseForCarte(List<DeclarationConsolide> declarationConsolideList) {
    Beneficiaire beneficiaireFound = getBeneficiaireForAdresse(declarationConsolideList);
    if (beneficiaireFound != null && !CollectionUtils.isEmpty(beneficiaireFound.getAdresses())) {
      // on n'a qu'une seule adresse par benef (celle du destinataire de releve de
      // prestation valide)
      Adresse adresse = beneficiaireFound.getAdresses().get(0);
      if (Constants.CLIENT_TYPE_OTP.equals(
          beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))) {
        if (adresse.getTypeAdresse() != null
            && Constants.ADRESSE_TYPE_GESTIONNAIRE.equals(adresse.getTypeAdresse().getType())) {
          return adresse;
        }
      } else {
        if (adresse.getTypeAdresse() != null
            && Constants.ADRESSE_TYPE_ADHERENT.equals(adresse.getTypeAdresse().getType())) {
          return adresse;
        }
      }
      return null;
    }
    return null;
  }

  private Beneficiaire getBeneficiaireForAdresse(
      List<DeclarationConsolide> declarationConsolideList) {
    List<DeclarationConsolide> sortedListByEffetDebut =
        declarationConsolideList.stream()
            .sorted(Comparator.comparing(DeclarationConsolide::getEffetDebut).reversed())
            .toList();
    DeclarationConsolide declarationWithAssurePrincipal =
        getDeclarationWithAssurePrincipal(sortedListByEffetDebut);
    if (declarationWithAssurePrincipal != null) {
      return declarationWithAssurePrincipal.getBeneficiaire();
    } else {
      DeclarationConsolide declarationWithLowestRangAdministratif =
          getDeclarationWithLowestRangAdministratif(sortedListByEffetDebut);
      if (declarationWithLowestRangAdministratif != null) {
        return declarationWithLowestRangAdministratif.getBeneficiaire();
      }
    }
    return null;
  }

  private DeclarationConsolide getDeclarationWithAssurePrincipal(
      List<DeclarationConsolide> declarationConsolideList) {
    return declarationConsolideList.stream()
        .filter(
            declarationConsolide ->
                Constants.QUALITE_A.equals(
                    declarationConsolide.getBeneficiaire().getAffiliation().getQualite()))
        .findFirst()
        .orElse(null);
  }

  private DeclarationConsolide getDeclarationWithLowestRangAdministratif(
      List<DeclarationConsolide> declarationConsolideList) {
    return declarationConsolideList.stream()
        .min(
            Comparator.comparing(
                declarationConsolide -> declarationConsolide.getContrat().getRangAdministratif()))
        .orElse(null);
  }

  public boolean checkAdresseExists(Adresse adresse) {
    return (adresse != null
        && (adresse.getLigne1() != null
            || adresse.getLigne2() != null
            || adresse.getLigne3() != null
            || adresse.getLigne4() != null
            || adresse.getLigne5() != null
            || adresse.getLigne6() != null
            || adresse.getLigne7() != null
            || adresse.getCodePostal() != null
            || adresse.getPays() != null));
  }
}
