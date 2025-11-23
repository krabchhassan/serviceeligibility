/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { i18n } from '@beyond-framework/common-uitoolkit-beyond';
import types from './types';
import actions from './actions';
import commonAction from '../../../../../common/actions';
import coreAction from '../../../../../middleware/core/actions';
import ContractElement from '../../../../../common/resources/ContractElement';
import Lot from '../../../../../common/resources/Lot';

/* **************************************** */

/* ********        MIDDLEWARE      ******** */
function processImportSuccess(action, store) {
    const response = action.payload;
    if (response.status < 200 || response.status >= 400) {
        if (response.status === 415) {
            response.json().then((data) => {
                const error = (data.errors[0] || {}).message;
                const alertObj = { message: error, headline: i18n.t('error:importWrongFormat') };
                store.dispatch(commonAction.addAlertError(alertObj));
            });
        } else {
            store.dispatch(commonAction.displayAlert(i18n.t('error:importOnError'), 'danger'));
        }
    } else {
        store.dispatch(commonAction.displayAlert(i18n.t('bobb:import.success'), 'success'));
    }
}

/* **************************************** */
const handleImportFlow = (store) => (next) => (action) => {
    next(action);

    if (action.type === types.START_IMPORT.type) {
        const file = action.payload;
        const formData = new FormData();
        formData.append('file', file);
        store.dispatch(actions.toggleImportPending());
        store.dispatch(
            coreAction.sendApiRequest(
                ContractElement.import().url(),
                'post',
                formData,
                {},
                types.IMPORT_SUCCESS.type,
                types.IMPORT_FAILED.type,
            ),
        );
    }

    if (action.type === types.IMPORT_SUCCESS.type) {
        store.dispatch(actions.toggleImportPending());
        processImportSuccess(action, store);
    }

    if (action.type === types.IMPORT_FAILED.type) {
        store.dispatch(actions.toggleImportPending());
        store.dispatch(commonAction.displayAlert(i18n.t('error:importOnError'), 'danger'));
    }
    if (action.type === types.START_IMPORT_LOT.type) {
        const file = action.payload;
        const formData = new FormData();
        formData.append('file', file);
        store.dispatch(actions.toggleImportLotPending());
        store.dispatch(
            coreAction.sendApiRequest(
                Lot.import().url(),
                'post',
                formData,
                {},
                types.IMPORT_LOT_SUCCESS.type,
                types.IMPORT_LOT_FAILED.type,
            ),
        );
    }

    if (action.type === types.IMPORT_LOT_SUCCESS.type) {
        store.dispatch(actions.toggleImportLotPending());
        processImportSuccess(action, store);
    }

    if (action.type === types.IMPORT_LOT_FAILED.type) {
        store.dispatch(actions.toggleImportLotPending());
        store.dispatch(commonAction.displayAlert(i18n.t('error:importOnError'), 'danger'));
    }
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default [handleImportFlow];
