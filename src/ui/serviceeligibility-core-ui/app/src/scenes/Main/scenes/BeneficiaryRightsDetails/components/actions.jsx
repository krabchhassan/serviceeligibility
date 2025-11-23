/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import BeneficiaryDetails from '../../../../../common/resources/BeneficiaryDetails';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */

/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ********* */

/* ************************************* */

function getNextConsolidatedContractTp(startIndex, amcId, contractNumber, subscriberId, personNumber, environment) {
    const criteria = {
        startIndex,
        amcId,
        contractNumber,
        subscriberId,
        personNumber,
        environment,
        requestId: '3',
    };

    return {
        ...types.GET_NEXT_CONSOLIDATED_CONTRACT_TP,
        payload: BeneficiaryDetails.getNextConsolidatedContractTp().get(criteria),
    };
}

function getNextCertificates(startIndex, idDeclarant, numeroContrat, personNumber, environment) {
    const criteria = {
        startIndex,
        idDeclarant,
        numContrat: numeroContrat,
        personNumber,
        environment,
        requestId: '1',
    };

    return {
        ...types.GET_NEXT_CERTIFICATIONS,
        payload: BeneficiaryDetails.getNextCertifications().get(criteria),
    };
}

function getNextDeclarations(
    startIndex,
    startIndexRestit,
    idDeclarant,
    numeroContrat,
    numeroAdherant,
    personNumber,
    environment,
) {
    const criteria = {
        startIndex,
        startIndexRestit,
        idDeclarant,
        numContrat: numeroContrat,
        subscriberNumber: numeroAdherant,
        personNumber,
        environment,
        requestId: '2',
    };

    return {
        ...types.GET_NEXT_DECLARATIONS,
        payload: BeneficiaryDetails.getNextDeclarations().get(criteria),
    };
}

export const actions = {
    getNextConsolidatedContractTp,
    getNextCertificates,
    getNextDeclarations,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
