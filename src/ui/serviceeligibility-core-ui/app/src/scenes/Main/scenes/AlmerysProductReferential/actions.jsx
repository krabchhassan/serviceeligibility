/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import AlmerysProductReferential from '../../../../common/resources/AlmerysProductReferential';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

const searchAlmerysProductReferential = () => (dispatch) =>
    dispatch({
        ...types.SEARCH_ALMERYS_REFERENTIAL,
        payload: AlmerysProductReferential.getAll(),
    });
function createAlmerysProduct(data) {
    return {
        ...types.CREATE_ALMERYS_PRODUCT,
        payload: AlmerysProductReferential.create().post(data),
    };
}

function updateAlmerysProduct(data) {
    return {
        ...types.UPDATE_ALMERYS_PRODUCT,
        payload: AlmerysProductReferential.update().post(data),
    };
}
function searchAlmerysProduct(params) {
    return {
        ...types.SEARCH_ALMERYS_PRODUCT,
        payload: AlmerysProductReferential.getAlmerysProduct.get(params),
    };
}

const actions = {
    searchAlmerysProductReferential,
    createAlmerysProduct,
    updateAlmerysProduct,
    searchAlmerysProduct,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
