/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import actions from '../actions';
import LastModifiedComponent from './LastModifiedComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    lastModified: state.main.home.homeReducer.lastModifiedAMC,
    loadingLastModifiedAMC: state.main.home.homeReducer.loadingLastModifiedAMC,
});

const mapDispatchToProps = {
    getLastModified: actions.getLastModified,
};

const LastModified = connect(mapStateToProps, mapDispatchToProps)(LastModifiedComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default LastModified;
