db.getCollection("cartesDemat").aggregate(
    [
        {
            $project: {
                _id : "$AMC_contrat",
                periode : {
                    periodeDebut : "$periodeDebut",
                    periodeFin : "$periodeFin",
                    isLastCarteDemat : "$isLastCarteDemat",
                    beneficiaires : "$beneficiaires.beneficiaire"
                },

            }
        },
        {
            $group: {
                _id: "$_id",
                periode: { $addToSet: "$periode" }
            }
        },
        {
            $match: {
                "periode.1" : {
                    $exists : true
                },
                $and : [
                    {
                        periode : {
                            $elemMatch : {
                                periodeDebut : {
                                    $gte : "2023/01/01"
                                },
                                periodeFin : {
                                    $lte : "2023/12/31"
                                },
                                isLastCarteDemat : true
                            }
                        }
                    },
                    {
                        periode : {
                            $not : {
                                $elemMatch : {
                                    periodeDebut : {
                                        $gte : "2024/01/01"
                                    },
                                    periodeFin : {
                                        $lte : "2024/12/31"
                                    },
                                    isLastCarteDemat : true
                                }
                            }
                        }
                    },
                    {
                        periode : {
                            $elemMatch : {
                                periodeDebut : {
                                    $gte : "2024/01/01"
                                },
                                periodeFin : {
                                    $lte : "2024/12/31"
                                },
                                isLastCarteDemat : false
                            }
                        }
                    }
                ]
            }
        },
        {
            $unwind: {
                path: "$periode",
            }
        },
        {
            $unwind: {
                path: "$periode.beneficiaires",
            }
        },
        {
            $group: {
                _id: "$periode.beneficiaires.numeroPersonne",
                detailBeneficiaire: {
                    $first: "$periode.beneficiaires"
                }
            }
        },
        {
            $replaceRoot: {
                newRoot: "$detailBeneficiaire"
            }
        }
    ],
);