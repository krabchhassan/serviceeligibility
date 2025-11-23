package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.dao.CommonDao;
import com.cegedim.next.serviceeligibility.almerys608.utils.Constants608;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.CodeMedia;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoBenef;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoCentreGestion;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoContrat;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoEntreprise;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoMembreContrat;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoProduit;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.InfoServiceTP;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.bson.BsonUndefined;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

@RequiredArgsConstructor
public class RetrieveTmpObjectsService {
  public static final String ADRESSEAGREGEES = "adresseagregees";
  public static final String ADRESSE_MEDIA = "adressemedia";
  public static final String MEDIA = "media";
  public static final String ACTIF = "actif";
  public static final String REF_INTERNE_OS = "refInterneOS";
  public static final String CARENCES = "carences";
  private final CommonDao commonDao;

  public <T> Stream<T> getAll(String collectionName, Class<T> objectClass) {
    return commonDao.getAllStream(collectionName, objectClass);
  }

  public Stream<InfoEntreprise> getAllInfoEntreprise(String collectionInfoEntreprise) {
    return commonDao.getAggregationStream(
        aggregationInfoEntreprise(), collectionInfoEntreprise, InfoEntreprise.class);
  }

  public Stream<InfoCentreGestion> getAllInfoCentreGestion(String collectionInfoCentreGestion) {
    return commonDao.getAggregationStream(
        aggregationInfoCentreGestion(), collectionInfoCentreGestion, InfoCentreGestion.class);
  }

  public Collection<InfoEntreprise> getInfoEntreprise(String collectionInfoEntreprise) {
    return commonDao.getAggregation(
        aggregationInfoEntreprise(), collectionInfoEntreprise, InfoEntreprise.class);
  }

  public Collection<InfoCentreGestion> getInfoCentreGestion(String collectionInfoCentreGestion) {
    return commonDao.getAggregation(
        aggregationInfoCentreGestion(), collectionInfoCentreGestion, InfoCentreGestion.class);
  }

  private Aggregation aggregationInfoEntreprise() {
    ProjectionOperation project =
        Aggregation.project()
            .andExclude(Constants.ID)
            .and(Constants.ID)
            .as("refentreprise")
            .and(Constants608.NOM_ENTREPRISE)
            .as("nomentreprise")
            .and(Constants608.NUM_CONTRAT_COLLECTIF)
            .as("numcontratcollectives")
            .and(
                VariableOperators.Map.itemsOf(Constants608.INFO_SITES)
                    .as("site")
                    .andApply(
                        agg ->
                            new Document()
                                .append("refsite", "$$site.refSite")
                                .append(
                                    "adressesite",
                                    new Document(ADRESSEAGREGEES, List.of("$$site.adresse")))
                                .append(
                                    "joignabilites",
                                    ArrayOperators.arrayOf(
                                            List.of(
                                                new Document()
                                                    .append(MEDIA, CodeMedia.VF.value())
                                                    .append(
                                                        ADRESSE_MEDIA, "$$site.adresse.telephone")
                                                    .append(ACTIF, true),
                                                new Document()
                                                    .append(MEDIA, CodeMedia.ME.value())
                                                    .append(ADRESSE_MEDIA, "$$site.adresse.email")
                                                    .append(ACTIF, true)))
                                        .filter()
                                        .as("this")
                                        .by(
                                            BooleanOperators.And.and(
                                                ComparisonOperators.Ne.valueOf(
                                                        Constants608.THIS_ADRESSE_MEDIA)
                                                    .notEqualToValue(new BsonUndefined()),
                                                ComparisonOperators.Ne.valueOf(
                                                        Constants608.THIS_ADRESSE_MEDIA)
                                                    .notEqualToValue("")))
                                        .toDocument(agg))))
            .as(Constants608.SITES);
    return Aggregation.newAggregation(project);
  }

