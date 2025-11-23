db.servicePrestation.createIndex(
	{"assures.identite.dateNaissance":1, "assures.droits.periode.debut":1, "assures.droits.periode.fin":1}, 
	{ name: "servicePrestation01" })

db.servicePrestation.createIndex(
	{"assures.identite.nir.code":1}, 
	{ name: "servicePrestation02" })

db.servicePrestation.createIndex(
	{"assures.identite.nirAffiliationRO.nir.code":1, "assures.identite.nirAffiliationRO.periode.debut":1, "assures.identite.nirAffiliationRO.periode.fin":1}, 
	{ name: "servicePrestation03" })

db.servicePrestation.createIndex(
	{"idDeclarant":1, "numeroAdherent":1}, 
	{ name: "servicePrestation04" })

db.servicePrestation.createIndex(
	{"idDeclarant":1, "numero":1, "numeroAdherent":1}, 
	{ name: "servicePrestation05" })

db.servicePrestation.createIndex(
	{"idDeclarant":1, "assures.identite.numeroPersonne":1, "assures.droits.periode.debut":1, "assures.droits.periode.fin":1}, 
	{ name: "servicePrestation06" })
