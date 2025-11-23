/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import get from 'lodash.get';
import ValiditesFieldArrayComponent from './ValiditesFieldArrayComponent';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    formValues: get(state, `form.${Constants.FORM_NAME}.values`, {}),
});

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ValiditesFieldArray = connect(mapStateToProps, null)(ValiditesFieldArrayComponent);

export default ValiditesFieldArray;
