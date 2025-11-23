/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import Declarant from '../../../../common/resources/Declarant';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function searchDeclarant(params) {
    return {
        ...types.SEARCH_DECLARANT,
        payload: Declarant.search().getAll(params),
    };
}

function changeSarchCriteria(value) {
    return {
        ...types.CHANGE_HOME_SEARCH_CRITERIA,
        payload: { value },
    };
}

function getLastModified() {
    return {
        ...types.GET_LAST_MODIFIED,
        payload: Declarant.last().get(),
    };
}

const actions = {
    searchDeclarant,
    changeSarchCriteria,
    getLastModified,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
