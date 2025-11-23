/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamAgreementComponent from './ParamAgreementComponent';
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
    agreementList: state.main.parametrageMetier.agreementList,
    isLoading: state.main.parametrageMetier.isLoading,
    agreementCode: formSelector(state, Constants.FIELDS.agreementCode) || '',
    agreementName: formSelector(state, Constants.FIELDS.agreementName) || '',
});

const mapDispatchToProps = {
    getAllAgreement: actions.getAllAgreement,
    createAgreement: actions.createAgreement,
    updateAgreement: actions.updateAgreement,
    deleteAgreement: actions.deleteAgreement,
    addAlert: globalActions.addAlertError,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamAgreement = connect(mapStateToProps, mapDispatchToProps)(ParamAgreementComponent);
export default ParamAgreement;
