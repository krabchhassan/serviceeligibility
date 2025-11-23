/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import get from 'lodash.get';
import PropTypes from 'prop-types';
import {
    Col,
    LoadingSpinner,
    Nav,
    NavItem,
    NavLink,
    Panel,
    PanelHeader,
    Row,
    Status,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import style from './styles.module.scss';
import stringUtils from '../../../../common/utils/StringUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
function splitContractNumber(contrat) {
    const part1 = contrat.slice(0, 14);
    const part2 = contrat.slice(14);
    return stringUtils.splitIntoDivs(`${part1}_${part2}`);
}
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility'], { wait: true })
class BeneficiaryDetailsMenuComponent extends Component {
    constructor(props) {
        super(props);

        const rightsList = get(this.props, 'declarationsList', []);
        const contrats = rightsList || [];
        const filteredContract = (contrats || []).filter((contrat) => contrat.numero === this.props.numeroContrat);
        const updatedHistory = get(filteredContract, '0.historique', {});
        const hasCertifications = (this.props.attestations || {})[this.props.numeroContrat]
            ? (this.props.attestations || {})[this.props.numeroContrat].certifications.length !== 0
            : false;

        let defaultTab;
        if (
            (this.props.contractToOpen === null || this.props.discardedContract) &&
            (this.props.declarationsList?.length !== 0 || hasCertifications)
        ) {
            // Cas du bénef sans contrat consolidé
            defaultTab = hasCertifications ? 2 : 3;
        } else {
            defaultTab = 0;
        }

        this.state = {
            tab: defaultTab,
            isOpen: this.props.isExpanded,
            idsHistorique: updatedHistory.infosHistorique
                ? updatedHistory.infosHistorique
                      .map((info) => info.idHistorique)
                      .filter((idHistorique) => idHistorique !== undefined)
                : [],
        };
    }

    componentDidUpdate(prevProps) {
        if (prevProps.isExpanded !== this.props.isExpanded) {
            this.handleExpandedState(this.props.isExpanded);
        }
    }

    @autobind
    setTab(selectedTab) {
        const { selectedMenuTab } = this.props;
        this.setState({
            tab: selectedTab,
        });
        selectedMenuTab(selectedTab);
    }

    handleExpandedState(isExpanded) {
        this.setState({ isOpen: isExpanded });
    }

    @autobind
    toggleCollapse() {
        const {
            numeroContrat,
            numeroAdherent,
            contractToOpen,
            benefKey,
            getBeneficiaryTpDetailsLight,
            updateBenefTpDetails,
            attestations,
            environment,
        } = this.props;
        const { idsHistorique } = this.state;

        this.setState((prevState) => ({
            isOpen: !prevState.isOpen,
        }));

        if (numeroContrat && numeroContrat !== contractToOpen?.numeroContrat) {
            getBeneficiaryTpDetailsLight(benefKey, idsHistorique, numeroContrat, numeroAdherent, environment).then(
                (response) => {
                    const newContractToOpen = response?.action?.payload?.body(false)?.contractToOpen;
                    const discardedContract = !newContractToOpen && numeroContrat;
                    const newDeclarationDetails = response?.action?.payload?.body(false)?.declarationDetails;
                    updateBenefTpDetails(newContractToOpen, newDeclarationDetails, discardedContract, numeroContrat);

                    if (discardedContract) {
                        const attestationsOfOpenContract = this.handleData(attestations, true)?.certifications;
                        this.setTab(attestationsOfOpenContract && attestationsOfOpenContract.length !== 0 ? 2 : 3);
                    } else {
                        this.setTab(0);
                    }
                },
            );
        }
    }

    handleData(data, isHistoConsoOrCertification = false) {
        const { contractToOpen, numeroContrat } = this.props;
        if (numeroContrat != null || contractToOpen?.numeroContrat != null) {
            return contractToOpen?.numeroContrat ? data[contractToOpen.numeroContrat] : data[numeroContrat];
        }
        return isHistoConsoOrCertification ? Object.values(data).flat()[0] : Object.values(data).flat();
    }

    generateLinksData() {
        const { t, declarationsList, contractToOpen, historiqueConsolidations, attestations, otherBenefs } = this.props;
        const historiqueConsoOfOpenContract = this.handleData(historiqueConsolidations, true)?.consolidatedContracts;
        const attestationsOfOpenContract = this.handleData(attestations, true)?.certifications;
        const otherBenefsOfOpenContract = this.handleData(otherBenefs);

        const disableCertifications = !attestationsOfOpenContract || attestationsOfOpenContract.length === 0;
        const disableConsoHistory = !historiqueConsoOfOpenContract || historiqueConsoOfOpenContract.length === 0;
        const disableOtherBenefs = !otherBenefsOfOpenContract || otherBenefsOfOpenContract.length === 0;

        return [
            {
                label: t('beneficiaryDetailsMenu.contrat'),
                disabled: contractToOpen === null,
                backgroundColor: contractToOpen === null ? '#d5d5d5ff' : 'transparent',
            },
            {
                label: t('beneficiaryDetailsMenu.domainesTP'),
                disabled: contractToOpen === null,
                backgroundColor: contractToOpen === null ? '#d5d5d5ff' : 'transparent',
            },
            {
                label: t('beneficiaryDetailsMenu.attestationsTP'),
                disabled: disableCertifications,
                backgroundColor: disableCertifications ? '#d5d5d5ff' : 'transparent',
            },
            {
                label: t('beneficiaryDetailsMenu.histoDeclarations'),
                disabled: declarationsList === null,
                backgroundColor: declarationsList === null ? '#d5d5d5ff' : 'transparent',
            },
            {
                label: t('beneficiaryDetailsMenu.histoConsolidations'),
                disabled: disableConsoHistory,
                backgroundColor: disableConsoHistory ? '#d5d5d5ff' : 'transparent',
            },
            {
                label: t('beneficiaryDetailsMenu.autresBenefs'),
                disabled: disableOtherBenefs,
                backgroundColor: disableOtherBenefs ? '#d5d5d5ff' : 'transparent',
            },
        ];
    }

    renderNavLink(index, label, disabled, backgroundColor) {
        const { t, numeroContrat, declarationsList, contractToOpen, discardedContract } = this.props;
        const { tab } = this.state;
        let isActive = false;
        if (contractToOpen) {
            isActive = tab === index && (index > 1 || numeroContrat === contractToOpen?.numeroContrat);
        } else if (discardedContract) {
            isActive =
                tab === 0
                    ? index === 2 && (index > 1 || numeroContrat === discardedContract)
                    : tab === index && (index > 1 || numeroContrat === discardedContract);
        } else if (
            contractToOpen === null &&
            discardedContract === null &&
            declarationsList != null &&
            declarationsList.length !== 0
        ) {
            // Cas du bénef sans contrat consolidé
            isActive = tab === index;
        }

        return (
            <NavItem key={numeroContrat + index}>
                <NavLink
                    active={isActive}
                    onClick={() => this.setTab(index)}
                    disabled={disabled}
                    href="#"
                    className={disabled ? 'text-default lt' : 'text-default'}
                    style={{
                        backgroundColor: isActive ? '#dfeffc' : backgroundColor,
                        boxShadow: isActive ? '0 0 3px #b3cddc' : 'none',
                    }}
                >
                    <Row>
                        <Col xs={1}>
                            <CgIcon name={tab === index ? 'angle-right' : 'angle-down'} className="mr-2" />
                        </Col>
                        <Col className="pl-1">{`${t(label)} `}</Col>
                    </Row>
                </NavLink>
            </NavItem>
        );
    }

    render() {
        const { t, numeroContrat, loading, loadingBenef, status } = this.props;
        const { isOpen } = this.state;
        const title =
            numeroContrat && numeroContrat.length > 14 ? (
                <h4 className="byd-ph-title">
                    <span>{`${t('beneficiaryDetailsMenu.contrat')} `}</span>
                    {splitContractNumber(numeroContrat)}
                </h4>
            ) : (
                <h4 className="byd-ph-title">{`${t('beneficiaryDetailsMenu.contrat')} ${numeroContrat || ''}`}</h4>
            );

        const panelHeader = (
            <PanelHeader
                title={title}
                status={
                    <Status
                        label={t(`consolidatedContract.${status.status}`)}
                        behavior={status.behavior}
                        style={{ display: 'flex', textAlign: 'center' }}
                    />
                }
            />
        );

        if (loading || loadingBenef) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }

        return (
            <Panel
                header={panelHeader}
                expanded={isOpen}
                onCollapseClick={this.toggleCollapse}
                className={style.margin}
            >
                <Nav key={numeroContrat} vertical pills className="pl-0">
                    {this.generateLinksData().map((link, index) =>
                        this.renderNavLink(index, link.label, link.disabled, link.backgroundColor),
                    )}
                </Nav>
            </Panel>
        );
    }
}

BeneficiaryDetailsMenuComponent.propTypes = {
    t: PropTypes.func,
    getBeneficiaryTpDetailsLight: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiaryDetailsMenuComponent;
