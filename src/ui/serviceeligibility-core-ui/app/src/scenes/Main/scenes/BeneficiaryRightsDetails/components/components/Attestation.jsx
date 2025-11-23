/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    PanelHeader,
    Panel,
    PanelSection,
    Row,
    Col,
    CgdTable,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import LabelValuePresenter from '../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';
import StringUtils from '../../../../../../common/utils/StringUtils';
import DateRangePresenter from '../../../../../../common/utils/DateRangePresenter';
import DateUtils from '../../../../../../common/utils/DateUtils';
import attestationUtils from '../../../../../../common/utils/AttestationUtils';
import Constants from '../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Attestation extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isOpen: props.openTab !== undefined ? props.openTab : false,
        };
    }

    getColumns() {
        return [
            this.generateCommonColumn('ordreEdition', '9%'),
            this.generateCommonColumn('domaineDroits', '11%'),
            this.generateCommonColumn('conventions', '16%'),
            this.generateCommonColumn('taux', '20%'),
            this.generateCommonColumn('renvois', '44%'),
        ];
    }

    getContext() {
        const { attestation } = this.props;
        const { periodeDebut, periodeFin } = attestation;

        return (
            <DateRangePresenter
                start={DateUtils.formatDisplayDate(periodeDebut, Constants.DEFAULT_DATE)}
                end={DateUtils.formatDisplayDate(periodeFin, Constants.DEFAULT_DATE)}
            />
        );
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`beneficiaryRightsDetails.${key}`), false),
            accessor: key,
            id: key,
            width,
            key,
        };
    }

    @autobind
    toggleCollapse() {
        const { isOpen } = this.state;
        this.setState({ isOpen: !isOpen });
    }

    render() {
        const { isOpen } = this.state;
        const { conventions, attestation, t, showItelisCode } = this.props;
        const canRender = attestation && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const pageTitle = t('beneficiaryRightsDetails.periode');
        const panelHeader = (
            <PanelHeader
                title={t('beneficiaryRightsDetails.periodeCarte')}
                context={this.getContext()}
                titleClassname="col-md-6"
                contextClassname="col-md-6"
            />
        );
        const isCartePapier = attestation.isCartePapier ? t('yes') : t('no');
        const isCarteDemat = attestation.isCarteDemat ? t('yes') : t('no');
        const { codeRenvoiEntete, rowsData } = attestationUtils.prepareAttestationRowsData(attestation, conventions);
        return (
            <Panel
                id="certificate-panel"
                header={panelHeader}
                label={pageTitle}
                expanded={isOpen}
                onCollapseClick={this.toggleCollapse}
            >
                <PanelSection>
                    <Row className="mt-2 mb-2">
                        <Col>
                            <LabelValuePresenter
                                id="certification-cartePapier"
                                label={t(`beneficiaryRightsDetails.cartePapier`)}
                                value={isCartePapier}
                            />
                            <LabelValuePresenter
                                id="certification-carteDemat"
                                label={t(`beneficiaryRightsDetails.carteDemat`)}
                                value={isCarteDemat}
                            />
                            <Col>
                                <LabelValuePresenter
                                    id="certification-codeRenvoi"
                                    label={t(`beneficiaryRightsDetails.codeRenvoi`)}
                                    value={codeRenvoiEntete}
                                />
                            </Col>
                        </Col>
                        <Col>
                            <LabelValuePresenter
                                id="certification-annex1"
                                label={t(`beneficiaryRightsDetails.annexe`) + 1}
                                value={attestation.annexe1Carte}
                            />
                            <LabelValuePresenter
                                id="certification-annex2"
                                label={t(`beneficiaryRightsDetails.annexe`) + 2}
                                value={attestation.annexe2Carte}
                            />
                        </Col>
                        <Col>
                            <LabelValuePresenter
                                id="certification-model"
                                label={t(`beneficiaryRightsDetails.fondCarte`)}
                                value={attestation.modeleCarte}
                            />
                            {showItelisCode ? (
                                <LabelValuePresenter
                                    id="certification-codeItelis"
                                    label={t(`beneficiaryRightsDetails.codeItelis`)}
                                    value={attestation.codeItelis}
                                />
                            ) : null}
                        </Col>
                    </Row>
                    <CgdTable
                        id="certification-table"
                        data={rowsData}
                        columns={this.getColumns()}
                        showPageSizeOptions={false}
                        showPagination={false}
                        pageSize={rowsData.length}
                    />
                </PanelSection>
            </Panel>
        );
    }
}

Attestation.propTypes = {
    t: PropTypes.func,
    attestation: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Attestation;
