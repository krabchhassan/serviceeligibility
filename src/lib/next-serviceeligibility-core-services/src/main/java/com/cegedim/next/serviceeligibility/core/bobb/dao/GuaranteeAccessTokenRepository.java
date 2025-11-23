package com.cegedim.next.serviceeligibility.core.bobb.dao;

import com.cegedim.next.serviceeligibility.core.bobb.GuaranteeAccessToken;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GuaranteeAccessTokenRepository
    extends MongoRepository<GuaranteeAccessToken, String> {
  Optional<GuaranteeAccessToken> findByContextKey(String contextKey);
}
