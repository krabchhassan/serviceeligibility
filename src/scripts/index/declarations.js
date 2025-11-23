db.declarations.createIndex({
	"effetDebut": 1,
	"idDeclarant": 1,
	"codeEtat": 1
}, { name: "declarations01" })

db.declarations.createIndex({
	"domaineDroits.periodeDroit.modeObtention": 1,
	"codeEtat": 1
}, { name: "declarations02" })

db.declarations.createIndex({
	"contrat.numero": 1,
	"idDeclarant": 1
}, { name: "declarations03" })

db.declarations.createIndex({
	"contrat.numeroAdherent": 1,
	"idDeclarant": 1
}, { name: "declarations04" })

db.declarations.createIndex({
	"idDeclarant": 1,
	"beneficiaire.numeroPersonne": 1,
}, { name: "declarations05" })

db.declarations.createIndex({
	"effetDebut": 1,
	"idDeclarant": 1,
	"contrat.critereSecondaireDetaille": 1,
}, { name: "declarations06" })

db.declarations.createIndex({
	"prioriteDroit.prioDroitNir1": 1,
	"domaineDroits.codeGarantie": 1
}, { name: "declarations07" })

db.declarations.createIndex({
	"beneficiaire.numeroPersonne": 1,
	"contrat.numero": 1
}, { name: "declarations08" })

db.declarations.createIndex({
	"beneficiaire.nirOd1": 1,
}, { name: "declarations09" })

db.declarations.createIndex({
	"domaineDroits.code": 1,
	"effetDebut": 1,
	"idDeclarant": 1
}, { name: "declarations10" })

db.declarations.createIndex({
	"effetDebut": 1,
	"contrat.numAMCEchange": 1
}, { name: "declarations11" })

db.declarations.createIndex({
	"beneficiaire.dateNaissance": 1,
	"idDeclarant": 1
}, { name: "declarations12" })

db.declarations.createIndex({
	"beneficiaire.nirBeneficiaire": 1
}, { name: "IdxNirBeneficiaire" })

db.declarations.createIndex({
	"beneficiaire.nirOd2": 1
}, { name: "IdxNirOd2" })

db.declarations.createIndex({
	"idDeclarant": 1,
	effetDebut: -1
}, { name: "IdxIdDeclarantEffetDebut" })

db.declarations.createIndex({
    "idDeclarant" : 1.0,
    "contrat.numeroAdherent" : 1.0
}, { name: "IdxidDeclNumAdh" })

db.declarations.createIndex({
    "dateCreation" : -1.0
}, { name: "IdxDateCreationDesc" })

db.declarations.createIndex({
	"idDeclarant" : -1,
	"contrat.qualification" : -1,
	"versionDeclaration" : -1,
	"effetDebut" : -1,
	"contrat.numero" : -1,
	"beneficiaire.numeroPersonne" : -1,
	"_id" : -1
}, { name: "IdxBatch620" })

db.getCollection("declarations").createIndex({
     "idDeclarant": 1,
     "contrat.numero": 1,
     "contrat.numeroAdherent": 1,
     "effetDebut": 1,
     "_id": 1
}, { name: "declarationsReprise" })
