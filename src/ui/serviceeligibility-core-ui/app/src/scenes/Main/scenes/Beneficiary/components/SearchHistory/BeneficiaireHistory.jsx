/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import BeneficiaireHistoryComponent from './BeneficiaireHistoryComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    beneficiaries: state.main.beneficiary.beneficiaries,
    serviceMetierList: state.common.common.serviceMetierList,
    loadingBeneficiaries: state.main.beneficiary.loadingBeneficiaries,
    declarants: state.main.beneficiary.declarants,
});

const BeneficiaireHistory = connect(mapStateToProps, null)(BeneficiaireHistoryComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiaireHistory;