  private Aggregation aggregationInfoCentreGestion() {
    ProjectionOperation project =
        Aggregation.project()
            .andExclude(Constants.ID)
            .and(Constants.ID)
            .as("refinternecg")
            .and("typeGestionnaire")
            .as("typegestionnaire")
            .and("gestionnaireContrat")
            .as("gestionnairecontrat")
            .and(agg -> new Document(ADRESSEAGREGEES, List.of("$adresseCG")))
            .as("adressecg")
            .and(
                agg ->
                    new Document()
                        .append(
                            "ligne1",
                            new Document(
                                Constants608.ARRAY_ELEM_AT,
                                Arrays.asList(Constants608.INFO_CARTES, 0)))
                        .append(
                            "ligne2",
                            new Document(
                                Constants608.ARRAY_ELEM_AT,
                                Arrays.asList(Constants608.INFO_CARTES, 1)))
                        .append(
                            "ligne3",
                            new Document(
                                Constants608.ARRAY_ELEM_AT,
                                Arrays.asList(Constants608.INFO_CARTES, 2)))
                        .append(
                            "ligne4",
                            new Document(
                                Constants608.ARRAY_ELEM_AT,
                                Arrays.asList(Constants608.INFO_CARTES, 3)))
                        .append(
                            "ligne5",
                            new Document(
                                Constants608.ARRAY_ELEM_AT,
                                Arrays.asList(Constants608.INFO_CARTES, 4)))
                        .append(
                            "ligne6",
                            new Document(
                                Constants608.ARRAY_ELEM_AT,
                                Arrays.asList(Constants608.INFO_CARTES, 5))))
            .as("infocarte");
    return Aggregation.newAggregation(project);
  }

  public boolean isRejected(String numContrat, String collection) {
    Rejet rejet =
        commonDao.queryOne(Rejet.class, collection, Criteria.where("numContrat").is(numContrat));
    return rejet != null;
  }

  public boolean exists(String ref, String refPath, Class<?> clazz, String collection) {
    return commonDao.queryOne(clazz, collection, Criteria.where(refPath).is(ref)) != null;
  }

  public String getRefCG(String refInterneCG, String collection) {
    if (exists(refInterneCG, Constants.ID, InfoCentreGestion.class, collection)) {
      return refInterneCG;
    }
    return null;
  }

  public List<InfoContrat.RATTACHEMENT> getRattachements(String numComtrat, String collection) {
    MatchOperation match =
        Aggregation.match(Criteria.where(Constants.NUMERO_CONTRAT).is(numComtrat));
    ProjectionOperation project =
        Aggregation.project()
            .andExclude(Constants.ID)
            .and("refOsRattachant")
            .as("refosrattachant")
            .and("refOsRattache")
            .as("refosrattache")
            .and("lienJuridique")
            .as("lienjuridique");
    return commonDao
        .getAggregationStream(
            Aggregation.newAggregation(match, project), collection, InfoContrat.RATTACHEMENT.class)
        .toList();
  }

