db.declarationsConsolideesAlmerys.createIndex({
	"idDeclarant": 1,
	"dateConsolidation": 1
}, { name: "declarationsConsolideesAlmerys01" })

db.declarationsConsolideesAlmerys.createIndex({
	"dateConsolidation": 1,
	"idDeclarant": 1,
	"contrat.critereSecondaireDetaille": 1
}, { name: "declarationsConsolideesAlmerys02" })