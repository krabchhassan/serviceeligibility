package com.cegedim.next.serviceeligibility.core.services.pojo;

import lombok.Data;

@Data
public class ParametreSimple {
  String _id;
  String code;
  String codeValeur;
  String libelleValeur;

  public ParametreSimple(String code, String codeValeur, String libelleValeur) {
    this._id = "UUID";
    this.code = code;
    this.codeValeur = codeValeur;
    this.libelleValeur = libelleValeur;
  }
}
