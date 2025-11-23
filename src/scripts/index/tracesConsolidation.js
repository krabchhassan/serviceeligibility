/*db.tracesConsolidation.createIndex({
	"idDeclarant": 1
}, { name: "tracesConsolidation01" })*/

db.tracesConsolidation.createIndex({
	"idDeclaration": 1
}, { name: "tracesConsolidation02" })

db.tracesConsolidation.createIndex({
	"idDeclarant": 1,
	"dateExecution": 1
}, { name: "IdxIdDeclarantDateExec" })

db.tracesConsolidation.createIndex({
	"idDeclaration": 1,
	"codeService": 1
}, { name: "tracesConsolidation03" })

