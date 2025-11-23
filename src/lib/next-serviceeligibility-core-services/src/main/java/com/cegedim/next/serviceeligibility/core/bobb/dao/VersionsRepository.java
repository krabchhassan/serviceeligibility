package com.cegedim.next.serviceeligibility.core.bobb.dao;

import static com.cegedim.next.serviceeligibility.core.bobbcorrespondance.constants.CommonConstants.ACTIVE;
import static com.cegedim.next.serviceeligibility.core.bobbcorrespondance.constants.CommonConstants.STATUS;

import com.cegedim.next.serviceeligibility.core.bobb.Versions;
import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class VersionsRepository extends AbstractRepository<Versions> {
  public VersionsRepository() {
    super(Versions.class);
  }

  public Optional<Versions> findActiveVersion() {
    Query query = new Query(Criteria.where(STATUS).is(ACTIVE));
    return findOne(query);
  }
}
