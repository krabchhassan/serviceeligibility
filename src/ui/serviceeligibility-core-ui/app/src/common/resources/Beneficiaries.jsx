/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import elasticSearchApi from './ElasticSearchApi';
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const beneficiaries = elasticSearchApi.all('v1/beneficiaries');
beneficiaries.beneficiaireHistory = () => Api.custom('v1/historiqueBeneficiaires');
beneficiaries.saveBeneficiaireHistory = (id) => Api.custom(`v1/historiqueBeneficiaires/${id}`);
beneficiaries.searchDetails = (idTechnique) => elasticSearchApi.custom(`v1/beneficiaries/open/${idTechnique}`);

export { beneficiaries as default };
