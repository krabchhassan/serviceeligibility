/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { AlertEvents } from '@beyond-framework/common-uitoolkit-beyond';
import EventTypes from './EventTypes';
import Service from './resources/Service';
import Circuits from './resources/Circuits';
import Declarant from './resources/Declarant';
import Conventions from './resources/Conventions';
import { Parameters } from './resources/Parameters';
import Lot from './resources/Lot';
import ContractElement from './resources/ContractElement';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

function removeAlert(alert) {
    return { ...AlertEvents.ALERT_SYSTEM_HIDE_NOTIFICATION, alert };
}

function addAlert({ message, headline = null, behavior = 'success', timeout = 5000 }) {
    return {
        ...AlertEvents.ALERT_SYSTEM_SHOW_NOTIFICATION,
        message,
        headline,
        behavior,
        timeout,
    };
}

function addAlertError({ message, headline, behavior = 'danger', timeout = null }) {
    return addAlert({
        message,
        headline,
        behavior,
        timeout,
    });
}

function getAllServices() {
    return {
        ...EventTypes.GET_ALL_SERVICES,
        payload: Service.getAll(),
    };
}

function getAllCircuits() {
    return {
        ...EventTypes.GET_ALL_CIRCUITS,
        payload: Circuits.getAll(),
    };
}

function getAllConventions() {
    return {
        ...EventTypes.GET_ALL_CONVENTIONS,
        payload: Conventions.getAll(),
    };
}

function getLightDeclarants() {
    return {
        ...EventTypes.GET_LIGHT_DECLARANTS,
        payload: Declarant.light().getAll(),
    };
}

function getAllServiceMetier() {
    return {
        ...EventTypes.GET_ALL_SERVICE_METIER,
        payload: Parameters.serviceMetier.get(),
    };
}

function displayAlert(message, behavior = 'success') {
    return {
        ...AlertEvents.ALERT_SYSTEM_SHOW_NOTIFICATION,
        message,
        behavior,
    };
}

function getAllGT(input) {
    return {
        ...EventTypes.GET_ALL_GT,
        payload: ContractElement.garanties().getAll(input),
    };
}

function getAllPeriod() {
    // TODO
    return {
        ...EventTypes.GET_ALL_PERIOD,
        payload: [],
    };
}

function getAllLot(params) {
    return {
        ...EventTypes.GET_ALL_LOT,
        payload: Lot.getLot.get(params),
    };
}

function getLots(params) {
    return {
        ...EventTypes.GET_LOTS,
        payload: Lot.getLots.get(params),
    };
}

function getLotsById(params) {
    const criteria = {
        ids: params.join(','),
    };
    return {
        ...EventTypes.GET_LOTS_BY_ID,
        payload: Lot.getLotsById.get(criteria),
    };
}

function getAllCodeRenvoi() {
    return {
        ...EventTypes.GET_ALL_CODES_RENVOI,
        payload: Parameters.codesRenvoi.get(),
    };
}

const actions = {
    removeAlert,
    addAlert,
    addAlertError,
    getAllServices,
    getAllCircuits,
    getAllConventions,
    getLightDeclarants,
    getAllServiceMetier,
    displayAlert,
    getAllGT,
    getAllLot,
    getAllCodeRenvoi,
    getLotsById,
    getLots,
    getAllPeriod,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
