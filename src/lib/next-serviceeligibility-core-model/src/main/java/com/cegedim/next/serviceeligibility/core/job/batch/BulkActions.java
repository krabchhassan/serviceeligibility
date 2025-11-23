package com.cegedim.next.serviceeligibility.core.job.batch;

import com.cegedim.beyond.schemas.Event;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions620;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BulkActions {
  @Setter private String firstIdDeclaration;
  @Setter private String lastIdDeclaration;
  @Setter private String firstNumContrat;
  @Setter private String lastNumContrat;
  @Setter private String lastNumAdherent;
  @Setter private String currentThread;
  private final List<DeclarationConsolide> toSave = new ArrayList<>();
  private final List<DeclarationConsolide> toDelete = new ArrayList<>();
  private final List<DeclarationConsolide> toUpdate = new ArrayList<>();
  private final List<CarteDemat> toInvalid = new ArrayList<>();
  private final List<CarteDemat> demats = new ArrayList<>();
  private final List<CartePapierEditique> papiers = new ArrayList<>();
  private final List<Rejection> rejections = new ArrayList<>();
  @Setter private HistoriqueExecutions620 historiqueExecution = new HistoriqueExecutions620();
  private final List<Pair<CarteDemat, String>> traceExtractionConsos = new ArrayList<>();

  @Getter @Setter private String contexte = Constants.ANNUEL;
  private final List<Event> events = new ArrayList<>();

  public void save(List<DeclarationConsolide> declarationConsolide) {
    toSave.addAll(declarationConsolide);
  }

  public void delete(List<DeclarationConsolide> declarationConsolide) {
    toDelete.addAll(declarationConsolide);
  }

  public void update(List<DeclarationConsolide> declarationConsolides) {
    toUpdate.addAll(declarationConsolides);
  }

  public void invalid(List<CarteDemat> carteDemats) {
    for (CarteDemat carteDemat : carteDemats) {
      if (!toInvalid.contains(carteDemat)) {
        toInvalid.add(carteDemat);
      }
    }
  }

  public void demat(CarteDemat carteDemat) {
    demats.add(carteDemat);
  }

  public void papier(CartePapierEditique cartePapierEditique) {
    papiers.add(cartePapierEditique);
  }

  public void reject(Rejection rejection) {
    rejections.add(rejection);
  }

  public void addTraceExtractionConsos(CarteDemat carteDemat, String rejet) {
    traceExtractionConsos.add(Pair.of(carteDemat, rejet));
  }

  public void event(Event event) {
    if (event != null) {
      events.add(event);
    }
  }
}
