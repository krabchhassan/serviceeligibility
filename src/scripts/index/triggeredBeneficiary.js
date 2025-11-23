db.triggeredBeneficiary.createIndex(
	{"idTrigger":1,
	"servicePrestationId":1,
	"statut":1},
	{ name: "triggeredBeneficiary01" })

db.triggeredBeneficiary.createIndex(
	{"idTrigger":1,
	"statut":1,
	"derniereAnomalie":1},
	{ name: "triggeredBeneficiary02" })

db.triggeredBeneficiary.createIndex(
	{"nir":1,
	"amc":1},
	{ name: "triggeredBeneficiary03" })

db.triggeredBeneficiary.createIndex(
	{"numeroContrat":1,
	"amc":1},
	{ name: "triggeredBeneficiary04" })

db.triggeredBeneficiary.createIndex(
	{"servicePrestationId":1,
		"amc":1},
	{ name: "triggeredBeneficiary05" })

db.triggeredBeneficiary.createIndex(
	{"idTrigger":1,
		"statut":1},
	{ name: "triggeredBeneficiary06" })

db.triggeredBeneficiary.createIndex(
	{"numeroContratCollectif":1},
	{ name: "triggeredBeneficiary07" })
