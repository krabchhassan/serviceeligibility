db.getCollection("declarants").updateMany(
    { "delaiRetention" : null },
    {$set: {"delaiRetention" : 3}}
);