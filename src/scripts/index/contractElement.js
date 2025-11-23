db.getCollection("contractElement").dropIndex(
  "codeContractElement_codeInsurer_codeAmc"
)

db.getCollection("contractElement").createIndex(
  {
    codeContractElement: 1,
    codeInsurer: 1,
    codeAMC: 1,
    status: 1,
  },
  { unique: true, name: "contractElementUnicity" }
)
