/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    PanelSection,
    Panel,
    CgdTable,
    PanelHeader,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import StringUtils from '../../../../../common/utils/StringUtils';
import businessUtils from '../../../../../common/utils/businessUtils';
import CommonSpinner from '../../../../../common/components/CommonSpinner/CommonSpinner';
import ParametrageCommonPanel from '../../ParametrageCommonPanel/ParametrageCommonPanel';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class SecondStepComponent extends Component {
    getColumns() {
        return [
            this.generateCommonColumn('ordreAffichage', '8%'),
            this.generateCommonColumn('codeDomaineTP', '10%'),
            this.generateCommonColumn('libelleDomaineTP', '10%'),
            this.generateCommonColumn('convention', '8%'),
            this.generateCommonColumn('codeRenvoiAction', '8%'),
            this.generateCommonColumn('codeRenvoi', '15%'),
            this.generateCommonColumn('messageRenvoi'),
        ];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`parametrageContrat.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    formatDetailDroits() {
        const { t, contratIndiv, returnCodesList } = this.props;
        const { parametrageDroitsCarteTP } = contratIndiv || {};
        const { detailsDroit } = parametrageDroitsCarteTP || [];
        return businessUtils.formatDetailDroits(t, detailsDroit, returnCodesList);
    }

    renderProperties() {
        const { t, contratIndiv, returnCodesList, codesItelis, showItelisCode } = this.props;
        const { parametrageDroitsCarteTP } = contratIndiv || {};
        const { codeRenvoi } = parametrageDroitsCarteTP || {};
        const codeRenvoiObject = Object.values(returnCodesList || {}).find(
            (returnCode) => returnCode.code === codeRenvoi,
        );

        return (
            <Panel header={<PanelHeader title={t('parametrageContrat.proprieteCarteTP')} />}>
                <PanelSection>
                    <ParametrageCommonPanel
                        parametrageDroitsCarteTP={parametrageDroitsCarteTP}
                        codesItelis={codesItelis}
                        showItelisCode={showItelisCode}
                        codeRenvoiObject={codeRenvoiObject}
                    />
                </PanelSection>
            </Panel>
        );
    }

    renderCardDetails() {
        const { t, contratIndiv } = this.props;
        const { parametrageDroitsCarteTP } = contratIndiv || {};
        const { detailsDroit } = parametrageDroitsCarteTP || [];
        const data = this.formatDetailDroits();

        return (
            <Panel
                header={
                    <PanelHeader
                        title={t('parametrageContrat.mentionsCarteTP')}
                        context={t('parametrageContrat.mentionsCarteTPWarning')}
                    />
                }
            >
                <PanelSection>
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
                </PanelSection>
            </Panel>
        );
    }

    render() {
        const { isLoading } = this.props;

        if (isLoading) {
            return <CommonSpinner />;
        }

        return (
            <Fragment>
                {this.renderProperties()}
                {this.renderCardDetails()}
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    contratIndiv: PropTypes.shape(),
    isLoading: PropTypes.bool,
    codesItelis: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
};
// Default props
const defaultProps = {};

// Add prop types
SecondStepComponent.propTypes = propTypes;
// Add default props
SecondStepComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SecondStepComponent;
