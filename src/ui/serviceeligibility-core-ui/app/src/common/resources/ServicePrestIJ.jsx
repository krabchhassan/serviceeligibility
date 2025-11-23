/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import servicePrestIJApi from './ServicePrestIJApi';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const servicePrestIJs = servicePrestIJApi.all('v1/prestijs');

servicePrestIJs.search = () => servicePrestIJs.custom('search');

export { servicePrestIJs as default };
