package com.cegedim.next.serviceeligibility.core.business.declaration.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.List;

/** Interface de la classe d'accès aux {@code Declaration} de la base de donnees. */
public interface DeclarationDao extends IMongoGenericDao<Declaration> {

  /**
   * Recherche dans la base de donnees une declaration a partir de son identifiant technique.
   *
   * @param id l'identifiant de la declaration.
   * @return la declaration.
   */
  Declaration findById(String id);

  /**
   * Recherche dans la base de donnees tous les declarations.
   *
   * @return liste des declarations trouves. Si aucune declaration n'est trouvee, cette methode
   *     retournera une liste vide.
   */
  List<Declaration> findAll();

  /**
   * Renvoi une liste de declarations valides d'un beneficiaire pour un declarant et a partir d'une
   * date d'effet. <br>
   * <br>
   *
   * @param dateNaissance la date de naissance du beneficiaire
   * @param rangNaissance le rang de naissance du beneficiaire
   * @param nirBeneficiaire le Nir du beneficiaire
   * @param cleNirBeneficiare la cle Nir du beneficiare
   * @param numeroPrefectoral le numero prefectoral du declarant
   * @param isRechercheCarteFamille boolean indiquant si on doit filtrer en plus sur des types
   *     contrats
   * @param isSearchByIdPrefectoral boolean, true si recherche par numeroPrefectoral, false si
   *     recherche par numero AMC echange
   * @return La liste des declarations.
   */
  List<Declaration> findDeclarationsByBeneficiaire(
      String dateNaissance,
      String rangNaissance,
      String nirBeneficiaire,
      String cleNirBeneficiare,
      String numeroPrefectoral,
      boolean isRechercheCarteFamille,
      boolean isSearchByIdPrefectoral,
      boolean isSearchByAdherent,
      String numeroAdherent);

  /**
   * Renvoie la liste des declarations en base de donnees pour le numeroAdherent du contrat passe en
   * parametre. Les declarations sont groupees par beneficiare.
   *
   * @param amc numero perfectoral.
   * @param numeroContrat le numero contrat.
   * @param isSearchByIdPrefectoral boolean, true si recherche par numeroPrefectoral, false si
   *     recherche par numero AMC echange
   * @return la liste des declaration
   */
  List<Declaration> findDeclarationsByNumeroContrat(
      String amc, String numeroContrat, boolean isSearchByIdPrefectoral);

  /** Supprime toutes les déclarations */
  void removeAll();
}
