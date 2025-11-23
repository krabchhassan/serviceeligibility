db.getCollection("servicePrestation").aggregate(
    [
        {
            "$match" : {
                "idDeclarant" : "0098534803"
            }
        },
        {
            "$unwind" : "$assures"
        },
        {
            "$project" : {
                "numPersonne" : "$assures.identite.numeroPersonne",
                "idBenef" : {
                    "$concat" : [
                        "$idDeclarant",
                        "-",
                        "$assures.identite.numeroPersonne"
                    ]
                },
                "amc" : "$idDeclarant",
                "numeroAdherent" : NumberInt(1),
                "numeroContrat" : "$numero",
                "insurerStartDate" : "$assures.dateAdhesionMutuelle",
                "droits" : {
                    "$map" : {
                        "input" : "$assures.droits",
                        "as" : "droits",
                        "in" : {
                            "groupingCode" : "$$droits.code",
                            "insurerGroupingCode" : "$$droits.codeAssureur",
                            "productStartDate" : "$$droits.dateAncienneteGarantie",
                            "period":"$$droits.periode"
                        }
                    }
                }
            }
        },
        {
            "$group" : {
                "_id" : "$numPersonne",
                "idBenef" : {
                    "$first" : "$idBenef"
                },
                "contrats" : {
                    "$push" : {
                        "amc" : "$amc",
                        "numeroAdherent" : "$numeroAdherent",
                        "numeroContrat" : "$numeroContrat",
                        "insurerStartDate" : "$insurerStartDate",
                        "droits" : "$droits"
                    }
                }
            }
        }
    ],
    {
        "allowDiskUse" : true
    }
);