package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CloseProductElementRequest(
    @NotNull LocalDate to, @NotNull ProductSelector selector) {}
