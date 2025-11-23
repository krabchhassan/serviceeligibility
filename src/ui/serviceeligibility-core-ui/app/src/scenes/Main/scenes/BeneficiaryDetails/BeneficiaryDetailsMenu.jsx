/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import BeneficiaryDetailsMenuComponent from './BeneficiaryDetailsMenuComponent';
import { actions as benefActions } from '../BeneficiaryDetails/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapDispatchToProps = {
    getBeneficiaryTpDetailsLight: benefActions.getBeneficiaryTpDetailsLight,
};

const BeneficiaryDetailsMenu = connect(null, mapDispatchToProps)(BeneficiaryDetailsMenuComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiaryDetailsMenu;
