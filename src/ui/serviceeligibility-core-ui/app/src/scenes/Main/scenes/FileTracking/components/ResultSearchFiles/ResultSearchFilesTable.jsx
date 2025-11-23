/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ResultSearchFilesTableComponent from './ResultSearchFilesTableComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    circuits: state.common.common.circuits,
});

const ResultSearchFilesTable = connect(mapStateToProps, null)(ResultSearchFilesTableComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ResultSearchFilesTable;
