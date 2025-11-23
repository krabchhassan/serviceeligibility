/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { actions } from '../actions';
import globalActions from '../../../../../../common/actions';
import HistoriqueDeclarationsComponent from './HistoriqueDeclarationsComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    showItelisCode: state.common.configuration.values.showItelisCode,
    isLoadingDeclarations: state.main.beneficiaryRightsDetails.isLoadingDeclarations,
});
const mapDispatchToProps = {
    getNextDeclarations: actions.getNextDeclarations,
    addAlert: globalActions.addAlert,
};

const HistoriqueDeclarations = connect(mapStateToProps, mapDispatchToProps)(HistoriqueDeclarationsComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueDeclarations;
