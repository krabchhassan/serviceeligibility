db.declarations.aggregate(
    [
        {
            "$match":
                {
                    "idDeclarant": "0001860998",
                    "domaineDroits.conventionnements.typeConventionnement.code":
                        {
                            "$regularExpression":
                                {
                                    "pattern": "AL",
                                    "options": ""
                                }
                        },
                    "contrat.critereSecondaireDetaille": "C29"
                }
        },
        {
            "$addFields":
                {
                    "domaineDroits":
                        {
                            "$filter":
                                {
                                    "input":
                                        {
                                            "$map":
                                                {
                                                    "input": "$domaineDroits",
                                                    "as": "this",
                                                    "in":
                                                        {
                                                            "$mergeObjects":
                                                                [
                                                                    "$$this",
                                                                    {
                                                                        "convNames":
                                                                            {
                                                                                "$reduce":
                                                                                    {
                                                                                        "input": "$$this.conventionnements",
                                                                                        "initialValue": "",
                                                                                        "in":
                                                                                            {
                                                                                                "$concat":
                                                                                                    [
                                                                                                        "$$value",
                                                                                                        "$$this.typeConventionnement.code"
                                                                                                    ]
                                                                                            }
                                                                                    }
                                                                            }
                                                                    }
                                                                ]
                                                        }
                                                }
                                        },
                                    "as": "this",
                                    "cond":
                                        {
                                            "$regexMatch":
                                                {
                                                    "input": "$$this.convNames",
                                                    "regex": "AL"
                                                }
                                        }
                                }
                        }
                }
        },
        {
            "$sort":
                {
                    "contrat.numero": 1,
                    "beneficiaire.numeroPersonne": 1,
                    "effetDebut": 1,
                    "codeEtat": 1
                }
        }
    ]
);