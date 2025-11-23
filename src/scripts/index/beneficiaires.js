db.beneficiaires.createIndex({
	"amc.idDeclarant": 1,
    "identite.numeroPersonne": 1
}, { unique: true, name: "cleFontionnelleUnique" })

db.beneficiaires.createIndex( {
	"key": 1
}, { name: "beneficiaires02" })

db.beneficiaires.createIndex({
	"contrats.numeroAdherent": 1
}, { name: "beneficiaires10" })

db.beneficiaires.createIndex({
	"identite.nir.code": 1
}, { name: "beneficiaires11" })

db.beneficiaires.createIndex({
	"identite.affiliationsRO.nir.code": 1
}, { name: "beneficiaires12" })

db.beneficiaires.createIndex({
	"amc.idDeclarant": 1,
	"numeroAdherent": 1,
	"contrats.numeroContrat": 1,
	"identite.numeroPersonne": 1
}, { name: "beneficiaires13" })
