import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE1"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas1.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE1B"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas1B.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "7319-01-P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_7319_01.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "1472_AJOUT_01_P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "1472_AJOUT_01_P02"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas1BisProduit2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "1472_AJOUT_01_P03"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE2"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_REMP2"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_REMP2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE3"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas3.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE4"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas4.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_REMP4_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_REMP4_01.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_REMP4_02"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_REMP4_02.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE5"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas5.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE6"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas6.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE7"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas7.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE8"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas8.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_REMP8"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_REMP8.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE9"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas9.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_REMP9"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_REMP9.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_BASE10"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_Cas10.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "6411Janv"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_6411_start_janv.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "6411Jul"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_6411_start_jul.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_REMP10"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_REMP10.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "KLESIA_CARCEPT"
                        parameter 'productCode': "PlatineComp"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_klesia_platineComp.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "KLESIA_CARCEPT"
                        parameter 'productCode': "PlatineBase"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_klesia_platineBase.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIM"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "INEXISTANT"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status 400
                body(file("pw_axa_Cas2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "18875"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status 400
                body(file("pw_axa_Cas3.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "CAM_BL_002"
                        parameter 'date': '2026-01-01'
                        parameter 'endDate': '2026-12-31'
                    }
                }
            }
            response {
                status OK()
                body(file("pw_Baloo_Cam_BL_002_2026.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "CAM_BL_002"
                        parameter 'date': '2025-01-01'
                        parameter 'endDate': '2025-12-31'
                    }
                }
            }
            response {
                status OK()
                body(file("pw_Baloo_Cam_BL_002_2025.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BLUE_001"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_blue_Cas1.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BLUE_001_ONLINE"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_blue_Cas1_online.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BLUE_001_MULTIPLE"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_blue_Cas1_multiple.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "CAM_BL_001"
                        parameter 'date': '2025-01-01'
                        parameter 'endDate': '2025-12-31'
                    }
                }
            }
            response {
                status OK()
                body(file("pw_Baloo_Cam_BL_001_2025-2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "SANTE_100"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_sante_100.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "GT_BASE"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_gt_base.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "GT_BASE2"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_gt_base2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV"
                        parameter 'date': "2021-01-01"
                        parameter 'endDate': "2021-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2021.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDI_OK"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV"
                        parameter 'date': "2022-01-01"
                        parameter 'endDate': "2022-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2021.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV"
                        parameter 'date': "2023-01-01"
                        parameter 'endDate': "2023-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2023.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV"
                        parameter 'date': "2023-01-01"
                        parameter 'endDate': "2023-03-01"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2023.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        // test_prioritaire.feature I renew a contract in 2021, 2022 and 2023 with 1 benef and resiliation date in 2023
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV"
                        parameter 'date': "2024-01-01"
                        parameter 'endDate': "2024-03-01"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2024.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV-6485"
                        parameter 'date': "2024-01-01"
                        parameter 'endDate': "2024-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_6485_2024.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV-6485-V2"
                        parameter 'date': "2024-01-01"
                        parameter 'endDate': "2024-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_6485_2024_v2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV-5726"
                        parameter 'date': "2025-01-01"
                        parameter 'endDate': "2025-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_5726-2024.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV-5726-LABO"
                        parameter 'date': "2025-01-01"
                        parameter 'endDate': "2025-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_5726-2024_labo.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV"
                        parameter 'date': "2024-07-25"
                        parameter 'endDate': "2025-07-24"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2024-2025.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV"
                        parameter 'date': "2024-07-25"
                        parameter 'endDate': "2025-04-28"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2024-2025.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIV-5515"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_Cas1-2023-split.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIM_5458"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_5458.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'productCode': "AXASC_CGDIM_5458_RS"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_5458_withReseauSoin.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BAL_5458_001_P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_5660.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BAL_5458_001_P011"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_5660_noReseauSoin.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BAL_5458_001_P012"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_5660_multiple.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BAL_5497_001_P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_5497.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BAL_5497_002_P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_5497_cas2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "1472_DELTA_01_P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_6476_P01.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "1472_DELTA_01_P02"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_6476_P02.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "ProduitMulti01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_klesia_produitMulti01.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "ProduitMulti02"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_klesia_produitMulti02.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "ProduitMulti03"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_klesia_produitMulti03.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_4883"
                        parameter 'date': "2023-01-01"
                        parameter 'endDate': "2023-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_4883_2023.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_4883"
                        parameter 'date': "2024-01-01"
                        parameter 'endDate': "2024-12-31"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_4883_2024.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PDT_JUIN"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_PDT_JUIN.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BAL_5727_P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_5727.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "NAT_P01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_benefitsNatures.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/tpRights') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "PROD_6602_001"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_6602.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
]
