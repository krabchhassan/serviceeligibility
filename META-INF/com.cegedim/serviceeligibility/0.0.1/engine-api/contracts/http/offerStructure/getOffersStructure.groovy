import org.springframework.cloud.contract.spec.Contract
import java.text.SimpleDateFormat

final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
[
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1B"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1B.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1B"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1B.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1B"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1B.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "1472_AJOUT_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "1472_AJOUT_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "1472_AJOUT_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "1472_AJOUT_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1B"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1B.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1B"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1B.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS1B"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1B.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "1472_AJOUT_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "1472_AJOUT_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "1472_AJOUT_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS1Bis.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS2"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS2"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS2"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS2.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS2_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS2_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS2_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS2_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS2_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS2_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },


        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS3"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS3.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS3"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS3.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS3"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS3.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4_REMP_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4_REMP_01.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4_REMP_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4_REMP_01.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4_REMP_01"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4_REMP_01.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4_REMP_02"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4_REMP_02.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4_REMP_02"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4_REMP_02.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS4_REMP_02"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS4_REMP_02.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS5"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS5.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS5"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS5.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS5"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS5.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS6"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS6.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS6"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS6.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS6"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS6.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS7"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS7.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS7"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS7.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS7"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS7.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS8"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS8.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS8"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS8.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS8"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS8.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS8_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS8_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS8_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS8_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS8_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS8_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },


        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS9"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS9.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS9"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS9.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS9"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS9.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS9_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS9_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS9_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS9_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS9_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS9_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },


        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS10"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS10.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS10"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS10.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS10"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS10.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS10_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS10_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS10_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS10_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "CAS10_REMP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_CAS10_REMP.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP"
                        parameter 'date': "2022-01-01"
                        parameter 'endDate': "2022-12-31"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp_20220101.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP"
                        parameter 'date': "2022-06-15"
                        parameter 'endDate': "2022-06-30"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp_20220615.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP"
                        parameter 'date': "2022-06-15"
                        parameter 'endDate': "2022-07-15"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp_20220615.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP"
                        parameter 'date': "2022-01-10"
                        parameter 'endDate': "2022-01-10"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp_20220101.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP"
                        parameter 'date': "2022-01-01"
                        parameter 'endDate': "2022-01-10"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp_20220101.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP"
                        parameter 'date': "2022-01-01"
                        parameter 'endDate': "2022-12-17"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp_20220101.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP2"
                        parameter 'date': "2022-07-01"
                        parameter 'endDate': "2022-07-15"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp2_20220701.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP2"
                        parameter 'date': "2022-06-15"
                        parameter 'endDate': "2022-07-15"
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp2_20220701.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'offerCode': "BLUE_TP"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "SANTE_100"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "SANTE_100"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_tp_off.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'offerCode': "CONTRATGROUPEAXA"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_contrat_groupe.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "CONTRAT_GROUPE_AXA"
                        parameter 'offerCode': "CONTRATGROUPEAXA"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_axa_contrat_groupe.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BLUE_001"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_001.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BLUE_001_MULTIPLE"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_001_multiple.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BLUE_001_MULTIPLE"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_001_multiple.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "BLUE_001_ONLINE"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_blue_001_online.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "ASSU00223"
                        parameter 'offerCode': "FSC71232"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_assu00223_fsc71232.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "ASSU00223"
                        parameter 'offerCode': "FSC65331"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_assu00223_fsc65331.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "KC_PlatineBase"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("offerStructure_KC_PlatineBase.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "KC_PlatineBase"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("offerStructure_KC_PlatineBase.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "KC_PlatineComp"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_OFFLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("offerStructure_KC_PlatineComp.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "KC_PlatineComp"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("offerStructure_KC_PlatineComp.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "ASSU00667"
                        parameter 'offerCode': "FSC59596"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'context': "HTP"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_double_offer.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "F7"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_f7.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },
        Contract.make {
            request {
                method 'GET'
                url('/v4/offerStructure') {
                    queryParameters {
                        parameter 'issuerCompany': "BALOO"
                        parameter 'productCode': "F7-E"
                        parameter 'date': value(regex(nonBlank()))
                        parameter 'endDate': value(regex(nonBlank()))
                        parameter 'context': "TP_ONLINE"
                    }
                }
            }
            response {
                status OK()
                body(file("pw_baloo_f7-e.json"))
                headers {
                    contentType applicationJson()
                }
            }
        },

]
