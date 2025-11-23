/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const Lot = Api.all('v1/lot');
Lot.getLot = Lot.custom('getLot');
Lot.getLotsById = Lot.custom('getLotsById');
Lot.getLots = Lot.custom('getLots');
Lot.create = (data) => Lot.custom('create', data);
Lot.update = (data) => Lot.custom('update', data);
Lot.import = () => Api.custom('v1/lot/import');
Lot.export = () => Api.custom('v1/lot/export');
Lot.exportOne = (code) => Api.custom(`v1/lot/exportOne/${code}`);

export { Lot as default };
