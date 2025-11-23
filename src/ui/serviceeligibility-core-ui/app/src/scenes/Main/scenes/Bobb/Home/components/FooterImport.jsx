/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, PanelFooter, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********       VARIABLES       ******** */
/* ************************************* */
const propTypes = {
    onCancel: PropTypes.func,
    disabled: PropTypes.bool,
    onImport: PropTypes.func,
    t: PropTypes.func,
};

const defaultProps = {};

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['common'], { wait: true })
class FooterImport extends Component {
    render() {
        const { t, onCancel, disabled, onImport } = this.props;
        return (
            <PanelFooter className="mt-4">
                <div className="d-flex justify-content-between w-100">
                    <Button behavior="secondary" type="button" onClick={onCancel}>
                        {t('action.cancel')}
                    </Button>
                    <Button behavior="primary" type="button" disabled={disabled} onClick={onImport}>
                        {t('action.import')}
                    </Button>
                </div>
            </PanelFooter>
        );
    }
}

FooterImport.propTypes = propTypes;
FooterImport.propTypes = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default FooterImport;
