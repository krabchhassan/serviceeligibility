package com.cegedim.next.serviceeligibility.core.dto;

public record GuaranteeSearchResultDto(
    String id, String codeContractElement, String codeInsurer, boolean ignored) {}
