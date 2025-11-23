db.retention.createIndex({
    "insurerId": 1,
    "subscriberNumber": 1,
    "contractNumber": 1,
    "personNumber": 1,
    "status": 1
}, { name: "retention01" })

db.retention.createIndex({
    "insurerId": 1,
    "receptionDate": 1,
    "status": 1
}, { name: "retention02" })