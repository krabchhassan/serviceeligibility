db.historiqueExecutions.updateMany({
    "batch": { $exists: true }
}, { $rename: { "batch": "Batch" } })