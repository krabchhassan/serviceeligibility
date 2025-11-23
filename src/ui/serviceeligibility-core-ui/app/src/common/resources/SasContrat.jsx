/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const SasContrat = Api.all('v1/sasContrats');
SasContrat.getSasContrats = (personNumber) => SasContrat.custom(`getSasContrats/${personNumber}`);

export { SasContrat as default };
