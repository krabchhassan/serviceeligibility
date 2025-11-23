/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import Declarant from '../../../../common/resources/Declarant';
import ref from '../../../../common/resources/DomainesTP';
import ref2 from '../../../../common/resources/ReseauSoin';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */

/* ************************************* */
function getOne(id) {
    return {
        ...types.GET_ONE_DECLARANT,
        payload: Declarant.one(id).get(),
    };
}

function create(body) {
    return {
        ...types.CREATE_DECLARANT,
        payload: Declarant.create().post(body),
    };
}

function update(body) {
    return {
        ...types.UPDATE_DECLARANT,
        payload: Declarant.update().post(body),
    };
}

function exists(id) {
    return (dispatch) =>
        dispatch({
            ...types.EXISTS_DECLARANT,
            payload: Declarant.exists(id).get(),
        }).then((result) => result.action.payload.body(false));
}

function getDomainesTP() {
    return {
        ...types.GET_DOMAINES_TP,
        payload: ref.getDomainesTP.get(),
    };
}

function getReseauxSoin() {
    return {
        ...types.GET_RESEAUX_SOIN,
        payload: ref2.getReseauxSoin.get(),
    };
}

const actions = {
    getOne,
    exists,
    create,
    update,
    getDomainesTP,
    getReseauxSoin,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
