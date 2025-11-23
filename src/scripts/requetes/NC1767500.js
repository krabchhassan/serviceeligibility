db.declarations.updateMany({'idDeclarant' : '0777927120'},{$set : {'domaineDroits.$[].isSuspension' : false, 'userModification':'NC1767500'}});
db.declarationsConsolideesAlmerys.updateMany({'idDeclarant' : '0777927120'},{$set : {'domaineDroit.isSuspension' : false, 'userModification':'NC1767500'}});
