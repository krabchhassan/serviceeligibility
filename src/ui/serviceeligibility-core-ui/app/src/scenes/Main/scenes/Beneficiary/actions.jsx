/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import ElasticSearch from '../../../../common/resources/ElasticSearch';
import Declarant from '../../../../common/resources/Declarant';
import SocieteEmettrice from '../../../../common/resources/SocieteEmettrice';

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********  PUBLIC FUNCTIONS  ******** */

/* ************************************* */

function getBeneficiaries(beneficiaryObject) {
    return {
        ...types.SEARCH_BENEFICIARY,
        payload: ElasticSearch.queryBeneficiaries(beneficiaryObject),
    };
}

function changeCriteriaResearch(value) {
    return {
        ...types.CHANGE_RESEARCH_CRITERIA,
        payload: { value },
    };
}

function getAllDeclarants() {
    return {
        ...types.GET_ALL_DECLARANT,
        payload: Declarant.getAll(),
    };
}

function getAllNames(partialNameQueryParameter = '', addSearchToResult = false) {
    return {
        ...types.GET_ALL_NAME,
        payload: ElasticSearch.queryDistinctNames(partialNameQueryParameter, addSearchToResult),
    };
}

function getAllFirstNames(partialFirstnameQueryParameter = '', addSearchToResult = false) {
    return {
        ...types.GET_ALL_FIRSTNAME,
        payload: ElasticSearch.queryDistinctFirstnames(partialFirstnameQueryParameter, addSearchToResult),
    };
}

function getAllNirs(partialNirQueryParameter = '') {
    return {
        ...types.GET_ALL_NIR,
        payload: ElasticSearch.queryDistinctNirs(partialNirQueryParameter),
    };
}

function getAllLocalites(partialLocaliteQueryParameter = '') {
    return {
        ...types.GET_ALL_LOCALITE,
        payload: ElasticSearch.queryDistinctLocalites(partialLocaliteQueryParameter),
    };
}

function getAllVoies(partialVoieQueryParameter = '') {
    return {
        ...types.GET_ALL_VOIE,
        payload: ElasticSearch.queryDistinctVoies(partialVoieQueryParameter),
    };
}

function getAllSocietesEmettrices() {
    return {
        ...types.GET_ALL_SOCIETES_EMETTRICES,
        payload: SocieteEmettrice.getAll(),
    };
}

const actions = {
    getBeneficiaries,
    changeCriteriaResearch,
    getAllDeclarants,
    getAllNames,
    getAllFirstNames,
    getAllNirs,
    getAllLocalites,
    getAllVoies,
    getAllSocietesEmettrices,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
