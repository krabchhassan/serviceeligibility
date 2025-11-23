/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';
import ParamTranscodage from '../../../../common/resources/ParamTranscodage';
import Service from '../../../../common/resources/Service';
import { Parameters, Rejects, CodesRenvoi } from '../../../../common/resources/Parameters';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

const getAllTranscodageParam = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_PARAM_TRANSCO,
        payload: ParamTranscodage.getAll(),
    });

const createTranscodageParam = (paramTransco) => (dispatch) =>
    dispatch({
        ...types.CREATE_PARAM_TRANSCO,
        payload: ParamTranscodage.post(paramTransco),
    });

const deleteTrancodageParam = (code) => (dispatch) => {
    ParamTranscodage.deleteByCode(code).delete();
    return dispatch({
        ...types.DELETE_PARAM_TRANSCO,
        codeDeleted: code,
    });
};

const updateTrancodageParam = (paramTransco) => (dispatch) =>
    dispatch({
        ...types.EDIT_PARAM_TRANSCO,
        payload: ParamTranscodage.editByCode(paramTransco.codeObjetTransco).put(paramTransco),
    });

const getTranscodageParamByCode = (code) => (dispatch) =>
    dispatch({
        ...types.GET_PARAM_TRANSCO_BY_CODE,
        payload: ParamTranscodage.findOneByCode(code).get(),
    });
const getAllServicesTransco = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_SERVICES_TRANSCO,
        payload: Service.getAll(),
    });

const createServiceTransco = (serviceTransco) => (dispatch) =>
    dispatch({
        ...types.CREATE_SERVICE_TRANSCO,
        payload: Service.post(serviceTransco),
    });

const deleteServiceTransco = (name) => (dispatch) => {
    Service.byName(name).delete();
    return dispatch({
        ...types.DELETE_SERVICE_TRANSCO,
        nameDeleted: name,
    });
};

const updateServiceTransco = (serviceTransco) => (dispatch) =>
    dispatch({
        ...types.UPDATE_SERVICE_TRANSCO,
        payload: Service.put(serviceTransco.id, serviceTransco),
    });

const getAllAgreement = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_AGREEMENT,
        payload: Parameters.conventionnement.get(),
    });

const createAgreement = (agreement) => (dispatch) =>
    dispatch({
        ...types.CREATE_AGREEMENT,
        payload: Parameters.conventionnement.post(agreement),
    });

const updateAgreement = (agreement) => (dispatch) =>
    dispatch({
        ...types.UPDATE_AGREEMENT,
        payload: Parameters.conventionnement.put(agreement),
    });

const deleteAgreement = (agreementCode) => (dispatch) => {
    Parameters.conventionnement.deleteByCode(agreementCode).delete();
    return dispatch({
        ...types.DELETE_AGREEMENT,
        codeDeleted: agreementCode,
    });
};

const getAllReturnCodes = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_RETURN_CODES,
        payload: CodesRenvoi.getAll(),
    });

const createCodesRenvoi = (codesRenvoi) => (dispatch) =>
    dispatch({
        ...types.GET_ALL_RETURN_CODES,
        payload: Parameters.codesRenvoi.post(codesRenvoi),
    });

const updateCodesRenvoi = (codesRenvoi) => (dispatch) =>
    dispatch({
        ...types.GET_ALL_RETURN_CODES,
        payload: Parameters.codesRenvoi.put(codesRenvoi),
    });

const getAllServiceMetier = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_SERVICE_METIER,
        payload: Parameters.serviceMetier.get(),
    });

const createServiceMetier = (serviceMetier) => (dispatch) =>
    dispatch({
        ...types.CREATE_SERVICE_METIER,
        payload: Parameters.serviceMetier.post(serviceMetier),
    });

const updateServiceMetier = (serviceMetier) => (dispatch) =>
    dispatch({
        ...types.UPDATE_SERVICE_METIER,
        payload: Parameters.serviceMetier.put(serviceMetier),
    });

const deleteServiceMetier = (serviceMetierCode) => (dispatch) => {
    Parameters.serviceMetier.deleteByCode(serviceMetierCode).delete();
    return dispatch({
        ...types.DELETE_SERVICE_METIER,
        codeDeleted: serviceMetierCode,
    });
};

const getAllProcess = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_PROCESS,
        payload: Parameters.processes.get(),
    });

const getAllFilesType = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_FILES_TYPE,
        payload: Parameters.filestype.get(),
    });

const getAllRejects = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_REJECTS,
        payload: Rejects.getAll(),
    });

const getAllClaims = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_CLAIMS,
        payload: Parameters.claim.get(),
    });

