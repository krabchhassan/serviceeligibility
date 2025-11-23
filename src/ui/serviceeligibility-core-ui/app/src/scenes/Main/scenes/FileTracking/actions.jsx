/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import Processus from '../../../../common/resources/Processus';
import TypeFile from '../../../../common/resources/TypeFile';
import Flux from '../../../../common/resources/Flux';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function getAllProcessus() {
    return {
        ...types.GET_ALL_PROCESSUS,
        payload: Processus.getAll(),
    };
}

function getAllTypeFile() {
    return {
        ...types.GET_ALL_TYPE_FILE,
        payload: TypeFile.getAll(),
    };
}

// this 2 methods was originally used to get data using a post method. This need to be refactor, but for now it's ISO.
function fetchingSentFlux(requestParameters, isNewSearch) {
    return {
        ...types.GET_SENT_FLUX,
        payload: Flux.post(requestParameters),
        meta: { isNewSearch },
    };
}

function fetchingReceivedFlux(requestParameters, isNewSearch) {
    return {
        ...types.GET_RECEIVED_FLUX,
        payload: Flux.post(requestParameters),
        meta: { isNewSearch },
    };
}

function fluxSentPaging(paging) {
    return {
        ...types.FLUX_SENT_PAGING,
        payload: paging,
    };
}

function fluxReceivedPaging(paging) {
    return {
        ...types.FLUX_RECEIVED_PAGING,
        payload: paging,
    };
}

const actions = {
    getAllProcessus,
    getAllTypeFile,
    fetchingSentFlux,
    fetchingReceivedFlux,
    fluxSentPaging,
    fluxReceivedPaging,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
