/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    beneficiaries: [],
    declarants: [],
    names: [],
    firstNames: [],
    nirs: [],
    societesEmettrices: [],
    researchCriteria: null,
    loadingBeneficiaries: false,
    loadingBeneficiariesDeclarants: false,
    loadingBeneficiariesNames: false,
    loadingBeneficiariesFirstnames: false,
    loadingBeneficiariesNirs: false,
    loadingBeneficiariesLocalites: false,
    loadingBeneficiariesVoies: false,
    loadingBeneficiariesSocietesEmettrices: false,
    beneficiaireHistory: [],
};

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export default (state = initialState, action) => {
    switch (action.type) {
        case types.SEARCH_BENEFICIARY_FULFILLED.type: {
            const body = action.payload;
            return {
                ...state,
                beneficiaries: Object.values(body),
                loadingBeneficiaries: false,
            };
        }
        case types.SEARCH_BENEFICIARY_PENDING.type: {
            return {
                ...state,
                loadingBeneficiaries: true,
            };
        }
        case types.SEARCH_BENEFICIARY_REJECTED.type: {
            return {
                ...state,
                loadingBeneficiaries: false,
            };
        }
        case types.GET_ALL_DECLARANT_REJECTED.type: {
            return {
                ...state,
                loadingRightsBeneficiariesDeclarants: false,
            };
        }
        case types.GET_ALL_DECLARANT_PENDING.type: {
            return {
                ...state,
                loadingRightsBeneficiariesDeclarants: true,
            };
        }
        case types.GET_ALL_DECLARANT_FULFILLED.type: {
            return {
                ...state,
                declarants: action.payload.body(false),
                loadingRightsBeneficiariesNames: false,
            };
        }
        case types.GET_ALL_NAME_FULFILLED.type: {
            const body = action.payload;
            return {
                ...state,
                names: Object.values(body),
                loadingRightsBeneficiariesNames: false,
            };
        }
        case types.GET_ALL_NAME_REJECTED.type: {
            return {
                ...state,
                loadingRightsBeneficiariesNames: false,
            };
        }
        case types.GET_ALL_NAME_PENDING.type: {
            return {
                ...state,
                loadingRightsBeneficiariesNames: true,
            };
        }
        case types.GET_ALL_FIRSTNAME_FULFILLED.type: {
            const body = action.payload;
            return {
                ...state,
                firstNames: Object.values(body),
                loadingRightsBeneficiariesFirstnames: false,
            };
        }
        case types.GET_ALL_FIRSTNAME_REJECTED.type: {
            return {
                ...state,
                loadingRightsBeneficiariesFirstnames: false,
            };
        }
        case types.GET_ALL_FIRSTNAME_PENDING.type: {
            return {
                ...state,
                loadingRightsBeneficiariesFirstnames: true,
            };
        }
        case types.GET_ALL_NIR_FULFILLED.type: {
            const body = action.payload;
            return {
                ...state,
                nirs: Object.values(body),
                loadingRightsBeneficiariesNirs: false,
            };
        }
        case types.GET_ALL_NIR_REJECTED.type: {
            return {
                ...state,
                loadingRightsBeneficiariesNirs: false,
            };
        }
        case types.GET_ALL_NIR_PENDING.type: {
            return {
                ...state,
                loadingRightsBeneficiariesNirs: true,
            };
        }
        case types.GET_ALL_LOCALITE_FULFILLED.type: {
            const body = action.payload;
            return {
                ...state,
                localites: Object.values(body),
                loadingRightsBeneficiariesLocalites: false,
            };
        }
        case types.GET_ALL_LOCALITE_REJECTED.type: {
            return {
                ...state,
                loadingRightsBeneficiariesLocalites: false,
            };
        }
        case types.GET_ALL_LOCALITE_PENDING.type: {
            return {
                ...state,
                loadingRightsBeneficiariesLocalites: true,
            };
        }
        case types.GET_ALL_VOIE_FULFILLED.type: {
            const body = action.payload;
            return {
                ...state,
                voies: Object.values(body),
                loadingRightsBeneficiariesVoies: false,
            };
        }
        case types.GET_ALL_VOIE_REJECTED.type: {
            return {
                ...state,
                loadingRightsBeneficiariesVoies: false,
            };
        }
        case types.GET_ALL_VOIE_PENDING.type: {
            return {
                ...state,
                loadingRightsBeneficiariesVoies: true,
            };
        }
        case types.GET_ALL_SOCIETES_EMETTRICES_REJECTED.type: {
            return {
                ...state,
                loadingBeneficiariesSocietesEmettrices: false,
            };
        }
        case types.GET_ALL_SOCIETES_EMETTRICES_PENDING.type: {
            return {
                ...state,
                loadingBeneficiariesSocietesEmettrices: true,
            };
        }
        case types.GET_ALL_SOCIETES_EMETTRICES_FULFILLED.type: {
            return {
                ...state,
                societesEmettrices: action.payload.body(false),
                loadingBeneficiariesSocietesEmettrices: false,
            };
        }
        case types.CHANGE_RESEARCH_CRITERIA.type: {
            return {
                ...state,
                researchCriteria: action.payload.value,
            };
        }
        default:
            return state;
    }
};
