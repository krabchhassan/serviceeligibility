/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    volumetrics: [],
    loadingVolumetrics: false,
    searchCriteria: {},
    declarants: [],
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
        case types.GET_ALL_VOLUMETRY_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                volumetrics: body,
                loadingVolumetrics: false,
            };
        }
        case types.GET_ALL_VOLUMETRY_PENDING.type: {
            return {
                ...state,
                loadingProcess: true,
            };
        }
        case types.GET_ALL_VOLUMETRY_REJECTED.type: {
            return {
                ...state,
                loadingVolumetrics: false,
                volumetrics: [],
            };
        }
        case types.CHANGE_VOLUMETRY_SEARCH_CRITERIA.type: {
            return {
                ...state,
                searchCriteria: action.payload,
            };
        }
        case types.GET_ALL_DECLARANT_REJECTED.type: {
            return {
                ...state,
                loadingRightsBeneficiariesDeclarants: false,
            };
        }
        case types.GET_ALL_DECLARANT_PENDING.type: {
            return {
                ...state,
                loadingRightsBeneficiariesDeclarants: true,
            };
        }
        case types.GET_ALL_DECLARANT_FULFILLED.type: {
            return {
                ...state,
                declarants: action.payload.body(false),
                loadingRightsBeneficiariesNames: false,
            };
        }
        default:
            return state;
    }
};
