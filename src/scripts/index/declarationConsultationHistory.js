db.declarationConsultationHistory.createIndex({
	"user": 1,
	"dateConsultation": -1
}, { name: "declarationConsultationHistory01" }) 