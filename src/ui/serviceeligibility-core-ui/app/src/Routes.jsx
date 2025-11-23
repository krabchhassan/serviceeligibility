/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Main from './scenes/Main/Main';
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
class Routes extends Component {
    render() {
        return <Route component={Main} />;
    }
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Routes;
