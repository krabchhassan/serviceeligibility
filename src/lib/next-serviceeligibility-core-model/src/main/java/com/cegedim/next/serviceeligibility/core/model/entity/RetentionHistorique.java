package com.cegedim.next.serviceeligibility.core.model.entity;

import java.time.LocalDateTime;

public record RetentionHistorique(
    LocalDateTime creation, String dateResiliation, String dateRadiation, String dateSansEffet) {}
