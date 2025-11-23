/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const AlmerysProductReferential = Api.all('v1/almerysProductReferential');
AlmerysProductReferential.getAlmerysProduct = AlmerysProductReferential.custom('getAlmerysProduct');
AlmerysProductReferential.create = (data) => AlmerysProductReferential.custom('create', data);
AlmerysProductReferential.update = (data) => AlmerysProductReferential.custom('update', data);

export { AlmerysProductReferential as default };
