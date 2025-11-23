/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import 'whatwg-fetch';
import restful, { fetchBackend } from 'restful.js';
import ApiUtils from '../utils/ApiUtils';

const referentialApi = restful('/referential/core/api', fetchBackend(fetch));
// const referentialApi = restful('https://htp-es14.dev.beyond.cegedim.cloud/referential/core/api', fetchBackend(fetch));

ApiUtils.withAuthentication(referentialApi);

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default referentialApi;
