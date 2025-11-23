/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import multiOCApi from './MultiOCApi';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const societeEmettrice = multiOCApi.all('v1/mainOrganizations');

export { societeEmettrice as default };
