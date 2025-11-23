/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Panel, PanelSection, Row, Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import DateUtils from '../../../../../../../common/utils/DateUtils';
import CommonSpinner from '../../../../../../../common/components/CommonSpinner/CommonSpinner';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import businessUtils from '../../../../../../../common/utils/businessUtils';
import styleCss from './style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Beneficiary extends Component {
    render() {
        const { beneficiary, t, amc, serviceTab, environment } = this.props;
        if (!beneficiary) {
            return <CommonSpinner />;
        }
        const { affiliation, numeroPersonne } = beneficiary;
        const nir = `${businessUtils.formatRO(beneficiary.nirBeneficiaire)} ${beneficiary.cleNirBeneficiaire}`;

        let name = '';
        const { nom, nomMarital, prenom } = affiliation || {};
        name = businessUtils.getBenefIdentity(nomMarital, nom, prenom);
        return (
            <Panel id={`otherBeneficiary-panel ${`${numeroPersonne}`}`} border={false} className="pt-0 pb-0">
                <PanelSection className="p-0">
                    <Row className="ml-0 pl-0">
                        <Col xs={11} className="p-0">
                            <Row>
                                <Col xs={3} className="pr-0 pl-0">
                                    {name}
                                </Col>
                                <Col className="pr-0 pl-0">
                                    <LabelValuePresenter
                                        id="beneficiary-dateNaissance"
                                        label={t(`consolidatedContract.birthdate`)}
                                        value={`${DateUtils.formatDisplayDate(beneficiary.dateNaissance)} (${
                                            beneficiary.rangNaissance
                                        })`}
                                        className={styleCss.row}
                                    />
                                </Col>
                                <Col className="pr-0 pl-0">
                                    <LabelValuePresenter
                                        id="beneficiary-nir"
                                        label={t(`consolidatedContract.nir`)}
                                        value={nir}
                                        className={styleCss.row}
                                    />
                                </Col>
                                <Col className="pr-0 pl-0">
                                    <LabelValuePresenter
                                        id="beneficiary-numeroPersonne"
                                        label={t(`consolidatedContract.numPersonne`)}
                                        value={numeroPersonne}
                                        className={styleCss.row}
                                    />
                                </Col>
                            </Row>
                        </Col>
                        <Col className="d-flex p-0 mr-2 justify-content-end">
                            <Link
                                id="open-button"
                                to={`/beneficiary_tracking/${`${environment}`}/details/${`${amc}-${numeroPersonne}/${serviceTab}`}`}
                                target="_blank"
                            >
                                {t('open')}
                            </Link>
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }
}

Beneficiary.propTypes = {
    t: PropTypes.func,
    beneficiary: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Beneficiary;
