/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import ParamDroits from '../../../../../../common/resources/ParametrageDroits';
import ref from '../../../../../../common/resources/DomainesTP';
import ref2 from '../../../../../../common/resources/CodeItelis';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function getParamForAmc(criteria) {
    return {
        ...types.GET_PARAM_DROIT_REF,
        payload: ParamDroits.getParamByAmc.get(criteria),
    };
}
function createParam(criteria) {
    return {
        ...types.CREATE_PARAM_DROIT,
        payload: ParamDroits.getByParams.post(criteria),
    };
}

function getCodesItelis() {
    return {
        ...types.GET_CODES_ITELIS,
        payload: ref2.getCodesItelis.get(),
    };
}

function getDomainesTP() {
    return {
        ...types.GET_DOMAINES_TP,
        payload: ref.getDomainesTP.get(),
    };
}

function reinitOffers() {
    return {
        ...types.REINIT_OFFERS,
        payload: null,
    };
}

function getPrioriteByAmc(amc) {
    return {
        ...types.GET_PRIORITE_PARAM,
        payload: ParamDroits.getPriorityByAMC(amc).get(),
    };
}

function getOneParam(id) {
    return {
        ...types.GET_ONE_PARAM,
        payload: ParamDroits.one(id).get(),
    };
}

const actions = {
    getParamForAmc,
    createParam,
    getDomainesTP,
    reinitOffers,
    getPrioriteByAmc,
    getCodesItelis,
    getOneParam,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
