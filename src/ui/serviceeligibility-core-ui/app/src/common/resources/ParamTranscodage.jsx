/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const paramTranscodage = Api.all('v1/paramTranscodage');
paramTranscodage.findOneByCode = (code) => paramTranscodage.custom(code);
paramTranscodage.deleteByCode = (code) => paramTranscodage.custom(code);
paramTranscodage.editByCode = (code) => paramTranscodage.custom(code);

export { paramTranscodage as default };
