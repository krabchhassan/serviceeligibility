/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React from 'react';
import { LoadingSpinner } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const CommonSpinner = () => (
    <LoadingSpinner type="over-container" iconSize="3x" iconType="circle-o-notch" behavior="primary" />
);

CommonSpinner.propTypes = {};
CommonSpinner.defaultProps = {};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CommonSpinner;
