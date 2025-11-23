import org.springframework.cloud.contract.spec.Contract
[
	Contract.make {
		request {
			method 'GET'
			url ('/v4/offers/withLastVersions') {
				queryParameters {
					parameter 'issuerCompany': "BALOO"
					parameter 'product': "SANTE_100"
					parameter 'page': "0"
					parameter 'perPage' : "10"
				}
			}
		}
		response {
			status OK()
			body(file("pw_withLastVersions_BALOO_SANTE_100.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	// en TDB/TFD -> le code produit correspond au code garantie
	Contract.make {
		request {
			method 'GET'
			url ('/v4/offers/withLastVersions') {
				queryParameters {
					parameter 'issuerCompany': "BALOO"
					parameter 'product': "GT_BASE"
					parameter 'page': "0"
					parameter 'perPage' : "10"
				}
			}
		}
		response {
			status OK()
			body(file("pw_withLastVersions_BALOO_SANTE_100.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/v4/offers/withLastVersions') {
				queryParameters {
					parameter 'issuerCompany': "BALOO"
					parameter 'product': "GT_BASE2"
					parameter 'page': "0"
					parameter 'perPage' : "10"
				}
			}
		}
		response {
			status OK()
			body(file("pw_withLastVersions_BALOO_SANTE_100.json"))
			headers {
				contentType applicationJson()
			}
		}
	}
]
