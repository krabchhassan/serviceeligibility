package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.services.pojo.ErrorValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExceptionService {
  private final TraceService traceService;

  /**
   * Gestion des exceptions de validation
   *
   * @param e l'Exception soulevée
   * @param response La réponse à retourner à l'appelant
   * @param traceCollectionName Le nom de la collection de trace
   * @param traceId Identifiant de la trace créée
   */
  public void manageValidationException(
      Exception e, CreationResponse response, String traceCollectionName, String traceId) {
    log.info("Managing Validation Exception");
    List<String> errorMessages = new ArrayList<>();
    if (e.getMessage() != null) {
      errorMessages = Arrays.asList(e.getMessage().split("\n"));
    }
    ArrayList<String> retErrorMessages = new ArrayList<>(errorMessages);
    response.setStatus(TraceStatus.ValidationFailed.toString());
    response.setErrorMessages(retErrorMessages);
    response.setProvider(Constants.ERROR_PROVIDER);
    traceService.updateStatusError(
        traceId, TraceStatus.ValidationFailed, retErrorMessages, traceCollectionName);
  }

  /**
   * Gestion des exceptions de validation de niveau contrat
   *
   * @param e l'Exception soulevée
   * @param response La réponse à retourner à l'appelant
   * @param traceCollectionName Le nom de la collection de trace
   * @param traceId Identifiant de la trace créée
   */
  public void manageValidationContratException(
      ValidationContractException e,
      CreationResponse response,
      String traceCollectionName,
      String traceId) {
    log.info("Managing Validation Exception");
    Map<String, List<String>> errorsByPerson = new HashMap<>();
    List<String> contratErrors = new ArrayList<>();
    for (ErrorValidationBean errorValidationBean : e.getErrorValidationBeans()) {
      if (Constants.ASSURE.equals(errorValidationBean.getLevel())) {
        errorsByPerson
            .computeIfAbsent(errorValidationBean.getPersonNumber(), key -> new ArrayList<>())
            .add(errorValidationBean.getError());
      } else {
        contratErrors.add(errorValidationBean.getError());
      }
    }
    JSONArray jsonErrorMessages = new JSONArray();
    contratErrors.forEach(jsonErrorMessages::put);
    errorsByPerson.forEach(
        (numeroPersonne, errors) -> {
          JSONObject errorObj = new JSONObject();
          errorObj.put("numeroPersonne", numeroPersonne);

          JSONArray personErrors = new JSONArray(errors);
          errorObj.put("errorMessages", personErrors);

          jsonErrorMessages.put(errorObj);
        });
    List<String> retErrorMessages = new ArrayList<>();
    e.getErrorValidationBeans()
        .forEach(errorValidationBean -> retErrorMessages.add(errorValidationBean.getError()));
    response.setStatus(TraceStatus.ValidationFailed.toString());
    response.setErrorMessages(retErrorMessages);
    response.setProvider(Constants.ERROR_PROVIDER);
    traceService.updateStatusError(
        traceId, TraceStatus.ValidationFailed, jsonErrorMessages, traceCollectionName);
  }

  /**
   * Gestion des exceptions de contrôle de l'IdClientBO
   *
   * @param e l'Exception soulevée
   * @param response La réponse à retourner à l'appelant
   * @param traceCollectionName Le nom de la collection de trace
   * @param traceId Identifiant de la trace créée
   */
  public void manageIdClientBoException(
      Exception e, CreationResponse response, String traceCollectionName, String traceId) {
    log.info("Managing IdClientBO Exception");
    ArrayList<String> retErrorMessages = new ArrayList<>();
    retErrorMessages.add(e.getLocalizedMessage());
    response.setStatus(TraceStatus.IdClientBOInvalid.toString());
    response.setErrorMessages(retErrorMessages);
    response.setProvider(Constants.ERROR_PROVIDER);
    traceService.updateStatusError(
        traceId, TraceStatus.IdClientBOInvalid, retErrorMessages, traceCollectionName);
  }

  /**
   * Gestion des exceptions JsonProcessingException
   *
   * @param e l'Exception soulevée
   * @param response La réponse à retourner à l'appelant
   * @param traceCollectionName Le nom de la collection de trace
   * @param traceId Identifiant de la trace créée
   */
  public void manageJsonProcessingException(
      Exception e, CreationResponse response, String traceCollectionName, String traceId) {
    log.info("Managing JsonProcessingException Exception");
    ArrayList<String> retErrorMessages = new ArrayList<>();
    retErrorMessages.add(e.getLocalizedMessage());
    response.setStatus(TraceStatus.ErrorDeserializing.toString());
    response.setErrorMessages(retErrorMessages);
    response.setProvider(Constants.ERROR_PROVIDER);
    traceService.updateStatusError(
        traceId, TraceStatus.ErrorDeserializing, retErrorMessages, traceCollectionName);
  }

  /**
   * Gestion des exceptions ContractNotFound
   *
   * @param e l'Exception soulevée
   * @param response La réponse à retourner à l'appelant
   * @param traceCollectionName Le nom de la collection de trace
   * @param traceId Identifiant de la trace créée
   */
  public void manageContractNotFoundException(
      Exception e,
      CreationResponse response,
      String traceCollectionName,
      String traceId,
      String version) {
    ArrayList<String> retErrorMessages = new ArrayList<>();
    retErrorMessages.add(e.getLocalizedMessage());
    response.setErrorMessages(retErrorMessages);
    response.setProvider(Constants.ERROR_PROVIDER);
    response.setStatus(TraceStatus.InsuredNotFound.toString());
    traceService.updateStatusError(
        traceId, TraceStatus.InsuredNotFound, retErrorMessages, traceCollectionName);
    for (String retErrorMessage : retErrorMessages) {
      log.info(retErrorMessage);
    }
  }

  /**
   * Gestion des exceptions non gérées
   *
   * @param e l'Exception soulevée
   * @param response La réponse à retourner à l'appelant
   * @param traceCollectionName Le nom de la collection de trace
   * @param traceId Identifiant de la trace créée
   */
  public void manageException(
      Exception e, CreationResponse response, String traceCollectionName, String traceId) {
    response.setStatus(TraceStatus.UnexpectedBddsException.toString());
    log.info("Manage default exception");
    ArrayList<String> retErrorMessages = new ArrayList<>();
    retErrorMessages.add(e.getMessage());
    response.setErrorMessages(retErrorMessages);
    response.setProvider(Constants.ERROR_PROVIDER);
    traceService.updateStatusError(
        traceId, TraceStatus.UnexpectedBddsException, retErrorMessages, traceCollectionName);
  }
}
