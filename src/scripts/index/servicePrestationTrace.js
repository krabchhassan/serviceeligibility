db.servicePrestationTrace.createIndex( {
	"fileName": 1
}, { name: "servicePrestationTrace01" })

db.servicePrestationTrace.createIndex( {
	"numeroContrat": 1,
	"idDeclarant": 1
	}, { name: "servicePrestationTrace02" })