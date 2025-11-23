package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscoParametrageDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscoParamDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.TranscoParametrage;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code TranscoParametrage}. */
@Service("transcoParametrageService")
public class TranscoParametrageServiceImpl extends GenericServiceImpl<TranscoParametrage>
    implements TranscoParametrageService {

  /**
   * public constructeur.
   *
   * @param transcoParametrageDao bean dao injecte
   */
  @Autowired
  public TranscoParametrageServiceImpl(
      @Qualifier("transcoParametrageDao")
          final IMongoGenericDao<TranscoParametrage> transcoParametrageDao) {
    super(transcoParametrageDao);
  }

  @Override
  @ContinueSpan(log = "getTranscoParametrageDao")
  public TranscoParametrageDao getTranscoParametrageDao() {
    return ((TranscoParametrageDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findTranscoParametrage")
  public TranscoParamDto findTranscoParametrage(final String codeObjetTransco) {
    final TranscoParametrage transco =
        this.getTranscoParametrageDao().findTranscoParametrage(codeObjetTransco);
    TranscoParamDto transcoDto = new TranscoParamDto();
    if (transco != null) {
      transcoDto = this.transcoParamEntityToDto(transco);
    }
    return transcoDto;
  }

  @Override
  @ContinueSpan(log = "findAllTranscoParametrage")
  public List<TranscoParamDto> findAllTranscoParametrage() {
    final List<TranscoParametrage> listeTransco =
        this.getTranscoParametrageDao().findAll(TranscoParametrage.class);
    final List<TranscoParamDto> listeTranscoDto = new ArrayList<>();
    if (listeTransco != null) {
      for (final TranscoParametrage transco : listeTransco) {
        final TranscoParamDto transcoDto = this.transcoParamEntityToDto(transco);
        listeTranscoDto.add(transcoDto);
      }
    }
    return listeTranscoDto;
  }

  @Override
  @ContinueSpan(log = "saveOrUpdate TranscoParamDto")
  public TranscoParamDto saveOrUpdate(final TranscoParamDto transcoParamDto) {
    final TranscoParametrage transcoParametrage =
        this.transcoParametrageDtoToEntity(transcoParamDto);

    // Searching transcoding parameter
    final TranscoParametrage existingTranscoParam =
        this.getTranscoParametrageDao()
            .findTranscoParametrage(transcoParamDto.getCodeObjetTransco());
    if (existingTranscoParam != null) {
      transcoParametrage.set_id(existingTranscoParam.get_id());
      this.getTranscoParametrageDao().update(transcoParametrage);
    } else {
      this.getTranscoParametrageDao().create(transcoParametrage);
    }

    return this.transcoParamEntityToDto(transcoParametrage);
  }

  @Override
  @ContinueSpan(log = "delete TranscoParamDto")
  public void delete(final String code) {
    final TranscoParametrage transcoParametrage =
        this.getTranscoParametrageDao().findTranscoParametrage(code);

    if (transcoParametrage != null) {
      this.getTranscoParametrageDao().delete(transcoParametrage);
    }
  }

  /**
   * Mapping transcoding parameter entity into Dto
   *
   * @param transcodingParameter Transcoding parameter entity
   * @return Transconding parameter Dto
   */
  private TranscoParamDto transcoParamEntityToDto(final TranscoParametrage transcodingParameter) {
    final TranscoParamDto transcoParamDto = new TranscoParamDto();
    transcoParamDto.setCodeObjetTransco(transcodingParameter.getCodeObjetTransco());
    transcoParamDto.setNomObjetTransco(transcodingParameter.getNomObjetTransco());
    transcoParamDto.setColNames(transcodingParameter.getColNames());

    return transcoParamDto;
  }

  /**
   * Mapping transcoding parameter Dto into entity
   *
   * @param transcodingParameterDto Transcoding parameter Dto
   * @return Transcoding parameter Entity
   */
  private TranscoParametrage transcoParametrageDtoToEntity(
      final TranscoParamDto transcodingParameterDto) {
    final TranscoParametrage transcoParametrage = new TranscoParametrage();
    transcoParametrage.setCodeObjetTransco(transcodingParameterDto.getCodeObjetTransco());
    transcoParametrage.setNomObjetTransco(transcodingParameterDto.getNomObjetTransco());
    transcoParametrage.setColNames(transcodingParameterDto.getColNames());

    return transcoParametrage;
  }
}
