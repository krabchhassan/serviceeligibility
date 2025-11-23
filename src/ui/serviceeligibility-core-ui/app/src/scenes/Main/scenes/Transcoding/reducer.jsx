/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    transcodageService: [],
    transcoMapping: {},
    isLoading: false,
    isLoadingMapping: false,
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
        case types.GET_ALL_TRANSCODAGE_SERVICE_PENDING.type: {
            return {
                ...state,
                isLoading: true,
            };
        }
        case types.GET_ALL_TRANSCODAGE_SERVICE_FULFILLED.type: {
            return {
                ...state,
                transcodageService: action.payload.body(false),
                isLoading: false,
            };
        }
        case types.GET_TRANSCO_MAPPING_PENDING.type: {
            return {
                ...state,
                isLoadingMapping: true,
            };
        }
        case types.GET_TRANSCO_MAPPING_FULFILLED.type: {
            return {
                ...state,
                transcoMapping: action.payload.body(false),
                isLoadingMapping: false,
            };
        }
        default:
            return state;
    }
};
