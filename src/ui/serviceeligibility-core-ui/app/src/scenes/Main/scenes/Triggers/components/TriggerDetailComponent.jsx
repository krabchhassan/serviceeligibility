/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    PanelSection,
    Panel,
    Row,
    Col,
    PanelHeader,
    FileDownloaderPopup,
    AuthProvider,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import businessUtils from '../../../../../common/utils/businessUtils';
import Triggers from '../../../../../common/resources/Triggers';

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
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common', 'errors'], { wait: true })
class TriggerDetailComponent extends Component {
    static populateHeader() {
        let downloadRequestHeaders = '';
        if (AuthProvider.get()) {
            downloadRequestHeaders = {
                Authorization: `Bearer ${AuthProvider.get().token}`,
                'X-CGD-TENANT': AuthProvider.get().realm,
            };
        }
        return downloadRequestHeaders;
    }

    constructor(props) {
        super(props);
        this.state = {
            startDownload: false,
        };
    }

    componentDidMount() {
        const { getKOTriggeredBeneficiaries, trigger } = this.props;
        getKOTriggeredBeneficiaries(trigger.id);
    }

    @autobind
    toggleFile() {
        const { startDownload } = this.state;
        this.setState({ startDownload: !startDownload });
    }

    renderTriggeredBeneficiary = (triggeredBeneficiary) => {
        const { t } = this.props;
        const { offersAndProducts, derniereAnomalie } = triggeredBeneficiary;

        return (
            <Row>
                <Col xs="12">
                    <Panel panelTheme="secondary" border={false}>
                        <PanelSection>
                            <Row>
                                <Col xs="3">
                                    <div>
                                        <LabelValuePresenter
                                            id="numeroContrat"
                                            label={t('triggerDetail.numeroContrat')}
                                            value={triggeredBeneficiary.numeroContrat}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                    <div>
                                        <LabelValuePresenter
                                            id="nir"
                                            label={t('triggerDetail.nir')}
                                            value={businessUtils.formatRO(triggeredBeneficiary.nir)}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                    <div>
                                        <LabelValuePresenter
                                            id="dateNaissance"
                                            label={t('triggerDetail.dateNaissance')}
                                            value={businessUtils.transformDate(triggeredBeneficiary.dateNaissance)}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                </Col>
                                <Col xs="3">
                                    {(offersAndProducts || []).length === 0 && (
                                        <LabelValuePresenter
                                            id="offre"
                                            label={t('triggerDetail.offres')}
                                            defaultValue="-"
                                            inline
                                        />
                                    )}
                                    {Object.values(
                                        (offersAndProducts || []).map((offre) => (
                                            <div>
                                                <span>
                                                    <LabelValuePresenter
                                                        id="offre"
                                                        label={t('triggerDetail.offres')}
                                                        value={offre.code}
                                                        defaultValue="-"
                                                        inline
                                                    />{' '}
                                                    <LabelValuePresenter
                                                        id="produits"
                                                        label={t('triggerDetail.produits')}
                                                        value={Object.values(offre.produits || [])
                                                            .map((produit) => produit.code)
                                                            .join(', ')}
                                                        defaultValue="-"
                                                        inline
                                                    />
                                                </span>
                                            </div>
                                        )),
                                    )}
                                </Col>
                                <Col xs="3">
                                    <div>
                                        <LabelValuePresenter
                                            id="college"
                                            label={t('triggerDetail.college')}
                                            value={triggeredBeneficiary.college}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                    <div>
                                        <LabelValuePresenter
                                            id="collectivite"
                                            label={t('triggerDetail.collectivite')}
                                            value={triggeredBeneficiary.collectivite}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                    <div>
                                        <LabelValuePresenter
                                            id="critereSecondaireDetaille"
                                            label={t('triggerDetail.critereSecondaireDetaille')}
                                            value={triggeredBeneficiary.critereSecondaireDetaille}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                </Col>
                                <Col xs="3">
                                    <div>
                                        <LabelValuePresenter
                                            id="motifAnomalie"
                                            label={t('triggerDetail.motifAnomalie')}
                                            value={derniereAnomalie ? derniereAnomalie.description : '-'}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                </Col>
                            </Row>
                        </PanelSection>
                    </Panel>
                </Col>
            </Row>
        );
    };

    render() {
        const { t, trigger, triggeredKOBeneficiaries, addAlert } = this.props;
        const { startDownload } = this.state;
        const context = t('triggerDetail.context') + (trigger || {}).nbBenef;
        const downloadUrl = Triggers.excel((trigger || {}).id).url();
        const buttonProps = !(trigger || {}).exported &&
            (trigger || {}).origine === 'Renewal' && {
                disabled: true,
                tooltip:
                    "Veuillez lancer l'OMU is-bdds-exptriginfos : Génération des fichiers de détail des déclencheurs de génération des droits TP",
                tooltipPlacement: 'bottom',
            };
        const toolbar = {
            label: t('triggerDetail.download'),
            action: this.toggleFile,
            id: 'download',
            behavior: 'primary',
            icon: 'download',
            buttonProps,
        };

        if (startDownload) {
            addAlert({
                message: t('triggerDetail.downloadMessage'),
                timeout: null,
            });
        }

        return (
            <Fragment>
                <FileDownloaderPopup
                    fileUrl={downloadUrl}
                    toggleStartDownload={this.toggleFile}
                    isDownloading={startDownload}
                    requestHeaders={TriggerDetailComponent.populateHeader()}
                    showConfirm={false}
                    useClient
                />
                <Panel
                    header={
                        <PanelHeader
                            title={t('triggerDetail.title')}
                            counter={(trigger || {}).nbBenefKO || 0}
                            context={context}
                            actions={[toolbar]}
                        />
                    }
                >
                    <PanelSection>
                        {(triggeredKOBeneficiaries || []).map((triggeredBeneficiary) =>
                            this.renderTriggeredBeneficiary(triggeredBeneficiary),
                        )}
                    </PanelSection>
                </Panel>
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    trigger: PropTypes.shape(),
    getKOTriggeredBeneficiaries: PropTypes.func,
    triggeredKOBeneficiaries: PropTypes.arrayOf(PropTypes.shape()),
    addAlert: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
TriggerDetailComponent.propTypes = propTypes;
// Add default props
TriggerDetailComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TriggerDetailComponent;
