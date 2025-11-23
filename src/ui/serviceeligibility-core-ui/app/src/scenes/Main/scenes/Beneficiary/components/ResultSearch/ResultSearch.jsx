/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import actions from '../../../ParametrageMetier/actions';
import ResultSearchComponent from './ResultSearchComponent';
import globalActions from '../../../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    beneficiaries: state.main.beneficiary.beneficiaries,
    loadingBeneficiaries: state.main.beneficiary.loadingBeneficiaries,
    declarants: state.main.beneficiary.declarants,
    serviceMetierList: state.main.parametrageMetier.serviceMetierList,
    isLoading: state.main.parametrageMetier.isLoading,
});

const mapDispatchToProps = {
    getAllServiceMetier: actions.getAllServiceMetier,
    addAlertError: globalActions.addAlertError,
};

const ResultSearch = connect(mapStateToProps, mapDispatchToProps)(ResultSearchComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ResultSearch;
