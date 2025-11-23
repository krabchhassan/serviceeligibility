package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.Amc;
import com.cegedim.next.serviceeligibility.core.model.kafka.Audit;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.HistoriqueDateRangNaissance;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class PersonService {
  public static final String SERVICE_PRESTATION = "ServicePrestation";
  public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  private final BenefInfosService benefInfos;

  @ContinueSpan(log = "extractBenefFromContratCommun")
  public List<BenefAIV5> extractBenefFromContratCommun(
      List<Assure> contractBenefs,
      ContratAICommun contract,
      String keycloakUsername,
      String traceId) {
    List<BenefAIV5> benefs = new ArrayList<>();

    if (contractBenefs != null) {
      for (Assure contractBenef : contractBenefs) {
        extractBenefCommon(contract, keycloakUsername, traceId, benefs, contractBenef);
      }
    }
    return benefs;
  }

  @ContinueSpan(log = "extractBenefFromContratV5")
  public List<BenefAIV5> extractBenefFromContratCommun(
      ContratAIV6 contract, String keycloakUsername, String traceId, String numeroPersonne) {
    List<BenefAIV5> benefs = new ArrayList<>();

    List<Assure> contractBenefs = contract.getAssures();
    if (contractBenefs != null) {
      for (Assure contractBenef : contractBenefs) {
        if (contractBenef.getIdentite() != null
            && contractBenef.getIdentite().getNumeroPersonne().equals(numeroPersonne)) {
          extractBenefCommon(contract, keycloakUsername, traceId, benefs, contractBenef);
        }
      }
    }
    return benefs;
  }

  private void extractBenefCommon(
      ContratAICommun contract,
      String keycloakUsername,
      String traceId,
      List<BenefAIV5> benefs,
      Assure contractBenef) {
    String declarantId = contract.getIdDeclarant();
    String numero = contract.getNumero();
    String numeroAdherent = contract.getNumeroAdherent();
    String societeEmettrice = contract.getSocieteEmettrice();

    BenefAIV5 benef = new BenefAIV5();
    DataAssure data = contractBenef.getData();
    IdentiteContrat contractid = contractBenef.getIdentite();

    List<Periode> periodesContrat =
        benefInfos.handlePeriodesContratForBenef(contract, contractBenef);

    extractedV5(
        keycloakUsername,
        traceId,
        declarantId,
        numero,
        numeroAdherent,
        benef,
        data,
        contractid,
        societeEmettrice,
        periodesContrat);

    benefs.add(benef);
  }

  private void extractedV5(
      String keycloakUsername,
      String traceId,
      String declarantId,
      String numero,
      String numeroAdherent,
      BenefAIV5 benef,
      DataAssure dataV1,
      IdentiteContrat contractid,
      String societeEmettrice,
      List<Periode> periodesContrat) {
    Amc amc = new Amc();
    amc.setIdDeclarant(declarantId);
    benef.setAmc(amc);
    benef.setIdClientBO(keycloakUsername);

    Audit audit = new Audit();
    LocalDateTime date = LocalDateTime.now(ZoneOffset.UTC);
    audit.setDateEmission(date.format(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
    benef.setAudit(audit);

    DataAssure newDataAssure = new DataAssure();
    newDataAssure.setNom(dataV1.getNom());
    newDataAssure.setAdresse(dataV1.getAdresse());
    newDataAssure.setContact(dataV1.getContact());

    if (!CollectionUtils.isEmpty(dataV1.getDestinatairesPaiements())) {
      newDataAssure.setDestinatairesPaiements(dataV1.getDestinatairesPaiements());
    }

    if (!CollectionUtils.isEmpty(dataV1.getDestinatairesRelevePrestations())) {
      newDataAssure.setDestinatairesRelevePrestations(dataV1.getDestinatairesRelevePrestations());
    }

    ContratV5 benefContrat = new ContratV5();
    benefContrat.setNumeroContrat(numero);
    benefContrat.setData(newDataAssure);
    benefContrat.setNumeroAdherent(numeroAdherent);
    benefContrat.setSocieteEmettrice(societeEmettrice);
    benefContrat.setPeriodes(periodesContrat);

    List<ContratV5> contractsBenef = new ArrayList<>();
    contractsBenef.add(benefContrat);
    benef.setContrats(contractsBenef);

    IdentiteContrat identite = new IdentiteContrat();

    if (contractid != null) {
      identite.setDateNaissance(contractid.getDateNaissance());
      identite.setNir(contractid.getNir());
      identite.setAffiliationsRO(contractid.getAffiliationsRO());
      identite.setNumeroPersonne(contractid.getNumeroPersonne());
      identite.setRangNaissance(contractid.getRangNaissance());

      List<HistoriqueDateRangNaissance> historiqueDateRangNaissances = new ArrayList<>();
      HistoriqueDateRangNaissance historiqueDateRangNaissance = new HistoriqueDateRangNaissance();
      historiqueDateRangNaissance.setDateNaissance(contractid.getDateNaissance());
      historiqueDateRangNaissance.setRangNaissance(contractid.getRangNaissance());
      historiqueDateRangNaissances.add(historiqueDateRangNaissance);
      identite.setHistoriqueDateRangNaissance(historiqueDateRangNaissances);
    } else {
      // BLUE-3370 : on affecte l'identite de l'assure sinon le
      // benef n'aura aucune infomation
      identite = contractid;
    }

    benef.setSocietesEmettrices(
        benefInfos.handlePeriodesSocieteEmettriceForBenef(List.of(benefContrat)));
    benef.setIdentite(identite);
    benef.setNumeroAdherent(numeroAdherent);
    List<String> services = new ArrayList<>();
    services.add(SERVICE_PRESTATION);
    benef.setServices(services);
    benef.setTraceId(traceId);
  }
}
