/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParametrageContratComponent from './ParametrageContratComponent';
import actions from './actions';
import globalActions from '../../../../common/actions';
import benefActions from '../Beneficiary/actions';
import parametrageMetierActions from '../ParametrageMetier/actions';
import parametrageDroitsActions from '../ParametrageDroits/components/Create/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const formSelector = formValueSelector('paramContratSearch');

const mapStateToProps = (state) => ({
    amcs: formSelector(state, 'amcs'),
    lightDeclarants: state.common.common.lightDeclarants,
    isLoadingLightDeclarants: state.common.common.isLoadingLightDeclarants,
    loading: state.main.paramContrat.loading,
    contratIndiv: state.main.paramContrat.contratIndiv,
    errorCode: state.main.paramContrat.errorCode,
    beneficiaries: state.main.beneficiary.beneficiaries,
    loadingBeneficiaries: state.main.beneficiary.loadingBeneficiaries,
    comboLabelsLots: state.common.common.comboLabelsLots,
    returnCodesList: state.main.parametrageMetier.returnCodesList,
    amc: state.main.paramContrat.amc,
    codesItelis: state.main.paramDroitsTP.codesItelis,
    showItelisCode: state.common.configuration.values.showItelisCode,
});

const mapDispatchToProps = {
    getLightDeclarants: globalActions.getLightDeclarants,
    getContratIndiv: actions.getContratIndiv,
    getBeneficiaries: benefActions.getBeneficiaries,
    generateParametrageContrat: actions.generateParametrageContrat,
    addAlertError: globalActions.addAlertError,
    addAlertSuccess: globalActions.addAlert,
    getLotsById: globalActions.getLotsById,
    getAllReturnCodes: parametrageMetierActions.getAllReturnCodes,
    getDeclarant: actions.getDeclarant,
    getCodesItelis: parametrageDroitsActions.getCodesItelis,
};

const ParametrageContrat = connect(mapStateToProps, mapDispatchToProps)(ParametrageContratComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageContrat;
