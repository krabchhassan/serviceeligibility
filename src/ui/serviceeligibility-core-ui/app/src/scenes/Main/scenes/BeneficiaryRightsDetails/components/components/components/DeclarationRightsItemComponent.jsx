/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Col,
    LoadingSpinner,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import style from './style.module.scss';
import DeclarationPrestations from './Prestation/DeclarationPrestations';
import DateRangePresenter from '../../../../../../../common/utils/DateRangePresenter';
import Constants from '../../../../../../../common/utils/Constants';
import DeclarationConvention from './Convention/DeclarationConvention';
import DeclarationCouverture from './Couverture/DeclarationCouverture';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class DeclarationRightsItemComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            panelSectionExpanded: false,
        };
    }

    getContext(periodesDroit) {
        const { t } = this.props;

        return (
            <Fragment>
                <span className="cgd-comment">{`${t('beneficiaryRightsDetails.periodeDroits')} :`}</span>
                &nbsp;
                <DateRangePresenter start={periodesDroit.periodeDebut} end={periodesDroit.periodeFin} />
            </Fragment>
        );
    }

    @autobind
    handleCollapse() {
        const { panelSectionExpanded } = this.state;
        this.setState({ panelSectionExpanded: !panelSectionExpanded });
    }

    renderCollapsible() {
        const { domain, TPOnlineSelected, clientType } = this.props;

        return (
            <Row>
                <Col className={style['full-size-panel']} xs={4}>
                    <DeclarationCouverture
                        domain={domain}
                        domainCode={domain.code}
                        TPOnlineSelected={TPOnlineSelected}
                        clientType={clientType}
                    />
                </Col>
                <Col className={style['full-size-panel']} xs={4}>
                    <DeclarationConvention conventions={domain.conventions} />
                </Col>
                {(!TPOnlineSelected || clientType !== Constants.TOPOLOGY_INSURER) && (
                    <Col className={style['full-size-panel']} xs={4}>
                        <DeclarationPrestations domain={domain} />
                    </Col>
                )}
            </Row>
        );
    }

    render() {
        const { domainList, domain, TPOnlineSelected } = this.props;
        const { panelSectionExpanded } = this.state;

        if (!domain || !domainList) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }

        const { code } = domain;
        const existingDomainList = Object.values(domainList).filter(
            (domainFiltered) => domainFiltered.code === domain.code,
        );

        const libelle = existingDomainList.length > 0 ? existingDomainList[0].libelle : '-';
        const title = `${code} (${libelle})`;

        const periode = TPOnlineSelected
            ? { periodeDebut: domain.periodeOnlineDebut, periodeFin: domain.periodeOnlineFin }
            : { periodeDebut: domain.periodeDebut, periodeFin: domain.periodeOfflineFin };

        return (
            <Fragment>
                <Panel
                    id={`rights-panel-${code}`}
                    header={
                        <PanelHeader
                            title={title}
                            context={this.getContext(periode)}
                            titleClassname="col-md-6"
                            contextClassname="col-md-3"
                        />
                    }
                    onCollapseClick={() => this.handleCollapse()}
                    expanded={panelSectionExpanded}
                >
                    <PanelSection>{this.renderCollapsible()}</PanelSection>
                </Panel>
            </Fragment>
        );
    }
}

DeclarationRightsItemComponent.propTypes = {
    t: PropTypes.func,
    domain: PropTypes.shape(),
    TPOnlineSelected: PropTypes.bool,
    domainList: PropTypes.shape(),
    clientType: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DeclarationRightsItemComponent;
