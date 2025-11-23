/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const volumetry = Api.all('v1/volumetrie');
volumetry.excel = (pathParam) => Api.custom('v1/volumetrie/excel', pathParam);

export { volumetry as default };
