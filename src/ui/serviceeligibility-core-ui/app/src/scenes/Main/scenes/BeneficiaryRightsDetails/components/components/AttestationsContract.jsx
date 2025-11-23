/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { actions } from '../actions';
import AttestationsContractComponent from './AttestationsContractComponent';
import globalActions from '../../../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    isLoadingCertifications: state.main.beneficiaryRightsDetails.isLoadingCertifications,
    showItelisCode: state.common.configuration.values.showItelisCode,
});

const mapDispatchToProps = {
    getNextCertificates: actions.getNextCertificates,
    addAlert: globalActions.addAlert,
};

const AttestationsContract = connect(mapStateToProps, mapDispatchToProps)(AttestationsContractComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AttestationsContract;
