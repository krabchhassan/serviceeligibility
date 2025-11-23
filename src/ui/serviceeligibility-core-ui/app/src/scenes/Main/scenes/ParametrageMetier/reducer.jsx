/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    transcoParamList: {},
    existingTransco: {},
    isLoading: false,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reducer;

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

const caseLoading = (state) => ({
    ...state,
    isLoading: true,
});

function caseTranscoParamList(state, action) {
    return {
        ...state,
        transcoParamList: { ...action.payload.body(false) },
        isLoading: false,
    };
}

const caseCreateTransco = (state, action) => {
    const transcoParamArr = Object.values({ ...state.transcoParamList });
    transcoParamArr.push(action.payload.body(false));
    return {
        ...state,
        transcoParamList: { ...transcoParamArr },
        isLoading: false,
    };
};

const caseEditTransco = (state, action) => {
    const transcoParamArr = Object.values({ ...state.transcoParamList });
    const createdTransco = action.payload.body(false);
    const index = transcoParamArr.findIndex((item) => item.codeObjetTransco === createdTransco.codeObjetTransco);
    if (index >= 0) {
        transcoParamArr[index] = { ...createdTransco };
    }
    return {
        ...state,
        transcoParamList: { ...transcoParamArr },
        isLoading: false,
    };
};

const caseDeleteTransco = (state, action) => {
    const transcoParamArr = Object.values({ ...state.transcoParamList });
    const newTranscoParamArr = transcoParamArr.filter((transco) => transco.codeObjetTransco !== action.codeDeleted);
    return {
        ...state,
        transcoParamList: { ...newTranscoParamArr },
        isLoading: false,
    };
};

const caseGetTranscoByCode = (state, action) => ({
    ...state,
    existingTransco: action.payload.body(false),
    isLoading: false,
});

const caseGetAllTranscoService = (state, action) => ({
    ...state,
    isLoading: false,
    serviceList: { ...action.payload.body(false) },
});

const caseCreateTranscoService = (state, action) => {
    const serviceTranscoArr = Object.values({ ...state.serviceList });
    serviceTranscoArr.push(action.payload.body(false));
    return {
        ...state,
        serviceList: { ...serviceTranscoArr },
        isLoading: false,
    };
};

const caseDeleteTranscoService = (state, action) => {
    const servicesTranscoArr = Object.values({ ...state.serviceList });
    const newServicesTranscoArr = servicesTranscoArr.filter((service) => service.nom !== action.nameDeleted);
    return {
        ...state,
        serviceList: { ...newServicesTranscoArr },
        isLoading: false,
    };
};

const caseUpdateTranscoService = (state, action) => {
    let serviceTranscoArr = Object.values({ ...state.serviceList });
    const serviceUpdated = action.payload.body(false);
    serviceTranscoArr = serviceTranscoArr.map((service) =>
        service.id === serviceUpdated.id ? serviceUpdated : service,
    );
    return {
        ...state,
        serviceList: { ...serviceTranscoArr },
        isLoading: false,
    };
};

