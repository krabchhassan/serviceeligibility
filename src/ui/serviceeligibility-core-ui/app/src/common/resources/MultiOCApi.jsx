/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import 'whatwg-fetch';
import restful, { fetchBackend } from 'restful.js';
import ApiUtils from '../utils/ApiUtils';

const multiOCApi = restful('/multioc/core/api', fetchBackend(fetch));

ApiUtils.withAuthentication(multiOCApi);

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default multiOCApi;
