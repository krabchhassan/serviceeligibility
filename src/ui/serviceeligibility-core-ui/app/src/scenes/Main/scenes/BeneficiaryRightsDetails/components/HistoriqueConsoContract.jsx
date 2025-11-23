/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import HistoriqueConsoContractComponent from './HistoriqueConsoContractComponent';
import { actions } from './actions';
import globalActions from '../../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    isLoadingHistoryContractTp: state.main.beneficiaryRightsDetails.isLoadingHistoryContractTp,
});
const mapDispatchToProps = {
    getNextConsolidatedContractTp: actions.getNextConsolidatedContractTp,
    addAlert: globalActions.addAlert,
};

const HistoriqueConsoContract = connect(mapStateToProps, mapDispatchToProps)(HistoriqueConsoContractComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueConsoContract;
