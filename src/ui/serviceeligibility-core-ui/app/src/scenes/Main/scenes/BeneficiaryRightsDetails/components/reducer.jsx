/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    isLoadingHistoryContractTp: false,
    nextHistoryConsoContractTp: [],
    isLoadingCertifications: false,
    nextCertifications: [],
    isLoadingDeclarations: false,
    nextDeclarations: [],
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
        case types.GET_NEXT_CONSOLIDATED_CONTRACT_TP_FULFILLED.type: {
            return {
                ...state,
                nextHistoryConsoContractTp: action.payload.body(false),
                isLoadingHistoryContractTp: false,
            };
        }
        case types.GET_NEXT_CONSOLIDATED_CONTRACT_TP_REJECTED.type: {
            return {
                ...state,
                isLoadingHistoryContractTp: false,
            };
        }
        case types.GET_NEXT_CONSOLIDATED_CONTRACT_TP_PENDING.type: {
            return {
                ...state,
                isLoadingHistoryContractTp: true,
            };
        }
        case types.GET_NEXT_CERTIFICATIONS_FULFILLED.type: {
            return {
                ...state,
                nextCertifications: action.payload.body(false),
                isLoadingCertifications: false,
            };
        }
        case types.GET_NEXT_CERTIFICATIONS_REJECTED.type: {
            return {
                ...state,
                isLoadingCertifications: false,
            };
        }
        case types.GET_NEXT_CERTIFICATIONS_PENDING.type: {
            return {
                ...state,
                isLoadingCertifications: true,
            };
        }
        case types.GET_NEXT_DECLARATIONS_FULFILLED.type: {
            return {
                ...state,
                nextDeclarations: action.payload.body(false),
                isLoadingDeclarations: false,
            };
        }
        case types.GET_NEXT_DECLARATIONS_REJECTED.type: {
            return {
                ...state,
                isLoadingDeclarations: false,
            };
        }
        case types.GET_NEXT_DECLARATIONS_PENDING.type: {
            return {
                ...state,
                isLoadingDeclarations: true,
            };
        }
        default:
            return state;
    }
};
