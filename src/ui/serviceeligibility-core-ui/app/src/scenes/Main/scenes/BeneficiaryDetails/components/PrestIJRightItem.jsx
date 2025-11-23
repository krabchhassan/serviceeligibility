/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Col,
    Status,
    Button,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import autobind from 'autobind-decorator';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import businessUtils from '../../../../../common/utils/businessUtils';
import DateUtils from '../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const goToPrestIJ = (nirCode) => {
    window.open(`/prestij/core/ui/#/registeredPeople?nir=${nirCode}`, '_blank');
};
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class PrestIJRightItem extends Component {
    constructor(props) {
        super(props);

        this.state = {
            expanded: this.props.isOpen,
        };
    }

    @autobind
    toggleCollapse() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    @autobind
    render() {
        const { t, prestIJDetails, droit, nirCode } = this.props;
        const { expanded } = this.state;
        const isOpen = DateUtils.betweenData(droit.dateDebut, droit.dateFin);
        const contractIcon = prestIJDetails.contrat.isContratIndividuel ? 'user-o' : 'contacts';
        const contratIndivOuCollLabel = prestIJDetails.contrat.isContratIndividuel
            ? t('beneficiaryDetails.contratIndividuel')
            : t('beneficiaryDetails.contratCollectif');
        const contractType = businessUtils.getContractType(droit.type);
        const title = `${t('beneficiaryDetails.contrat')} : ${prestIJDetails.contrat.numero}`;
        const context = (
            <Fragment>
                <CgIcon name={contractIcon} size="1x" />
                <span className="pr-1 pl-1">{contratIndivOuCollLabel}</span>
                <span>
                    {' - '}
                    {t('beneficiaryDetails.typeContrat')} : {contractType}
                </span>
            </Fragment>
        );
        const subContext = (
            <Button behavior="link" onClick={() => goToPrestIJ(nirCode)}>
                {t('beneficiaryDetails.suiviPrestIJ')}
            </Button>
        );

        return (
            <Panel
                onCollapseClick={this.toggleCollapse}
                expanded={expanded}
                header={
                    <PanelHeader
                        title={title}
                        context={context}
                        status={
                            <Status
                                behavior={isOpen ? 'success' : 'warning'}
                                label={
                                    isOpen ? t('beneficiaryDetails.prestijOpen') : t('beneficiaryDetails.prestijClosed')
                                }
                            />
                        }
                    />
                }
            >
                <PanelSection>
                    <Row>
                        <Col>
                            <Panel
                                panelTheme="primary"
                                border
                                header={
                                    <PanelHeader
                                        title={t('beneficiaryDetails.inscriptionService')}
                                        context={subContext}
                                    />
                                }
                            >
                                <PanelSection>
                                    <Row>
                                        <Col xs="12" md="4">
                                            <LabelValuePresenter
                                                id="dateDebut"
                                                label={t('beneficiaryDetails.dateDebut')}
                                                value={DateUtils.transformDateForDisplay(droit.dateDebut)}
                                                inline
                                            />
                                        </Col>
                                        <Col xs="12" md="4">
                                            <LabelValuePresenter
                                                id="dateFin"
                                                label={t('beneficiaryDetails.dateFin')}
                                                value={DateUtils.transformDateForDisplay(droit.dateFin)}
                                                inline
                                            />
                                        </Col>
                                        <Col xs="12" md="4">
                                            <LabelValuePresenter
                                                id="entreprise"
                                                label={t('beneficiaryDetails.entreprise')}
                                                value={prestIJDetails.entreprise.raisonSociale}
                                                inline
                                            />
                                        </Col>
                                    </Row>
                                </PanelSection>
                            </Panel>
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }
}
PrestIJRightItem.propTypes = {
    t: PropTypes.func,
    prestIJDetails: PropTypes.shape(),
    droit: PropTypes.shape(),
    nirCode: PropTypes.string,
    isOpen: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default PrestIJRightItem;
