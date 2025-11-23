package com.cegedim.next.serviceeligibility.core.bobb;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record GuaranteeCreationResponse(
    String insurerCode, String guaranteeId, LocalDate validityDate) {}
