package com.cegedim.next.serviceeligibility.core.services.cartedemat.cartepapier;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.MAXIMUM_EXTRACT_LIST_SIZE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.CartePapierDao;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartesPapierService {

  private final CartePapierDao cartePapierDao;

  private final TraceExtractionConsoService traceExtractionConsoService;

  private final EventService eventService;

  private final BeyondPropertiesService beyondPropertiesService;

  List<TraceExtractionConso> traceExtractionConsoList = new ArrayList<>();

  public CartesPapierService(
      CartePapierDao cartePapierDao,
      TraceExtractionConsoService traceExtractionConsoService,
      EventService eventService,
      BeyondPropertiesService beyondPropertiesService) {
    this.traceExtractionConsoService = traceExtractionConsoService;
    this.cartePapierDao = cartePapierDao;
    this.eventService = eventService;
    this.beyondPropertiesService = beyondPropertiesService;
  }

  public Stream<CartePapierEditique> getCartesEdites(String identifiant) {
    return cartePapierDao.getAllCards(identifiant);
  }

  public void processCartePapier(DataForJob620 dataForJob620) {
    Stream<CartePapierEditique> cartePapierEditiques =
        getCartesEdites(dataForJob620.getIdentifiant());
    cartePapierEditiques.forEach(
        cartePapierEditique -> {
          // si issued :
          if (Constants.ISSUED.equals(cartePapierEditique.getInternal().getStatus())) {
            dataForJob620.getLastExecution().incNbCartesPapierOk(1);
            List<TraceExtractionConso> traceExtractionConsos = new ArrayList<>();
            for (TraceExtractionConso traceExtractionConso :
                cartePapierEditique.getInternal().getTraceExtractionConso()) {
              if (StringUtils.isNotBlank(
                  cartePapierEditique.getInternal().getExtractionFileName())) {
                String[] fileName =
                    cartePapierEditique.getInternal().getExtractionFileName().split("/");
                traceExtractionConso.setNomFichier(fileName[fileName.length - 1]);
              }
              traceExtractionConsos.add(traceExtractionConso);
            }
            manageExtractionTrace(traceExtractionConsos);
            eventService.sendObservabilityEventCartePapier(cartePapierEditique.getCartePapier());
          } else {
            // si ko : TO_BE_ISSUED ou PROCESSING (voir orange, si on peut avoir l'erreur ?)
            // voir si trop d'erreur, rajouter une table temporaire ou prejob pour récupérer
            // différement les cartes à éditer.
            eventService.sendObservabilityEventCartePapierFailed(
                cartePapierEditique.getCartePapier(), "Erreur de génération de la carte papier");
            dataForJob620.getLastExecution().incNbCartesPapierKo(1);
          }
        });
    saveExtractionTraces();
  }

  private void manageExtractionTrace(List<TraceExtractionConso> tracesExtraction) {
    traceExtractionConsoList.addAll(tracesExtraction);

    // If we reached max bulk size is reached, save current traces and clear traces
    // list
    // Prevents eventual OOMs
    if (traceExtractionConsoList.size()
        >= beyondPropertiesService.getIntegerProperty(MAXIMUM_EXTRACT_LIST_SIZE).orElse(5000)) {
      saveExtractionTraces();
    }
  }

  private void saveExtractionTraces() {
    if (CollectionUtils.isNotEmpty(traceExtractionConsoList)) {
      log.debug("Saved {} traces", traceExtractionConsoList.size());
      traceExtractionConsoService.saveTraceList(traceExtractionConsoList, null);

      // Empty the list for next bulk
      traceExtractionConsoList = new ArrayList<>();
    }
  }
}
