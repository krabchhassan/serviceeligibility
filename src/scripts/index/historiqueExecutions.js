db.historiqueExecutions.createIndex({
	"Batch": 1,
	"idDeclarant": 1,
	"codeService": 1,
	"typeConventionnement": 1
}, { name: "historiqueExecutions01" })

db.historiqueExecutions.createIndex({
	"critereSecondaireDetaille": 1,
	"Batch": 1,
	"idDeclarant": 1,
	"dateExecution": 1
}, { name: "historiqueExecutions02" })

db.historiqueExecutions.createIndex({
	"dateExecution": 1,
	"Batch": 1,
	"idDeclarant": 1,
	"critereSecondaireDetaille": 1
}, { name: "historiqueExecutions03" })

db.historiqueExecutions.createIndex({
	"Batch": 1,
	"codeClient": 1
}, { name: "historiqueExecutions04" })