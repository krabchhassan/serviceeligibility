package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto;

public enum GroupBy {
  PRODUCT,
  PERIOD;

  public static GroupBy fromString(String raw) {
    if (raw == null || raw.isBlank()) return PRODUCT;
    return switch (raw.trim().toLowerCase()) {
      case "product" -> PRODUCT;
      case "period" -> PERIOD;
      default -> throw new IllegalArgumentException("Invalid groupBy. Allowed: product, period");
    };
  }
}
