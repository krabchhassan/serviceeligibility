db.sasContrat.createIndex(
	{"triggersBenefs.triggerId":1}, 
	{ name: "sasContrat01" })

db.sasContrat.createIndex(
	{"servicePrestationId":1}, 
	{ name: "sasContrat02" })

db.sasContrat.createIndex(
	{"idDeclarant":1,
	"numeroContrat":1}, 
	{ name: "sasContrat03" })
