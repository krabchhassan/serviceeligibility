/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamClaimComponent from './ParamClaimComponent';
import globalActions from '../../../../../../common/actions';
import actions from '../../actions';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formSelector = formValueSelector(Constants.FORM_NAME);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    claimList: state.main.parametrageMetier.claimList,
    isLoading: state.main.parametrageMetier.isLoading,
    claimCode: formSelector(state, Constants.FIELDS.claimCode) || '',
    claimName: formSelector(state, Constants.FIELDS.claimName) || '',
    claimDomains: formSelector(state, Constants.FIELDS.claimDomains) || '',
    domainList: state.main.parametrageMetier.domainList,
});

const mapDispatchToProps = {
    getAllClaims: actions.getAllClaims,
    createClaim: actions.createClaim,
    updateClaim: actions.updateClaim,
    deleteClaim: actions.deleteClaim,
    addAlert: globalActions.addAlertError,
    getAllDomains: actions.getAllDomains,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamClaim = connect(mapStateToProps, mapDispatchToProps)(ParamClaimComponent);
export default ParamClaim;
