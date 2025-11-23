/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React from 'react';
import PropTypes from 'prop-types';
import { CollapseCard, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
// Prop types
const propTypes = {
    error: PropTypes.instanceOf(Error).isRequired,
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
function LoadingComponentErrorComponent({ t, error }) {
    return (
        <div>
            <h1 className="text-danger.dk">{t('loadingComponent.title')}</h1>
            <p>{t('loadingComponent.message')}</p>
            <CollapseCard behavior="danger" header={t('loadingComponent.card.title')} chevron>
                <div className="font-weight-bold">{t('loadingComponent.card.message')}</div>
                <div>{error.message}</div>
                <div className="font-weight-bold">{t('loadingComponent.card.stack')}</div>
                <div>{error.stack}</div>
            </CollapseCard>
        </div>
    );
}

// Add prop types
LoadingComponentErrorComponent.propTypes = propTypes;
// Add default props
LoadingComponentErrorComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TranslationsSubscriber(['errors'], { wait: true })(LoadingComponentErrorComponent);
