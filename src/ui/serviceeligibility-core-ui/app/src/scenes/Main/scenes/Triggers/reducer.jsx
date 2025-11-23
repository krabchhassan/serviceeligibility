/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    triggers: [],
    totalElements: 0,
    totalPages: 0,
    loading: false,
    triggeredKOBeneficiaries: [],
    koTotalElements: 0,
    koTotalPages: 0,
    koLoading: false,
};

export default (state = initialState, action) => {
    switch (action.type) {
        case types.SEARCH_TRIGGERS_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                triggers: body.triggers || [],
                totalElements: body.paging.totalElements,
                totalPages: body.paging.totalPages,
                loading: false,
            };
        }
        case types.SEARCH_TRIGGERS_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.GET_KO_TRIGGERED_BENEFICIARIES_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                triggeredKOBeneficiaries: body.triggeredBeneficiaries || [],
                koTotalElements: body.paging.totalElements,
                koTotalPages: body.paging.totalPages,
                koLoading: false,
            };
        }
        case types.GET_KO_TRIGGERED_BENEFICIARIES_PENDING.type: {
            return {
                ...state,
                koLoading: true,
            };
        }
        case types.CALL_RECYCLE_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                trigger: body,
                loading: false,
            };
        }
        case types.CALL_RECYCLE_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.CALL_CANCEL_PROCESS_FULFILLED.type:
        case types.CALL_TRIGGER_ABANDON_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                trigger: body,
                loading: false,
            };
        }
        case types.CALL_CANCEL_PROCESS_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }
        case types.CALL_TRIGGER_GENERATION_FULFILLED.type: {
            const body = action.payload.body(false) || {};
            return {
                ...state,
                trigger: body,
                loading: false,
            };
        }
        case types.CALL_TRIGGER_GENERATION_PENDING.type: {
            return {
                ...state,
                loading: true,
            };
        }

        default:
            return state;
    }
};
