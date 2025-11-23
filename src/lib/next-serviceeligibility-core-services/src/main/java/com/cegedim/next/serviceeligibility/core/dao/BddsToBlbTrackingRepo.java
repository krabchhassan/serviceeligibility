package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbTracking;
import com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BddsToBlbTrackingRepo extends MongoRepository<BddsToBlbTracking, ObjectId> {
  @ExistsQuery("{'nir': ?0, 'dateNaissance': ?1, 'rangNaissance': ?2}")
  boolean exist(String nir, String dateNaissance, String rangNaissance);

  @CountQuery("{'status': ?0}")
  Long countByStatus(BddsToBlbStatus status);

  @ExistsQuery("{'status': ?0}")
  boolean existsByStatus(BddsToBlbStatus status);

  default BddsToBlbTracking findByIdHex(String offerId) {
    if (offerId == null || !ObjectId.isValid(offerId)) return null; // early exit
    return findById(new ObjectId(offerId)).orElse(null);
  }
}
