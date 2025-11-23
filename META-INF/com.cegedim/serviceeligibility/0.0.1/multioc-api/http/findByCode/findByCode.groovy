import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            request {
                method 'GET'
                url('/v1/mainOrganizations/findByCode') {
                    queryParameters {
                        parameter 'code': "BALOO"
                    }
                }
            }
            response {
                status OK()
                body(file("baloo.json"))
                headers {
                    contentType applicationJson()
                }
            }
        }
]
