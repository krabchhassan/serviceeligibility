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
    domainesTP: [],
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
        case types.REINIT_OFFERS.type: {
            return {
                ...state,
                offers: [],
            };
        }
        default:
            return state;
    }
};
