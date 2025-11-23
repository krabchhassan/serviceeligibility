package com.cegedim.next.serviceeligibility.core.elastic;

import static org.mockito.Mockito.*;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.elast.BenefElasticPageResult;
import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import com.cegedim.next.serviceeligibility.core.elast.BenefSearchRequest;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.HistoriqueDateRangNaissance;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class BenefElasticServiceTest {

  /**
   * Pour un bénéficiaire avec comme date de naissance et rang 10/04/2008 - 1 et 01/04/2008 - 2. Cas
   * 1 : 10/04/2008 - 1 : retrouvera le bénéficiaire Cas 2 : 01/04/2008 - 2 : retrouvera le
   * bénéficiaire Cas 3 : 10/04/2008 - 2 : ne retrouvera pas le bénéficiaire Cas 4 : 01/04/2008 - 1
   * : ne retrouvera pas le bénéficiaire
   */
  @Test
  void searchBenefWithHistoDateRangNaissanceTest() {
    BenefElasticService benefElasticService = mock(BenefElasticService.class);

    when(benefElasticService.search(any(BenefSearchRequest.class)))
        .thenAnswer(
            invocation -> {
              BenefAIV5 benef = new BenefAIV5();
              IdentiteContrat identiteContrat = new IdentiteContrat();
              identiteContrat.setHistoriqueDateRangNaissance(
                  List.of(
                      new HistoriqueDateRangNaissance("20080410", "1"),
                      new HistoriqueDateRangNaissance("20080401", "2")));
              benef.setIdentite(identiteContrat);

              BenefSearchRequest req = (BenefSearchRequest) invocation.getArgument(0);
              BenefElasticPageResult res = new BenefElasticPageResult();
              if ((req.getBirthDate().equals("20080410") && req.getBirthRank().equals("1"))
                  || (req.getBirthDate().equals("20080401") && req.getBirthRank().equals("2"))) {
                res.setData(List.of(benef));
              } else {
                res.setData(new ArrayList<>());
              }
              return res;
            });

    BenefSearchRequest request = new BenefSearchRequest();
    BenefElasticPageResult result;

    // Cas 1
    request.setBirthDate("20080410");
    request.setBirthRank("1");
    result = benefElasticService.search(request);
    Assertions.assertEquals(1, result.getData().size());

    // Cas 2
    request.setBirthDate("20080401");
    request.setBirthRank("2");
    result = benefElasticService.search(request);
    Assertions.assertEquals(1, result.getData().size());

    // Cas 3
    request.setBirthDate("20080410");
    request.setBirthRank("2");
    result = benefElasticService.search(request);
    Assertions.assertEquals(0, result.getData().size());

    // Cas 4
    request.setBirthDate("20080401");
    request.setBirthRank("1");
    result = benefElasticService.search(request);
    Assertions.assertEquals(0, result.getData().size());
  }

  /**
   * Pour un bénéficiaire ayant un nom de famille 'DUPONT', un numéro contrat 0058355012, un
   * numeroAdherent 0058356056 Cas 1 : recherche avec DUPONT, un bénéficiaire attendu. Cas 2 :
   * recherche avec 0058355012, un bénéficiaire attendu. Cas 3 : 0058356056, un bénéficiaire
   * attendu. Cas 4 : recherche avec JEAN, pas de bénéficiaire attendu
   */
  @Test
  void searchBenefWithNameOrSubscriberIdOrContractNumberTest() {
    BenefElasticService benefElasticService = mock(BenefElasticService.class);

    when(benefElasticService.search(any(BenefSearchRequest.class)))
        .thenAnswer(
            invocation -> {
              BenefAIV5 benef = new BenefAIV5();
              ContratV5 contrat = new ContratV5();
              contrat.setData(new DataAssure());
              contrat.getData().setNom(new NomAssure());
              contrat.getData().getNom().setNomFamille("DUPONT");
              contrat.getData().getNom().setNomUsage("");
              contrat.setNumeroContrat("0058355012");
              contrat.setNumeroAdherent("0058356056");
              benef.setContrats(List.of(contrat));

              BenefSearchRequest req = (BenefSearchRequest) invocation.getArgument(0);
              BenefElasticPageResult res = new BenefElasticPageResult();
              if (req.getNameOrSubscriberIdorContractNumber()
                      .equals(contrat.getData().getNom().getNomFamille())
                  || req.getNameOrSubscriberIdorContractNumber().equals(contrat.getNumeroContrat())
                  || req.getNameOrSubscriberIdorContractNumber()
                      .equals(contrat.getNumeroAdherent())) {
                res.setData(List.of(benef));
              } else {
                res.setData(new ArrayList<>());
              }
              return res;
            });

    BenefSearchRequest request = new BenefSearchRequest();
    BenefElasticPageResult result;

    // Cas 1
    request.setNameOrSubscriberIdorContractNumber("DUPONT");
    result = benefElasticService.search(request);
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(
        "DUPONT", result.getData().get(0).getContrats().get(0).getData().getNom().getNomFamille());

    // Cas 2
    request.setNameOrSubscriberIdorContractNumber("0058355012");
    result = benefElasticService.search(request);
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(
        "0058355012", result.getData().get(0).getContrats().get(0).getNumeroContrat());

    // Cas 3
    request.setNameOrSubscriberIdorContractNumber("0058356056");
    result = benefElasticService.search(request);
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(
        "0058356056", result.getData().get(0).getContrats().get(0).getNumeroAdherent());

    // Cas 4
    request.setNameOrSubscriberIdorContractNumber("JEAN");
    result = benefElasticService.search(request);
    Assertions.assertEquals(0, result.getData().size());
  }
}
