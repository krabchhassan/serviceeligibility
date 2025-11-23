package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.PRESTIJ_TRACE;

import com.cegedim.next.serviceeligibility.core.model.kafka.Amc;
import com.cegedim.next.serviceeligibility.core.model.kafka.Audit;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.HistoriqueDateRangNaissance;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestPrestIJService {

  private final MongoTemplate template;

  @ContinueSpan(log = "getPrestIJ")
  public PrestIJ getPrestIJ(String id) {
    return template.findById(id, PrestIJ.class);
  }

  @ContinueSpan(log = "findByBeneficiary")
  public List<PrestIJ> findByBeneficiary(
      String idDeclarant,
      String numeroContrat,
      String nirCode,
      String nirCle,
      String dateNaissance,
      String rangNaissance) {
    Criteria criteria =
        Criteria.where("assures")
            .elemMatch(
                Criteria.where("nir.code")
                    .is(nirCode)
                    .and("nir.cle")
                    .is(nirCle)
                    .and("dateNaissance")
                    .is(dateNaissance)
                    .and("rangNaissance")
                    .is(rangNaissance));

    if (StringUtils.isNotBlank(idDeclarant)) {
      criteria.and("oc.identifiant").is(idDeclarant);
    }

    if (StringUtils.isNotBlank(numeroContrat)) {
      criteria.and("contrat.numero").is(numeroContrat);
    }

    Query query = Query.query(criteria);
    return template.find(query, PrestIJ.class);
  }

  @ContinueSpan(log = "getPrestIJTrace")
  public Trace getPrestIJTrace(String id) {
    return template.findById(id, Trace.class, PRESTIJ_TRACE);
  }

  @ContinueSpan(log = "getPrestIJByTraceId")
  public PrestIJ getPrestIJByTraceId(String id) {
    Query query = new Query();
    Criteria traceId = Criteria.where("traceId").is(id);
    query.addCriteria(traceId);
    List<PrestIJ> lstPrestIJ = template.find(query, PrestIJ.class);
    if (lstPrestIJ.isEmpty()) {
      return null;
    } else {
      return lstPrestIJ.get(0);
    }
  }

  @ContinueSpan(log = "prestIJMapping")
  public List<BenefAIV5> prestIJMapping(PrestIJ prestIJ) {
    List<BenefAIV5> benefs = new ArrayList<>();

    for (Assure assure : prestIJ.getAssures()) {
      BenefAIV5 benef = new BenefAIV5();

      // hotfix/BLUE-5365
      benef.setIdClientBO(prestIJ.getOc().getIdClientBO());

      Amc amc = new Amc();
      amc.setIdDeclarant(prestIJ.getOc().getIdentifiant());
      amc.setLibelle(prestIJ.getOc().getDenomination());
      benef.setAmc(amc);

      benef.setNumeroAdherent(prestIJ.getContrat().getNumeroAdherent());

      // Un seul contrat dans PrestIJ
      List<ContratV5> contrats = new ArrayList<>();
      ContratV5 contrat = new ContratV5();
      contrat.setNumeroContrat(prestIJ.getContrat().getNumero());
      // Le bloc Data de PrestIJ ne contient que le nom
      DataAssure data = new DataAssure();
      data.setNom(assure.getData().getNom());
      contrat.setData(data);
      contrat.setNumeroAdherent(prestIJ.getContrat().getNumeroAdherent());
      contrats.add(contrat);
      benef.setContrats(contrats);

      IdentiteContrat identite = new IdentiteContrat();
      identite.setNumeroPersonne(assure.getNumeroPersonne());
      identite.setDateNaissance(assure.getDateNaissance());
      identite.setRangNaissance(assure.getRangNaissance());

      List<HistoriqueDateRangNaissance> historiqueDateRangNaissances = new ArrayList<>();
      HistoriqueDateRangNaissance historiqueDateRangNaissance = new HistoriqueDateRangNaissance();
      historiqueDateRangNaissance.setDateNaissance(assure.getDateNaissance());
      historiqueDateRangNaissance.setRangNaissance(assure.getRangNaissance());
      historiqueDateRangNaissances.add(historiqueDateRangNaissance);
      identite.setHistoriqueDateRangNaissance(historiqueDateRangNaissances);

      identite.setNir(assure.getNir());
      benef.setIdentite(identite);

      Audit audit = new Audit();
      audit.setDateEmission(
          LocalDateTime.now(ZoneOffset.UTC)
              .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
      benef.setAudit(audit);

      List<String> services = new ArrayList<>();
      services.add("PrestIJ");
      benef.setServices(services);
      benef.setTraceId(prestIJ.getTraceId());

      benefs.add(benef);
    }

    return benefs;
  }
}
