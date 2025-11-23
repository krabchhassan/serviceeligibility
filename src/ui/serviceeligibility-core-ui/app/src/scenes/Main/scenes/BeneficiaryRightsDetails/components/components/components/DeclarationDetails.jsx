/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import fill from 'lodash/fill';
import {
    Col,
    FormGroup,
    Input,
    Label,
    LoadingSpinner,
    Panel,
    PanelSection,
    Row,
    Switch,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import businessUtils from '../../../../../../../common/utils/businessUtils';
import Trace from '../../Trace';
import Contrat from '../../Contrat';
import Beneficiaire from '../../Beneficiaire';
import DeclarationRightsItem from './DeclarationRightsItem';
import Attestations from '../../Attestations';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getKey = (index) => {
    return index + 0;
};

const fillArrayWithEmptyStrings = (array, finalLength) => {
    const numberToFill = finalLength - array.length;
    return [...array, ...fill(Array(numberToFill), '')];
};

const formalizeDomain = (domains, conventions, t) =>
    domains.map((domain) => {
        // immutable pattern
        const clonedDomain = { ...domain };

        // conventions
        clonedDomain.conventions = (domain.conventions || []).map((convention) => {
            const conventionReferentialObject = businessUtils.getConvention(conventions, convention.code);
            const libelle = conventionReferentialObject ? conventionReferentialObject.libelle : '';
            return {
                ...convention,
                libelle: libelle || '',
            };
        });

        // prestations
        const lengthArray = (domain.prestations || []).map((item) =>
            item.formule.parametres ? item.formule.parametres.length : 0,
        );
        const maxLength = Math.max(...lengthArray);

        clonedDomain.prestations = (domain.prestations || []).map((prestation) => {
            const params = prestation.formule.parametres || [];

            let paramPlat = params.map((entry) => `${entry.code} - ${entry.valeur}`);
            if (paramPlat.length === 0) {
                paramPlat[0] = t('beneficiaryRightsDetails.claim.noParameterFormula');
            } else {
                paramPlat = fillArrayWithEmptyStrings(paramPlat, maxLength);
            }

            return {
                ...prestation,
                formule: {
                    ...prestation.formule,
                    paramPlat,
                },
            };
        });

        return clonedDomain;
    });

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class DeclarationDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            domaines: [],
            tabToDisplay: 2,
            TPOnlineSelected: true,
        };
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps && prevState) {
            if (nextProps.rights && nextProps.rights.domaines !== prevState.domaines && nextProps.t) {
                return {
                    domaines: formalizeDomain(nextProps.rights.domaines, nextProps.conventions, nextProps.t),
                };
            }
            if (nextProps.rights && nextProps.rights.domaines !== prevState.domaines) {
                return { domaines: formalizeDomain(nextProps.rights.domaines, nextProps.conventions, nextProps.t) };
            }
        }

        return null;
    }

    switchOnlineOffline() {
        const { TPOnlineSelected } = this.state;
        this.setState({
            TPOnlineSelected: !TPOnlineSelected,
        });
    }

    renderSwitch() {
        const { t } = this.props;
        const { TPOnlineSelected } = this.state;

        return (
            <PanelSection border>
                <Row>
                    <Col className="d-flex">
                        <Label check for="tp" className="pr-2">
                            {t('beneficiaryRightsDetails.TPOnline')}
                        </Label>
                        <Switch
                            behaviorOff="info"
                            behaviorOn="info"
                            checked={!TPOnlineSelected}
                            onChange={() => this.switchOnlineOffline()}
                        />
                        <Label check for="htp" className="pl-2">
                            {t('beneficiaryRightsDetails.TPOffline')}
                        </Label>
                    </Col>
                </Row>
            </PanelSection>
        );
    }

    renderTabs() {
        const { t } = this.props;
        const { tabToDisplay } = this.state;

        return (
            <PanelSection>
                <Row>
                    <Col xs={2} className="pl-0">
                        <FormGroup check>
                            <Input
                                name="radio1"
                                type="radio"
                                value="zero"
                                id="zero"
                                checked={tabToDisplay === 0}
                                onChange={() => this.setState({ tabToDisplay: 0 })}
                            />
                            <Label check for="zero">
                                {t('consolidatedContract.infosContrat')}
                            </Label>
                        </FormGroup>
                    </Col>
                    <Col xs={3} className="pl-4">
                        <FormGroup check>
                            <Input
                                name="radio2"
                                type="radio"
                                value="one"
                                id="one"
                                checked={tabToDisplay === 1}
                                onChange={() => this.setState({ tabToDisplay: 1 })}
                            />
                            <Label check for="one">
                                {t('consolidatedContract.infosBenef')}
                            </Label>
                        </FormGroup>
                    </Col>
                    <Col xs={2} className="pl-0 pr-0">
                        <FormGroup check>
                            <Input
                                name="radio3"
                                type="radio"
                                value="two"
                                id="two"
                                checked={tabToDisplay === 2}
                                onChange={() => this.setState({ tabToDisplay: 2 })}
                            />
                            <Label check for="two">
                                {t('consolidatedContract.domainesDroits')}
                            </Label>
                        </FormGroup>
                    </Col>
                    <Col xs={2} className="pl-0 ml-4">
                        <FormGroup check>
                            <Input
                                name="radio4"
                                type="radio"
                                value="three"
                                id="three"
                                checked={tabToDisplay === 3}
                                onChange={() => this.setState({ tabToDisplay: 3 })}
                            />
                            <Label check for="three">
                                {t('consolidatedContract.tracabilite')}
                            </Label>
                        </FormGroup>
                    </Col>
                    <Col xs={2} className="pl-0">
                        <FormGroup check>
                            <Input
                                name="radio5"
                                type="radio"
                                value="four"
                                id="four"
                                checked={tabToDisplay === 4}
                                onChange={() => this.setState({ tabToDisplay: 4 })}
                            />
                            <Label check for="four">
                                {t('consolidatedContract.attestations')}
                            </Label>
                        </FormGroup>
                    </Col>
                </Row>
            </PanelSection>
        );
    }

    renderContractData() {
        const { contrat, conventions, amcName, amcNumber, numerosAMCEchanges, rights, isHTP } = this.props;
        return (
            <Contrat
                contrat={contrat}
                conventions={conventions}
                open
                amcName={amcName}
                amcNumber={amcNumber}
                numerosAMCEchanges={numerosAMCEchanges}
                debutDroit={rights.periodeDroitDebut}
                finDroit={
                    rights.periodeDroitFin > rights.periodeDroitOfflineFin
                        ? rights.periodeDroitFin
                        : rights.periodeDroitOfflineFin
                }
                isHTP={isHTP}
            />
        );
    }

    renderBeneficiaryData() {
        const { contrat, conventions, identification, rights } = this.props;
        const { domaines } = this.state;
        return (
            <Beneficiaire
                contrat={contrat}
                conventions={conventions}
                domaines={domaines}
                open
                identification={identification}
                debutDroit={rights.periodeDroitDebut}
                finDroit={
                    rights.periodeDroitFin > rights.periodeDroitOfflineFin
                        ? rights.periodeDroitFin
                        : rights.periodeDroitOfflineFin
                }
            />
        );
    }

    renderRightItems() {
        const { rights, isTdb } = this.props;
        const { domaines, TPOnlineSelected } = this.state;
        const { dateRestitutionCarte } = rights;
        return (domaines || []).map((item, index) => {
            return (
                <DeclarationRightsItem
                    id={`rights-${index}`}
                    key={`rights-${item.code}-${getKey(index)}`}
                    domain={item}
                    TPOnlineSelected={TPOnlineSelected}
                    dateRestitutionCarte={dateRestitutionCarte}
                    isTdb={isTdb}
                />
            );
        });
    }

    renderTracabilite() {
        const { trace, circuits } = this.props;
        return <Trace trace={trace} circuits={circuits} open />;
    }

    renderAttestations() {
        const { contrat, amcNumber, attestations, conventions, personNumber, showItelisCode } = this.props;
        return (
            <Attestations
                attestations={attestations}
                conventions={conventions}
                idDeclarant={amcNumber}
                numContrat={contrat.numero}
                personNumber={personNumber}
                showItelisCode={showItelisCode}
                open
            />
        );
    }

    render() {
        const { rights } = this.props;
        const { tabToDisplay } = this.state;
        if (!rights) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }

        return (
            <Panel id="rights-panel" border={false}>
                <PanelSection>
                    <Row>
                        <Col xs={7}>{this.renderTabs()}</Col>
                        <Col xs={3} />
                        <Col xs={2}>{tabToDisplay === 2 && this.renderSwitch()}</Col>
                    </Row>
                    {tabToDisplay === 0 && this.renderContractData()}
                    {tabToDisplay === 1 && this.renderBeneficiaryData()}
                    {tabToDisplay === 2 && this.renderRightItems()}
                    {tabToDisplay === 3 && this.renderTracabilite()}
                    {tabToDisplay === 4 && this.renderAttestations()}
                </PanelSection>
            </Panel>
        );
    }
}

DeclarationDetails.propTypes = {
    t: PropTypes.func,
    rights: PropTypes.shape(),
    attestations: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    isHTP: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DeclarationDetails;