  public List<InfoMembreContrat> getMembres(
      String numContrat, String collectionMembre, String collectionAdresseAD) {
    MatchOperation match =
        Aggregation.match(Criteria.where(Constants.NUMERO_CONTRAT).is(numContrat));
    LookupOperation lookup =
        Aggregation.lookup(collectionAdresseAD, "refInterneOs", Constants.ID, Constants608.ADRESSE);
    AddFieldsOperation addField =
        Aggregation.addFields()
            .addField(Constants608.ADRESSE)
            .withValue(ArrayOperators.arrayOf("$" + Constants608.ADRESSE).elementAt(0))
            .build();

    ProjectionOperation projection =
        Aggregation.project()
            .andExclude(Constants.ID)
            .and(ConvertOperators.ToBool.toBoolean("$souscripteur"))
            .as("souscripteur")
            .and("position")
            .as("position")
            .and("typeRegime")
            .as("typeregime")
            .and("dateEntree")
            .as("dateentree")
            .and("regimeSpecial")
            .as("regimespecial")
            .and(ConvertOperators.ToBool.toBoolean("$autonome"))
            .as("autonome")
            .and("modePaiement")
            .as("modepaiement")
            .and("nni")
            .as("nni")
            .andArrayOf("$nniRatt")
            .as("nniratts")
            .and("dateRadiation")
            .as("dateradiation")
            .and(
                agg ->
                    new Document("refinterneos", "$refInterneOs")
                        .append("datenaissance", "$dateNaissance")
                        .append("rangnaissance", "$rangNaissance")
                        .append("nompatronimique", "$nomPatronimique")
                        .append("nomusage", "$nomUsage")
                        .append("prenom", "$prenom")
                        .append("medecintraitant", "$medecinTraitant"))
            .as("individu")
            .and(
                agg ->
                    new Document(
                        ADRESSEAGREGEES,
                        List.of(
                            new Document("ligne1", "$adresse.adresse.ligne1")
                                .append("ligne2", "$adresse.adresse.ligne2")
                                .append("ligne3", "$adresse.adresse.ligne3")
                                .append("ligne4", "$adresse.adresse.ligne4")
                                .append("ligne5", "$adresse.adresse.ligne5")
                                .append("ligne6", "$adresse.adresse.ligne6")
                                .append("ligne7", "$adresse.adresse.ligne7"))))
            .as("adressemembre")
            .and(
                agg ->
                    ArrayOperators.arrayOf(
                            List.of(
                                new Document()
                                    .append(MEDIA, CodeMedia.VF.value())
                                    .append(ADRESSE_MEDIA, "$adresse.adresse.fixe")
                                    .append(ACTIF, true),
                                new Document()
                                    .append(MEDIA, CodeMedia.VB.value())
                                    .append(ADRESSE_MEDIA, "$adresse.adresse.telephone")
                                    .append(ACTIF, true),
                                new Document()
                                    .append(MEDIA, CodeMedia.ME.value())
                                    .append(ADRESSE_MEDIA, "$adresse.adresse.email")
                                    .append(ACTIF, true)))
                        .filter()
                        .as("this")
                        .by(
                            BooleanOperators.And.and(
                                ComparisonOperators.Ne.valueOf(Constants608.THIS_ADRESSE_MEDIA)
                                    .notEqualToValue(new BsonUndefined()),
                                ComparisonOperators.Ne.valueOf(Constants608.THIS_ADRESSE_MEDIA)
                                    .notEqualToValue("")))
                        .toDocument(agg))
            .as("joignabilites");

    return commonDao
        .getAggregationStream(
            Aggregation.newAggregation(match, lookup, addField, projection),
            collectionMembre,
            InfoMembreContrat.class)
        .toList();
  }

  public InfoServiceTP getInfoServiceTP(String collection, String numContrat, String refInterneOs) {
    MatchOperation match =
        Aggregation.match(
            Criteria.where(Constants.NUMERO_CONTRAT)
                .is(numContrat)
                .and(REF_INTERNE_OS)
                .is(refInterneOs));
    LimitOperation first = Aggregation.limit(1);
    ProjectionOperation project =
        Aggregation.project()
            .andExclude(Constants.ID)
            .and("dateDebutValidite")
            .as("datedebutvalidite")
            .and("dateFinValidite")
            .as("datefinvalidite")
            .and("dateDebutSuspension")
            .as("datedebutsuspension")
            .and("dateFinSuspension")
            .as("datefinsuspension")
            .and("activationDesactivation")
            .as("activationdesactivation")
            .and("envoi")
            .as("envoi");

    return commonDao
        .getAggregationStream(
            Aggregation.newAggregation(match, first, project), collection, InfoServiceTP.class)
        .findFirst()
        .orElse(null);
  }

