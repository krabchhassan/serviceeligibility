/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const service = Api.all('v1/services');
service.byName = (name) => service.custom(name);

export { service as default };
