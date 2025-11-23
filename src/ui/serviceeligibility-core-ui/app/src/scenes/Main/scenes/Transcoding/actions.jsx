/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import Transcodage from '../../../../common/resources/Transcodage';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function getAllTranscodageService() {
    return {
        ...types.GET_ALL_TRANSCODAGE_SERVICE,
        payload: Transcodage.getAll(),
    };
}

function getMapping(serviceCode, transcoCode) {
    return {
        ...types.GET_TRANSCO_MAPPING,
        payload: Transcodage.mapping(serviceCode, transcoCode).get(),
    };
}

function updateMapping(serviceCode, transcoCode, newTransco) {
    return {
        ...types.UPDATE_TRANSCO_MAPPING,
        payload: Transcodage.mapping(serviceCode, transcoCode).post(newTransco),
    };
}

const actions = {
    getAllTranscodageService,
    getMapping,
    updateMapping,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
