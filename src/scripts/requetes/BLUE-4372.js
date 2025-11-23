db.servicePrestation.find({
	"periodesContratCMUOuvert": {
		$exists: true,
		$ne: []
	},
	"periodesContratCMUOuvert.0.code": {
		$exists: false
	}
}).forEach(function (doc) {
	var newArray = [];
	doc.periodesContratCMUOuvert.forEach(function (doc2) {
		var aNewItem = {
			"code": "CMU",
			"periode": {
				"debut": doc2.debut,
				"fin": doc2.fin,
			}
		};
		newArray.push(aNewItem);
	})
	db.servicePrestation.update({
		_id: doc._id
	}, {
		"$set": {
			"periodesContratCMUOuvert": newArray
		}
	});
});
