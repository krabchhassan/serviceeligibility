db.declarationsConsolideesCarteDemat.createIndex({
	"AMC_contrat": 1,
	"idDeclarant": 1,
	"periodeFin": 1
}, { name: "declarationsConsolideesCarteDemat01" })

db.declarationsConsolideesCarteDemat.createIndex({
	"idDeclarant" : 1,
	"contrat.numero" : 1,
	"beneficiaire.numeroPersonne" : 1
}, { name: "declarationsConsolideesCarteDematIdxBatch620" })
