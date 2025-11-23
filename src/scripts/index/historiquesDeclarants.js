db.historiqueDeclarants.createIndex({
	"numeroPrefectoral": 1
}, { name: "historiqueDeclarants01" })

db.historiqueDeclarants.createIndex({
	"numerosAMCEchanges": 1
}, { name: "historiqueDeclarants02" })

db.historiqueDeclarants.createIndex({
	"codePartenaire": 1
}, { name: "historiqueDeclarants03" })

db.historiqueDeclarants.createIndex({
	"userCreation": 1
}, { name: "historiqueDeclarants04" })

db.historiqueDeclarants.createIndex({
	"userModification": 1
}, { name: "historiqueDeclarants05" })

db.historiqueDeclarants.createIndex({
	"nom": 1
}, { name: "historiqueDeclarants06" })