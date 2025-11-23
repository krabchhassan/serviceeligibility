package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.mongodb.client.ClientSession;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/** Interface de la classe d'accès aux {@code cartePapier} de la base de donnees. */
public interface CartePapierDao extends IMongoGenericDao<CartePapierEditique> {
  Collection<CartePapierEditique> insertAll(
      List<CartePapierEditique> papiers, ClientSession session);

  Stream<CartePapierEditique> getAllCards(String identifiant);

  long deleteByAMC(String amc);

  long deleteByDeclaration(String declaration);
}
