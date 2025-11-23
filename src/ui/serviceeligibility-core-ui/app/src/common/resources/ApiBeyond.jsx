/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import 'whatwg-fetch';
import restful, { fetchBackend } from 'restful.js';
import ApiUtils from '../utils/ApiUtils';
import ConfUtils from '../../common/utils/ConfUtils';

/* ************************************* */
/* ********        CODE         ******** */
/* ************************************* */
const ApiBeyond = () => {
    const url = ConfUtils.getApiUrl();
    const apiInstance = restful(url, fetchBackend(fetch));
    ApiUtils.withAuthentication(apiInstance);
    ApiUtils.withLanguageHeader(apiInstance);
    return apiInstance;
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export default ApiBeyond;
