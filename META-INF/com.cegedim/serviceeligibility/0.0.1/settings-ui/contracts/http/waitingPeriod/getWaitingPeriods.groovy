import org.springframework.cloud.contract.spec.Contract
import java.text.SimpleDateFormat
final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
[
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS1"
					parameter 'productCode.equals': "PDT_BASE1"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas1.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS2"
					parameter 'productCode.equals': "PDT_BASE2"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas2.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS3"
					parameter 'productCode.equals': "PDT_BASE3"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas3.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS4"
					parameter 'productCode.equals': "PDT_BASE4"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas4.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS5"
					parameter 'productCode.equals': "PDT_BASE5"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas5.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS6"
					parameter 'productCode.equals': "PDT_BASE6"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas6.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS7"
					parameter 'productCode.equals': "PDT_BASE7"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas7.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS8"
					parameter 'productCode.equals': "PDT_BASE8"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas8.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS9"
					parameter 'productCode.equals': "PDT_BASE9"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas9.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS10"
					parameter 'productCode.equals': "PDT_BASE10"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas10.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS11"
					parameter 'productCode.equals': "PDT_BASE11"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas11.json"))
			headers {
				contentType applicationJson()
			}
		}
	},
	Contract.make {
		request {
			method 'GET'
			url ('/api/carences') {
				queryParameters {
					parameter 'issuerCompany.equals': "BALOO"
					parameter 'offerCode.equals': "CAS12"
					parameter 'productCode.equals': "PDT_BASE12"
				}
			}
		}
		response {
			status OK()
			body(file("wp_baloo_Cas12.json"))
			headers {
				contentType applicationJson()
			}
		}
	}
]
