package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.RDO_KEY_SEPARATOR;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.RDOServicePrestationDAO;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapper;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.RDOGroup;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ServicePrestationsRdo;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RDOServicePrestationService {
  private final RDOServicePrestationDAO rdoServicePrestationDAO;
  private final AssureRdoServicePrestationsMapper assureRdoServicePrestationsMapper;
  private final BeyondPropertiesService beyondPropertiesService;

  public int upsertRdo(ContratAIV6 contrat) {
    String insuredId = contrat.getIdDeclarant();
    String subscriberId = contrat.getNumeroAdherent();
    Map<String, List<ServicePrestationsRdo>> servPresRdoByKeys = new HashMap<>();
    for (Assure assure : contrat.getAssures()) {
      String birthDate = assure.getIdentite().getDateNaissance();
      String birthRank = assure.getIdentite().getRangNaissance();

      for (String nir : getNirs(assure)) {
        ServicePrestationsRdo rdo = new ServicePrestationsRdo();
        rdo.setIdDeclarant(insuredId);
        rdo.setNumero(contrat.getNumero());
        rdo.setNumeroAdherent(subscriberId);
        rdo.setOrdrePriorisation(contrat.getOrdrePriorisation());
        rdo.setDateResiliation(contrat.getDateResiliation());
        rdo.setDateSouscription(contrat.getDateSouscription());
        rdo.setAssure(
            assureRdoServicePrestationsMapper.assureToAssureRdoServicePrestations(assure));

        String rdoKey = generateKey(insuredId, subscriberId, nir, birthDate, birthRank);
        servPresRdoByKeys.putIfAbsent(rdoKey, new ArrayList<>());
        servPresRdoByKeys.get(rdoKey).add(rdo);
      }
    }

    List<RDOGroup> rdoToUpserts =
        servPresRdoByKeys.entrySet().stream()
            .map(
                entry -> {
                  RDOGroup rdoGroup = new RDOGroup();
                  rdoGroup.set_id(entry.getKey());
                  rdoGroup.setServicePrestationsRdo(entry.getValue());
                  return rdoGroup;
                })
            .toList();

    rdoServicePrestationDAO.upsertMulti(rdoToUpserts);
    return rdoToUpserts.size();
  }

  public long deleteByAMC(String idAMC) {
    return rdoServicePrestationDAO.deleteByAMC(idAMC);
  }

  /**
   * Retourne un Set du nir principal si present ansi que des nir affiliation ro differents du
   * nirPrincipal
   */
  private Set<String> getNirs(Assure assure) {
    Set<String> nirs = new LinkedHashSet<>();
    Nir nirPrincipal = assure.getIdentite().getNir();
    if (nirPrincipal != null) {
      nirs.add(nirPrincipal.getCode());
    }
    for (com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO nirRattachementRO :
        ListUtils.emptyIfNull(assure.getIdentite().getAffiliationsRO())) {
      Nir affNir = nirRattachementRO.getNir();
      if (affNir != null) {
        nirs.add(affNir.getCode());
      }
    }

    return nirs;
  }

  private String generateKey(
      String insuredId, String subscriberId, String nir, String birthDate, String birthRank) {
    StringJoiner joiner =
        new StringJoiner(beyondPropertiesService.getProperty(RDO_KEY_SEPARATOR).orElse("#"));
    joiner.add(insuredId).add(subscriberId).add(nir).add(birthDate).add(birthRank);
    return joiner.toString();
  }
}