const caseAgreementFulfilled = (state, action) => ({
    ...state,
    agreementList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseDeleteAgreementFulfilled = (state, action) => {
    const agreements = Object.values({ ...state.agreementList });
    const newAgreements = agreements.filter((agreement) => agreement.code !== action.codeDeleted);
    return {
        ...state,
        agreementList: { ...newAgreements },
        isLoading: false,
    };
};

const caseServiceMetierFulfilled = (state, action) => ({
    ...state,
    serviceMetierList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseDeleteServiceMetierFulfilled = (state, action) => {
    const services = Object.values({ ...state.serviceMetierList });
    const newServices = services.filter((service) => service.code !== action.codeDeleted);
    return {
        ...state,
        serviceMetierList: { ...newServices },
        isLoading: false,
    };
};

const caseGetAllProcesses = (state, action) => ({
    ...state,
    processList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseGetAllFileTypes = (state, action) => ({
    ...state,
    filesTypeList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseGetAllRejects = (state, action) => ({
    ...state,
    rejectsList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseClaimFulfilled = (state, action) => ({
    ...state,
    claimList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseDeleteClaimFulfilled = (state, action) => {
    const claims = Object.values({ ...state.claimList });
    const newClaims = claims.filter((claim) => claim.code !== action.codeDeleted);
    return {
        ...state,
        claimList: { ...newClaims },
        isLoading: false,
    };
};

const caseDomaineFulfilled = (state, action) => ({
    ...state,
    domainList: { ...action.payload.body(false) },
    domainComboList: action.payload.body(false),
    isLoading: false,
});

const caseDeleteDomaineFulfilled = (state, action) => {
    const domains = Object.values({ ...state.domainList });
    const newDomains = domains.filter((domain) => domain.code !== action.codeDeleted);
    return {
        ...state,
        domainList: { ...newDomains },
        isLoading: false,
    };
};

const caseDomaineIsFulfilled = (state, action) => ({
    ...state,
    domainISList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseDeleteDomaineIsFulfilled = (state, action) => {
    const domains = Object.values({ ...state.domainISList });
    const newDomains = domains.filter((domain) => domain.code !== action.codeDeleted);
    return {
        ...state,
        domainISList: { ...newDomains },
        isLoading: false,
    };
};

const caseDomaineSpFulfilled = (state, action) => ({
    ...state,
    domainSPList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseDeleteDomaineSpFulfilled = (state, action) => {
    const domains = Object.values({ ...state.domainSPList });
    const newDomains = domains.filter((domain) => domain.code !== action.codeDeleted);
    return {
        ...state,
        domainSPList: { ...newDomains },
        isLoading: false,
    };
};

const caseFormula = (state, action) => ({
    ...state,
    formulaList: { ...action.payload.body(false) },
    isLoading: false,
});

const caseDeleteFormula = (state, action) => {
    const formulas = Object.values({ ...state.formulaList });
    const newFormulas = formulas.filter((formula) => formula.code !== action.codeDeleted);
    return {
        ...state,
        formulaList: { ...newFormulas },
        isLoading: false,
    };
};

const caseReturnCodesFulfilled = (state, action) => {
    return {
        ...state,
        returnCodesList: { ...action.payload.body(false) },
        isLoading: false,
    };
};

/**
 * Reducer.
 * @param state
 * @param action
 */
function reducer(state = initialState, action) {
    const reducerMapper = {
        [types.GET_ALL_PARAM_TRANSCO_PENDING.type]: () => caseLoading(state),
        [types.CREATE_PARAM_TRANSCO_PENDING.type]: () => caseLoading(state),
        [types.EDIT_PARAM_TRANSCO_PENDING.type]: () => caseLoading(state),
        [types.GET_PARAM_TRANSCO_BY_CODE_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_SERVICES_TRANSCO_PENDING.type]: () => caseLoading(state),
        [types.CREATE_SERVICE_TRANSCO_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_SERVICE_TRANSCO_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_AGREEMENT_PENDING.type]: () => caseLoading(state),
        [types.CREATE_AGREEMENT_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_AGREEMENT_PENDING.type]: () => caseLoading(state),
        [types.DELETE_AGREEMENT_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_SERVICE_METIER_PENDING.type]: () => caseLoading(state),
        [types.CREATE_SERVICE_METIER_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_SERVICE_METIER_PENDING.type]: () => caseLoading(state),
        [types.DELETE_SERVICE_METIER_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_PROCESS_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_FILES_TYPE_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_REJECTS_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_CLAIMS_PENDING.type]: () => caseLoading(state),
        [types.CREATE_CLAIM_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_CLAIM_PENDING.type]: () => caseLoading(state),
        [types.DELETE_CLAIM_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_DOMAINS_PENDING.type]: () => caseLoading(state),
        [types.CREATE_DOMAIN_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_DOMAIN_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_DOMAINS_IS_PENDING.type]: () => caseLoading(state),
        [types.CREATE_DOMAIN_IS_PENDING.type]: () => caseLoading(state),
        [types.DELETE_DOMAIN_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_DOMAIN_IS_PENDING.type]: () => caseLoading(state),
        [types.DELETE_DOMAIN_IS_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_DOMAINS_SP_PENDING.type]: () => caseLoading(state),
        [types.CREATE_DOMAIN_SP_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_DOMAIN_SP_PENDING.type]: () => caseLoading(state),
        [types.DELETE_DOMAIN_SP_PENDING.type]: () => caseLoading(state),
        [types.GET_ALL_FORMULAS_PENDING.type]: () => caseLoading(state),
        [types.CREATE_FORMULA_PENDING.type]: () => caseLoading(state),
        [types.UPDATE_FORMULA_PENDING.type]: () => caseLoading(state),
        [types.DELETE_FORMULA_PENDING.type]: () => caseLoading(state),

        [types.GET_ALL_PARAM_TRANSCO_FULFILLED.type]: () => caseTranscoParamList(state, action),
        [types.CREATE_PARAM_TRANSCO_FULFILLED.type]: () => caseCreateTransco(state, action),
        [types.EDIT_PARAM_TRANSCO_FULFILLED.type]: () => caseEditTransco(state, action),
        [types.DELETE_PARAM_TRANSCO.type]: () => caseDeleteTransco(state, action),
        [types.GET_PARAM_TRANSCO_BY_CODE_FULFILLED.type]: () => caseGetTranscoByCode(state, action),
        [types.GET_ALL_SERVICES_TRANSCO_FULFILLED.type]: () => caseGetAllTranscoService(state, action),
        [types.CREATE_SERVICE_TRANSCO_FULFILLED.type]: () => caseCreateTranscoService(state, action),
        [types.DELETE_SERVICE_TRANSCO.type]: () => caseDeleteTranscoService(state, action),
        [types.UPDATE_SERVICE_TRANSCO_FULFILLED.type]: () => caseUpdateTranscoService(state, action),
        [types.GET_ALL_AGREEMENT_FULFILLED.type]: () => caseAgreementFulfilled(state, action),
        [types.CREATE_AGREEMENT_FULFILLED.type]: () => caseAgreementFulfilled(state, action),
        [types.UPDATE_AGREEMENT_FULFILLED.type]: () => caseAgreementFulfilled(state, action),
        [types.DELETE_AGREEMENT_FULFILLED.type]: () => caseDeleteAgreementFulfilled(state, action),
        [types.GET_ALL_SERVICE_METIER_FULFILLED.type]: () => caseServiceMetierFulfilled(state, action),
        [types.CREATE_SERVICE_METIER_FULFILLED.type]: () => caseServiceMetierFulfilled(state, action),
        [types.UPDATE_SERVICE_METIER_FULFILLED.type]: () => caseServiceMetierFulfilled(state, action),
        [types.DELETE_SERVICE_METIER_FULFILLED.type]: () => caseDeleteServiceMetierFulfilled(state, action),
        [types.GET_ALL_PROCESS_FULFILLED.type]: () => caseGetAllProcesses(state, action),
        [types.GET_ALL_FILES_TYPE_FULFILLED.type]: () => caseGetAllFileTypes(state, action),
        [types.GET_ALL_REJECTS_FULFILLED.type]: () => caseGetAllRejects(state, action),
        [types.GET_ALL_CLAIMS_FULFILLED.type]: () => caseClaimFulfilled(state, action),
        [types.CREATE_CLAIM_FULFILLED.type]: () => caseClaimFulfilled(state, action),
        [types.UPDATE_CLAIM_FULFILLED.type]: () => caseClaimFulfilled(state, action),
        [types.DELETE_CLAIM_FULFILLED.type]: () => caseDeleteClaimFulfilled(state, action),
        [types.GET_ALL_DOMAINS_FULFILLED.type]: () => caseDomaineFulfilled(state, action),
        [types.CREATE_DOMAIN_FULFILLED.type]: () => caseDomaineFulfilled(state, action),
        [types.UPDATE_DOMAIN_FULFILLED.type]: () => caseDomaineFulfilled(state, action),
        [types.DELETE_DOMAIN_FULFILLED.type]: () => caseDeleteDomaineFulfilled(state, action),

        [types.GET_ALL_DOMAINS_IS_FULFILLED.type]: () => caseDomaineIsFulfilled(state, action),
        [types.CREATE_DOMAIN_IS_FULFILLED.type]: () => caseDomaineIsFulfilled(state, action),
        [types.UPDATE_DOMAIN_IS_FULFILLED.type]: () => caseDomaineIsFulfilled(state, action),
        [types.DELETE_DOMAIN_IS_FULFILLED.type]: () => caseDeleteDomaineIsFulfilled(state, action),

        [types.GET_ALL_DOMAINS_SP_FULFILLED.type]: () => caseDomaineSpFulfilled(state, action),
        [types.CREATE_DOMAIN_SP_FULFILLED.type]: () => caseDomaineSpFulfilled(state, action),
        [types.UPDATE_DOMAIN_SP_FULFILLED.type]: () => caseDomaineSpFulfilled(state, action),
        [types.DELETE_DOMAIN_SP_FULFILLED.type]: () => caseDeleteDomaineSpFulfilled(state, action),

        [types.GET_ALL_FORMULAS_FULFILLED.type]: () => caseFormula(state, action),
        [types.CREATE_FORMULA_FULFILLED.type]: () => caseFormula(state, action),
        [types.UPDATE_FORMULA_FULFILLED.type]: () => caseFormula(state, action),
        [types.DELETE_FORMULA_FULFILLED.type]: () => caseDeleteFormula(state, action),

        [types.GET_ALL_RETURN_CODES_FULFILLED.type]: () => caseReturnCodesFulfilled(state, action),
    };
    const returnFunc = reducerMapper[action.type];
    if (returnFunc !== undefined) {
        return returnFunc();
    }
    return state;
}
