db.restitutionCarte.createIndex({
	"effetDebut": 1,
	"idDeclarant": 1
}, { name: "restitutionCarte01" })

db.restitutionCarte.createIndex({
	"numeroPersonne": 1,
	"numeroContrat": 1,
	"numeroAdherent": 1,
	"idDeclarant": 1
}, { name: "restitutionCarte02" })
