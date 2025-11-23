package com.cegedim.next.serviceeligibility.core.business.carte.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeCarteDemat;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat.MapperCarteDemat;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.UniteDomainesDroitsEnum;
import com.cegedim.next.serviceeligibility.core.model.domain.ValiditeDomainesDroits;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.query.CriteresRechercheCarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code Declaration}. */
@Service("carteDematService")
public class CarteDematServiceImpl extends GenericServiceImpl<CarteDemat>
    implements CarteDematService {

  @Autowired private MapperCarteDemat mapperCarteDemat;

  @Autowired private DeclarantService declarantService;

  @Autowired private CarteDematDao carteDematDao;

  /**
   * public constructeur.
   *
   * @param carteDematDao bean dao injecte
   */
  @Autowired
  public CarteDematServiceImpl(
      @Qualifier("carteDematDao") final IMongoGenericDao<CarteDemat> carteDematDao) {
    super(carteDematDao);
  }

  @Override
  @ContinueSpan(log = "getCartesDemat")
  public List<CarteDematDto> getCartesDemat(final DemandeCarteDemat demande, final boolean isV2) {

    final CriteresRechercheCarteDemat criteres =
        new CriteresRechercheCarteDemat(
            demande.getNumeroAMC(), demande.getNumeroContrat(), demande.getDateReference());
    final List<CarteDemat> cartesList;
    if (Constants.NUMERO_AON.equals(criteres.getNumeroAMC())) {
      cartesList = carteDematDao.findCartesDematByAdherent(criteres);
    } else {
      cartesList = carteDematDao.findCartesDematByCriteria(criteres);
    }
    if (cartesList != null) {
      if (isV2) {
        this.limiteDomainListeCarteByParam(cartesList, demande.getDateReference());
      }

      final List<CarteDematDto> cartesListDto =
          this.mapperCarteDemat.entityListToDtoList(cartesList, null, false, false, null);
      cartesListDto.sort((c1, c2) -> c1.getPeriodeFin().compare(c2.getPeriodeFin()));
      return cartesListDto;
    }
    return new ArrayList<>();
  }

  private void limiteDomainListeCarteByParam(final List<CarteDemat> cartes, final String dateRef) {
    for (final CarteDemat carte : cartes) {
      this.limiteDomainByParam(carte, dateRef);
    }
  }

  private void limiteDomainByParam(final CarteDemat carte, final String dateRef) {
    // recup parametrage
    final Declarant declarant = this.declarantService.findById(carte.getIdDeclarant());
    if (CollectionUtils.isEmpty(declarant.getPilotages())) {
      return;
    }
    final var pilotageList =
        declarant.getPilotages().stream()
            .filter(x -> "CARTE-DEMATERIALISEE".equals(x.getCodeService()))
            .toList();
    if (CollectionUtils.isEmpty(pilotageList)) {
      return;
    }
    final Pilotage pil = pilotageList.get(0);
    if (pil == null || pil.getCaracteristique() == null) {
      return;
    }
    final List<ValiditeDomainesDroits> validites =
        pil.getCaracteristique().getValiditesDomainesDroits();
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    for (final BenefCarteDemat b : carte.getBeneficiaires()) {
      if (CollectionUtils.isNotEmpty(validites)) {
        for (final DomaineDroit dd : b.getDomainesCouverture()) {
          final ValiditeDomainesDroits validite = this.getValidite(validites, dd.getCode());
          if (validite != null) {
            final List<LocalDate> listDate = new ArrayList<>();
            final String dateFin = dd.getPeriodeDroit().getPeriodeFin();
            if (StringUtils.isNotBlank(dateFin)) {
              final LocalDate ldf = LocalDate.parse(dateFin, dtf);
              listDate.add(ldf);
            }

            final LocalDate localDateRef = LocalDate.parse(dateRef, dtf);
            LocalDate dateRefPlus;

            if (UniteDomainesDroitsEnum.Mois.equals(validite.getUnite())) {
              dateRefPlus = localDateRef.plusMonths(validite.getDuree());
              if (validite.isPositionnerFinDeMois()) {
                dateRefPlus = YearMonth.from(dateRefPlus).atEndOfMonth();
              }
            } else {
              dateRefPlus = localDateRef.plusDays(validite.getDuree());
            }
            listDate.add(dateRefPlus);

            final LocalDate dateFinNew =
                listDate.stream().min(LocalDate::compareTo).orElse(LocalDate.now(ZoneOffset.UTC));
            dd.getPeriodeDroit().setPeriodeFin(dateFinNew.format(dtf));
          }
        }
      }
    }
  }

  private ValiditeDomainesDroits getValidite(
      final List<ValiditeDomainesDroits> list, final String code) {
    for (final ValiditeDomainesDroits v : list) {
      if (code.equals(v.getCodeDomaine())) {
        return v;
      }
    }
    return null;
  }
}
