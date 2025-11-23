/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import 'whatwg-fetch';
import restful, { fetchBackend } from 'restful.js';
import ApiUtils from '../utils/ApiUtils';

/* ************************************* */
/* ********        CODE         ******** */
/* ************************************* */
const Api = restful('/serviceeligibility/core/api', fetchBackend(fetch));

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
ApiUtils.withAuthentication(Api);
ApiUtils.withLanguageHeader(Api);

export default Api;
