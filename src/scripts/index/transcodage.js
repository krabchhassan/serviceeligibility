db.transcodage.createIndex({
	"codeObjetTransco": 1,
	"codeService": 1
}, { name: "transcodage01" })

db.transcodage.createIndex({
	"domaineDroits.conventionnements.typeConventionnement.code": 1
}, { name: "transcodage02" })