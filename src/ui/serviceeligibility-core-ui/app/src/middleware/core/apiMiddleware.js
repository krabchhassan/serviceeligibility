/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { AuthProvider } from '@beyond-framework/common-uitoolkit-beyond';
import types from './types';
import actions from './actions';

/* **************************************** */
/* ********        MIDDLEWARE      ******** */
/* **************************************** */
const requestApi = (store) => (next) => (action) => {
    next(action);

    if (action.type === types.SEND_API_REQUEST.type) {
        const { url, method, body, headers, onEventSuccess, onEventError } = action.payload;
        const requestPayload = {
            method,
            body,
            headers,
        };

        if (AuthProvider.get()) {
            requestPayload.headers.Authorization = `Bearer ${AuthProvider.get().token}`;
            requestPayload.headers['X-CGD-TENANT'] = AuthProvider.get().realm;
        }
        fetch(url, requestPayload)
            .then((response) => store.dispatch(actions.createAction(onEventSuccess, response)))
            .catch((response) => store.dispatch(actions.createAction(onEventError, response)));
    }
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default [requestApi];
