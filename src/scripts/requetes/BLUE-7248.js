db.trigger
    .find({ "status": "ToProcess", "dateCreation": { $lte: ISODate("2025-07-25T00:00:00.000+0000") } })
    .forEach(trigger => {
        console.log(trigger._id);
        db.sasContrat.updateMany({
            "triggersBenefs.triggerId": trigger._id.toString()
        }, {
            $pull: {
                "triggersBenefs": { "triggerId": trigger._id.toString() }
            }
        });

        db.trigger.updateOne({ "_id": trigger._id }, { $set: { "status": "Abandonned" } });
    });

db.sasContrat.deleteMany({ "triggersBenefs.0": { $exists: false } })