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
import Couverture from './components/Couverture/Couverture';
import TypeConvention from './components/Convention/TypeConvention';
import Prestations from './components/Prestation/Prestations';
import DateRangePresenter from '../../../../../../common/utils/DateRangePresenter';
import Constants from '../../../../../../common/utils/Constants';
import DateUtils from '../../../../../../common/utils/DateUtils';
import dateConstants from '../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class RightsItemComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            panelSectionExpanded: Array.from({ length: props.domain.periodesDroit.length }, () => false),
        };
    }

    getContext(periodeDroit) {
        const { t, TPOnlineSelected } = this.props;
        let endDateToDisplay = periodeDroit.periodeFin;
        if (!TPOnlineSelected) {
            endDateToDisplay = periodeDroit.periodeFinOffline;
        }
        return (
            <Fragment>
                <span className="cgd-comment">{`${t('beneficiaryRightsDetails.periodeDroits')} :`}</span>
                &nbsp;
                <DateRangePresenter
                    start={DateUtils.formatDisplayDate(periodeDroit.periodeDebut, dateConstants.DEFAULT_DATE)}
                    end={DateUtils.formatDisplayDate(endDateToDisplay, dateConstants.DEFAULT_DATE)}
                />
            </Fragment>
        );
    }

    @autobind
    handleCollapse(index, isExpanded) {
        const { panelSectionExpanded } = this.state;
        const newExpandedState = [...panelSectionExpanded];
        newExpandedState[index] = isExpanded;
        this.setState({ panelSectionExpanded: newExpandedState });
    }

    renderCollapsible(periodeOfDomain) {
        const { domain, TPOnlineSelected, clientType, selectedStitch, domainesFromHistoConsoByMaille } = this.props;
        const { code } = domain;

        return (
            <Row>
                <Col className="m-0 p-1" xs={4}>
                    <Couverture
                        domainCode={code}
                        domain={periodeOfDomain}
                        clientType={clientType}
                        selectedStitch={selectedStitch}
                        TPOnlineSelected={TPOnlineSelected}
                        domainesFromHistoConsoByMaille={domainesFromHistoConsoByMaille}
                    />
                </Col>
                <Col className="m-0 p-1" xs={4}>
                    <TypeConvention
                        conventions={periodeOfDomain.conventionnements}
                        domainesFromHistoConsoByMaille={domainesFromHistoConsoByMaille}
                    />
                </Col>
                {(clientType !== Constants.TOPOLOGY_INSURER || !TPOnlineSelected) && (
                    <Col className="m-0 p-1" xs={4}>
                        <Prestations
                            domain={periodeOfDomain}
                            domainesFromHistoConsoByMaille={domainesFromHistoConsoByMaille}
                        />
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

        const { code, periodesDroit } = domain;
        const existingDomainList = Object.values(domainList).filter(
            (domainFiltered) => domainFiltered.code === domain.code,
        );

        const libelle = existingDomainList.length > 0 ? existingDomainList[0].libelle : '-';
        const title = `${code} (${libelle})`;
        const periodesToRender = periodesDroit.filter((periode) =>
            TPOnlineSelected ? periode.typePeriode === 'ONLINE' : periode.typePeriode === 'OFFLINE',
        );

        return (
            <Fragment>
                {(periodesToRender || []).map((periode, index) => (
                    <Panel
                        key={`panel-${periode}`}
                        id={`rights-panel-${index}`}
                        header={
                            <PanelHeader
                                title={title}
                                context={this.getContext(periode)}
                                titleClassname="col-md-4"
                                contextClassname="col-md-7"
                            />
                        }
                        onCollapseClick={(event, isExpanded) => this.handleCollapse(index, isExpanded)}
                        expanded={panelSectionExpanded[index]}
                    >
                        <PanelSection>{this.renderCollapsible(periode)}</PanelSection>
                    </Panel>
                ))}
            </Fragment>
        );
    }
}

RightsItemComponent.propTypes = {
    t: PropTypes.func,
    domain: PropTypes.shape(),
    TPOnlineSelected: PropTypes.bool,
    domainList: PropTypes.shape(),
    clientType: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default RightsItemComponent;
