/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import types from './types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */
function sendApiRequest(url, method, body, headers, onEventSuccess, onEventError) {
    return {
        ...types.SEND_API_REQUEST,
        payload: {
            url,
            method,
            body,
            headers,
            onEventSuccess,
            onEventError,
        },
    };
}

function createAction(type, payload) {
    return {
        type,
        payload,
    };
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default {
    sendApiRequest,
    createAction,
};
