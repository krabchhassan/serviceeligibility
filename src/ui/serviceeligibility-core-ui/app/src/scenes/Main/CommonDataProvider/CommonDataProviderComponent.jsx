/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { Component } from 'react';
import PropTypes from 'prop-types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

class CommonDataProviderComponent extends Component {
    componentDidMount() {
        const { getAllServices, getAllCircuits, getAllConventions, getAllServiceMetier } = this.props;
        getAllServices();
        getAllCircuits();
        getAllConventions();
        getAllServiceMetier();
    }

    render() {
        return null;
    }
}

CommonDataProviderComponent.propTypes = {
    getAllServices: PropTypes.func,
    getAllCircuits: PropTypes.func,
    getAllConventions: PropTypes.func,
    getAllServiceMetier: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CommonDataProviderComponent;
