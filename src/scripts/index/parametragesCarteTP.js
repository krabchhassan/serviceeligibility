db.parametragesCarteTP.createIndex(
	{"amc":1,
	"statut":1},
	{ name: "parametragesCarteTP02" })

db.parametragesCarteTP.createIndex(
	{"statut":1,
	"parametrageRenouvellement.dateRenouvellementCarteTP":1},
	{ name: "parametragesCarteTP03" })