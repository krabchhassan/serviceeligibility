/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import fluxUtils from './fluxUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    // process
    processus: null,
    loadingProcess: false,

    // type file
    typesFile: null,
    loadingTypesFile: false,

    // flux sent
    fluxSent: null,
    totalFluxSent: 0,
    fluxSentPaging: 0,
    loadingSentFlux: false,

    // flux received
    fluxReceived: null,
    totalFluxReceived: 0,
    fluxReceivedPaging: 0,
    loadingReceivedFlux: false,
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
        case types.GET_ALL_PROCESSUS_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                processus: body,
                loadingProcess: false,
            };
        }
        case types.GET_ALL_PROCESSUS_PENDING.type: {
            return {
                ...state,
                loadingProcess: true,
            };
        }
        case types.GET_ALL_PROCESSUS_REJECTED.type: {
            return {
                ...state,
                loadingProcess: false,
                processus: null,
            };
        }
        case types.GET_ALL_TYPE_FILE_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                typesFile: body,
                loadingTypesFile: false,
            };
        }
        case types.GET_ALL_TYPE_FILE_PENDING.type: {
            return {
                ...state,
                loadingTypesFile: true,
            };
        }
        case types.GET_ALL_TYPE_FILE_REJECTED.type: {
            return {
                ...state,
                loadingTypesFile: false,
                typesFile: null,
            };
        }
        case types.GET_SENT_FLUX_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                fluxSent: fluxUtils.enrichirRetourRest(body.fluxInfo),
                ...(action.meta.isNewSearch && { totalFluxSent: body.totalFlux }),
                loadingSentFlux: false,
            };
        }
        case types.GET_SENT_FLUX_PENDING.type: {
            return {
                ...state,
                loadingSentFlux: true,
            };
        }
        case types.GET_SENT_FLUX_REJECTED.type: {
            return {
                ...state,
                loadingSentFlux: false,
                fluxSent: null,
            };
        }
        case types.GET_RECEIVED_FLUX_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                fluxReceived: fluxUtils.enrichirRetourRest(body.fluxInfo),
                ...(action.meta.isNewSearch && { totalFluxReceived: body.totalFlux }),
                loadingReceivedFlux: false,
            };
        }
        case types.GET_RECEIVED_FLUX_PENDING.type: {
            return {
                ...state,
                loadingReceivedFlux: true,
            };
        }
        case types.GET_RECEIVED_FLUX_REJECTED.type: {
            return {
                ...state,
                loadingReceivedFlux: false,
                fluxReceived: null,
            };
        }
        case types.FLUX_SENT_PAGING.type: {
            return {
                ...state,
                fluxSentPaging: action.payload,
            };
        }
        case types.FLUX_RECEIVED_PAGING.type: {
            return {
                ...state,
                fluxReceivedPaging: action.payload,
            };
        }
        default:
            return state;
    }
};
