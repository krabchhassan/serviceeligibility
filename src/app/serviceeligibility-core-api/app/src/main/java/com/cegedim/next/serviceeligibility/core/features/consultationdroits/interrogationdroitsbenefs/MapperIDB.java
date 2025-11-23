package com.cegedim.next.serviceeligibility.core.features.consultationdroits.interrogationdroitsbenefs;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract.DomaineDroitContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.VisiodroitUtils;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.IDBCLCStructMapper;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.utility.I18NService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeBeneficiaire;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.IDBResponse;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.PeriodeWithDateRenouv;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperIDB {

  private final IDBCLCStructMapper mapper;

  private final I18NService i18NService = new I18NService();

  public TypeCodeReponse createCodeReponseOK() {
    TypeCodeReponse typeCodeReponse = new TypeCodeReponse();
    typeCodeReponse.setCode(CodeReponse.OK.getCode());
    typeCodeReponse.setLibelle(i18NService.getMessage(CodeReponse.OK.getCode()));
    return typeCodeReponse;
  }

  public List<IDBResponse> mapIdbResponses(
      Pair<String, Periode> numContratWithPeriode,
      List<ContractDto> contractList,
      DemandeInfoBeneficiaire requestInfo,
      Declarant declarant) {
    List<IDBResponse> idbResponseList = new ArrayList<>();
    IDBResponse idbResponse = new IDBResponse();
    if (numContratWithPeriode != null) {
      Optional<ContractDto> contract =
          contractList.stream()
              .filter(
                  contractDto ->
                      numContratWithPeriode.getFirst().equals(contractDto.getContrat().getNumero()))
              .findFirst();
      if (contract.isPresent()) {
        ContractDto contractDto = contract.get();
        idbResponse.setDeclarantAmc(
            mapper.declarantDtoToDeclarantAmc(contractDto.getDeclarantAmc()));
        idbResponse.setBeneficiaire(mapBeneficiaire(contractDto.getBeneficiaire()));
        idbResponse.setContrat(mapper.contratDtoToTypeContrat(contractDto.getContrat()));
        setPeriodesDroits(numContratWithPeriode, requestInfo, declarant, contractDto, idbResponse);
        idbResponseList.add(idbResponse);
      }
    }

    return idbResponseList;
  }

  private static void setPeriodesDroits(
      Pair<String, Periode> numContratWithPeriode,
      DemandeInfoBeneficiaire requestInfo,
      Declarant declarant,
      ContractDto contractDto,
      IDBResponse idbResponse) {
    List<String> codeDomainesDroitsContrat =
        contractDto.getDomaineDroits().stream()
            .map(DomaineDroitContratDto::getCode)
            .collect(Collectors.toList());

    // Si dans la requête, on n'a pas demandé le domaine HOSP => ce n'est pas un
    // appel pour ROC => pas de dateRenouvellement
    if (!Constants.HOSP.equals(requestInfo.getSegmentRecherche())
        && !requestInfo.getListeSegmentRecherche().contains(Constants.HOSP)) {
      setPeriodesDroitsWithoutDateRenouv(numContratWithPeriode, idbResponse);
    } else if (!codeDomainesDroitsContrat.contains(Constants.HOSP)) {
      // Si dans la requête, on a demandé HOSP mais que le domaine n'est pas dans le
      // contrat sélectionné => pas de dateRenouvellement
      setPeriodesDroitsWithoutDateRenouv(numContratWithPeriode, idbResponse);
    } else {
      // Sinon, on calcule la dateRenouvellement
      PeriodeWithDateRenouv periodeWithDateRenouv = new PeriodeWithDateRenouv();
      String dateRenouvellement = VisiodroitUtils.computeDateRenouvellement(declarant);
      if (StringUtils.isNotBlank(dateRenouvellement)) {
        periodeWithDateRenouv.setDebut(
            DateUtils.formatDate(numContratWithPeriode.getSecond().getDebut()));
        periodeWithDateRenouv.setFin(
            DateUtils.formatDate(numContratWithPeriode.getSecond().getFin()));
        dateRenouvellement =
            restrainDateRenouv(requestInfo, periodeWithDateRenouv.getFin(), dateRenouvellement);
        periodeWithDateRenouv.setDateRenouvellement(DateUtils.formatDate(dateRenouvellement));
      } else {
        String dateRenouvellementSansParam =
            VisiodroitUtils.computeDateRenouvellementSansParametrage(
                DateUtils.parseDate(
                    numContratWithPeriode.getSecond().getFin(), DateUtils.FORMATTERSLASHED),
                requestInfo.getDateFin());
        periodeWithDateRenouv.setDebut(
            DateUtils.formatDate(numContratWithPeriode.getSecond().getDebut()));
        periodeWithDateRenouv.setFin(
            DateUtils.formatDate(numContratWithPeriode.getSecond().getFin()));

        periodeWithDateRenouv.setDateRenouvellement(
            DateUtils.formatDate(dateRenouvellementSansParam));
      }
      idbResponse.setPeriodesDroits(List.of(periodeWithDateRenouv));
    }
  }

  private static String restrainDateRenouv(
      DemandeInfoBeneficiaire requestInfo, String periodeFin, String dateRenouvellement) {
    String finDemande = DateUtils.formatDate(requestInfo.getDateFin());
    if (DateUtils.beforeAnyFormat(periodeFin, finDemande)) {
      return periodeFin;
    }
    return dateRenouvellement;
  }

  private static void setPeriodesDroitsWithoutDateRenouv(
      Pair<String, Periode> numContratWithPeriode, IDBResponse idbResponse) {
    Periode periode =
        new Periode(
            DateUtils.formatDate(numContratWithPeriode.getSecond().getDebut()),
            DateUtils.formatDate(numContratWithPeriode.getSecond().getFin()));
    idbResponse.setPeriodesDroits(List.of(periode));
  }

  private TypeBeneficiaire mapBeneficiaire(BeneficiaireDto beneficiaireDto) {
    TypeBeneficiaire typeBeneficiaire = mapper.beneficiaireDtoToTypeBeneficiaire(beneficiaireDto);
    typeBeneficiaire
        .getHistoriqueAffiliations()
        .add(mapper.affiliationDtoToTypeHistoriqueAffiliation(beneficiaireDto.getAffiliation()));
    return typeBeneficiaire;
  }
}
