import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            request {
                method 'GET'
                url('/v1/referentialDataWithoutPaging') {
                    queryParameters {
                        parameter 'code': "controles_service_prestation"
                        parameter 'referenceDate': value(regex(nonBlank()))
                    }
                }
            }
            response {
                status OK()
                body(file("referential.json"))
                headers {
                    contentType applicationJson()
                }
            }
        }
]
