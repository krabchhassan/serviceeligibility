db.servicePrestIJ.createIndex(
	{"traceId":1}, 
	{ name: "servicePrestIJ01" })

db.servicePrestIJ.createIndex(
	{"assures.nir.code":1, 
	"assures.nir.dateNaissance":1,
	"assures.nir.rangNaissance":1}, 
	{ name: "servicePrestIJ02" })
