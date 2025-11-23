/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ParamProcessComponent from './ParamProcessComponent';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    processList: state.main.parametrageMetier.processList,
    isLoading: state.main.parametrageMetier.isLoading,
});

const mapDispatchToProps = {
    getAllProcess: actions.getAllProcess,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamProcess = connect(mapStateToProps, mapDispatchToProps)(ParamProcessComponent);
export default ParamProcess;
