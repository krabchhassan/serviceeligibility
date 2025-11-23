/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import store from '../../store';

/* ************************************* */
/* ********        CODE         ******** */
/* ************************************* */

function getApiUrl() {
    const configuration = store?.getState()?.common?.configuration?.values;
    return configuration?.useBeyondApi ? configuration?.beyondApiUrl : '/serviceeligibility/core/api';
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ConfUtils = {
    getApiUrl,
};

export default ConfUtils;
