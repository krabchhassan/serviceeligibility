/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import autobind from 'autobind-decorator';
import PropTypes from 'prop-types';
import {
    LoadingSpinner,
    Panel,
    PanelHeader,
    PanelSection,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import ConsolidationContract from './components/ConsolidationContract';
import ShowMoreButton from './components/ShowMoreButton';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getKey = (index) => {
    return index + 0;
};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class HistoriqueConsoContractComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            historiqueConsolidationsToDisplay: props.historiqueConsolidations || [],
            firstIndexToDisplay: 0,
            nextHistoryConsoContractTp: [],
        };
    }

    @autobind
    setHistoriqueConso(response, newFirstIndexToDisplay) {
        const { t, addAlert } = this.props;
        const result = response?.action?.payload?.body(false);
        const newConsolidations = result?.consolidatedContracts || [];
        const searchNext = result?.searchNextConsoContractTp || false;
        if (searchNext === false) {
            addAlert({
                message: t('beneficiaryRightsDetails.historiqueConsolidations.allConsolidatedContractTpDisplayed'),
            });
        }

        this.setState((prevState) => ({
            historiqueConsolidationsToDisplay: [...prevState.historiqueConsolidationsToDisplay, ...newConsolidations],
            firstIndexToDisplay: newFirstIndexToDisplay,
            nextHistoryConsoContractTp: result,
        }));
    }

    @autobind
    getNextConsolidatedContractTp() {
        const { getNextConsolidatedContractTp, numeroPersonne, environment } = this.props;
        const { firstIndexToDisplay, historiqueConsolidationsToDisplay } = this.state;
        const newFirstIndexToDisplay = firstIndexToDisplay + 5;
        const { numeroContrat, idDeclarant, numeroAdherent } = historiqueConsolidationsToDisplay[0];

        getNextConsolidatedContractTp(
            newFirstIndexToDisplay,
            idDeclarant,
            numeroContrat,
            numeroAdherent,
            numeroPersonne,
            environment,
        )
            .then((response) => {
                this.setHistoriqueConso(response, newFirstIndexToDisplay);
            })
            .catch((error) => {
                console.error(error);
            });
    }

    displayHistoContractTP() {
        const { conventions, numeroPersonne } = this.props;
        const { historiqueConsolidationsToDisplay } = this.state;

        return (historiqueConsolidationsToDisplay || []).map((consolidation, index) => (
            <div key={consolidation.id || index}>
                <ConsolidationContract
                    id={`contratConsolide-${index}`}
                    key={`contratConsolide-${getKey(index)}`}
                    consolidation={consolidation}
                    conventions={conventions}
                    numeroPersonne={numeroPersonne}
                />
                {index !== historiqueConsolidationsToDisplay.length - 1 && (
                    <hr
                        style={{
                            borderTop: '1px solid #404447',
                            marginTop: '0px',
                            marginBottom: '0px',
                        }}
                    />
                )}
            </div>
        ));
    }

    render() {
        const { t, toolbar, searchNextConsoContractTp, isLoadingHistoryContractTp } = this.props;
        const { historiqueConsolidationsToDisplay, nextHistoryConsoContractTp } = this.state;
        const shouldShowMoreButton = nextHistoryConsoContractTp?.searchNextConsoContractTp ?? searchNextConsoContractTp;
        if (!historiqueConsolidationsToDisplay) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }

        const panelHeader = (
            <PanelHeader title={t('beneficiaryRightsDetails.historiqueConsolidations.title')} actions={[toolbar]} />
        );

        return (
            <Panel id="rights-panel" header={panelHeader}>
                <PanelSection>
                    {this.displayHistoContractTP()}
                    <ShowMoreButton
                        isLoading={isLoadingHistoryContractTp}
                        shouldShowMoreButton={shouldShowMoreButton}
                        onClick={this.getNextConsolidatedContractTp}
                    />
                </PanelSection>
            </Panel>
        );
    }
}

HistoriqueConsoContractComponent.propTypes = {
    t: PropTypes.func,
    historiqueConsolidations: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    toolbar: PropTypes.shape(),
    getNextConsolidatedContractTp: PropTypes.func,
    searchNextConsoContractTp: PropTypes.bool,
    numeroPersonne: PropTypes.string,
    environment: PropTypes.string,
    addAlert: PropTypes.func,
    isLoadingHistoryContractTp: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueConsoContractComponent;
