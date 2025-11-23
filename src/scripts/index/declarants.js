db.declarants.createIndex({
	"numeroPrefectoral": 1
}, { name: "declarants01" })

db.declarants.createIndex({
	"numerosAMCEchanges": 1
}, { name: "declarants02" })

db.declarants.createIndex({
	"codePartenaire": 1
}, { name: "declarants03" })

db.declarants.createIndex({
	"userCreation": 1
}, { name: "declarants04" })

db.declarants.createIndex({
	"userModification": 1
}, { name: "declarants05" })

db.declarants.createIndex({
	"nom": 1
}, { name: "declarants06" })