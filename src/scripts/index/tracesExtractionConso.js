db.tracesExtractionConso.createIndex({
	"idDeclarant": 1
}, { name: "tracesExtractionConso01" })

db.tracesExtractionConso.createIndex({
	"idDeclarationConsolidee": 1
}, { name: "tracesExtractionConso02" })

db.tracesExtractionConso.createIndex({
	"idDeclaration": 1,
	"codeService": 1
}, { name: "tracesExtractionConso03" })

db.tracesExtractionConso.createIndex({
	"nomFichier": 1
}, { name: "tracesExtractionConso04" })
