db.tracesRetours.createIndex({
	"batchOrigine": 1,
	"idDeclarant": 1
}, { name: "tracesRetours01" })

db.tracesRetours.createIndex({
	"batch": 1,
	"idDeclarant": 1
}, { name: "tracesRetours02" })