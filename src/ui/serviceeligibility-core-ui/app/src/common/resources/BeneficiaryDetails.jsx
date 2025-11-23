/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import ApiBeyond from './ApiBeyond';
import store from '../../store';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const configuration = store?.getState()?.common?.configuration?.values;
const useBeyondApi = configuration?.useBeyondApi;
const beneficiaryTpDetailsUrl = useBeyondApi ? 'beneficiaryTpDetails' : 'v1/beneficiaryTpDetails';
const nextConsolidatedContractTpUrl = useBeyondApi
    ? 'beneficiaryTpDetails'
    : 'v1/beneficiaryTpDetails/nextConsolidatedContratsTP';
const nextCertificationsUrl = useBeyondApi ? 'beneficiaryTpDetails' : 'v1/beneficiaryTpDetails/nextCertifications';
const nextDeclarationsUrl = useBeyondApi ? 'beneficiaryTpDetails' : 'v1/beneficiaryTpDetails/nextDeclarations';
const beneficiaryDetails = ApiBeyond().all(beneficiaryTpDetailsUrl);
beneficiaryDetails.searchTp = () => ApiBeyond().custom(beneficiaryTpDetailsUrl);
beneficiaryDetails.getNextConsolidatedContractTp = () => ApiBeyond().custom(nextConsolidatedContractTpUrl);
beneficiaryDetails.getNextCertifications = () => ApiBeyond().custom(nextCertificationsUrl);
beneficiaryDetails.getNextDeclarations = () => ApiBeyond().custom(nextDeclarationsUrl);

export { beneficiaryDetails as default };
