/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ParamRejectsComponent from './ParamRejectsComponent';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    rejectsList: state.main.parametrageMetier.rejectsList,
    isLoading: state.main.parametrageMetier.isLoading,
});

const mapDispatchToProps = {
    getAllRejects: actions.getAllRejects,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamRejects = connect(mapStateToProps, mapDispatchToProps)(ParamRejectsComponent);
export default ParamRejects;
