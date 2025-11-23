/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import ref from '../../../../../../../../common/resources/DomainesTP';
import Declarant from '../../../../../../../../common/resources/Declarant';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function getDomainesTP() {
    return {
        ...types.GET_DOMAINES_TP,
        payload: ref.getDomainesTP.get(),
    };
}

function getOne(id) {
    return {
        ...types.GET_ONE_DECLARANT,
        payload: Declarant.one(id).get(),
    };
}

function update(body) {
    return {
        ...types.UPDATE_DECLARANT,
        payload: Declarant.update().post(body),
    };
}

const actions = {
    getDomainesTP,
    getOne,
    update,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
