/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    almerysProductCombinationList: [],
    totalElements: 0,
    totalPages: 0,
    loading: false,
    loadingUpdate: false,
    product: {},
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

export default (state = initialState, action) => {
    switch (action.type) {
        case types.SEARCH_ALMERYS_REFERENTIAL_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                almerysProductCombinationList: body || [],
                loading: false,
            };
        }
        case types.SEARCH_ALMERYS_REFERENTIAL_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.SEARCH_ALMERYS_PRODUCT_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            const { paging } = action.payload.body(false);
            return {
                ...state,
                almerysProductCombinationList: body.almerysProductList || [],
                pagination: paging,
                loading: false,
            };
        }
        case types.SEARCH_ALMERYS_PRODUCT_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.UPDATE_ALMERYS_PRODUCT_PENDING.type: {
            return {
                ...state,
                loadingUpdate: true,
            };
        }
        case types.UPDATE_ALMERYS_PRODUCT_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                product: body,
                loadingUpdate: false,
            };
        }
        default:
            return state;
    }
};
