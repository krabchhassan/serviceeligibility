/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ParamFilesTypeComponent from './ParamFilesTypeComponent';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    filesTypeList: state.main.parametrageMetier.filesTypeList,
    isLoading: state.main.parametrageMetier.isLoading,
});

const mapDispatchToProps = {
    getAllFilesType: actions.getAllFilesType,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamFilesType = connect(mapStateToProps, mapDispatchToProps)(ParamFilesTypeComponent);
export default ParamFilesType;
