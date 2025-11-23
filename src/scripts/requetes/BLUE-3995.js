db.beneficiaires.aggregate([{$group:{_id:{"idDeclarant":"$amc.idDeclarant","numeroPersonne":"$identite.numeroPersonne"}, dups:{$push:"$_id"}, count: {$sum: 1}}},
{$match:{count: {$gt: 1}}}
], {allowDiskUse: true}).forEach(function(doc){
  doc.dups.pop();
  db.beneficiaires.remove({_id : {$in: doc.dups}});
});

db.beneficiaires.update(
    {},
    [
        {"$set": {"key": { "$concat": ["$amc.idDeclarant", "-", "$identite.numeroPersonne"]}}}
    ],
    { "multi": true }
)