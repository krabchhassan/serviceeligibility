db.restitutionCarte.find({ "effetDebut" : { $gte : ISODate("2021-01-01T00:00:00.000+0000") } }).forEach(function(doc){
  db.declarations.aggregate([{$match:{idDeclarant : doc.idDeclarant, "beneficiaire.numeroPersonne": doc.numeroPersonne, "contrat.numero": doc.numeroContrat, "beneficiaire.nirOd1": doc.nirOd1, "beneficiaire.dateNaissance": doc.dateNaissance, "beneficiaire.rangNaissance": doc.rangNaissance, effetDebut: {$lte : doc.effetDebut}, codeEtat: "R"}}, {$sort:{effetDebut: -1}}, {$limit:1}]).forEach(function(doc2){
    db.declarations.update(
	  {_id: doc2._id},
      {"$set": {"dateRestitutionCarte": doc.dateRestitutionCarte}}
      )
  })
});
