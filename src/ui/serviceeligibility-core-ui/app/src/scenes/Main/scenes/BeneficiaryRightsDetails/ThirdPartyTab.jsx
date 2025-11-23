/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import actions from '../ParametrageMetier/actions';
import { actions as actionsBenef } from '../BeneficiaryDetails/actions';
import globalActions from '../../../../common/actions';
import ThirdPartyTabComponent from './ThirdPartyTabComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    declarationsList: state.main.beneficiaryDetails.declarationsList,
    declarationDetails: state.main.beneficiaryDetails.declarationDetails,
    circuits: state.common.common.circuits,
    conventions: state.common.common.conventions,
    contractToOpen: state.main.beneficiaryDetails.contractToOpen,
    consolidatedContractList: state.main.beneficiaryDetails.consolidatedContractList,
    historiqueConsolidations: state.main.beneficiaryDetails.historiqueConsolidations,
    attestations: state.main.beneficiaryDetails.attestations,
    otherBenefs: state.main.beneficiaryDetails.otherBenefs,
    clientType: state.common.configuration.values.clientType,
});

const mapDispatchToProps = {
    getAllDomains: actions.getAllDomains,
    getLightDeclarants: globalActions.getLightDeclarants,
    postConsolidate: actionsBenef.postConsolidate,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ThirdPartyTab = connect(mapStateToProps, mapDispatchToProps)(ThirdPartyTabComponent);
export default ThirdPartyTab;
