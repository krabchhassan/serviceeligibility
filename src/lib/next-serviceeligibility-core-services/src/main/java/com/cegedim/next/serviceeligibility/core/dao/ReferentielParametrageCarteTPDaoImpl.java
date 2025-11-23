package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ReferentielParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("referentielParametrageCarteTPDaoImpl")
@RequiredArgsConstructor
public class ReferentielParametrageCarteTPDaoImpl implements ReferentielParametrageCarteTPDao {

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "getByAmc ReferentielParametrageCarteTP")
  public ReferentielParametrageCarteTP getByAmc(String amc) {
    return template.findOne(
        new Query(Criteria.where(Constants.AMC).is(amc)),
        ReferentielParametrageCarteTP.class,
        Constants.REFERENTIEL_PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "getByAmcs ReferentielParametrageCarteTP")
  public List<ReferentielParametrageCarteTP> getByAmcs(List<String> amcs) {
    return template.find(
        new Query(Criteria.where(Constants.AMC).in(amcs)),
        ReferentielParametrageCarteTP.class,
        Constants.REFERENTIEL_PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "update ReferentielParametrageCarteTP")
  public void update(ReferentielParametrageCarteTP referentielParametrageCarteTP) {
    ReferentielParametrageCarteTP existingReferentielParametrageCarteTP =
        getByAmc(referentielParametrageCarteTP.getAmc());
    if (existingReferentielParametrageCarteTP == null) {
      existingReferentielParametrageCarteTP = referentielParametrageCarteTP;
    } else {
      List<String> identifiantsCollectivite =
          existingReferentielParametrageCarteTP.getIdentifiantsCollectivite();
      identifiantsCollectivite.addAll(referentielParametrageCarteTP.getIdentifiantsCollectivite());
      Collections.sort(identifiantsCollectivite);
      existingReferentielParametrageCarteTP.setIdentifiantsCollectivite(
          new ArrayList<>(new HashSet<>(identifiantsCollectivite)));

      List<String> groupesPopulation = existingReferentielParametrageCarteTP.getGroupesPopulation();
      if (groupesPopulation == null) {
        groupesPopulation = new ArrayList<>();
      }
      groupesPopulation.addAll(referentielParametrageCarteTP.getGroupesPopulation());
      Collections.sort(groupesPopulation);
      existingReferentielParametrageCarteTP.setGroupesPopulation(
          new ArrayList<>(new HashSet<>(groupesPopulation)));

      List<String> portefeuille = existingReferentielParametrageCarteTP.getPortefeuille();
      if (portefeuille == null) {
        portefeuille = new ArrayList<>();
      }
      portefeuille.addAll(referentielParametrageCarteTP.getPortefeuille());
      Collections.sort(portefeuille);
      existingReferentielParametrageCarteTP.setPortefeuille(
          new ArrayList<>(new HashSet<>(portefeuille)));
    }
    template.save(
        existingReferentielParametrageCarteTP,
        Constants.REFERENTIEL_PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "deleteByAmc ReferentielParametrageCarteTP")
  public long deleteByAmc(String amc) {
    Criteria criteria = Criteria.where(Constants.AMC).is(amc);
    return template
        .remove(
            new Query(criteria),
            ReferentielParametrageCarteTP.class,
            Constants.REFERENTIEL_PARAMETRAGE_CARTE_TP_COLLECTION)
        .getDeletedCount();
  }
}
