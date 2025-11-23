db.trigger.createIndex(
	{"origine":1,
	"statut":1,
	"dateDebutTraitement":1}, 
	{ name: "trigger01" })

db.trigger.createIndex(
	{"userCreation":1}, 
	{ name: "trigger02" })

db.trigger.createIndex(
	{"amc":1}, 
	{ name: "trigger03" })

db.trigger.createIndex(
	{"dateDebutTraitement":1}, 
	{ name: "dateDebutTraitement_" })

db.trigger.createIndex(
	{"dateFinTraitement":1}, 
	{ name: "dateFinTraitement_" })
