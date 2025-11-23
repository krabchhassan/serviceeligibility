// Étape 1 : Supprimer les doublons
db.contractElement.aggregate([
	{
		// Champs temporaires où on enlève les espaces en trop
		$set: {
			trimmedCodeContractElement: { $trim: { input: "$codeContractElement" } },
			trimmedCodeInsurer: { $trim: { input: "$codeInsurer" } }
		}
	},
	{
		// Garder que les documents où il y avait des espaces en trop
		$match: {
			$or: [
				{ $expr: { $ne: ["$codeContractElement", "$trimmedCodeContractElement"] } },
				{ $expr: { $ne: ["$codeInsurer", "$trimmedCodeInsurer"] } }
			]
		}
	},
	{
		$project: {
			_id: 1
		}
	}
]).forEach(doc =>
	// Supprimer les documents identifiés comme doublons
	db.contractElement.deleteOne({ _id: doc._id })
);


// Étape 2 : Nettoyer les espaces des champs
db.contractElement.updateMany({}, [
	{
		"$set": {
			"codeInsurer": { "$trim": { "input": "$codeInsurer" } },
			"codeContractElement": { "$trim": { "input": "$codeContractElement" } },
			"productElements": {
				"$map": {
					"input": "$productElements",
					"as": "elem",
					"in": {
						"$mergeObjects": [
							"$$elem",
							{
								"codeOffer": { "$trim": { "input": "$$elem.codeOffer" } },
								"codeProduct": { "$trim": { "input": "$$elem.codeProduct" } },
								"codeBenefitNature": { "$trim": { "input": "$$elem.codeBenefitNature" } },
								"codeAmc": { "$trim": { "input": "$$elem.codeAmc" } }
							}
						]
					}
				}
			}
		}
	}
])
