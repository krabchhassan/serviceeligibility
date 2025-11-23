/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import SearchFormComponent from './SearchFormComponent';
import actions from '../../actions';
import formConstants from '../../Constants';
import globalActions from '../../../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const formSelector = formValueSelector('homeSearchForm');

const mapStateToProps = (state) => ({
    amcNumber: formSelector(state, formConstants.FIELDS.amcNumber),
    amcLabel: formSelector(state, formConstants.FIELDS.amcLabel),
    couloir: formSelector(state, formConstants.FIELDS.couloir),
    service: formSelector(state, formConstants.FIELDS.service),
    services: state.common.common.services,
    comboLightDeclarants: state.common.common.comboLightDeclarants,
    comboLabelsLightDeclarants: state.common.common.comboLabelsLightDeclarants,
    isLoadingLightDeclarants: state.common.common.isLoadingLightDeclarants,
});

const mapDispatchToProps = {
    searchDeclarant: actions.searchDeclarant,
    changeSarchCriteria: actions.changeSarchCriteria,
    getLightDeclarants: globalActions.getLightDeclarants,
};

const SearchForm = connect(mapStateToProps, mapDispatchToProps)(SearchFormComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SearchForm;
