/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import ParametrageContratsIndividuels from '../../../../common/resources/ParametrageContratsIndividuels';
import Triggers from '../../../../common/resources/Triggers';
import Declarant from '../../../../common/resources/Declarant';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */
/* ************************************* */

function getContratIndiv(body) {
    return {
        ...types.SEARCH_PARAM_CONTRAT_INDIV,
        payload: ParametrageContratsIndividuels.getContratIndiv(body.amc, body.contrat, body.numeroAdherent).get(),
        meta: {
            ignoreNotif: true,
        },
    };
}

function generateParametrageContrat(body) {
    return {
        ...types.GENERATE_PARAMETRAGE_CONTRAT,
        payload: Triggers.generateParametrageContrat.post(body),
        meta: {
            ignoreNotif: true,
        },
    };
}

function getDeclarant(idAmc) {
    return {
        ...types.SEARCH_DECLARANT,
        payload: Declarant.one(idAmc).get(),
    };
}

const actions = {
    getContratIndiv,
    generateParametrageContrat,
    getDeclarant,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
