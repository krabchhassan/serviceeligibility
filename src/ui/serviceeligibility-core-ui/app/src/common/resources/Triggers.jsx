/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const flux = Api.all('v1');
flux.search = () => Api.custom('v1/declencheursCarteTP');
flux.getKOTriggeredBeneficiaries = (idTrigger) =>
    Api.custom(
        `v1/declencheursCarteTP/beneficiaires?idTrigger=${idTrigger}&status=Error&page=1&perPage=20&sortDirection=DESC`,
    );
flux.excel = (idTrigger) => Api.custom(`v1/declencheursCarteTP/${idTrigger}/export`);
flux.callRecycle = (idTrigger) => Api.custom(`v1/declencheursCarteTP/${idTrigger}/statut/ToProcess`);
flux.callCancelProcess = (idTrigger) => Api.custom(`v1/declencheursCarteTP/${idTrigger}/statut/Deleted`);
flux.callTriggerGeneration = (idTrigger) => Api.custom(`v1/declencheursCarteTP/${idTrigger}/statut/ToProcess`);
flux.callAbandon = (idTrigger) => Api.custom(`v1/declencheursCarteTP/${idTrigger}/statut/Abandoning`);
flux.generateParametrageContrat = Api.custom('v1/declencheursCarteTP');
export { flux as default };
