db.traces.createIndex({
	"idDeclaration": 1
}, { name: "traces01" })

db.traces.createIndex({
	"listeConsolidations.idDeclarationConsolidee": 1
}, { name: "IdxIdDeclarationConsolidee"})

db.traces.createIndex({
	"idDeclarant": 1,
	"nirOd1": 1
}, { name: "traces02" })
db.traces.createIndex({
    "idDeclarant" : 1,
    "listeConsolidations.listeExtractions.batch" : 1,
    "listeConsolidations.listeExtractions.numeroFichier" : 1,
    "listeConsolidations.listeExtractions.idLigne" : 1
}, { name: "traces03" })
