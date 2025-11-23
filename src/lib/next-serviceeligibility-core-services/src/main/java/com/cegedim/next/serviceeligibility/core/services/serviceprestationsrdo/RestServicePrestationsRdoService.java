package com.cegedim.next.serviceeligibility.core.services.serviceprestationsrdo;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.RDO_KEY_SEPARATOR;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.RDOServicePrestationDAO;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.dto.ServicePrestationsRdoDto;
import com.cegedim.next.serviceeligibility.core.dto.ServicePrestationsRdoV2Dto;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.RDOGroup;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.ServicePrestationsRdo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service("servicePrestationsRdo")
public class RestServicePrestationsRdoService {

  public static final int KEY_SIZE_WITH_CONTRACTS = 6;
  public static final int KEY_SIZE_WITHOUT_CONTRACTS = 5;

  private final BeyondPropertiesService beyondPropertiesService;
  private final ServicePrestationDao dao;
  private final RDOServicePrestationDAO rdoServicePrestationDAO;

  public RestServicePrestationsRdoService(
      BeyondPropertiesService beyondPropertiesService,
      ServicePrestationDao dao,
      RDOServicePrestationDAO rdoServicePrestationDAO) {
    this.beyondPropertiesService = beyondPropertiesService;
    this.dao = dao;
    this.rdoServicePrestationDAO = rdoServicePrestationDAO;
  }

  public ServicePrestationsRdoDto getServicePrestationsRdo(
      String idDeclarant,
      String numeroAdherent,
      String dateNaissance,
      String rangNaissance,
      String nir,
      List<String> contractNumberList) {
    final ServicePrestationsRdoDto servicePrestationsRdo = new ServicePrestationsRdoDto();
    servicePrestationsRdo.setContrats(
        dao.findServicePrestationsRdo(
            idDeclarant, numeroAdherent, dateNaissance, rangNaissance, nir, contractNumberList));
    return servicePrestationsRdo;
  }

  public List<ServicePrestationsRdoV2Dto> getServicePrestationsRdoV2(List<String> body) {
    List<ServicePrestationsRdoV2Dto> servicePrestationsRdoV2Dtos = new ArrayList<>();
    for (String key : body) {

      String[] splittedKey =
          key.split(
              Pattern.quote(beyondPropertiesService.getProperty(RDO_KEY_SEPARATOR).orElse("#")));
      ServicePrestationsRdoV2Dto servicePrestationsRdoV2Dto = new ServicePrestationsRdoV2Dto();
      servicePrestationsRdoV2Dto.setKey(key);

      if (splittedKey.length < KEY_SIZE_WITHOUT_CONTRACTS
          || splittedKey.length > KEY_SIZE_WITH_CONTRACTS) {
        // La clé n'a pas le bon format, on renvoit une liste de contrat vide
        servicePrestationsRdoV2Dto.setContrats(new ArrayList<>());
      } else if (splittedKey.length == KEY_SIZE_WITH_CONTRACTS) {
        // La clé contient la liste des contrats, on ne renvoit que les contrats de
        // cette liste
        String[] contractNumbers = splittedKey[KEY_SIZE_WITH_CONTRACTS - 1].split(",");
        String keyWithoutContracts =
            Arrays.stream(splittedKey, 0, KEY_SIZE_WITHOUT_CONTRACTS)
                .collect(
                    Collectors.joining(
                        beyondPropertiesService.getProperty(RDO_KEY_SEPARATOR).orElse("#")));
        RDOGroup rdoServicePrestation =
            rdoServicePrestationDAO.getRDOGroupById(keyWithoutContracts);

        if (rdoServicePrestation == null) {
          // Pas de document correspondant à la clé, on renvoit une liste de contrat vide
          servicePrestationsRdoV2Dto.setContrats(new ArrayList<>());
        } else {
          // Filtre sur les contrats
          List<ServicePrestationsRdo> servicePrestationsRdoList =
              rdoServicePrestation.getServicePrestationsRdo().stream()
                  .filter(
                      servicePrestationsRdo ->
                          Arrays.asList(contractNumbers)
                              .contains(servicePrestationsRdo.getNumero()))
                  .toList();
          servicePrestationsRdoV2Dto.setContrats(servicePrestationsRdoList);
        }
      } else {
        // La clé ne contient pas la liste des contrats, on renvoit tous les contrats
        RDOGroup rdoServicePrestation = rdoServicePrestationDAO.getRDOGroupById(key);
        if (rdoServicePrestation == null) {
          // Pas de document correspondant à la clé, on renvoit une liste de contrat vide
          servicePrestationsRdoV2Dto.setContrats(new ArrayList<>());
        } else {
          servicePrestationsRdoV2Dto.setContrats(rdoServicePrestation.getServicePrestationsRdo());
        }
      }
      servicePrestationsRdoV2Dtos.add(servicePrestationsRdoV2Dto);
    }
    return servicePrestationsRdoV2Dtos;
  }

  public void createServicePrestationsRdo(RDOGroup rdoGroup) {
    rdoServicePrestationDAO.createRDOGroup(rdoGroup);
  }

  public void deleteAll() {
    rdoServicePrestationDAO.deleteAll();
  }
}
