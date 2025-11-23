/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    declarationId: null,
    declarationsList: null,
    loading: false,
    loadingBenef: false,
    benefDetails: null,
    servicePrestations: null,
    loadingPrestation: false,
    loadingServicePrestIJ: false,
    loadingDeclarant: false,
    loadingSas: false,
    servicePrestIJs: null,
    amcName: 'Non trouvée',
    numerosAMCEchanges: [],
    declarationDetails: [],
    consolidatedContractList: [],
    contractToOpen: null,
    historiqueConsolidations: [],
    attestations: [],
    otherBenefs: [],
    consoFinished: false,
    sasContractList: [],
};

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */
const getId = (list) =>
    list &&
    list.length > 0 &&
    list[0].contrats &&
    list[0].contrats.length > 0 &&
    list[0].contrats[0].idTechniqueDeclaration;
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export default (state = initialState, action) => {
    switch (action.type) {
        case types.GET_ONE_BENEF_FULFILLED.type: {
            return {
                ...state,
                benefDetails: action.payload,
                loadingBenef: false,
            };
        }
        case types.GET_ONE_BENEF_PENDING.type: {
            return {
                ...state,
                loadingBenef: true,
            };
        }
        case types.GET_ONE_BENEF_REJECTED.type: {
            return {
                ...state,
                loadingBenef: false,
            };
        }
        case types.GET_PRESTATION_SERVICE_FULFILLED.type: {
            return {
                ...state,
                servicePrestations: action.payload.body(false),
                loadingPrestation: false,
            };
        }
        case types.GET_PRESTATION_SERVICE_PENDING.type: {
            return {
                ...state,
                loadingPrestation: true,
            };
        }
        case types.GET_PRESTATION_SERVICE_REJECTED.type: {
            return {
                ...state,
                loadingPrestation: false,
            };
        }
        case types.GET_SERVICE_PRESTIJ_FULFILLED.type: {
            return {
                ...state,
                servicePrestIJs: action.payload.body(false),
                loadingServicePrestIJ: false,
            };
        }
        case types.GET_SERVICE_PRESTIJ_PENDING.type: {
            return {
                ...state,
                loadingServicePrestIJ: true,
            };
        }
        case types.GET_SERVICE_PRESTIJ_REJECTED.type: {
            return {
                ...state,
                loadingServicePrestIJ: false,
            };
        }
        case types.GET_ONE_DECLARANT_FULFILLED.type: {
            const res = action.payload.body(false);
            return {
                ...state,
                amcName: res.nom,
                numerosAMCEchanges: res.numerosAMCEchanges,
                loadingDeclarant: false,
            };
        }
        case types.GET_ONE_DECLARANT_PENDING.type: {
            return {
                ...state,
                loadingDeclarant: true,
            };
        }
        case types.GET_ONE_DECLARANT_REJECTED.type: {
            return {
                ...state,
                loadingDeclarant: false,
            };
        }
        case types.GET_BENEFICIARY_TP_DETAILS_FULFILLED.type: {
            const body = action.payload.body(false);
            const {
                benefDetails,
                declarationDetails,
                declarationToOpen,
                consolidatedContractList,
                contractToOpen,
                historiqueConsolidations,
                attestations,
                otherBenefs,
                sasContractList,
            } = body;
            const { droits } = declarationToOpen || {};
            const id = getId(droits);
            return {
                ...state,
                benefDetails,
                declarationId: id,
                declarationsList: droits,
                declarationDetails,
                consolidatedContractList,
                historiqueConsolidations,
                attestations,
                otherBenefs,
                contractToOpen,
                sasContractList,
                loading: false,
                loadingBenef: false,
                consoFinished: false,
            };
        }
        case types.GET_BENEFICIARY_TP_DETAILS_PENDING.type: {
            return {
                ...state,
                loading: true,
                loadingBenef: true,
            };
        }
        case types.GET_BENEFICIARY_TP_DETAILS_REJECTED.type: {
            return {
                ...state,
                declarationDetails: [],
                loading: false,
                loadingBenef: false,
                consoFinished: false,
            };
        }
        case types.GET_BENEFICIARY_TP_DETAILS_LIGHT_REJECTED.type: {
            return {
                ...state,
                declarationDetails: [],
                loading: false,
                loadingBenef: false,
            };
        }
        case types.POST_CONSOLIDATE_FULFILLED.type: {
            return {
                ...state,
                consoFinished: true,
            };
        }
        case types.POST_CONSOLIDATE_REJECTED.type: {
            return {
                ...state,
                consoFinished: false,
            };
        }
        case types.POST_CONSOLIDATE_PENDING.type: {
            return {
                ...state,
                consoFinished: false,
            };
        }
        case types.CLEAR_BENEFICIARY_DETAILS.type: {
            return {
                ...state,
                benefDetails: null,
            };
        }
        case types.GET_SAS_CONTRATS_FULFILLED.type: {
            return {
                ...state,
                sasContractList: action.payload.body(false),
                loadingSas: false,
            };
        }
        case types.GET_SAS_CONTRATS_PENDING.type: {
            return {
                ...state,
                loadingSas: true,
            };
        }
        case types.GET_SAS_CONTRATS_REJECTED.type: {
            return {
                ...state,
                loadingSas: false,
            };
        }
        default:
            return state;
    }
};
