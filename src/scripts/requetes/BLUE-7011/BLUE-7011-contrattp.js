db.getCollection("contratsTP").aggregate(
    [
        {
            "$match" : {
                "$or" : [
                    {
                        "beneficiaires.0.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit" : {
                            "$exists" : true
                        },
                        "beneficiaires.0.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0" : {
                            "$exists" : true
                        },
                        "beneficiaires.0.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1" : {
                            "$exists" : true
                        },
                        "beneficiaires.0.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2" : {
                            "$exists" : true
                        },
                        "$or" : [
                            {
                                "beneficiaires.0.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.0.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.0.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2.periodeFin" : "2023/12/31"
                            }
                        ]
                    },
                    {
                        "beneficiaires.1" : {
                            "$exists" : true
                        },
                        "beneficiaires.1.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit" : {
                            "$exists" : true
                        },
                        "beneficiaires.1.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0" : {
                            "$exists" : true
                        },
                        "beneficiaires.1.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1" : {
                            "$exists" : true
                        },
                        "beneficiaires.1.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2" : {
                            "$exists" : true
                        },
                        "$or" : [
                            {
                                "beneficiaires.1.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.1.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.1.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2.periodeFin" : "2023/12/31"
                            }
                        ]
                    },
                    {
                        "beneficiaires.2" : {
                            "$exists" : true
                        },
                        "beneficiaires.2.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit" : {
                            "$exists" : true
                        },
                        "beneficiaires.2.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0" : {
                            "$exists" : true
                        },
                        "beneficiaires.2.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1" : {
                            "$exists" : true
                        },
                        "beneficiaires.2.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2" : {
                            "$exists" : true
                        },
                        "$or" : [
                            {
                                "beneficiaires.2.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.2.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.2.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2.periodeFin" : "2023/12/31"
                            }
                        ]
                    },
                    {
                        "beneficiaires.3" : {
                            "$exists" : true
                        },
                        "beneficiaires.3.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit" : {
                            "$exists" : true
                        },
                        "beneficiaires.3.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0" : {
                            "$exists" : true
                        },
                        "beneficiaires.3.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1" : {
                            "$exists" : true
                        },
                        "beneficiaires.3.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2" : {
                            "$exists" : true
                        },
                        "$or" : [
                            {
                                "beneficiaires.3.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.3.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.3.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2.periodeFin" : "2023/12/31"
                            }
                        ]
                    },
                    {
                        "beneficiaires.4" : {
                            "$exists" : true
                        },
                        "beneficiaires.4.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit" : {
                            "$exists" : true
                        },
                        "beneficiaires.4.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0" : {
                            "$exists" : true
                        },
                        "beneficiaires.4.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1" : {
                            "$exists" : true
                        },
                        "beneficiaires.4.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2" : {
                            "$exists" : true
                        },
                        "$or" : [
                            {
                                "beneficiaires.4.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.0.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.4.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.1.periodeFin" : "2023/12/31"
                            },
                            {
                                "beneficiaires.4.domaineDroits.0.garanties.0.produits.0.referencesCouverture.0.naturesPrestation.0.periodesDroit.2.periodeFin" : "2023/12/31"
                            }
                        ]
                    }
                ]
            }
        },
        {
            "$project" : {
                "_id" : NumberInt(0),
                "numeroContrat" : NumberInt(1),
                "numeroAdherent" : NumberInt(1),
                "idDeclarant" : NumberInt(1)
            }
        }
    ],
    {
        "allowDiskUse" : false
    }
);
