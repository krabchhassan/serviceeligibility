/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ResultSearchComponent from './ResultSearchComponent';
import globalActions from '../../../../../../../common/actions';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    lots: state.main.HomeStore.lots,
    lotCreated: state.main.HomeStore.lotCreated,
    pagination: state.main.HomeStore.pagination,
    isImportLotPending: state.main.HomeStore.isImportLotPending,
});

const mapDispatchToProps = {
    addAlertError: globalActions.addAlertError,
    searchLots: actions.searchLots,
};

const ResultSearch = connect(mapStateToProps, mapDispatchToProps)(ResultSearchComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ResultSearch;
