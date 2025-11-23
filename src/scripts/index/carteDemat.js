db.cartesDemat.createIndex( {
	"AMC_contrat": 1,
	"idDeclarant": 1,
	"isLastCarteDemat": 1
}, { name: "carteDemat01" })

db.cartesDemat.createIndex( {
	"dateConsolidation": 1,
	"idDeclarant": 1
}, { name: "carteDemat02" })

db.cartesDemat.createIndex( {
	"AMC_contrat": 1,
	"periodeFin": 1,
	"isLastCarteDemat": 1
}, { name: "carteDemat03" })

db.cartesDemat.createIndex( {
	"contrat.numeroAdherent": 1,
	"periodeFin": 1,
	"isLastCarteDemat": 1,
	"idDeclarant": 1
}, { name: "carteDemat04" })

db.cartesDemat.createIndex( {
	"idDeclarant": 1
}, { name: "carteDemat05" })

db.cartesDemat.createIndex( {
	"idDeclarant": 1,
	"periodeDebut": 1,
	"periodeFin": 1,
	"isLastCarteDemat": 1
}, { name: "carteDematComptageCarteActives" })

db.cartesDemat.createIndex( {
	"AMC_contrat": 1,
	"idDeclarant": 1,
	"idDeclarations": 1
}, { name: "carteDematPourAffichageDeclaration" })

db.cartesDemat.createIndex( {
	"AMC_contrat": 1,
	"idDeclarant": 1,
	"beneficiaires.beneficiaire.numeroPersonne": 1
}, { name: "carteDemat06" })
