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
    CgdTable,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import StringUtils from '../../../../../../../common/utils/StringUtils';
import CommonSpinner from '../../../../../../../common/components/CommonSpinner/CommonSpinner';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import attestationUtils from '../../../../../../../common/utils/AttestationUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class AttestationContractDetail extends Component {
    getColumns() {
        return [
            this.generateCommonColumn('ordreEdition', '12%'),
            this.generateCommonColumn('domaineDroits', '14%'),
            this.generateCommonColumn('conventions', '12%'),
            this.generateCommonColumn('taux', '18%'),
            this.generateCommonColumn('renvois', '44%'),
        ];
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

    render() {
        const { conventions, attestation, isCartePapier, carteDemat, t, showItelisCode } = this.props;
        const canRender = attestation && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const pageTitle = t('beneficiaryRightsDetails.attestationDetails');
        const { codeRenvoiEntete, rowsData } = attestationUtils.prepareAttestationRowsData(attestation, conventions);

        return (
            <Panel id="certificate-panel" label={pageTitle} border={false}>
                <PanelSection>
                    <Row>
                        <h4 className="ml-2 pl-2">{t(`consolidatedContract.infoAttestation`)}</h4>
                    </Row>
                    <Row>
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
                            <LabelValuePresenter
                                id="certification-codeRenvoi"
                                label={t(`beneficiaryRightsDetails.codeRenvoi`)}
                                value={codeRenvoiEntete}
                            />
                        </Col>
                        <Col>
                            <LabelValuePresenter
                                id="certification-cartePapier"
                                label={t(`consolidatedContract.cartePapier`)}
                                value={isCartePapier}
                            />
                            <LabelValuePresenter
                                id="certification-carteDemat"
                                label={t(`consolidatedContract.carteDemat`)}
                                value={carteDemat}
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
                    <Row>
                        <h4 className="ml-2 pl-2">{t(`consolidatedContract.detailAttestation`)}</h4>
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

AttestationContractDetail.propTypes = {
    t: PropTypes.func,
    attestation: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AttestationContractDetail;
