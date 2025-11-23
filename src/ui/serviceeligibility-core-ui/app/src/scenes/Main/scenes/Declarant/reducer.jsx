/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import DeclarantUtils from './DeclarantUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    declarant: null,
    formDeclarant: null,
    loading: false,
    loadingDomainesTP: false,
    domainesTP: [],
    reseauxSoin: [],
    loadingReseauxSoin: false,
};

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export default (state = initialState, action) => {
    switch (action.type) {
        case types.GET_ONE_DECLARANT_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                declarant: body,
                formDeclarant: DeclarantUtils.transformDeclarantFromApi(body),
                loading: false,
            };
        }
        case types.GET_ONE_DECLARANT_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.GET_ONE_DECLARANT_REJECTED.type: {
            return {
                ...state,
                loading: false,
                declarant: null,
                formDeclarant: null,
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
        case types.GET_RESEAUX_SOIN_FULFILLED.type: {
            return {
                ...state,
                reseauxSoin: action.payload.body(false).data || [],
                loadingReseauxSoin: false,
            };
        }
        case types.GET_RESEAUX_SOIN_REJECTED.type: {
            return {
                ...state,
                reseauxSoin: [],
                loadingReseauxSoin: false,
            };
        }
        case types.GET_RESEAUX_SOIN_PENDING.type: {
            return {
                ...state,
                loadingReseauxSoin: true,
            };
        }
        default:
            return state;
    }
};
