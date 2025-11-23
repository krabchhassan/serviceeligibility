/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import 'whatwg-fetch';
import restful, { fetchBackend } from 'restful.js';
import ApiUtils from '../utils/ApiUtils';

const servicePrestIJApi = restful('/serviceeligibility/prestij/worker', fetchBackend(fetch));

ApiUtils.withAuthentication(servicePrestIJApi);

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default servicePrestIJApi;
