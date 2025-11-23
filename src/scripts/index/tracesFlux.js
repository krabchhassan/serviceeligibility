db.tracesFlux.createIndex({
	"idDeclarant": 1,
	"dateExecution": 1
}, { name: "tracesFlux01" })

db.tracesFlux.createIndex({
	"dateExecution": 1,
	"infoAMC.nomAMC": 1,
	"infoAMC.numAMCEchange": 1
}, { name: "tracesFlux02" })

db.tracesFlux.createIndex({
	"infoFichierRecu.nomFichier": 1
}, { name: "tracesFlux03" })