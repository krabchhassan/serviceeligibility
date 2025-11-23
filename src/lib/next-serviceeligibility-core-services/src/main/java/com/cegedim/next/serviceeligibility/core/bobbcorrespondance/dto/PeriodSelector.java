package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PeriodSelector(@NotNull LocalDate from, LocalDate to) {}
