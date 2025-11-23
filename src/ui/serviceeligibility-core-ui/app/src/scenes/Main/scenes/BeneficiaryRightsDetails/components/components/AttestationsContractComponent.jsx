/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import autobind from 'autobind-decorator';
import PropTypes from 'prop-types';
import { PanelHeader, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import AttestationContract from './components/AttestationContract';
import ShowMoreButton from './ShowMoreButton';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class AttestationsContractComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            firstIndexToDisplay: 0,
            certificationToDisplay: this.props.attestations || [],
            nextCertification: [],
        };
    }

    @autobind
    setCertifications(response, newFirstIndexToDisplay) {
        const { t, addAlert } = this.props;
        const result = response?.action?.payload?.body(false);
        const newCertifications = result.certifications || [];
        const searchNext = result.searchNextCertifications || false;
        if (searchNext === false) {
            addAlert({
                message: t('beneficiaryRightsDetails.allCertificationsDisplayed'),
            });
        }

        this.setState((prevState) => ({
            certificationToDisplay: [...prevState.certificationToDisplay, ...newCertifications],
            firstIndexToDisplay: newFirstIndexToDisplay,
            nextCertification: result,
        }));
    }

    @autobind
    getNextCertifications() {
        const { getNextCertificates, idDeclarant, numeroContrat, personNumber, environment } = this.props;
        const { firstIndexToDisplay } = this.state;
        const newFirstIndexToDisplay = firstIndexToDisplay + 15;

        getNextCertificates(newFirstIndexToDisplay, idDeclarant, numeroContrat, personNumber, environment)
            .then((response) => {
                this.setCertifications(response, newFirstIndexToDisplay);
            })
            .catch((error) => {
                console.error(error);
            });

        this.setState({ firstIndexToDisplay: newFirstIndexToDisplay });
    }

    displayCertification() {
        const { conventions, searchNextCertifications, showItelisCode, isLoadingCertifications } = this.props;
        const { certificationToDisplay, nextCertification } = this.state;
        const shouldShowMoreButton = nextCertification?.searchNextCertifications ?? searchNextCertifications;

        return (
            <div>
                {(certificationToDisplay || []).map((attestation, index) => (
                    <div>
                        <AttestationContract
                            attestation={attestation}
                            conventions={conventions}
                            showItelisCode={showItelisCode}
                        />
                        {index !== certificationToDisplay.length - 1 && (
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
                <ShowMoreButton
                    isLoading={isLoadingCertifications}
                    shouldShowMoreButton={shouldShowMoreButton}
                    onClick={this.getNextCertifications}
                />
            </div>
        );
    }

    render() {
        const { t } = this.props;

        const pageTitle = t('consolidatedContract.attestations');
        const panelHeader = (
            <PanelHeader
                title={t('consolidatedContract.attestationPageTitle')}
                titleClassname="col-md-6"
                contextClassname="col-md-6"
            />
        );

        return (
            <Panel id="certificate-panel" header={panelHeader} label={pageTitle}>
                <PanelSection>{this.displayCertification()}</PanelSection>
            </Panel>
        );
    }
}

AttestationsContractComponent.propTypes = {
    t: PropTypes.func,
    attestations: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    getNextCertificates: PropTypes.func,
    searchNextCertifications: PropTypes.bool,
    isLoadingCertifications: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AttestationsContractComponent;
