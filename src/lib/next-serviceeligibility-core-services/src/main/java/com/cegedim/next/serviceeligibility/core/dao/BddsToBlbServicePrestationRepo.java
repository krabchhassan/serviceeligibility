package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbServicePrestation;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BddsToBlbServicePrestationRepo
    extends MongoRepository<BddsToBlbServicePrestation, ObjectId> {
  @Query(value = "{}", fields = "{'assures.identite': 1}")
  Page<BddsToBlbServicePrestation> findAllContrat(Pageable pageable);
}
