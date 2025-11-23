/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import Lot from '../../../../../common/resources/Lot';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */

/* ************************************* */
function toggleImportPending() {
    return {
        ...types.TOGGLE_IMPORT_PENDING,
    };
}

function importSuccess(response) {
    return {
        ...types.IMPORT_SUCCESS,
        payload: response,
    };
}

function importFailed(response) {
    return {
        ...types.IMPORT_FAILED,
        payload: response,
    };
}

function importBobbMappingFile(file) {
    return {
        ...types.START_IMPORT,
        payload: file,
    };
}

function toggleImportLotPending() {
    return {
        ...types.TOGGLE_IMPORT_LOT_PENDING,
    };
}

function importLotSuccess(response) {
    return {
        ...types.IMPORT_LOT_SUCCESS,
        payload: response,
    };
}

function importLotFailed(response) {
    return {
        ...types.IMPORT_LOT_FAILED,
        payload: response,
    };
}

function importLotMappingFile(file) {
    return {
        ...types.START_IMPORT_LOT,
        payload: file,
    };
}

function searchLots(params) {
    return {
        ...types.SEARCH_LOTS,
        payload: Lot.getLot.get(params),
    };
}

function newLot(data) {
    return {
        ...types.NEW_LOT,
        payload: Lot.create().post(data),
    };
}

function updateLot(data) {
    return {
        ...types.UPDATE_LOT,
        payload: Lot.update().post(data),
    };
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default {
    importBobbMappingFile,
    toggleImportPending,
    importLotMappingFile,
    toggleImportLotPending,
    searchLots,
    newLot,
    updateLot,
    importSuccess,
    importFailed,
    importLotSuccess,
    importLotFailed,
};
