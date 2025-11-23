/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const initialState = {
    isImportLotPending: false,
    isLoading: false,
    lots: [],
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
const caseImportLotsPending = (state) => ({
    ...state,
    isImportLotPending: !state.isImportLotPending,
});

const caseLotsFulfilled = (state, action) => {
    const { lot } = action.payload.body(false);
    const { paging } = action.payload.body(false);
    return {
        ...state,
        lots: lot,
        pagination: paging,
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
        [types.TOGGLE_IMPORT_LOT_PENDING.type]: () => caseImportLotsPending(state),
        [types.SEARCH_LOTS_FULFILLED.type]: () => caseLotsFulfilled(state, action),
    };
    const returnFunc = reducerMapper[action.type];
    if (returnFunc !== undefined) {
        return returnFunc();
    }
    return state;
}
