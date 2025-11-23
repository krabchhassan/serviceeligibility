/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import type from './types';

/* ************************************* */
/* ********      PRIVATE        ******** */
/* ************************************* */

const getConfiguration = () => fetch('./configuration').then((response) => response.json());

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */
const init = (store) =>
    store.dispatch({
        type: type.INIT_CONFIGURATION.type,
        payload: getConfiguration(),
    });

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export default { init };
