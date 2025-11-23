package com.cegedim.next.serviceeligibility.core.bobb.services;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb.TechnicalGuaranteeRequest;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.bobb.LotRequest;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.LotRepository;
import com.cegedim.next.serviceeligibility.core.dao.AlmerysProductReferentialRepository;
import com.cegedim.next.serviceeligibility.core.dao.LotDao;
import com.cegedim.next.serviceeligibility.core.dao.ParametrageCarteTPDaoImpl;
import com.cegedim.next.serviceeligibility.core.model.entity.LotResponse;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GuaranteeException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LotService {
  private final LotRepository repository;
  private final LotDao lotDao;
  private final ParametrageCarteTPDaoImpl parametrageCarteTPDaoImpl;
  private final AlmerysProductReferentialRepository almerysProductReferentialRepository;
  private final ContractElementRepository contractElementRepository;

  @ContinueSpan(log = "getLot")
  public LotResponse getLot(LotRequest request) {
    return repository.getLot(request);
  }

  @ContinueSpan(log = "getLotsById")
  public List<Lot> getLotsById(List<String> idLots) {
    return repository.getLotsById(idLots);
  }

  @ContinueSpan(log = "getLots")
  public List<Lot> getLots() {
    return repository.getLots();
  }

  @ContinueSpan(log = "getLotsByParametrageCarteTPActifs")
  public List<Lot> getLotsByParametrageCarteTPActifs() {
    List<String> idLots = parametrageCarteTPDaoImpl.getIdLotsInParametrageCarteTPActif();
    return repository.getLotsByParametrageCarteTPActifs(idLots);
  }

  @ContinueSpan(log = "getLotsAlmerysValides")
  public List<Lot> getLotsAlmerysValides() {
    List<String> listCode = almerysProductReferentialRepository.getIdLotsInAlmerysProduct();
    return repository.getLotsAlmerysValides(listCode);
  }

  @ContinueSpan(log = "createLot")
  public void createLot(Lot lot) {
    repository.createLot(lot);
  }

  @ContinueSpan(log = "updateLot")
  public void updateLot(Lot lot) {
    repository.updateLot(lot);
  }

  @ContinueSpan(log = "update already checked lot")
  public void update(Lot lot) {
    repository.update(lot);
  }

  @ContinueSpan(log = "getByCode lot")
  public Lot getByCode(String codeLot) {
    return lotDao.getByCode(codeLot);
  }

  @ContinueSpan(log = "deleteAll lots")
  public void deleteAllLots() {
    lotDao.deleteAllLots();
  }

  @ContinueSpan(log = "findAll Lots")
  public Collection<Lot> findAll() {
    return repository.getAll();
  }

  @ContinueSpan(log = "addGarantieTechniqueToLot")
  public void addGarantieTechniqueToLot(String lotCode, TechnicalGuaranteeRequest request) {
    validateLotAndGuaranteeExistence(lotCode, request);
    repository.addGarantieTechnique(lotCode, request);
  }

  @ContinueSpan(log = "dissociateGtFromLot")
  public void dissociateGtFromLot(String lotCode, TechnicalGuaranteeRequest request) {
    validateLotAndGuaranteeExistence(lotCode, request);
    repository.updateDateSuppressionLogiqueGT(lotCode, request);
  }

  private void validateLotAndGuaranteeExistence(String lotCode, TechnicalGuaranteeRequest request) {
    if (repository.findByCode(lotCode) == null) {
      throw new GuaranteeException(
          "Le lot " + lotCode + " est introuvable",
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_GUARANTEE);
    }
    if (!contractElementRepository.existsByKey(
        request.getGuaranteeCode(), request.getInsurerCode())) {
      throw new GuaranteeException(
          "La garantie technique "
              + request.getGuaranteeCode()
              + " n'existe pas pour l'assureur "
              + request.getInsurerCode(),
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_GUARANTEE);
    }
  }
}
