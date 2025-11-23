/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const declarant = Api.all('v1/declarants');
declarant.one = (id) => Api.one('v1/declarants/open', id);
declarant.search = () => Api.all('v1/declarants/find');
declarant.exists = (id) => Api.one('v1/declarants', id);
declarant.update = (value) => Api.custom('v1/declarants/update', value);
declarant.create = (value) => Api.custom('v1/declarants/create', value);
declarant.light = () => Api.all('v1/declarants/light');
declarant.last = () => Api.custom('v1/declarants/last');

export { declarant as default };
