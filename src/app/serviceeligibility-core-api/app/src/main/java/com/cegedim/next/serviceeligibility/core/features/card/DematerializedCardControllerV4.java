package com.cegedim.next.serviceeligibility.core.features.card;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CardRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.CardResponse;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.ws.CardServiceV4;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v4/carteDematerialisee")
@AllArgsConstructor
public class DematerializedCardControllerV4 {
  private final CardServiceV4 cardServiceV4;

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<CardResponse>> getCardsV4(@RequestBody CardRequest request) {
    // Validate request, get cards and return formatted response
    List<CardResponse> response = cardServiceV4.buildResponse(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
