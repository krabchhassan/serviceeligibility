package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto;

import jakarta.validation.constraints.NotNull;

public record ClosePeriodRequest(java.time.@NotNull LocalDate to, @NotNull PeriodSelector period) {}
