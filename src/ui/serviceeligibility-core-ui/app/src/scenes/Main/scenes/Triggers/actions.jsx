/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import Triggers from '../../../../common/resources/Triggers';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function searchTriggers(params) {
    return {
        ...types.SEARCH_TRIGGERS,
        payload: Triggers.search().get(params),
    };
}

function getKOTriggeredBeneficiaries(idTrigger) {
    return {
        ...types.GET_KO_TRIGGERED_BENEFICIARIES,
        payload: Triggers.getKOTriggeredBeneficiaries(idTrigger).get(),
    };
}

function callRecycle(id) {
    return {
        ...types.CALL_RECYCLE,
        payload: Triggers.callRecycle(id).put(),
    };
}

function callCancelProcess(id) {
    return {
        ...types.CALL_CANCEL_PROCESS,
        payload: Triggers.callCancelProcess(id).put(),
    };
}

function callTriggerGeneration(id) {
    return {
        ...types.CALL_TRIGGER_GENERATION,
        payload: Triggers.callTriggerGeneration(id).put(),
    };
}
function callAbandon(id) {
    return {
        ...types.CALL_TRIGGER_ABANDON,
        payload: Triggers.callAbandon(id).put(),
    };
}

const actions = {
    searchTriggers,
    getKOTriggeredBeneficiaries,
    callRecycle,
    callCancelProcess,
    callTriggerGeneration,
    callAbandon,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
