package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ReferentielParametrageCarteTP;
import java.util.List;

public interface ReferentielParametrageCarteTPDao {

  /**
   * Retourne le référentiel pour l'AMC fournit
   *
   * @param amc AMC
   * @return Le référentiel paramétrage de Cartes TP de l'AMC
   */
  public ReferentielParametrageCarteTP getByAmc(String amc);

  /**
   * Retourne les référentiels pour la liste d'AMC fournit
   *
   * @param amcs liste d'AMC
   * @return Les référentiels paramétrage de Cartes TP des AMCs
   */
  public List<ReferentielParametrageCarteTP> getByAmcs(List<String> amcs);

  /**
   * Mise à jour d'un référentiel paramétrage de Carte TP
   *
   * @param referentielParametrageCarteTP
   */
  public void update(ReferentielParametrageCarteTP referentielParametrageCarteTP);

  long deleteByAmc(String amc);
}
