package com.cegedim.next.serviceeligibility.core.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponseModel {
  private Integer page;
  private Integer totalPages;
  private Integer totalElements;
  private Integer perPage;
}
