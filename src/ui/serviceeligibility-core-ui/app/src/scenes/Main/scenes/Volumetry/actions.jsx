/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import Volumetry from '../../../../common/resources/Volumetry';
import Declarant from '../../../../common/resources/Declarant';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */
/* ************************************* */
function getAllVolumetryData() {
    return {
        ...types.GET_ALL_VOLUMETRY,
        payload: Volumetry.getAll(),
    };
}

function changeVolumetrySearchCriteria(searchCriteria) {
    return {
        ...types.CHANGE_VOLUMETRY_SEARCH_CRITERIA,
        payload: searchCriteria,
    };
}

function getAllDeclarants() {
    return {
        ...types.GET_ALL_DECLARANT,
        payload: Declarant.getAll(),
    };
}

const actions = {
    getAllVolumetryData,
    changeVolumetrySearchCriteria,
    getAllDeclarants,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
