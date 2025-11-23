/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import BeneficiaryRightsDetailsComponent from './BeneficiaryRightsDetailsComponent';
import globalActions from '../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapDispatchToProps = {
    addAlert: globalActions.addAlert,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const BeneficiaryRightsDetails = connect(null, mapDispatchToProps)(BeneficiaryRightsDetailsComponent);
export default BeneficiaryRightsDetails;
