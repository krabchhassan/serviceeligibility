/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    CgdTable,
    Col,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import businessUtils from '../../../../../common/utils/businessUtils';
import StringUtils from '../../../../../common/utils/StringUtils';
import DateUtils from '../../../../../common/utils/DateUtils';
import ParametrageCommonPanel from '../../ParametrageCommonPanel/ParametrageCommonPanel';

/* eslint-disable no-plusplus */
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class ParametrageDetail extends Component {
    getColumns() {
        return [
            this.generateCommonColumn('ordreAffichage', 100),
            this.generateCommonColumn('codeDomaineTP', 110),
            this.generateCommonColumn('libelleDomaineTP', 350),
            this.generateCommonColumn('convention', 250),
            this.generateCommonColumn('codeRenvoi', 110),
            this.generateCommonColumn('messageRenvoi'),
            this.generateCommonColumn('codeRenvoiAction'),
        ];
    }

    getLibelleCodeRenvoi(codeRenvoi) {
        const { t, codesRenvoiList } = this.props;
        if (codesRenvoiList && Object.keys(codesRenvoiList || {}).includes(codeRenvoi)) {
            return Object.values(codesRenvoiList).filter(
                (codeRenvoiElement) => codeRenvoi === codeRenvoiElement.code,
            )[0]?.libelle;
        }
        return t('parametrageDetail.noLibelle');
    }

    prepareDetailDroitMessageRenvoi(detailsDroit) {
        const { t, codesRenvoiList } = this.props;
        detailsDroit.forEach((dd) => {
            if (dd.codeRenvoi) {
                dd.messageRenvoi = this.getLibelleCodeRenvoi(dd.codeRenvoi);
            }
        });
        return businessUtils.formatDetailDroits(t, detailsDroit, codesRenvoiList);
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`parametrageDetail.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    renderCardDetails() {
        const { t, parametrage } = this.props;
        const { parametrageDroitsCarteTP } = parametrage || {};
        const { detailsDroit } = parametrageDroitsCarteTP || [];
        const data = this.prepareDetailDroitMessageRenvoi(detailsDroit);
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row className="cgd-comment">{t('parametrageDetail.mentionsCarteTP')}</Row>
                    <Row>
                        <CgdTable
                            id="domainTable"
                            data={data}
                            columns={this.getColumns()}
                            showPageSizeOptions={false}
                            showPagination={false}
                            initialPageSize={6}
                            pageSize={(detailsDroit || []).length}
                            manual
                        />
                    </Row>
                </Col>
            </Row>
        );
    }

    renderGeneralInfo() {
        const { t, parametrage } = this.props;
        return (
            <Panel header={<PanelHeader title="Population sélectionnée" />}>
                <PanelSection>
                    <Row>
                        <Col xs="3">
                            <div>
                                <LabelValuePresenter
                                    id="amc"
                                    label={t('parametrageDetail.amc')}
                                    value={parametrage.amc}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="identifiantCollectivite"
                                    label={t('parametrageDetail.identifiantCollectivite')}
                                    value={parametrage.identifiantCollectivite}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="priority"
                                    label={t('parametersDroits.priorite')}
                                    value={parametrage.priorite}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="3">
                            <div>
                                <LabelValuePresenter
                                    id="groupePopulation"
                                    label={t('parametrageDetail.groupePopulation')}
                                    value={parametrage.groupePopulation}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="critereSecondaireDetaille"
                                    label={t('parametrageDetail.critereSecondaireDetaille')}
                                    value={parametrage.critereSecondaireDetaille}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="3">{this.renderLotGT()}</Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    renderLotGT() {
        const { t, parametrage, allLot } = this.props;
        const { idLots, garantieTechniques } = parametrage || {};
        return (
            <Fragment>
                <Row>
                    <Col>
                        <LabelValuePresenter
                            className="d-flex justify-content-start"
                            id="lots"
                            label={t('parametersDroits.label_lots')}
                            value={
                                StringUtils.renderLongText(businessUtils.stringifyListLots(idLots, allLot), ',', 70) ||
                                '-'
                            }
                            labelPortion={1.5}
                            key="lots"
                        />
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <LabelValuePresenter
                            className="d-flex justify-content-start"
                            id="gt"
                            label={t('parametersDroits.label_gts')}
                            value={
                                StringUtils.renderLongText(
                                    businessUtils.stringifyListGTs(garantieTechniques),
                                    ',',
                                    70,
                                ) || '-'
                            }
                            labelPortion={1.5}
                            key="gt"
                        />
                    </Col>
                </Row>
            </Fragment>
        );
    }

    renderParametrageDroitsCarteTP() {
        const { t, parametrage, codesItelis, showItelisCode, codesRenvoiList } = this.props;
        const { parametrageDroitsCarteTP } = parametrage || {};

        return (
            <Panel header={<PanelHeader title={t('parametrageDetail.droitsCarteTP')} />}>
                <PanelSection>
                    <ParametrageCommonPanel
                        parametrageDroitsCarteTP={parametrageDroitsCarteTP}
                        codesItelis={codesItelis}
                        showItelisCode={showItelisCode}
                        codesRenvoiList={codesRenvoiList}
                    />
                    {this.renderCardDetails()}
                </PanelSection>
            </Panel>
        );
    }

    renderParametrageRenouvellement() {
        const { t, parametrage } = this.props;
        const { parametrageRenouvellement } = parametrage;
        const { dateRenouvellementCarteTP } = parametrageRenouvellement;
        const { annulDroitsOffline } = parametrageRenouvellement || {};

        const validityPeriodLabel = t(
            `parametersDroits.form.dureeValiditeDroits${parametrageRenouvellement.dureeValiditeDroitsCarteTP}`,
        );

        return (
            <Panel header={<PanelHeader title={t('parametrageDetail.renouvellement')} />}>
                <PanelSection>
                    <Row>
                        <Col xs="4">
                            <div>
                                <LabelValuePresenter
                                    id="dateRenouvellementCarteTP"
                                    label={t('parametrageDetail.dateRenouvellementCarteTP')}
                                    value={t(`parametrageDetail.${dateRenouvellementCarteTP}`)}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            {parametrageRenouvellement.modeDeclenchement === 'Manuel' ? (
                                <div>
                                    <LabelValuePresenter
                                        id="dateDebutDroitTP"
                                        label={t('parametrageDetail.dateDebutDroitTP')}
                                        value={DateUtils.transformDateForDisplay(
                                            parametrageRenouvellement.dateDebutDroitTP,
                                        )}
                                        defaultValue="-"
                                        inline
                                    />
                                </div>
                            ) : null}
                            {parametrageRenouvellement.modeDeclenchement === 'Manuel' ? (
                                <div>
                                    <LabelValuePresenter
                                        id="dateExecutionBatch"
                                        label={t('parametrageDetail.dateExecutionBatch')}
                                        value={DateUtils.transformDateForDisplay(
                                            parametrageRenouvellement.dateExecutionBatch,
                                        )}
                                        defaultValue="-"
                                        inline
                                    />
                                </div>
                            ) : null}
                            <div>
                                <LabelValuePresenter
                                    id="annulDroitsOffline"
                                    label={t('parametrageDetail.annulDroitsOffline')}
                                    value={annulDroitsOffline ? t('yes') : t('no')}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="3">
                            <div>
                                <LabelValuePresenter
                                    id="delaiDeclenchementCarteTP"
                                    label={t('parametrageDetail.delaiDeclenchement')}
                                    value={parametrageRenouvellement.delaiDeclenchementCarteTP}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="dureeValiditeDroitsCarteTP"
                                    label={t('parametrageDetail.dureeValiditeDroitsCarteTP')}
                                    value={validityPeriodLabel}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            {parametrageRenouvellement.modeDeclenchement === 'Manuel' ? (
                                <div>
                                    <LabelValuePresenter
                                        id="dateDerniereExecution"
                                        label={t('parametrageDetail.dateDerniereExecution')}
                                        value={DateUtils.formatDisplayDate(parametrageRenouvellement.derniereExecution)}
                                        defaultValue="-"
                                        inline
                                    />
                                </div>
                            ) : null}
                        </Col>
                        <Col xs="3">
                            <div>
                                <LabelValuePresenter
                                    id="debutEcheance"
                                    label={t('parametrageDetail.debutEcheance')}
                                    value={parametrageRenouvellement.debutEcheance}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="modeDeclenchement"
                                    label={t('parametrageDetail.modeDeclenchement')}
                                    value={parametrageRenouvellement.modeDeclenchement}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="2">
                            <div>
                                <LabelValuePresenter
                                    id="seuilSecurite"
                                    label={t('parametrageDetail.seuilSecurite')}
                                    value={parametrageRenouvellement.seuilSecurite}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="dateDebutValidite"
                                    label={t('parametrageDetail.dateDebutValidite')}
                                    value={DateUtils.transformDateForDisplay(parametrage.dateDebutValidite)}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                            {parametrageRenouvellement.modeDeclenchement === 'Manuel' &&
                            parametrageRenouvellement.dateRenouvellementCarteTP === 'AnniversaireContrat' ? (
                                <div>
                                    <LabelValuePresenter
                                        id="delaiRenouvellement"
                                        label={t('parametrageDetail.delaiRenouvellement')}
                                        value={parametrageRenouvellement.delaiRenouvellement}
                                        inline
                                    />
                                </div>
                            ) : null}
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    render() {
        return (
            <Fragment>
                {this.renderGeneralInfo()}
                {this.renderParametrageDroitsCarteTP()}
                {this.renderParametrageRenouvellement()}
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    parametrage: PropTypes.shape(),
    allLot: PropTypes.arrayOf(PropTypes.shape()),
    codesItelis: PropTypes.arrayOf(PropTypes.shape()),
    codesRenvoiList: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
};
// Default props
const defaultProps = {};

// Add prop types
ParametrageDetail.propTypes = propTypes;
// Add default props
ParametrageDetail.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageDetail;
