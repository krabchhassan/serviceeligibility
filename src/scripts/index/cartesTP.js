db.cartesTP.createIndex({
	"dateConsolidation": 1,
	"idDeclarant": 1,
	"traitementOrigine": 1
}, { name: "cartesTP01" })

db.cartesTP.createIndex({
	"carteSelectionnee.lotFichier": 1
}, { name: "cartesTP02" })

db.cartesTP.createIndex({
	"idDeclarant": 1
}, { name: "cartesTP03" })