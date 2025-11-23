/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import ServicePrestation from '../../../../common/resources/ServicePrestation';
import ElasticSearch from '../../../../common/resources/ElasticSearch';
import ServicePrestIJ from '../../../../common/resources/ServicePrestIJ';
import Beneficiaires from '../../../../common/resources/Beneficiaries';
import Declarant from '../../../../common/resources/Declarant';
import BeneficiaryDetails from '../../../../common/resources/BeneficiaryDetails';
import Consolidation from '../../../../common/resources/Consolidation';
import SasContrat from '../../../../common/resources/SasContrat';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */

/* ************************************* */
function splitBenefKey(key) {
    // Splits the key in an array of two elements by the first '-'
    // splitBenefKey(key)[0] => insurerId
    // splitBenefKey(key)[1] => personNumber
    const [first, ...rest] = key.split('-');
    return [first, rest.join('-')];
}

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ********* */

/* ************************************* */

function getOneBenef(key) {
    return {
        ...types.GET_ONE_BENEF,
        payload: ElasticSearch.getOneBenef(key),
    };
}

function getPrestationService(criteria) {
    return {
        ...types.GET_PRESTATION_SERVICE,
        payload: ServicePrestation.search().get(criteria),
    };
}

function getServicePrestIJs(criteria) {
    return {
        ...types.GET_SERVICE_PRESTIJ,
        payload: ServicePrestIJ.search().get(criteria),
    };
}

function saveBenefHistorique(id, env) {
    return {
        payload: Beneficiaires.saveBeneficiaireHistory(`${id}/${env}`).get(),
    };
}

function getOneDeclarant(idDeclarant) {
    return {
        ...types.GET_ONE_DECLARANT,
        payload: Declarant.one(idDeclarant).get(),
    };
}

function getBeneficiaryTpDetails(benefKey, environment) {
    const splittedKey = splitBenefKey(benefKey);

    const criteria = {
        insurerId: splittedKey[0],
        personNumber: splittedKey[1],
        environment,
    };

    return {
        ...types.GET_BENEFICIARY_TP_DETAILS,
        payload: BeneficiaryDetails.searchTp().get(criteria),
    };
}

function getSasContrats(benefKey) {
    const splittedKey = splitBenefKey(benefKey);

    return {
        ...types.GET_SAS_CONTRATS,
        payload: SasContrat.getSasContrats(splittedKey[1]).get(),
    };
}

function getBeneficiaryTpDetailsLight(benefKey, declarationId, contractNumber, subscriberNumber, environment) {
    const splittedKey = splitBenefKey(benefKey);

    const criteria = {
        insurerId: splittedKey[0],
        personNumber: splittedKey[1],
        declarationsIds: declarationId.join(','),
        contractNumber,
        subscriberNumber,
        environment,
    };

    return {
        ...types.GET_BENEFICIARY_TP_DETAILS_LIGHT,
        payload: BeneficiaryDetails.searchTp().get(criteria),
    };
}

function postConsolidate(idDeclarant, numContrat, numAdherent) {
    return {
        ...types.POST_CONSOLIDATE,
        payload: Consolidation.contrat(idDeclarant, numContrat, numAdherent).post(),
    };
}

function clearBeneficiaryDetails() {
    return {
        ...types.CLEAR_BENEFICIARY_DETAILS,
    };
}

export const actions = {
    getOneBenef,
    getPrestationService,
    getServicePrestIJs,
    saveBenefHistorique,
    getOneDeclarant,
    getBeneficiaryTpDetails,
    getBeneficiaryTpDetailsLight,
    postConsolidate,
    clearBeneficiaryDetails,
    getSasContrats,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
