/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { PanelHeader, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import Attestation from './components/Attestation';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Attestations extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isOpen: !!props.open,
        };
    }

    displayCertification() {
        const { t, attestations, conventions, showItelisCode } = this.props;
        if ((attestations || []).length === 0) {
            return <div>{t('beneficiaryRightsDetails.noAttestation')}</div>;
        } else if ((attestations || []).length === 1) {
            return (
                <Attestation
                    attestation={attestations[0]}
                    conventions={conventions}
                    showItelisCode={showItelisCode}
                    openTab
                />
            );
        }
        return (attestations || []).map((attestation) => (
            <Attestation
                attestation={attestation}
                conventions={conventions}
                showItelisCode={showItelisCode}
                openTab={false}
            />
        ));
    }

    @autobind
    toggleCollapse() {
        const { isOpen } = this.state;
        this.setState({ isOpen: !isOpen });
    }

    render() {
        const { isOpen } = this.state;
        const { t } = this.props;

        const pageTitle = t('beneficiaryRightsDetails.attestations');
        const panelHeader = (
            <PanelHeader
                title={t('beneficiaryRightsDetails.attestationPageTitle')}
                titleClassname="col-md-6"
                contextClassname="col-md-6"
            />
        );

        return (
            <Panel
                id="certificate-panel"
                header={panelHeader}
                label={pageTitle}
                expanded={isOpen}
                onCollapseClick={this.toggleCollapse}
            >
                <PanelSection>{this.displayCertification()}</PanelSection>
            </Panel>
        );
    }
}

Attestations.propTypes = {
    t: PropTypes.func,
    open: PropTypes.bool,
    attestations: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Attestations;
