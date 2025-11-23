/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    paramDroitsList: [],
    totalElements: 0,
    totalPages: 0,
    loading: false,
};

export default (state = initialState, action) => {
    switch (action.type) {
        case types.SEARCH_PARAM_DROITS_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                paramDroitsList: body.parametragesCarteTP || [],
                totalElements: body.paging.totalElements,
                totalPages: body.paging.totalPages,
                loading: false,
            };
        }
        case types.SEARCH_PARAM_DROITS_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        default:
            return state;
    }
};
