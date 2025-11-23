/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import HomeComponent from './HomeComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    searchResults: state.main.home.homeReducer.searchResults,
    loading: state.main.home.homeReducer.loading,
    searchCriteria: state.main.home.homeReducer.searchCriteria,
});

const Home = connect(mapStateToProps)(HomeComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Home;
