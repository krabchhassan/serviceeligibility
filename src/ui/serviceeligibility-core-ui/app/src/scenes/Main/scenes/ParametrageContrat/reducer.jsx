/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import get from 'lodash/get';
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    contratIndiv: undefined,
    errorCode: undefined,
    loading: false,
    amc: undefined,
    loadingAmc: false,
};

export default (state = initialState, action) => {
    switch (action.type) {
        case types.SEARCH_PARAM_CONTRAT_INDIV_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                contratIndiv: body,
                error: undefined,
                loading: false,
            };
        }
        case types.SEARCH_PARAM_CONTRAT_INDIV_REJECTED.type: {
            const errorCode = get(action.payload.response.data.errors[0], 'error_code') || {};

            return {
                ...state,
                contratIndiv: undefined,
                errorCode,
                loading: false,
            };
        }
        case types.SEARCH_PARAM_CONTRAT_INDIV_PENDING.type: {
            return {
                ...state,
                error: undefined,
                loading: true,
            };
        }
        case types.SEARCH_DECLARANT_PENDING.type: {
            return {
                ...state,
                error: undefined,
                loadingAmc: true,
            };
        }
        case types.SEARCH_DECLARANT_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                amc: body,
                error: undefined,
                loading: false,
            };
        }
        case types.SEARCH_DECLARANT_REJECTED.type: {
            const errorCode = get(action.payload.response.data.errors[0], 'error_code') || {};

            return {
                ...state,
                amc: undefined,
                errorCode,
                loading: false,
            };
        }
        default:
            return state;
    }
};
