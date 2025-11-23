package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductSelector(
    @NotBlank String codeOffer,
    @NotBlank String codeProduct,
    @NotBlank String codeBenefitNature,
    @NotBlank String codeAmc,
    java.time.LocalDate from) {}
