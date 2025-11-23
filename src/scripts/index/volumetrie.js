db.volumetrie.createIndex({
	"dateEffet": 1,
	"idDeclarant": 1
}, { name: "volumetrie01" })

db.volumetrie.createIndex({
	"idDeclarant": 1,
	"dateEffet": 1
}, { name: "volumetrie02" })