/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import 'whatwg-fetch';
import restful, { fetchBackend } from 'restful.js';
import ApiUtils from '../utils/ApiUtils';

// const elasticSearchApi = restful(
//     'https://dev-evol2.dev.beyond.cegedim.cloud/serviceeligibility/core/api',
//     fetchBackend(fetch),
// );
// const elasticSearchApi = restful('http://localhost:8080', fetchBackend(fetch));
const elasticSearchApi = restful('/serviceeligibility/core/api', fetchBackend(fetch));
ApiUtils.withAuthentication(elasticSearchApi);

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default elasticSearchApi;
