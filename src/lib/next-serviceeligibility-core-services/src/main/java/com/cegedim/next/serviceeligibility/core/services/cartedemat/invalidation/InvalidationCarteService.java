package com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation;

import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DeclarationConsolideUtils;
import com.mongodb.client.ClientSession;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvalidationCarteService {

  @Autowired private CarteDematDao carteDematDao;

  @Autowired private EventService eventService;

  public boolean shouldInvalidCarte(
      CarteDemat carte, String dateDebutMinimum, String dateFinMinimum) {
    return carte.getPeriodeFin() != null
        && dateDebutMinimum != null
        && dateFinMinimum != null
        && (carte.getPeriodeFin().compareTo(dateFinMinimum) >= 0
            || carte.getPeriodeFin().compareTo(dateDebutMinimum) > 0);
  }

  public boolean shouldInvalidCarteOTP(
      CarteDemat carte, String dateDebutMinimum, String dateFinMinimum) {
    return carte.getPeriodeFin() != null
        && dateDebutMinimum != null
        && dateFinMinimum != null
        && carte.getPeriodeFin().compareTo(dateDebutMinimum) >= 0
        && carte.getPeriodeDebut().compareTo(dateFinMinimum) <= 0;
  }

  public <T> List<CarteDemat> invalidationCartes(
      List<CarteDemat> existingCarteDemat,
      List<T> selectedDeclarations,
      Function<T, List<DomaineDroit>> getDomaineDroits,
      String clientType,
      List<Periode> periodeSuspensionList) {
    List<CarteDemat> invalidated = new ArrayList<>();
    List<DomaineDroit> flatDomainesDroits =
        selectedDeclarations.stream()
            .flatMap(decl -> getDomaineDroits.apply(decl).stream())
            .toList();
    String dateDebutMin =
        DeclarationConsolideUtils.getDateDebutMinimumOnDomaineDroits(flatDomainesDroits);
    String dateFinMin =
        DeclarationConsolideUtils.getDateFinMinimumOnDomaineDroits(flatDomainesDroits, clientType);
    if (Constants.CLIENT_TYPE_INSURER.equals(clientType)
        && isLeveeSuspension(periodeSuspensionList)) {
      List<DomaineDroit> domaineDroitsEditable =
          flatDomainesDroits.stream().filter(DomaineDroit::getIsEditable).toList();
      dateDebutMin =
          DeclarationConsolideUtils.getDateDebutMinimumOnDomaineDroits(domaineDroitsEditable);
      dateFinMin =
          DeclarationConsolideUtils.getDateFinMinimumOnDomaineDroits(
              domaineDroitsEditable, clientType);
    }
    for (CarteDemat carte : existingCarteDemat) {
      if (Constants.CLIENT_TYPE_OTP.equals(clientType)) {
        String dateFinMaxOTP =
            DeclarationConsolideUtils.getDateFinFermetureMaxOnDomaineDroits(flatDomainesDroits);
        if (shouldInvalidCarteOTP(carte, dateDebutMin, dateFinMaxOTP)) {
          carte.setIsLastCarteDemat(false);
          invalidated.add(carte);
        }
      } else if (shouldInvalidCarte(carte, dateDebutMin, dateFinMin)) {
        carte.setIsLastCarteDemat(false);
        invalidated.add(carte);
      }
    }

    return invalidated;
  }

  private boolean isLeveeSuspension(List<Periode> periodeSuspensionList) {
    if (CollectionUtils.isNotEmpty(periodeSuspensionList)) {
      return periodeSuspensionList.stream().anyMatch((periode -> periode.getFin() != null));
    } else {
      return false;
    }
  }

  public void saveInvalid(List<CarteDemat> carteDemats, ClientSession session) {
    carteDematDao.updateIsLastCarteAll(carteDemats, session);
  }

  public void sendEventInvalidCards(List<CarteDemat> carteDemats) {
    for (CarteDemat carte : carteDemats) {
      if (carte.getCodeServices() == null
          || carte.getCodeServices().contains(Constants.CARTE_DEMATERIALISEE)) {
        eventService.sendObservabilityEventCarteDematDesactivated(carte);
      }
    }
  }

  public List<CarteDemat> getLastCartesByAmcContrats(String amcContrats) {
    return carteDematDao.getLastCartesByAmcContrats(amcContrats);
  }
}
