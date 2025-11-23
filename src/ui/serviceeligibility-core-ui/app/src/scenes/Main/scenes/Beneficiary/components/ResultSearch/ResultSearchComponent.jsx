/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    LoadingSpinner,
    Panel,
    PanelHeader,
    PanelSection,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import SearchResultItem from './components/SearchResultItem';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const MAX_RESULTAT_COUNT = 10;
let isRender = false;

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class ResultSearchComponent extends Component {
    componentDidMount() {
        const { getAllServiceMetier } = this.props;

        getAllServiceMetier();
    }

    returnEmptyComponent() {
        const { t, addAlertError } = this.props;

        if (!isRender) {
            addAlertError({
                message: t('beneficiaries.errorParameterMissing'),
                behavior: 'danger',
            });
        }
        isRender = true;

        return null;
    }

    returnResultComponent(totalBenef) {
        const { t, beneficiaries, researchCriteria, declarants, serviceMetierList } = this.props;
        const hasCriteria = researchCriteria && beneficiaries.length > 0;
        const maxCount = beneficiaries.length >= MAX_RESULTAT_COUNT;

        return (
            <PanelSection className="pl-0 pr-0">
                {maxCount && <span className="cgd-comment ml-2">{t('beneficiaries.resultSearch.tooMany')}</span>}
                {(beneficiaries || []).map(
                    (item, index) =>
                        index < MAX_RESULTAT_COUNT && (
                            <SearchResultItem
                                item={item}
                                declarants={declarants}
                                fromExternalEnv={item?.metaMap?.environnement === 'external'}
                                serviceMetierList={serviceMetierList}
                            />
                        ),
                )}
                {hasCriteria && totalBenef === 0 && t('noResults')}
            </PanelSection>
        );
    }

    @autobind
    renderHeader(totalBenef) {
        const { t } = this.props;
        return (
            <PanelHeader
                title={t('beneficiaries.resultSearch.pageTitle')}
                counter={totalBenef || 0}
                className="pl-0 pr-0"
            />
        );
    }

    render() {
        const { beneficiaries, researchCriteria, loadingBeneficiaries, serviceMetierList, isLoading } = this.props;
        // if we are loading data, we display a spinner
        if (loadingBeneficiaries || isLoading) {
            return <LoadingSpinner iconSize="3x" behavior="primary" type="container" />;
        }

        // if no data fetched and no researchCriteria, the result search panel shouldn't appear
        if (!beneficiaries && !researchCriteria) {
            return null;
        }

        const totalBenef = Math.min(MAX_RESULTAT_COUNT, beneficiaries.length);
        return (
            <Panel header={this.renderHeader(totalBenef)} className="bg-background-variant-1 pl-0 pr-0" border={false}>
                {JSON.stringify(serviceMetierList) === '{}' || !serviceMetierList
                    ? this.returnEmptyComponent(isRender)
                    : this.returnResultComponent(totalBenef)}
            </Panel>
        );
    }
}

ResultSearchComponent.propTypes = {
    t: PropTypes.func,
    beneficiaries: PropTypes.arrayOf(PropTypes.shape()),
    researchCriteria: PropTypes.shape(),
    loadingBeneficiaries: PropTypes.bool,
    declarants: PropTypes.arrayOf(PropTypes.shape()),
    serviceMetierList: PropTypes.shape(),
    getAllServiceMetier: PropTypes.func,
    addAlertError: PropTypes.func,
    isLoading: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ResultSearchComponent;
