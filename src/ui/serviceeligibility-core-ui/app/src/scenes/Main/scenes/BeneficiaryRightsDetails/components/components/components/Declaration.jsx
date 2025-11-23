/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Panel,
    PanelSection,
    Row,
    Col,
    Button,
    Modal,
    ModalHeader,
    ModalBody,
    Status,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import CommonSpinner from '../../../../../../../common/components/CommonSpinner/CommonSpinner';
import DateUtils from '../../../../../../../common/utils/DateUtils';
import DeclarationDetails from './DeclarationDetails';
import styleCss from './style.module.scss';
import DateRangePresenter from '../../../../../../../common/utils/DateRangePresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Declaration extends Component {
    constructor(props) {
        super(props);

        this.state = {
            modalDeclaration: false,
            declarationToOpen: null,
        };
    }

    toggleDeclaration(declarationToOpen) {
        const { modalDeclaration } = this.state;
        this.setState({ modalDeclaration: !modalDeclaration, declarationToOpen });
    }

    renderModalTitle(dateEffet, timeEffet) {
        const { t, declaration } = this.props;
        const { droits } = declaration;

        const title = t('consolidatedContract.dateReceptionRecu', {
            date: dateEffet,
            time: timeEffet,
        });

        let status = <Status label={t('consolidatedContract.closedRights')} behavior="default" />;

        if (droits.isDroitOuvert) {
            status = <Status label={t('consolidatedContract.openedRights')} behavior="success" />;
        }

        return (
            <Row>
                <Col xs={4} className="pr-0">{`${title}`}</Col>
                <Col xs={2} className="pl-3 pr-0">
                    {status}
                </Col>
                <Col xs={6} className="pl-4 pr-0">
                    <div className="d-flex">
                        <span className="cgd-comment">{`${t('beneficiaryRightsDetails.periodeDroits')} :`}</span>
                        &nbsp;
                        <DateRangePresenter
                            start={droits.periodeDroitDebut}
                            end={`${droits.periodeDroitFin} (${droits.periodeDroitOfflineFin})`}
                        />
                    </div>
                </Col>
            </Row>
        );
    }

    render() {
        const { modalDeclaration, declarationToOpen, TPOnlineSelected } = this.state;
        const {
            conventions,
            declaration,
            t,
            circuits,
            amcName,
            amcNumber,
            numerosAMCEchanges,
            personNumber,
            showItelisCode,
            isHTP,
        } = this.props;
        const canRender = declaration && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const { contrat, droits, isTdb, attestations, trace, identification } = declaration;
        const effetDebutRework = DateUtils.parseDate(declaration.effetDebut).format('DD/MM/YYYY HH:mm:ss');
        const dateEffet = effetDebutRework.substr(0, 10);
        const timeEffet = effetDebutRework.substr(11, 5);

        return (
            <Panel id="certificate-panel" border={false} wrapperClassName="pt-0 pb-0">
                <PanelSection className="p-0">
                    <Row className="mt-0 mb-0">
                        <Col xs={4} className="pt-1">
                            {t('consolidatedContract.dateReceptionRecu', {
                                date: dateEffet,
                                time: timeEffet,
                            })}
                        </Col>
                        <Col xs={2} className="pt-1">
                            <CgIcon
                                name="state"
                                className={`text-${droits.isDroitOuvert ? 'success' : 'danger'} pr-1`}
                            />
                            {droits.isDroitOuvert
                                ? t('consolidatedContract.openedRights')
                                : t('consolidatedContract.closedRights')}
                        </Col>
                        <Col xs={5} className="pt-1">
                            <div className="d-flex">
                                <span className="cgd-comment">{`${t(
                                    'beneficiaryRightsDetails.periodeDroits',
                                )} :`}</span>
                                &nbsp;
                                <DateRangePresenter
                                    start={droits.periodeDroitDebut}
                                    end={`${droits.periodeDroitFin} (${droits.periodeDroitOfflineFin})`}
                                />
                            </div>
                        </Col>
                        <Col xs={1}>
                            <Button
                                id="open-button"
                                behavior="link"
                                onClick={() => this.toggleDeclaration(declaration)}
                            >
                                {`${t('open')} `}
                                <CgIcon name="right-scroll" />
                            </Button>
                        </Col>
                    </Row>
                    {declarationToOpen && (
                        <Modal
                            size="full-width"
                            isOpen={modalDeclaration}
                            toggle={() => this.toggleDeclaration()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleDeclaration()} className={styleCss['modal-header']}>
                                {this.renderModalTitle(dateEffet, timeEffet)}
                            </ModalHeader>
                            <ModalBody className="mt-0 pt-0">
                                <DeclarationDetails
                                    contrat={contrat}
                                    rights={droits}
                                    conventions={conventions}
                                    trace={trace}
                                    attestations={attestations}
                                    circuits={circuits}
                                    isTdb={isTdb}
                                    TPOnlineSelected={TPOnlineSelected}
                                    amcName={amcName}
                                    amcNumber={amcNumber}
                                    numerosAMCEchanges={numerosAMCEchanges}
                                    personNumber={personNumber}
                                    identification={identification}
                                    showItelisCode={showItelisCode}
                                    isHTP={isHTP}
                                />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

Declaration.propTypes = {
    t: PropTypes.func,
    declaration: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    isHTP: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Declaration;
