/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { PanelHeader, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import Contract from './components/Contract';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Contracts extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isOpen: false,
        };
    }

    displayContract() {
        const { t, consolidatedContractList, contractToOpen, conventions, benefToDisplay } = this.props;
        if ((consolidatedContractList || []).length === 0) {
            return <div>{t('beneficiaryRightsDetails.noContract')}</div>;
        } else if ((consolidatedContractList || []).length === 1) {
            return (
                <Contract contract={contractToOpen} benefToDisplay={benefToDisplay} conventions={conventions} openTab />
            );
        }
        return (consolidatedContractList || []).map((contract) => (
            <Contract contract={contract} benefToDisplay={benefToDisplay} conventions={conventions} openTab={false} />
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

        const pageTitle = t('beneficiaryRightsDetails.contracts');
        const panelHeader = (
            <PanelHeader
                title={t('beneficiaryRightsDetails.contractPageTitle')}
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
                <PanelSection>{this.displayContract()}</PanelSection>
            </Panel>
        );
    }
}

Contracts.propTypes = {
    t: PropTypes.func,
    consolidatedContractList: PropTypes.arrayOf(PropTypes.shape()),
    contractToOpen: PropTypes.shape(),
    benefToDisplay: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Contracts;
