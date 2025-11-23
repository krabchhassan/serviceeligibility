/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React from 'react';
import PropTypes from 'prop-types';
import { Panel, PanelHeader } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

const noDataPresenter = ({ codeService, noDataLabel }) => (
    <Panel panelTheme="secondary" header={<PanelHeader title={codeService} context={noDataLabel} />} />
);

noDataPresenter.propTypes = {
    codeService: PropTypes.string,
    noDataLabel: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default noDataPresenter;
