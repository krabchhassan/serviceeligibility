/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { getFormValues } from 'redux-form';
import VolumetrySearchFormComponent from './VolumetrySearchFormComponent';
import actions from '../../actions';
import Constants from '../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    formValues: getFormValues(Constants.FORM_NAME)(state),
    declarants: state.main.beneficiary.declarants,
});

const mapDispatchToProps = {
    changeVolumetrySearchCriteria: actions.changeVolumetrySearchCriteria,
    getAllDeclarants: actions.getAllDeclarants,
};

const VolumetrySearchForm = connect(mapStateToProps, mapDispatchToProps)(VolumetrySearchFormComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default VolumetrySearchForm;
