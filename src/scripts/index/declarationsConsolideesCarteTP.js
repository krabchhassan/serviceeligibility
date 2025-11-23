db.declarationsConsolideesCarteTP.createIndex({
	"contrat.numero": 1,
	"idDeclarant": 1
}, { name: "declarationsConsolideesCarteTP01" })

db.declarationsConsolideesCarteTP.createIndex({
	"idDeclarant": 1,
	"periodeReferenceDebut": 1,
	"periodeReferenceFin": 1,
}, { name: "declarationsConsolideesCarteTP02" })