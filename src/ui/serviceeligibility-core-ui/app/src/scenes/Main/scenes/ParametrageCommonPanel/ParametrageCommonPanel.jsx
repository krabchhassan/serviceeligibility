/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Row, Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../common/components/LabelValuePresenter/LabelValuePresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class ParametrageCommonPanel extends Component {
    getLibelleCodeRenvoi(codeRenvoi) {
        const { t, codesRenvoiList, codeRenvoiObject } = this.props;

        if (codeRenvoiObject !== undefined) {
            return codeRenvoiObject.libelle;
        }
        if (codesRenvoiList && Object.keys(codesRenvoiList || {}).includes(codeRenvoi)) {
            return codesRenvoiList[codeRenvoi].libelle;
        }
        return t('parametrageDetail.noLibelle');
    }

    getCodeRenvoi() {
        const { codeRenvoiObject, parametrageDroitsCarteTP } = this.props;

        if (codeRenvoiObject === undefined) {
            return (parametrageDroitsCarteTP || {}).codeRenvoi;
        }
        return codeRenvoiObject.code;
    }

    render() {
        const { t, parametrageDroitsCarteTP, showItelisCode, codesItelis } = this.props;
        const { isCarteEditablePapier } = parametrageDroitsCarteTP || {};
        const { isCarteDematerialisee } = parametrageDroitsCarteTP || {};
        let itelisLabel = null;
        if (showItelisCode) {
            itelisLabel = (codesItelis.find((item) => item.value.code === parametrageDroitsCarteTP.codeItelis) || {})
                .value?.label;
        }

        return (
            <Row>
                <Col>
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="codeConventionTP"
                        label={t('parametrageDetail.codeConventionTP')}
                        value={
                            parametrageDroitsCarteTP.codeConventionTP ? parametrageDroitsCarteTP.codeConventionTP : '-'
                        }
                        labelPortion={1.5}
                        key="codeConventionTP"
                    />
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="refFondCarte"
                        label={t('parametrageDetail.refFondCarte')}
                        value={
                            (parametrageDroitsCarteTP || {}).refFondCarte
                                ? (parametrageDroitsCarteTP || {}).refFondCarte
                                : '-'
                        }
                        labelPortion={1.5}
                        key="refFondCarte"
                    />
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="codeRenvoi"
                        label={t('parametrageDetail.codeRenvoi')}
                        value={this.getCodeRenvoi() || '-'}
                        labelPortion={1.5}
                        key="codeRenvoi"
                    />
                </Col>
                <Col>
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="codeOperateurTP"
                        label={t('parametrageDetail.codeOperateurTP')}
                        value={
                            parametrageDroitsCarteTP.codeOperateurTP ? parametrageDroitsCarteTP.codeOperateurTP : '-'
                        }
                        labelPortion={1.5}
                        key="codeOperateurTP"
                    />
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="codeAnnexe1"
                        label={t('parametrageDetail.codeAnnexe1')}
                        value={parametrageDroitsCarteTP.codeAnnexe1 ? parametrageDroitsCarteTP.codeAnnexe1 : '-'}
                        labelPortion={1.5}
                        key="codeAnnexe1"
                    />
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="libelleCodeRenvoi"
                        label={t('parametrageDetail.messageRenvoi')}
                        value={
                            (parametrageDroitsCarteTP || {}).codeRenvoi
                                ? this.getLibelleCodeRenvoi((parametrageDroitsCarteTP || {}).codeRenvoi)
                                : '-'
                        }
                        labelPortion={1.5}
                        key="libelleCodeRenvoi"
                    />
                </Col>
                <Col>
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="isCarteEditablePapier"
                        label={t('parametrageDetail.isCarteEditablePapier')}
                        value={isCarteEditablePapier ? t('yes') : t('no')}
                        labelPortion={1.5}
                        key="isCarteEditablePapier"
                    />
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="codeAnnexe2"
                        label={t('parametrageDetail.codeAnnexe2')}
                        value={
                            (parametrageDroitsCarteTP || {}).codeAnnexe2
                                ? (parametrageDroitsCarteTP || {}).codeAnnexe2
                                : '-'
                        }
                        labelPortion={1.5}
                        key="codeAnnexe2"
                    />
                </Col>
                <Col>
                    <LabelValuePresenter
                        className="d-flex justify-content-start"
                        id="isCarteDematerialisee"
                        label={t('parametrageDetail.isCarteDematerialisee')}
                        value={isCarteDematerialisee ? t('yes') : t('no')}
                        labelPortion={1.5}
                        key="isCarteDematerialisee"
                    />
                    {showItelisCode ? (
                        <LabelValuePresenter
                            className="d-flex justify-content-start"
                            id="codeItelis"
                            label={t('parametrageDetail.codeItelis')}
                            value={itelisLabel || '-'}
                            labelPortion={1.5}
                            key="codeRenvoi"
                        />
                    ) : null}
                </Col>
            </Row>
        );
    }
}

ParametrageCommonPanel.propTypes = {
    t: PropTypes.func,
    parametrageDroitsCarteTP: PropTypes.shape(),
    codesItelis: PropTypes.arrayOf(PropTypes.shape()),
    codesRenvoiList: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
    codeRenvoiObject: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageCommonPanel;
