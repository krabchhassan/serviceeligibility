/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import SearchPanelComponent from './SearchPanelComponent';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapDispatchToProps = {
    searchLots: actions.searchLots,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const SearchPanel = connect(null, mapDispatchToProps)(SearchPanelComponent);
export default SearchPanel;
