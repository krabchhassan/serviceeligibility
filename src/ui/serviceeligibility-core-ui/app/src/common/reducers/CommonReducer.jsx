/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import orderBy from 'lodash/orderBy';
import EventTypes from '../EventTypes';

const sortServices = (lines) => orderBy(lines, ['nom'], ['asc']);

const enrichCircuits = (list) =>
    list.map((item) => ({ ...item, label: `${item.codeCircuit} - ${item.libelleCircuit}` }));

const enrichConventions = (list) => list.map((item) => ({ ...item, label: `${item.code} - ${item.libelle}` }));

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    services: null,
    circuits: null,
    conventions: null,
    lightDeclarants: null,
    isLoadingLightDeclarants: null,
    comboLightDeclarants: null,
    serviceMetierList: null,
    codesRenvoiList: null,
    isLoadingAllGT: false,
    isLoadingLotsById: false,
    isLoadingLots: false,
    isLoadingAllPeriod: false,
    comboLabelsAllGT: null,
    comboLabelsAllLot: null,
};

const transformToComboList = (list, order, extractProperty) =>
    orderBy(
        list.map((item) => ({ value: item[extractProperty], label: item[extractProperty] })),
        [order],
        ['asc'],
    );

const transformToComboCompleteList = (list, order, extractProperty, valueProperty) =>
    orderBy(
        list.map((item) => ({
            value: item[extractProperty],
            label: `${item[valueProperty]} - ${item[extractProperty]}`,
            number: item[valueProperty],
        })),
        [order],
        ['asc'],
    );

const addForComboCompleteList = (list, order, extractProperty, valueProperty) =>
    orderBy(
        list.map((item) => ({
            ...item,
            value: item[extractProperty],
            label: `${item[valueProperty]} - ${item[extractProperty]}`,
            number: item[valueProperty],
        })),
        [order],
        ['asc'],
    );

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
        case EventTypes.GET_ALL_SERVICES_FULFILLED.type: {
            return {
                ...state,
                services: sortServices(action.payload.body(false)),
            };
        }
        case EventTypes.GET_ALL_CIRCUITS_FULFILLED.type: {
            return {
                ...state,
                circuits: enrichCircuits(action.payload.body(false)),
            };
        }
        case EventTypes.GET_ALL_CONVENTIONS_FULFILLED.type: {
            return {
                ...state,
                conventions: enrichConventions(action.payload.body(false)),
            };
        }
        case EventTypes.GET_LIGHT_DECLARANTS_PENDING.type: {
            return {
                ...state,
                isLoadingLightDeclarants: true,
            };
        }
        case EventTypes.GET_LIGHT_DECLARANTS_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                isLoadingLightDeclarants: false,
                lightDeclarants: body,
                comboLightDeclarants: transformToComboList(body, 'value', 'numero'),
                comboLabelsLightDeclarants: transformToComboCompleteList(body, 'label', 'nom', 'numero'),
            };
        }
        case EventTypes.GET_ALL_SERVICE_METIER_FULFILLED.type: {
            return {
                ...state,
                serviceMetierList: [...action.payload.body(false)],
            };
        }
        case EventTypes.GET_ALL_CODES_RENVOI_FULFILLED.type: {
            return {
                ...state,
                codesRenvoiList: [...action.payload.body(false)],
            };
        }
        case EventTypes.GET_ALL_GT_PENDING.type: {
            return {
                ...state,
                isLoadingAllGT: true,
            };
        }
        case EventTypes.GET_ALL_GT_FULFILLED.type: {
            const body = action.payload.body(false);
            return {
                ...state,
                isLoadingAllGT: false,
                allGT: body,
                comboLabelsAllGT: transformToComboCompleteList(body, 'label', 'codeGarantie', 'codeAssureur'),
            };
        }
        case EventTypes.GET_ALL_LOT_PENDING.type: {
            return {
                ...state,
                isLoadingAllLot: true,
            };
        }
        case EventTypes.GET_ALL_LOT_FULFILLED.type: {
            const { lot } = action.payload.body(false);
            return {
                ...state,
                isLoadingAllLot: false,
                allLot: lot,
                comboLabelsAllLot: transformToComboCompleteList(lot, 'label', 'code', 'libelle'),
            };
        }
        case EventTypes.GET_LOTS_BY_ID_PENDING.type: {
            return {
                ...state,
                isLoadingLotsById: true,
            };
        }
        case EventTypes.GET_LOTS_BY_ID_FULFILLED.type: {
            const lots = action.payload.body(false);
            return {
                ...state,
                isLoadingLotsById: true,
                lotsById: lots,
                comboLabelsLots: addForComboCompleteList(lots, 'label', 'libelle', 'code'),
            };
        }
        case EventTypes.GET_LOTS_PENDING.type: {
            return {
                ...state,
                isLoadingLots: true,
            };
        }
        case EventTypes.GET_LOTS_FULFILLED.type: {
            const lots = action.payload.body(false);
            return {
                ...state,
                isLoadingLots: false,
                lots,
                comboLabelsAllLot: addForComboCompleteList(lots, 'label', 'libelle', 'code'),
            };
        }

        default:
            return state;
    }
};
