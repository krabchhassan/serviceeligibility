db.beneficiaryTrace.createIndex( {
	"benefId": 1
}, { name: "beneficiaryTrace01" })

db.beneficiaryTrace.createIndex( {
	"idDeclarant": 1
}, { name: "beneficiaryTrace02" })