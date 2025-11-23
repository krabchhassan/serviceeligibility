package com.cegedim.next.serviceeligibility.core.elast;

import static com.cegedim.next.serviceeligibility.core.utils.BenefSearchConstants.*;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP;

import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.SocieteEmettrice;
import com.cegedim.next.serviceeligibility.core.utils.BenefSearchConstants;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.data.client.orhlc.NativeSearchQuery;
import org.opensearch.data.client.orhlc.NativeSearchQueryBuilder;
import org.opensearch.index.query.*;
import org.opensearch.search.aggregations.Aggregation;
import org.opensearch.search.aggregations.AggregationBuilder;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.opensearch.search.aggregations.bucket.nested.ParsedNested;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.FieldSortBuilder;
import org.opensearch.search.sort.SortBuilder;
import org.opensearch.search.sort.SortBuilders;
import org.opensearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BenefElasticService {
  private final IndexBenef indexBenef;

  private final ElasticsearchOperations elastic;

  private final RestHighLevelClient client;

  private final ElasticAuthorizationScopeHandler elasticAuthorizationScopeHandler;

  private static final Logger logger = LoggerFactory.getLogger(BenefElasticService.class);

  /**
   * Adapte la requête en ajoutant un wildcard (*) à la fin pour permettre une autocomplétion plus
   * flexible dans Elasticsearch. Par exemple : - "Jean" devient "Jean*" - "Jean-Marc" devient
   * "Jean-Marc*"
   *
   * @param entry Requête envoyer depuis l'UI
   * @return {String} Requête comprehensible par ES
   */
  private String frontRequestToElasticSearchRequestAdapter(String entry) {
    return entry + "*";
  }

  @ContinueSpan(log = "getAutocomplete")
  public List<String> getAutocomplete(String field, String value) {
    SearchRequest searchRequest = new SearchRequest(indexBenef.getIndexAlias());
    value = this.escapeSlashes(value);
    if (BenefSearchConstants.CONTRATS_DATA_NOM_FAMILLE.equals(field)) {
      return getAutoCompleteForName(field, value, searchRequest);
    } else {
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
      if (StringUtils.isNotBlank(value)) {
        BoolQueryBuilder contratQueryBuilder = QueryBuilders.boolQuery();
        value = frontRequestToElasticSearchRequestAdapter(value);
        BoolQueryBuilder orOpeartor = QueryBuilders.boolQuery();
        orOpeartor.should(
            QueryBuilders.wildcardQuery(field + KEYWORD, value.toUpperCase()).boost(1.0f));
        orOpeartor.should(
            QueryBuilders.wildcardQuery(field + KEYWORD, "*" + value.toUpperCase()).boost(0.5f));
        contratQueryBuilder.must(orOpeartor);
        NestedQueryBuilder contratNested = contratNestedQuery(contratQueryBuilder);
        boolQueryBuilder.must(contratNested);
      }
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      AggregationBuilder nestedAgg =
          AggregationBuilders.nested(CONTRATS_IN_COLLECTION_BENEF, CONTRATS_IN_COLLECTION_BENEF)
              .subAggregation(
                  AggregationBuilders.terms(field + KEYWORD).field(field + KEYWORD).size(20));
      searchSourceBuilder.aggregation(nestedAgg);
      searchSourceBuilder.query(boolQueryBuilder);
      searchSourceBuilder.size(0);
      searchRequest.source(searchSourceBuilder);
      try {
        SearchResponse result = client.search(searchRequest, RequestOptions.DEFAULT);
        return getStringList(result);
      } catch (IOException e) {
        logger.error(
            "Error getting autocompletion for field {}, with error:{}", field, e.getMessage());
      }
    }

    return new ArrayList<>();
  }

  private static List<String> getStringList(SearchResponse result) {
    List<Aggregation> aggregations = result.getAggregations().asList();
    List<String> names = new ArrayList<>();

    for (Aggregation aggregation : aggregations) {
      List<Aggregation> subAggregations = ((ParsedNested) aggregation).getAggregations().asList();
      for (Aggregation subAggregation : subAggregations) {
        var buckets = ((ParsedStringTerms) subAggregation).getBuckets();
        names.addAll(
            buckets.stream()
                .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
      }
    }

    return names.stream().distinct().toList();
  }

  private List<String> getAutoCompleteForName(
      String field, String value, SearchRequest searchRequest) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(value)) {
      value = frontRequestToElasticSearchRequestAdapter(value);
      BoolQueryBuilder orOpeartor = QueryBuilders.boolQuery();
      orOpeartor.should(
          QueryBuilders.wildcardQuery(CONTRATS_DATA_NOM_FAMILLE + KEYWORD, value).boost(1.0f));
      orOpeartor.should(
          QueryBuilders.wildcardQuery(CONTRATS_DATA_NOM_USAGE + KEYWORD, value).boost(1.0f));

      orOpeartor.should(
          QueryBuilders.wildcardQuery(CONTRATS_DATA_NOM_FAMILLE + KEYWORD, "*" + value)
              .boost(0.5f));
      orOpeartor.should(
          QueryBuilders.wildcardQuery(CONTRATS_DATA_NOM_USAGE + KEYWORD, "*" + value).boost(0.5f));

      NestedQueryBuilder contratNested =
          contratNestedQuery(QueryBuilders.boolQuery().must(orOpeartor));
      boolQueryBuilder.must(contratNested);
    }
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    AggregationBuilder nestedAgg =
        AggregationBuilders.nested(CONTRATS_IN_COLLECTION_BENEF, CONTRATS_IN_COLLECTION_BENEF)
            .subAggregation(
                AggregationBuilders.terms(
                        BenefSearchConstants.CONTRATS_DATA_NOM_USAGE + BenefSearchConstants.KEYWORD)
                    .field(
                        BenefSearchConstants.CONTRATS_DATA_NOM_USAGE + BenefSearchConstants.KEYWORD)
                    .size(20))
            .subAggregation(
                AggregationBuilders.terms(field + BenefSearchConstants.KEYWORD)
                    .field(field + BenefSearchConstants.KEYWORD)
                    .size(20));
    searchSourceBuilder.aggregation(nestedAgg);
    searchSourceBuilder.query(boolQueryBuilder);
    searchSourceBuilder.size(0);
    searchRequest.source(searchSourceBuilder);
    try {
      return getStringList(client.search(searchRequest, RequestOptions.DEFAULT));
    } catch (IOException e) {
      logger.error(
          "Error getting autocompletion for field {}, with error:{}", field, e.getMessage());
    }
    return new ArrayList<>();
  }

  private void generateSearchRequestOnContract(
      BenefSearchRequest search, BoolQueryBuilder boolQueryBuilder) {
    BoolQueryBuilder contratQueryBuilder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(search.getFirstName())) {
      contratQueryBuilder.must(
          QueryBuilders.matchQuery(
                  BenefSearchConstants.CONTRATS_DATA_NOM_PRENOM + BenefSearchConstants.KEYWORD,
                  search.getFirstName())
              .operator(Operator.AND));
    }
    if (StringUtils.isNotBlank(search.getName())) {
      BoolQueryBuilder orOpeartor = QueryBuilders.boolQuery();
      orOpeartor.should(
          QueryBuilders.matchQuery(
                  BenefSearchConstants.CONTRATS_DATA_NOM_FAMILLE + BenefSearchConstants.KEYWORD,
                  search.getName())
              .operator(Operator.AND));
      orOpeartor.should(
          QueryBuilders.matchQuery(
                  BenefSearchConstants.CONTRATS_DATA_NOM_USAGE + BenefSearchConstants.KEYWORD,
                  search.getName())
              .operator(Operator.AND));
      contratQueryBuilder.must(orOpeartor);
    }
    String subscriberIdOrContractNumber = search.getSubscriberIdOrContractNumber();
    if (StringUtils.isNotBlank(subscriberIdOrContractNumber)) {
      BoolQueryBuilder orOpeartor = QueryBuilders.boolQuery();
      orOpeartor.should(
          QueryBuilders.queryStringQuery(subscriberIdOrContractNumber)
              .field(BenefSearchConstants.CONTRATS_NUMERO_CONTRAT_KEYWORD));
      orOpeartor.should(
          QueryBuilders.queryStringQuery(subscriberIdOrContractNumber)
              .field(BenefSearchConstants.CONTRATS_NUMERO_ADHERENT_KEYWORD));
      contratQueryBuilder.must(orOpeartor);
    }
    if (StringUtils.isNotBlank(search.getContractNumber())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getContractNumber())
              .field(BenefSearchConstants.CONTRATS_NUMERO_CONTRAT_KEYWORD));
    }
    if (StringUtils.isNotBlank(search.getSubscriberNumber())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getSubscriberNumber())
              .field(BenefSearchConstants.CONTRATS_NUMERO_ADHERENT_KEYWORD));
    }
    if (StringUtils.isNotBlank(search.getIssuingCompany())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getIssuingCompany())
              .field(
                  BenefSearchConstants.CONTRATS_SOCIETE_EMETTRICE + BenefSearchConstants.KEYWORD));
    }

    if (StringUtils.isNotBlank(search.getInsurerExchangeId())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getInsurerExchangeId())
              .field("contrats.numeroAMCEchange.keyword"));
    }

    if (StringUtils.isNotBlank(search.getSubscriberId())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getSubscriberId())
              .field(BenefSearchConstants.CONTRATS_NUMERO_ADHERENT_KEYWORD));
    }

    if (StringUtils.isNotBlank(search.getEmail())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getEmail())
              .field("contrats.data.contact.email.keyword"));
    }
    if (StringUtils.isNotBlank(search.getBic())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getBic())
              .field("contrats.data.destinatairesPaiements.rib.bic.keyword"));
    }
    if (StringUtils.isNotBlank(search.getIban())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getIban())
              .field("contrats.data.destinatairesPaiements.rib.iban.keyword"));
    }
    if (StringUtils.isNotBlank(search.getCity())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getCity())
              .field("contrats.data.adresse.ligne6.keyword"));
    }
    if (StringUtils.isNotBlank(search.getStreet())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getStreet())
              .field("contrats.data.adresse.ligne4.keyword"));
    }

    if (StringUtils.isNotBlank(search.getPostalCode())) {
      contratQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getPostalCode())
              .field("contrats.data.adresse.codePostal.keyword"));
    }

    if (StringUtils.isNotBlank(search.getNameOrSubscriberIdorContractNumber())) {
      BoolQueryBuilder orOpeartor = QueryBuilders.boolQuery();
      orOpeartor.should(
          QueryBuilders.queryStringQuery(search.getNameOrSubscriberIdorContractNumber())
              .field("contrats.data.nom.nomFamille.keyword"));
      orOpeartor.should(
          QueryBuilders.queryStringQuery(search.getNameOrSubscriberIdorContractNumber())
              .field("contrats.data.nom.nomUsage.keyword"));
      orOpeartor.should(
          QueryBuilders.queryStringQuery(search.getNameOrSubscriberIdorContractNumber())
              .field(BenefSearchConstants.CONTRATS_NUMERO_CONTRAT_KEYWORD));
      contratQueryBuilder.must(orOpeartor);
    }

    elasticAuthorizationScopeHandler.executeForAuthorizedIssuingCompanies(
        CLIENT_TYPE_INSURER,
        authorizedIssuingCompanies ->
            contratQueryBuilder.must(
                QueryBuilders.termsQuery(
                    BenefSearchConstants.CONTRATS_SOCIETE_EMETTRICE + BenefSearchConstants.KEYWORD,
                    authorizedIssuingCompanies)));

    NestedQueryBuilder contratNested = contratNestedQuery(contratQueryBuilder);
    boolQueryBuilder.must(contratNested);
  }

  private void generateSearchRequestOnIdentite(
      BenefSearchRequest search, BoolQueryBuilder boolQueryBuilder) {
    if (StringUtils.isNotBlank(search.getNir())) {
      BoolQueryBuilder orOpeartor = QueryBuilders.boolQuery();
      orOpeartor.should(
          QueryBuilders.queryStringQuery(search.getNir()).field("identite.nir.code.keyword"));
      orOpeartor.should(
          QueryBuilders.queryStringQuery(search.getNir())
              .field("identite.affiliationsRO.nir.code.keyword"));
      boolQueryBuilder.must(orOpeartor);
    }
    Map<String, String> historiqueDateRangNaissanceParams = new HashMap<>();

    if (StringUtils.isNotBlank(search.getBirthDate())) {
      historiqueDateRangNaissanceParams.put(
          "identite.historiqueDateRangNaissance.dateNaissance.keyword", search.getBirthDate());
    }

    if (StringUtils.isNotBlank(search.getBirthRank())) {
      historiqueDateRangNaissanceParams.put(
          "identite.historiqueDateRangNaissance.rangNaissance.keyword", search.getBirthRank());
    }

    if (!historiqueDateRangNaissanceParams.isEmpty()) {
      BoolQueryBuilder historiqueDateRangNaissanceBoolQuery = QueryBuilders.boolQuery();
      historiqueDateRangNaissanceParams.forEach(
          (field, value) ->
              historiqueDateRangNaissanceBoolQuery.must(
                  QueryBuilders.queryStringQuery(value).field(field)));
      boolQueryBuilder.must(
          QueryBuilders.nestedQuery(
              "identite.historiqueDateRangNaissance",
              historiqueDateRangNaissanceBoolQuery,
              ScoreMode.None));
    }
  }

  private void generateSearchRequestOnAmc(
      BenefSearchRequest search, BoolQueryBuilder boolQueryBuilder) {
    if (StringUtils.isNotBlank(search.getDeclarantId())) {
      boolQueryBuilder.must(
          QueryBuilders.queryStringQuery(search.getDeclarantId()).field("amc.idDeclarant.keyword"));
    }

    if (StringUtils.isNotBlank(search.getDeclarantIdOrLabel())) {
      BoolQueryBuilder orOpeartor = QueryBuilders.boolQuery();
      orOpeartor.should(
          QueryBuilders.queryStringQuery(search.getDeclarantIdOrLabel()).field("amc.idDeclarant"));
      orOpeartor.should(
          QueryBuilders.queryStringQuery(search.getDeclarantIdOrLabel()).field("amc.libelle"));
      boolQueryBuilder.must(orOpeartor);
    }

    elasticAuthorizationScopeHandler.executeForAuthorizedIssuingCompanies(
        CLIENT_TYPE_OTP,
        authorizedIssuingCompanies ->
            boolQueryBuilder.must(
                QueryBuilders.termsQuery(
                    BenefSearchConstants.IDDECLARANT + BenefSearchConstants.KEYWORD,
                    authorizedIssuingCompanies)));
  }

  private void generateSearchRequestOnServiceTP(
      BenefSearchRequest search, BoolQueryBuilder boolQueryBuilder) {
    if (StringUtils.isNotBlank(search.getServiceMetier())) {
      boolQueryBuilder.must(
          QueryBuilders.wildcardQuery("services", search.getServiceMetier().toLowerCase()));
    }
  }

  public BoolQueryBuilder generateSearchRequest(BenefSearchRequest search) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

    this.escapeSlashesInSearch(search);

    generateSearchRequestOnContract(search, boolQueryBuilder);
    generateSearchRequestOnIdentite(search, boolQueryBuilder);
    generateSearchRequestOnAmc(search, boolQueryBuilder);
    generateSearchRequestOnServiceTP(search, boolQueryBuilder);

    return boolQueryBuilder;
  }

  private void escapeSlashesInSearch(BenefSearchRequest search) {
    search.setBic(this.escapeSlashes(search.getBic()));
    search.setBirthRank(this.escapeSlashes(search.getBirthRank()));
    search.setCity(this.escapeSlashes(search.getCity()));
    search.setDeclarantId(this.escapeSlashes(search.getDeclarantId()));
    search.setEmail(this.escapeSlashes(search.getEmail()));
    search.setFirstName(this.escapeSlashes(search.getFirstName()));
    search.setIban(this.escapeSlashes(search.getIban()));
    search.setName(this.escapeSlashes(search.getName()));
    search.setNir(this.escapeSlashes(search.getNir()));
    search.setStreet(this.escapeSlashes(search.getStreet()));
    search.setSubscriberIdOrContractNumber(
        this.escapeSlashes(search.getSubscriberIdOrContractNumber()));
    search.setNameOrSubscriberIdorContractNumber(
        this.escapeSlashes(search.getNameOrSubscriberIdorContractNumber()));
  }

  private String escapeSlashes(String searchParam) {
    if (StringUtils.isNotBlank(searchParam)) {
      return searchParam.replaceAll("\\/", "\\\\/"); // NOSONAR replace ne marche pas pareil
    }
    return searchParam;
  }

  public BenefElasticPageResult search(BenefSearchRequest search) {
    BoolQueryBuilder boolQueryBuilder = generateSearchRequest(search);
    int perPage = search.getPerPage();
    if (perPage == 0) {
      perPage = 10;
    }
    NativeSearchQueryBuilder searchQueryBuilder =
        new NativeSearchQueryBuilder()
            .withQuery(boolQueryBuilder)
            .withPageable(PageRequest.of(search.getPage(), perPage))
            .withSorts(
                new FieldSortBuilder(
                        BenefSearchConstants.CONTRATS_DATA_NOM_PRENOM
                            + BenefSearchConstants.KEYWORD)
                    .order(SortOrder.ASC),
                new FieldSortBuilder(
                        BenefSearchConstants.CONTRATS_DATA_NOM_USAGE + BenefSearchConstants.KEYWORD)
                    .order(SortOrder.ASC));
    NativeSearchQuery searchQuery = searchQueryBuilder.build();
    var result =
        SearchHitSupport.searchPageFor(
            elastic.search(
                searchQuery, BenefAIV5.class, IndexCoordinates.of(indexBenef.getIndexAlias())),
            searchQuery.getPageable());

    PagingResponseModel page = new PagingResponseModel();
    page.setPage(search.getPage());
    page.setPerPage(search.getPerPage());
    page.setTotalElements(result.getTotalPages());
    BenefElasticPageResult pagedResult = new BenefElasticPageResult();

    String environnement =
        determineEnvInterneOrExterne(elasticAuthorizationScopeHandler.getClientType());
    List<BenefAIV5> enrichedContent =
        result.getContent().stream()
            .map(
                searchHit -> {
                  BenefAIV5 benef = searchHit.getContent();
                  benef.setEnvironnement(environnement);
                  return benef;
                })
            .toList();

    pagedResult.setData(enrichedContent);
    pagedResult.setPaging(page);

    return pagedResult;
  }

  private String determineEnvInterneOrExterne(String clientType) {
    return Constants.CLIENT_TYPE_INSURER.equals(clientType)
        ? Constants.ENV_EXTERNE
        : Constants.ENV_INTERNE;
  }

  @ContinueSpan(log = "getBenefById elastic")
  public BenefAIV5 getBenefById(String id) {

    BenefAIV5 beneficiary =
        elastic.get(id, BenefAIV5.class, IndexCoordinates.of(indexBenef.getIndexAlias()));

    if (beneficiary == null) {
      logger.warn("Bénéficiaire avec ID {} non trouvé dans Elasticsearch", id);
      return null;
    }

    // Vérification des autorisations
    if (!elasticAuthorizationScopeHandler.isAuthorized(beneficiary)) {
      logger.warn("Accès non autorisé pour le bénéficiaire avec ID {}", id);
      return null;
    }

    return beneficiary;
  }

  @ContinueSpan(log = "getByListOfIds elastic")
  public List<BenefAIV5> getByListOfIds(List<String> ids) {
    if (CollectionUtils.isNotEmpty(ids)) {
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

      var idsQuery = QueryBuilders.idsQuery().addIds(ids.toArray(new String[0]));
      boolQueryBuilder.must(idsQuery);

      elasticAuthorizationScopeHandler.executeForAuthorizedIssuingCompanies(
          CLIENT_TYPE_INSURER,
          authorizedIssuingCompanies -> {
            BoolQueryBuilder contratQueryBuilder = QueryBuilders.boolQuery();
            contratQueryBuilder.must(
                QueryBuilders.termsQuery(
                    BenefSearchConstants.CONTRATS_SOCIETE_EMETTRICE + BenefSearchConstants.KEYWORD,
                    authorizedIssuingCompanies));

            NestedQueryBuilder contratNested = contratNestedQuery(contratQueryBuilder);
            boolQueryBuilder.must(contratNested);
          });

      elasticAuthorizationScopeHandler.executeForAuthorizedIssuingCompanies(
          CLIENT_TYPE_OTP,
          authorizedIssuingCompanies -> {
            boolQueryBuilder.must(
                QueryBuilders.termsQuery(
                    BenefSearchConstants.IDDECLARANT + BenefSearchConstants.KEYWORD,
                    authorizedIssuingCompanies));
          });

      NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
      searchQueryBuilder.withQuery(boolQueryBuilder);

      var searchQuery = searchQueryBuilder.build();
      var result =
          elastic
              .search(searchQuery, BenefAIV5.class, IndexCoordinates.of(indexBenef.getIndexAlias()))
              .getSearchHits();

      return result.stream().map(SearchHit::getContent).toList();
    }
    return new ArrayList<>();
  }

  @ContinueSpan(log = "delete benef elastic")
  public void delete(String id) {
    try {
      elastic.delete(id, IndexCoordinates.of(indexBenef.getIndexAlias()));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    }
  }

  @ContinueSpan(log = "deleteBulk benefs elastic")
  public void deleteBulk(String amc) {
    NativeSearchQuery query =
        new NativeSearchQueryBuilder()
            .withQuery(QueryBuilders.matchQuery("amc.idDeclarant", amc))
            .build();
    try {
      elastic.delete(query, BenefAIV5.class, IndexCoordinates.of(indexBenef.getIndexAlias()));
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw e;
    }
  }

  @ContinueSpan(log = "search benef elastic")
  public BenefElasticPageResult search(BenefAdvancedSearchRequest request) throws IOException {
    SearchSourceBuilder searchQuery = buildSearchQuery(request);

    SearchRequest searchRequest = new SearchRequest(indexBenef.getIndexAlias());
    searchRequest.source(searchQuery);
    SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
    return mapToBenefElasticSearchResponse(response, request.getPage(), request.getPerPage());
  }

  public SearchSourceBuilder buildSearchQuery(BenefAdvancedSearchRequest request) {
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    BoolQueryBuilder contratQueryBuilder = QueryBuilders.boolQuery();

    if (StringUtils.isNotBlank(request.getName())) {
      BoolQueryBuilder nameQuery =
          QueryBuilders.boolQuery()
              .should(
                  QueryBuilders.matchPhrasePrefixQuery(
                      BenefSearchConstants.CONTRATS_DATA_NOM_FAMILLE,
                      request.getName().toLowerCase()))
              .should(
                  QueryBuilders.matchPhrasePrefixQuery(
                      BenefSearchConstants.CONTRATS_DATA_NOM_USAGE,
                      request.getName().toLowerCase()));
      contratQueryBuilder.must(nameQuery);
    }

    if (StringUtils.isNotBlank(request.getFirstName())) {
      contratQueryBuilder.must(
          QueryBuilders.matchPhraseQuery(
              BenefSearchConstants.CONTRATS_DATA_NOM_PRENOM, request.getFirstName().toLowerCase()));
    }

    if (StringUtils.isNotBlank(request.getNir())) {
      queryBuilder.must(
          QueryBuilders.boolQuery()
              .should(
                  QueryBuilders.wildcardQuery(
                          BenefSearchConstants.IDENTITE_NIR_CODE, request.getNir().toLowerCase())
                      .caseInsensitive(true))
              .should(
                  QueryBuilders.wildcardQuery(
                          BenefSearchConstants.IDENTITE_AFFILIATIONS_RO_NIR_CODE,
                          request.getNir().toLowerCase())
                      .caseInsensitive(true)));
    }

    if (StringUtils.isNotBlank(request.getSubscriberId())) {
      contratQueryBuilder.must(
          QueryBuilders.wildcardQuery(
              BenefSearchConstants.CONTRATS_NUMERO_ADHERENT,
              request.getSubscriberId().toLowerCase()));
    }

    if (StringUtils.isNotBlank(request.getContractNumber())) {
      contratQueryBuilder.must(
          QueryBuilders.matchQuery(
              BenefSearchConstants.CONTRATS_NUMERO_CONTRAT, request.getContractNumber()));
    }

    if (StringUtils.isNotBlank(request.getBirthDate())) {
      queryBuilder.must(
          QueryBuilders.boolQuery()
              .should(
                  QueryBuilders.matchQuery(
                      BenefSearchConstants.IDENTITE_DATE_NAISSANCE, request.getBirthDate()))
              .should(
                  QueryBuilders.matchQuery(
                      BenefSearchConstants.IDENTITE_HISTORIQUE_DATE_RANG_NAISSANCE_DATE_NAISSANCE,
                      request.getBirthDate())));
    }

    if (StringUtils.isNotBlank(request.getBirthRank())) {
      queryBuilder.must(
          QueryBuilders.matchQuery(
              BenefSearchConstants.IDENTITE_RANG_NAISSANCE, request.getBirthRank()));
    }

    if (StringUtils.isNotBlank(request.getPostalCode())) {
      contratQueryBuilder.must(
          QueryBuilders.matchQuery(
              BenefSearchConstants.CONTRATS_DATA_ADRESSE_CODE_POSTAL, request.getPostalCode()));
    }

    if (StringUtils.isNotBlank(request.getIssuingCompany())) {
      contratQueryBuilder.must(
          QueryBuilders.matchQuery(
              BenefSearchConstants.CONTRATS_SOCIETE_EMETTRICE, request.getIssuingCompany()));
    }

    if (StringUtils.isNotBlank(request.getInsurerId())) {
      queryBuilder.must(
          QueryBuilders.matchQuery(BenefSearchConstants.IDDECLARANT, request.getInsurerId()));
    }

    if (StringUtils.isNotBlank(request.getDateReference())) {
      InnerHitBuilder innerHitsMaxSize = new InnerHitBuilder();
      innerHitsMaxSize.setSize(BenefSearchConstants.MAX_SIZE_INNER_HITS);

      BoolQueryBuilder contratRangesQuery =
          QueryBuilders.boolQuery()
              .must(
                  QueryBuilders.rangeQuery(BenefSearchConstants.CONTRATS_PERIODES_DEBUT)
                      .lte(request.getDateReference()))
              .must(
                  QueryBuilders.rangeQuery(BenefSearchConstants.CONTRATS_PERIODES_FIN)
                      .gte(request.getDateReference()));
      NestedQueryBuilder contratNested =
          QueryBuilders.nestedQuery(
                  BenefSearchConstants.CONTRATS_PERIODES, contratRangesQuery, ScoreMode.Total)
              .innerHit(innerHitsMaxSize);
      queryBuilder.must(contratNested);

      BoolQueryBuilder osRangesQuery =
          QueryBuilders.boolQuery()
              .must(
                  QueryBuilders.rangeQuery(BenefSearchConstants.SOCIETES_EMETTRICES_PERIODES_DEBUT)
                      .lte(request.getDateReference()))
              .must(
                  QueryBuilders.rangeQuery(BenefSearchConstants.SOCIETES_EMETTRICES_PERIODES_FIN)
                      .gte(request.getDateReference()));
      NestedQueryBuilder osNested =
          QueryBuilders.nestedQuery(
                  BenefSearchConstants.SOCIETES_EMETTRICES_PERIODES, osRangesQuery, ScoreMode.Total)
              .innerHit(innerHitsMaxSize);
      queryBuilder.must(osNested);
    }

    elasticAuthorizationScopeHandler.executeForAuthorizedIssuingCompanies(
        CLIENT_TYPE_INSURER,
        authorizedIssuingCompanies ->
            contratQueryBuilder.must(
                QueryBuilders.termsQuery(
                    BenefSearchConstants.CONTRATS_SOCIETE_EMETTRICE + BenefSearchConstants.KEYWORD,
                    authorizedIssuingCompanies)));
    elasticAuthorizationScopeHandler.executeForAuthorizedIssuingCompanies(
        CLIENT_TYPE_OTP,
        authorizedIssuingCompanies ->
            contratQueryBuilder.must(
                QueryBuilders.termsQuery(
                    BenefSearchConstants.IDDECLARANT + BenefSearchConstants.KEYWORD,
                    authorizedIssuingCompanies)));

    if (CollectionUtils.isNotEmpty(contratQueryBuilder.must())) {
      NestedQueryBuilder contratNested = contratNestedQuery(contratQueryBuilder);
      queryBuilder.must(contratNested);
    }

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(queryBuilder);

    addSorting(request, searchSourceBuilder);

    searchSourceBuilder.size(request.getPerPage());
    searchSourceBuilder.from(request.getPerPage() * request.getPage());

    return searchSourceBuilder;
  }

  private void addSorting(
      BenefAdvancedSearchRequest request, SearchSourceBuilder searchSourceBuilder) {
    List<SortBuilder<?>> sorts =
        request.getSorts().stream()
            .map(
                sort ->
                    SortBuilders.fieldSort(sort.getField())
                        .order(SortOrder.fromString(sort.getOrder())))
            .collect(Collectors.toList());
    for (SortBuilder sort : sorts) {
      searchSourceBuilder.sort(sort);
    }
  }

  private BenefElasticPageResult mapToBenefElasticSearchResponse(
      SearchResponse searchResponse, int wantedPage, int wantedPerPage) throws IOException {
    List<BenefAIV5> beneficiaries = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    for (org.opensearch.search.SearchHit hit : searchResponse.getHits().getHits()) {
      if (hit.hasSource()) {
        BenefAIV5 benef = mapper.readValue(hit.getSourceAsString(), BenefAIV5.class);
        benef.setId(hit.getId());
        filterTFromNested(
            benef.getSocietesEmettrices(),
            hit,
            BenefSearchConstants.SOCIETES_EMETTRICES_PERIODES,
            SocieteEmettrice::getPeriodes);
        filterTFromNested(
            benef.getContrats(),
            hit,
            BenefSearchConstants.CONTRATS_PERIODES,
            ContratV5::getPeriodes);
        beneficiaries.add(benef);
      }
    }

    TotalHits totalHits = searchResponse.getHits().getTotalHits();
    int totalElements = totalHits != null ? (int) totalHits.value : 0;
    int totalPages =
        wantedPerPage > 0 ? (int) Math.ceil((double) totalElements / wantedPerPage) : 0;
    int resteTotal = totalElements - wantedPerPage * wantedPage;
    int restePage = Math.min(Math.max(resteTotal, 0), wantedPerPage);

    PagingResponseModel paging =
        new PagingResponseModel(wantedPage, totalPages, totalElements, restePage);

    BenefElasticPageResult result = new BenefElasticPageResult();
    result.setData(beneficiaries);
    result.setPaging(paging);

    return result;
  }

  /**
   * Récupère l'objet le plus gros d'une requête nested ex : requete nested sur le periodes de
   * contrats (contrats.periodes) -> récupère les contrats pour lesquels le nested a retourner une
   * periode. T type générique pour accepter n'importe quelle liste d'objet a extraire des donnees
   * nested dans les searchHits
   */
  private <T> void filterTFromNested(
      List<T> fullList,
      org.opensearch.search.SearchHit searchHit,
      String path,
      Function<T, List<Periode>> getPeriodes) {
    if (MapUtils.isNotEmpty(searchHit.getInnerHits())
        && searchHit.getInnerHits().containsKey(path)) {
      Map<Integer, Set<Integer>> offsetsAndSubOffSets = new HashMap<>();

      // Recupere les offsets des objets principaux et une liste des offsets child par
      // objet pour filtrer la liste de periode plus tard
      for (org.opensearch.search.SearchHit hit : searchHit.getInnerHits().get(path)) {
        if (hit.getNestedIdentity() != null) {
          int mainIndex = hit.getNestedIdentity().getOffset();
          offsetsAndSubOffSets.putIfAbsent(mainIndex, new LinkedHashSet<>());
          if (hit.getNestedIdentity().getChild() != null) {
            int childIndex = hit.getNestedIdentity().getChild().getOffset();
            offsetsAndSubOffSets.get(mainIndex).add(childIndex);
          }
        }
      }

      // Filtre la liste fullList suivant les offset des objets principaux trouves. La
      // liste de periodes par objet est aussi filtree afin d'avoir seulement les
      // periodes demandees dans elastic
      List<T> result = new ArrayList<>();
      offsetsAndSubOffSets.forEach(
          (index, periodesIndexs) -> {
            if (fullList.size() > index) {
              T mainObj = fullList.get(index);
              List<Periode> periodes = getPeriodes.apply(mainObj);
              List<Periode> filteredPeriodes = new ArrayList<>();
              periodesIndexs.stream()
                  .filter(periodeIndex -> periodes.size() > periodeIndex)
                  .map(periodes::get)
                  .forEach(filteredPeriodes::add);
              periodes.clear();
              periodes.addAll(filteredPeriodes);

              result.add(mainObj);
            }
          });

      fullList.clear();
      fullList.addAll(result);
    }
  }

  private NestedQueryBuilder contratNestedQuery(BoolQueryBuilder boolQueryBuilder) {
    InnerHitBuilder noInnerHitsSize = new InnerHitBuilder();
    noInnerHitsSize.setSize(SIZE_NO_INNER_HITS);
    return QueryBuilders.nestedQuery(
            CONTRATS_IN_COLLECTION_BENEF, boolQueryBuilder, ScoreMode.Total)
        .innerHit(noInnerHitsSize);
  }
}