  public List<InfoBenef> getBenef(String collection, String numContrat) {
    MatchOperation match =
        Aggregation.match(Criteria.where(Constants.NUMERO_CONTRAT).is(numContrat));
    ProjectionOperation projection =
        Aggregation.project()
            .andExclude(Constants.ID)
            .and(
                agg ->
                    new Document(
                        "noemise", ConvertOperators.ToBool.toBoolean("$noemise").toDocument(agg)))
            .as("statutnoemisation")
            .and(REF_INTERNE_OS)
            .as("refinterneos")
            .and("typeBenef")
            .as("typebenef")
            .and(ConvertOperators.ToBool.toBoolean("$contratResponsable"))
            .as("contratresponsable")
            .and("codeMouvementCarte")
            .as("codemouvementcarte")
            .and(
                agg ->
                    ArrayOperators.arrayOf(
                            List.of(
                                new Document()
                                    .append("codegrandregime", "$codeGrandRegime")
                                    .append("nni", "$Nni")
                                    .append("codecaissero", "$codeCaisseRo")
                                    .append("centress", "$centreSS"),
                                new Document()
                                    .append("codegrandregime", "$codeGrandRegime2")
                                    .append("nni", "$Nni2")
                                    .append("codecaissero", "$codeCaisseRo2")
                                    .append("centress", "$centreSS2")))
                        .filter()
                        .as("this")
                        .by(
                            BooleanOperators.And.and(
                                ComparisonOperators.Ne.valueOf("$$this.codegrandregime")
                                    .notEqualToValue(new BsonUndefined()),
                                ComparisonOperators.Ne.valueOf("$$this.nni")
                                    .notEqualToValue(new BsonUndefined())))
                        .toDocument(agg))
            .as("ros")
            .and(
                agg ->
                    new Document()
                        .append("fondcarte", "$fondCarte")
                        .append("codeannexes", List.of("$annexe1Carte", "$annexe2Carte")))
            .as("detailcarte");

    return commonDao
        .getAggregationStream(
            Aggregation.newAggregation(match, projection), collection, InfoBenef.class)
        .toList();
  }

  public List<InfoProduit> getProduits(
      String produitColl, String carenceColl, String numContrat, String refinterneos) {
    MatchOperation match =
        Aggregation.match(
            Criteria.where(Constants.NUMERO_CONTRAT)
                .is(numContrat)
                .and(REF_INTERNE_OS)
                .is(refinterneos));
    AggregationOperation lookupCarences =
        agg ->
            new Document(
                "$lookup",
                new Document()
                    .append("from", carenceColl)
                    .append(
                        "let",
                        new Document()
                            .append(Constants.NUMERO_CONTRAT, "$numeroContrat")
                            .append(REF_INTERNE_OS, "$refInterneOS")
                            .append("refProduit", "$referenceProduit"))
                    .append(
                        "pipeline",
                        List.of(
                            new Document(
                                "$match",
                                EvaluationOperators.Expr.valueOf(
                                        BooleanOperators.And.and(
                                            List.of(
                                                ComparisonOperators.Eq.valueOf("$$numeroContrat")
                                                    .equalToValue("$numeroContrat"),
                                                ComparisonOperators.Eq.valueOf("$$refInterneOS")
                                                    .equalToValue("$refInterneOS"),
                                                ComparisonOperators.Eq.valueOf("$$refProduit")
                                                    .equalToValue("$refProduit"))))
                                    .toDocument(agg))))
                    .append("as", CARENCES));
    AddFieldsOperation reduceCarence =
        Aggregation.addFields()
            .addField(CARENCES)
            .withValue(ArrayOperators.arrayOf("$carences").elementAt(0))
            .build();
    ProjectionOperation project =
        Aggregation.project()
            .andExclude(Constants.ID)
            .and("ordre")
            .as("ordre")
            .and("referenceProduit")
            .as("referenceproduit")
            .and("dateEntreeProduit")
            .as("dateentreeproduit")
            .and("dateSortieProduit")
            .as("datesortieproduit")
            .and(
                VariableOperators.Map.itemsOf("$carences.carenceInfos")
                    .as("this")
                    .andApply(
                        agg ->
                            new Document()
                                .append("referenceproduit", "$referenceProduit")
                                .append("identcolonne", "$$this.identColonne")
                                .append("datedebuteffet", "$$this.dateDebutEffet")
                                .append("datefineffet", "$$this.dateFinEffet")))
            .as(CARENCES);

    return commonDao
        .getAggregationStream(
            Aggregation.newAggregation(match, lookupCarences, reduceCarence, project),
            produitColl,
            InfoProduit.class)
        .toList();
  }

  public String getRefSite(String refEntreprise, String refSite, String collection) {
    Criteria criteria =
        Criteria.where("refEntreprise").is(refEntreprise).and("infoSites.refSite").is(refSite);
    if (commonDao.queryOne(InfoEntreprise.class, collection, criteria) != null) {
      return refSite;
    }
    return null;
  }
}
