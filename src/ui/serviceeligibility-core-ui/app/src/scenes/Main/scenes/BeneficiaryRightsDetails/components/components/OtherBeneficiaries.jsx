/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { PanelHeader, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Beneficiary from './components/Beneficiary';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class OtherBeneficiaries extends Component {
    render() {
        const { t, beneficiaries, amc, serviceTab, environment } = this.props;

        const pageTitle = t('consolidatedContract.beneficiaries');
        const panelHeader = (
            <PanelHeader
                title={t('consolidatedContract.beneficiaryPageTitle')}
                titleClassname="col-md-6"
                contextClassname="col-md-6"
            />
        );

        const { length } = beneficiaries || [];
        return (
            <Panel id="certificate-panel" header={panelHeader} label={pageTitle}>
                <PanelSection>
                    {length > 0 &&
                        (beneficiaries || []).map((beneficiary, index) => (
                            <div key={beneficiary.numeroPersonne}>
                                <Beneficiary
                                    beneficiary={beneficiary}
                                    amc={amc}
                                    serviceTab={serviceTab}
                                    environment={environment}
                                />
                                {index !== length - 1 && (
                                    <hr
                                        style={{
                                            borderTop: '1px solid #404447',
                                            marginTop: '0px',
                                            marginBottom: '0px',
                                        }}
                                    />
                                )}
                            </div>
                        ))}
                </PanelSection>
            </Panel>
        );
    }
}

OtherBeneficiaries.propTypes = {
    t: PropTypes.func,
    beneficiaries: PropTypes.arrayOf(PropTypes.shape()),
    amc: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default OtherBeneficiaries;
