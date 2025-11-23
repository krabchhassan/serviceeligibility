/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const transcodage = Api.all('v1/transcodage/services');
transcodage.mapping = (serviceCode, transcoCode) => transcodage.custom(`${serviceCode}/${transcoCode}`);

export { transcodage as default };
