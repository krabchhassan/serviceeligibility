package com.cegedim.next.serviceeligibility.core.bobb.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;

public class AbstractRepository<T> {

  private static final String ID = "_id";
  // We need class type for mongoTemplate
  private final Class<T> typeParameterClass;

  @Autowired protected MongoTemplate template;

  public AbstractRepository(final Class<T> typeParameterClass) {
    this.typeParameterClass = typeParameterClass;
  }

  // Launch by batch
  public AbstractRepository(final Class<T> typeParameterClass, MongoTemplate mongoTemplate) {
    this.typeParameterClass = typeParameterClass;
    this.template = mongoTemplate;
  }

  // Find
  public Optional<T> findOneById(final String id) {
    final Query query = new Query();
    query.addCriteria(Criteria.where(ID).is(id));

    return findOne(query);
  }

  public Optional<T> findOne(final Query query) {
    final T one = template.findOne(query, typeParameterClass);
    if (one != null) {
      return Optional.of(one);
    }
    return Optional.empty();
  }

  public Collection<T> find(final Query query) {
    return template.find(query, typeParameterClass);
  }

  public Collection<T> find() {
    return template.findAll(typeParameterClass);
  }

  // Delete
  public void removeById(final String id) {
    final Query query = new Query();
    query.addCriteria(Criteria.where(ID).is(id));

    remove(query);
  }

  public void remove(final Query query) {
    template.remove(query, typeParameterClass);
  }

  // Create, Put
  public T save(final T t) {
    template.save(t);

    return t;
  }

  public Sort sortDocuments(String sortBy, String direction) {
    String sortColumn;
    Sort.Direction sortDirection;
    if (StringUtils.isNotBlank(sortBy)) {
      sortColumn = sortBy;
      if (direction.equalsIgnoreCase("DESC")) {
        sortDirection = Sort.Direction.DESC;
      } else {
        sortDirection = Sort.Direction.ASC;
      }
    } else {
      sortColumn = Constants.CODE;
      sortDirection = Sort.Direction.ASC;
    }
    return Sort.by(sortDirection, sortColumn);
  }

  public List<T> find(Query query, @Nullable Pageable pageable) {
    Query q = (query == null) ? new Query() : query;
    if (pageable != null) q.with(pageable);
    return template.find(q, typeParameterClass);
  }

  public long count(Query query) {
    return template.count(query, typeParameterClass);
  }

  protected String resolvedCollection() {
    return template.getCollectionName(typeParameterClass);
  }

  /** Construit l’agrégation avec options par défaut (allowDiskUse=true). */
  protected Aggregation buildAggregation(List<AggregationOperation> ops) {
    return newAggregation(ops).withOptions(newAggregationOptions().allowDiskUse(true).build());
  }

  /** Exécute l’agrégation sur la collection de T et mappe vers resultType. */
  protected <R> List<R> runAggregation(Aggregation agg, Class<R> resultType) {
    return template.aggregate(agg, resolvedCollection(), resultType).getMappedResults();
  }

  /** DTO simple pour projeter une valeur unique (ex: _id → value). */
  public static class Value {
    private String value;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  /**
   * Ajoute sort(_id ASC) + project(_id as value), exécute l’agrégation et retourne List<String>.
   */
  protected List<String> distinctByIdAsc(List<AggregationOperation> ops) {
    ops.add(sort(Sort.by(Sort.Direction.ASC, ID)));
    ops.add(project().and(ID).as("value").andExclude(ID));

    Aggregation agg = buildAggregation(ops);
    return runAggregation(agg, Value.class).stream().map(Value::getValue).toList();
  }
}
