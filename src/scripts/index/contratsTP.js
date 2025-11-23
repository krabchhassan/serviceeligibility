db.contratsTP.createIndex({
    "idDeclarant": 1,
    "numeroAdherent": 1,
    "numeroContrat": 1
}, { name: "contratsTP01" })

db.contratsTP.createIndex({
    "idDeclarant": 1,
    "beneficiaires.nirBeneficiaire": 1
}, { name: "contratsTP02" })

db.contratsTP.createIndex({
    "idDeclarant": 1,
    "beneficiaires.nirOd1": 1
}, { name: "contratsTP03" })

db.contratsTP.createIndex({
    "idDeclarant": 1,
    "beneficiaires.nirOd2": 1
}, { name: "contratsTP04" })

db.contratsTP.createIndex({
    "beneficiaires.numeroPersonne": 1,
    "idDeclarant": 1
}, { name: "contratsTP05" })

db.contratsTP.createIndex({
    "beneficiaires.numeroPersonne": 1,
    "numAMCEchange": 1
}, { name: "contratsTP06" })

db.contratsTP.createIndex({
    "idDeclarant": 1,
    "beneficiaires.domaineDroits.garanties.produits.referencesCouverture.naturesPrestation.periodesDroit.periodeFin": 1
}, { name: "contratsTP07" })