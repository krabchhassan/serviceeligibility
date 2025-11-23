/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const contract = Api.all('v1/contrats');
contract.one = (id) => Api.one('v1/contrats/find', id);
export { contract as default };
