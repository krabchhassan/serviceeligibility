/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    paramDroitsRefsList: [],
    offers: [],
    loading: false,
    loadingOffers: false,
    loadingDomainesTP: false,
    loadingCodesItelis: false,
    codesItelis: [],
    domainesTP: [],
    paramPriorites: [],
    paramToDuplicate: null,
    loadingParamToDuplicate: false,
};

export default (state = initialState, action) => {
    switch (action.type) {
        case types.GET_PARAM_DROIT_REF_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                paramDroitsRefsList: body || [],
                loading: false,
            };
        }
        case types.GET_PARAM_DROIT_REF_REJECTED.type: {
            return {
                ...state,
                paramDroitsRefsList: [],
                loading: false,
            };
        }
        case types.GET_PARAM_DROIT_REF_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.GET_OFFERS_FULFILLED.type: {
            return {
                ...state,
                offers: action.payload.body(false) || [],
                loadingOffers: false,
            };
        }
        case types.GET_OFFERS_REJECTED.type: {
            return {
                ...state,
                offers: [],
                loadingOffers: false,
            };
        }
        case types.GET_OFFERS_PENDING.type: {
            return {
                ...state,
                loadingOffers: true,
            };
        }
        case types.GET_CODES_ITELIS_FULFILLED.type: {
            return {
                ...state,
                codesItelis: action.payload.body(false).data || [],
                loadingCodesItelis: false,
            };
        }
        case types.GET_CODES_ITELIS_REJECTED.type: {
            return {
                ...state,
                codesItelis: [],
                loadingCodesItelis: false,
            };
        }
        case types.GET_CODES_ITELIS_PENDING.type: {
            return {
                ...state,
                loadingCodesItelis: true,
            };
        }
        case types.GET_DOMAINES_TP_FULFILLED.type: {
            return {
                ...state,
                domainesTP: action.payload.body(false).data || [],
                loadingDomainesTP: false,
            };
        }
        case types.GET_DOMAINES_TP_REJECTED.type: {
            return {
                ...state,
                domainesTP: [],
                loadingDomainesTP: false,
            };
        }
        case types.GET_DOMAINES_TP_PENDING.type: {
            return {
                ...state,
                loadingDomainesTP: true,
            };
        }
        case types.GET_PRIORITE_PARAM_FULFILLED.type: {
            return {
                ...state,
                paramPriorites: (action.payload.body(false) || []).map((num) => num.toString()),
            };
        }
        case types.GET_ONE_PARAM_FULFILLED.type: {
            return {
                ...state,
                paramToDuplicate: action.payload.body(false),
                loadingParamToDuplicate: false,
            };
        }
        case types.GET_ONE_PARAM_PENDING.type: {
            return {
                ...state,
                loadingParamToDuplicate: true,
            };
        }
        case types.GET_ONE_PARAM_REJECTED.type: {
            return {
                ...state,
                paramToDuplicate: null,
                loadingParamToDuplicate: true,
            };
        }
        default:
            return state;
    }
};
