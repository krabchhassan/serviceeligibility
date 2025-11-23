db.servicePrestation.update({},
	[{
			"$unset": "periodesDroitsComptablesOuverts"
		}
	], {
	"multi": true
})

db.servicePrestation.update({}, {
	"$set": {
		"assures.$[elem].data.destinatairesRelevePrestations.$[elem2].dematerialisation": {
			"isDematerialise": false
		}
	}
}, {
	arrayFilters: [{
			"elem.data.destinatairesRelevePrestations": {
				$exists: true
			}
		}, {
			"elem2.dematerialisation": {
				"$exists": false
			}
		}
	],
	"multi": true
})
