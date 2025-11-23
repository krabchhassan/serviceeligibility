/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const service = Api.all('v6/servicePrestations');
service.search = () => service.custom('search');
export { service as default };
