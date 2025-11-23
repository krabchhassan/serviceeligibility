/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const Consolidation = Api.all('v1/consolidation');
Consolidation.contrat = (idDeclarant, numContrat, numAdherent) =>
    Consolidation.custom(`${idDeclarant}/${numContrat}/${numAdherent}`);

export { Consolidation as default };
