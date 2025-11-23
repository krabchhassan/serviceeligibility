/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import ParametrageDroits from '../../../../common/resources/ParametrageDroits';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function searchParametrage(params) {
    return {
        ...types.SEARCH_PARAM_DROITS,
        payload: ParametrageDroits.getByParams.get(params),
    };
}
function changeStatus(id, status) {
    return {
        ...types.CHANGE_STATUS,
        payload: ParametrageDroits.changeStatus(id).put(status),
    };
}

const actions = {
    searchParametrage,
    changeStatus,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
