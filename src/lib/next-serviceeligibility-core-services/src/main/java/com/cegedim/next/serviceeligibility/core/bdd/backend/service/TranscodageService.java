package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscodageDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.ServicesTranscoDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscodageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscodageListDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Transcodage}. */
public interface TranscodageService extends GenericService<Transcodage> {

  /**
   * @return La DAO des transcodage
   */
  TranscodageDao getTranscodageDao();

  /**
   * Trouve trascodage pour une cle
   *
   * @param codeService code service.
   * @param codeObjetTransco code Objet Transco.
   * @return transco list
   */
  TranscodageListDto findTranscoByCle(String codeService, String codeObjetTransco);

  /**
   * Trouve le code trascodage pour une cle
   *
   * @param codeService code service.
   * @param codeObjetTransco code Objet Transco.
   * @return code transco.
   */
  String findCodeByCle(String codeService, String codeObjetTransco);

  /**
   * MAJ de la liste des transcodages.
   *
   * @param transcodageDto transcodage dto
   */
  void updateTranscodage(TranscodageListDto transcodageDto);

  /**
   * Effacer transcodage
   *
   * @param transcodageDto transcodage dto
   */
  void deleteTranscodage(TranscodageDto transcodageDto);

  /**
   * Trouve la liste des services
   *
   * @return la liste des codes services.
   */
  List<ServicesTranscoDto> getCodesServiceTranscodage();
}