const createClaim = (claim) => (dispatch) =>
    dispatch({
        ...types.CREATE_CLAIM,
        payload: Parameters.claim.post(claim),
    });

const updateClaim = (claim) => (dispatch) =>
    dispatch({
        ...types.UPDATE_CLAIM,
        payload: Parameters.claim.put(claim),
    });

const deleteClaim = (claimCode) => (dispatch) => {
    Parameters.claim.deleteByCode(claimCode).delete();
    return dispatch({
        ...types.DELETE_CLAIM,
        codeDeleted: claimCode,
    });
};

const getAllDomains = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_DOMAINS,
        payload: Parameters.domain.get(),
    });

const createDomain = (domain) => (dispatch) =>
    dispatch({
        ...types.CREATE_DOMAIN,
        payload: Parameters.domain.post(domain),
    });

const updateDomain = (domain) => (dispatch) =>
    dispatch({
        ...types.UPDATE_DOMAIN,
        payload: Parameters.domain.put(domain),
    });

const deleteDomain = (domainCode) => (dispatch) => {
    Parameters.domain.deleteByCode(domainCode).delete();
    return dispatch({
        ...types.DELETE_DOMAIN,
        codeDeleted: domainCode,
    });
};

const getAllDomainsIS = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_DOMAINS_IS,
        payload: Parameters.domainIS.get(),
    });

const createDomainIS = (domainIS) => (dispatch) =>
    dispatch({
        ...types.CREATE_DOMAIN_IS,
        payload: Parameters.domainIS.post(domainIS),
    });

const updateDomainIS = (domainIS) => (dispatch) =>
    dispatch({
        ...types.UPDATE_DOMAIN_IS,
        payload: Parameters.domainIS.put(domainIS),
    });

const deleteDomainIS = (domainISCode) => (dispatch) => {
    Parameters.domainIS.deleteByCode(domainISCode).delete();
    return dispatch({
        ...types.DELETE_DOMAIN_IS,
        codeDeleted: domainISCode,
    });
};

const getAllDomainsSP = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_DOMAINS_SP,
        payload: Parameters.domainSP.get(),
    });

const createDomainSP = (domainSP) => (dispatch) =>
    dispatch({
        ...types.CREATE_DOMAIN_SP,
        payload: Parameters.domainSP.post(domainSP),
    });

const updateDomainSP = (domainSP) => (dispatch) =>
    dispatch({
        ...types.UPDATE_DOMAIN_SP,
        payload: Parameters.domainSP.put(domainSP),
    });

const deleteDomainSP = (domainSPCode) => (dispatch) => {
    Parameters.domainSP.deleteByCode(domainSPCode).delete();
    return dispatch({
        ...types.DELETE_DOMAIN_SP,
        codeDeleted: domainSPCode,
    });
};

const getAllFormulas = () => (dispatch) =>
    dispatch({
        ...types.GET_ALL_FORMULAS,
        payload: Parameters.formula.get(),
    });

const createFormula = (formula) => (dispatch) =>
    dispatch({
        ...types.CREATE_FORMULA,
        payload: Parameters.formula.post(formula),
    });

const updateFormula = (formula) => (dispatch) =>
    dispatch({
        ...types.UPDATE_FORMULA,
        payload: Parameters.formula.put(formula),
    });

const deleteFormula = (formulaCode) => (dispatch) => {
    Parameters.formula.deleteByCode(formulaCode).delete();
    return dispatch({
        ...types.DELETE_FORMULA,
        codeDeleted: formulaCode,
    });
};

const actions = {
    getAllTranscodageParam,
    createTranscodageParam,
    updateTrancodageParam,
    getTranscodageParamByCode,
    deleteTrancodageParam,

    getAllServicesTransco,
    createServiceTransco,
    deleteServiceTransco,
    updateServiceTransco,

    getAllAgreement,
    createAgreement,
    updateAgreement,
    deleteAgreement,

    getAllServiceMetier,
    createServiceMetier,
    updateServiceMetier,
    deleteServiceMetier,

    getAllProcess,

    getAllFilesType,

    getAllRejects,

    getAllClaims,
    createClaim,
    updateClaim,
    deleteClaim,

    getAllDomains,
    createDomain,
    updateDomain,
    deleteDomain,

    getAllDomainsIS,
    createDomainIS,
    updateDomainIS,
    deleteDomainIS,

    getAllDomainsSP,
    createDomainSP,
    updateDomainSP,
    deleteDomainSP,

    getAllFormulas,
    createFormula,
    updateFormula,
    deleteFormula,

    getAllReturnCodes,
    createCodesRenvoi,
    updateCodesRenvoi,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export { actions as default };
