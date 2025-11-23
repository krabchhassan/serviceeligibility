/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    searchResults: null,
    searchCriteria: null,
    loading: false,
    lastModifiedAMC: null,
    loadingLastModifiedAMC: false,
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
        case types.SEARCH_DECLARANT_FULFILLED.type: {
            return {
                ...state,
                searchResults: action.payload.body(false),
                loading: false,
            };
        }
        case types.SEARCH_DECLARANT_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.CHANGE_HOME_SEARCH_CRITERIA.type: {
            return {
                ...state,
                searchCriteria: action.payload.value,
            };
        }
        case types.GET_LAST_MODIFIED_PENDING.type: {
            return {
                ...state,
                loadingLastModifiedAMC: true,
            };
        }
        case types.GET_LAST_MODIFIED_REJECTED.type: {
            return {
                ...state,
                loadingLastModifiedAMC: false,
            };
        }
        case types.GET_LAST_MODIFIED_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                loadingLastModifiedAMC: false,
                lastModifiedAMC: Object.values(body),
            };
        }
        default:
            return state;
    }
};
