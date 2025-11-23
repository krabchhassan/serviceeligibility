/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import SearchResultItemComponent from './SearchResultItemComponent';
import globalActions from '../../../../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    serviceMetierList: state.main.parametrageMetier.serviceMetierList,
    clientType: state.common.configuration.values.clientType,
});

const mapDispatchToProps = {
    addAlertError: globalActions.addAlertError,
};

const SearchResultItem = connect(mapStateToProps, mapDispatchToProps)(SearchResultItemComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SearchResultItem;
