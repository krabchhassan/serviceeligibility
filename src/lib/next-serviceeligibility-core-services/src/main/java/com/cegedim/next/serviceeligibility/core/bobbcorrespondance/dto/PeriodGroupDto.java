package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto;

import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import java.time.LocalDateTime;
import java.util.List;

public record PeriodGroupDto(LocalDateTime from, LocalDateTime to, List<ProductElement> items) {}
