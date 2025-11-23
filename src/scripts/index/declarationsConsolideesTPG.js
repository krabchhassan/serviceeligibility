db.declarationsConsolideesTPG.createIndex( {
	"idDeclarant": 1,
	"dateConsolidation": 1
}, { name: "declarationsConsolideesTPG01" })

db.declarationsConsolideesTPG.createIndex( {
	"effetDebut": 1,
	"codeEtat": 1
}, { name: "declarationsConsolideesTPG03" })

db.declarationsConsolideesTPG.createIndex( {
	"beneficiaire.numeroPersonne": 1
}, { name: "declarationsConsolideesTPG05" })

db.declarationsConsolideesTPG.createIndex( {
    "idDeclarant" : 1,
    "codeEtat" : 1,
    "codeService" : 1,
    "contrat.numAMCEchange" : 1,
    "contrat.numeroAdherent" : 1,
    "beneficiaire.numeroPersonne" : 1,
    "contrat.numero" : 1,
    "effetDebut" : -1
}, { name: "declarationsConsolideesTPG07" })