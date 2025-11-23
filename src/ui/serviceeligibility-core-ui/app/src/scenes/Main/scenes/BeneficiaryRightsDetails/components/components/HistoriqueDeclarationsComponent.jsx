/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Col,
    PanelHeader,
    Panel,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import Declaration from './components/Declaration';
import ShowMoreButton from './ShowMoreButton';
import DateUtils from '../../../../../../common/utils/DateUtils';
import LabelValuePresenter from '../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class HistoriqueDeclarationsComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            declarationsToDisplay: props.declarations || [],
            firstIndexToDisplay: 0,
            nextDeclarations: [],
            countReturnedCard: props.declarations.filter((declaration) => declaration.isRestitutionCarte).length,
            countAllDeclarations: props.declarations.filter((declaration) => !declaration.isRestitutionCarte).length,
            countAllReturnedCards: props.declarations.filter((declaration) => declaration.isRestitutionCarte).length,
        };
    }

    componentDidUpdate(prevProps) {
        const { declarations } = this.props;
        // Si les declarations ont changé, on synchronise le state
        if (declarations !== prevProps.declarations) {
            this.updateDeclarationsState();
        }
    }

    @autobind
    setDeclarations(response, newFirstIndexToDisplay) {
        const { t, addAlert } = this.props;
        const result = response?.action?.payload?.body(false);
        const newDeclarations = result?.infosAssureDtos || [];
        const searchNext = result?.searchNextDeclarations || false;
        if (searchNext === false) {
            addAlert({
                message: t('beneficiaryRightsDetails.allDeclarationsDisplayed'),
            });
        }

        this.setState((prevState) => ({
            declarationsToDisplay: [...prevState.declarationsToDisplay, ...newDeclarations],
            firstIndexToDisplay: newFirstIndexToDisplay,
            nextDeclarations: result,
            countReturnedCard: result.infosAssureDtos.filter((declaration) => declaration.isRestitutionCarte).length,
            countAllDeclarations:
                prevState.declarationsToDisplay.filter((declaration) => !declaration.isRestitutionCarte).length +
                result.infosAssureDtos.filter((declaration) => !declaration.isRestitutionCarte).length,
            countAllReturnedCards:
                prevState.declarationsToDisplay.filter((declaration) => declaration.isRestitutionCarte).length +
                result.infosAssureDtos.filter((declaration) => declaration.isRestitutionCarte).length,
        }));
    }

    @autobind
    getNextDeclarations() {
        const { getNextDeclarations, amcNumber, personNumber, environment } = this.props;
        const { firstIndexToDisplay, declarationsToDisplay, countReturnedCard, countAllReturnedCards } = this.state;

        // Filtrer les déclarations pour exclure les restitutionsCarte
        const filteredDeclaration =
            declarationsToDisplay.filter((declaration) => !declaration.isRestitutionCarte).length !== 0
                ? declarationsToDisplay.filter((declaration) => !declaration.isRestitutionCarte)[0]
                : declarationsToDisplay[0];

        // Calculer le nouvel index de départ
        const newFirstIndexToDisplay = firstIndexToDisplay + 10 - countReturnedCard;

        const numeroContrat = filteredDeclaration?.contrat.numero;
        const numeroAdherent = filteredDeclaration?.contrat.numeroAdherent;

        getNextDeclarations(
            newFirstIndexToDisplay,
            countAllReturnedCards,
            amcNumber,
            numeroContrat,
            numeroAdherent,
            personNumber,
            environment,
        )
            .then((response) => {
                this.setDeclarations(response, newFirstIndexToDisplay);
            })
            .catch((error) => {
                console.error(error);
            });
    }

    updateDeclarationsState() {
        const { declarations } = this.props;

        const countReturned = declarations.filter((declaration) => declaration.isRestitutionCarte).length;
        const countDeclarations = declarations.length - countReturned;

        this.setState({
            declarationsToDisplay: declarations,
            nextDeclarations: [],
            firstIndexToDisplay: 0,
            countReturnedCard: countReturned,
            countAllReturnedCards: countReturned,
            countAllDeclarations: countDeclarations,
        });
    }

    displayDeclaration() {
        const {
            t,
            conventions,
            circuits,
            amcName,
            amcNumber,
            numerosAMCEchanges,
            searchNextDeclarations,
            showItelisCode,
            isLoadingDeclarations,
            isHTP,
        } = this.props;
        const { declarationsToDisplay, nextDeclarations } = this.state;
        const shouldShowMoreButton = nextDeclarations?.searchNextDeclarations ?? searchNextDeclarations;
        if (!declarationsToDisplay) {
            return <div>{t('consolidatedContract.noDeclaration')}</div>;
        }

        return (
            <div>
                {(declarationsToDisplay || []).map((declaration, index) => (
                    <div key={declaration.id}>
                        {declaration.isRestitutionCarte ? (
                            this.renderRestitutionCarte(declaration)
                        ) : (
                            <Declaration
                                declaration={declaration}
                                conventions={conventions}
                                circuits={circuits}
                                amcName={amcName}
                                amcNumber={amcNumber}
                                numerosAMCEchanges={numerosAMCEchanges}
                                showItelisCode={showItelisCode}
                                isHTP={isHTP}
                            />
                        )}
                        {index !== declarationsToDisplay.length - 1 && (
                            <hr
                                style={{
                                    background: '#404447',
                                    color: '#404447',
                                    borderColor: '#404447',
                                    height: '0.5px',
                                    borderTop: '1px solid #404447',
                                    marginTop: '0px',
                                    marginBottom: '0px',
                                }}
                            />
                        )}
                    </div>
                ))}
                <ShowMoreButton
                    isLoading={isLoadingDeclarations}
                    shouldShowMoreButton={shouldShowMoreButton}
                    onClick={this.getNextDeclarations}
                />
            </div>
        );
    }

    renderRestitutionCarte(declaration) {
        const { t } = this.props;
        const effetDebutRework = DateUtils.parseDate(declaration.effetDebut).format('DD/MM/YYYY HH:mm:ss');
        const dateEffet = effetDebutRework.substr(0, 10);
        const timeEffet = effetDebutRework.substr(11, 5);

        return (
            <Panel id="certificate-panel" border={false} wrapperClassName="pt-0 pb-0">
                <PanelSection className="p-0">
                    <Row className="mt-0 mb-0">
                        <Col xs={4} className="pt-1 pb-1">
                            {t('beneficiaryRightsDetails.dateReceptionRestitution', {
                                date: dateEffet,
                                time: timeEffet,
                            })}
                        </Col>
                        <Col xs={2} />
                        <Col xs={5} className="pt-1 pb-1">
                            <LabelValuePresenter
                                id="history-restitution-date"
                                label={t(`beneficiaryRightsDetails.dateRestitution`)}
                                value={declaration.dateRestitution}
                                inline
                            />
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    render() {
        const { t, toolbar } = this.props;
        const { countAllDeclarations, countAllReturnedCards } = this.state;

        const pageTitle = t('consolidatedContract.declarations');
        const declarationContext = `${countAllDeclarations} ${t('beneficiaryRightsDetails.compteurDeclaration')}`;
        const contractNumbercontext = `${countAllReturnedCards} ${t('beneficiaryRightsDetails.compteurRestitution')}`;
        const context = `${declarationContext} - ${contractNumbercontext}`;
        const panelHeader = (
            <PanelHeader
                title={t('consolidatedContract.declarationPageTitle')}
                titleClassname="col-md-4"
                contextClassname="col-md-4"
                actions={[toolbar]}
                context={context}
            />
        );

        return (
            <Panel id="certificate-panel" header={panelHeader} label={pageTitle}>
                <PanelSection>{this.displayDeclaration()}</PanelSection>
            </Panel>
        );
    }
}

HistoriqueDeclarationsComponent.propTypes = {
    t: PropTypes.func,
    declarations: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    toolbar: PropTypes.shape(),
    getNextDeclarations: PropTypes.func,
    searchNextDeclarations: PropTypes.bool,
    isLoadingDeclarations: PropTypes.bool,
    isHTP: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueDeclarationsComponent;
