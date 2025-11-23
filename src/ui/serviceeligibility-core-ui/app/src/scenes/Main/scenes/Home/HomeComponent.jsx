/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';

import {
    BodyHeader,
    LoadingSpinner,
    PageLayout,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import SearchForm from './components/SearchForm/SearchForm';
import SearchResult from './components/SearchResult';
import history from '../../../../history';
import permissionConstants from '../../PermissionConstants';
import LastModified from './components/LastModified';
import HomeBreadcrumb from './components/HomeBreadcrumb';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const goToCreatePage = () => history.push('/create_insurer');

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility'], { wait: true })
class HomeComponent extends Component {
    getToolbar() {
        const { t } = this.props;
        return {
            label: t('headers:home.actions'),
            items: [
                {
                    label: t('declarant.createAmc'),
                    action: goToCreatePage,
                    allowedPermissions: permissionConstants.CREATE_AMC_DATA_PERMISSION,
                },
            ],
        };
    }

    render() {
        const { t, loading, searchResults, searchCriteria } = this.props;

        return (
            <PageLayout header={<BodyHeader title={t('home.pageTitle')} toolbar={this.getToolbar()} />}>
                <HomeBreadcrumb />
                <SearchForm />
                {loading && (
                    <LoadingSpinner type="over-container" iconSize="3x" iconType="circle-o-notch" behavior="primary" />
                )}
                <SearchResult searchResults={searchResults} searchCriteria={searchCriteria} />
                <LastModified />
            </PageLayout>
        );
    }
}

HomeComponent.propTypes = {
    t: PropTypes.func,
    loading: PropTypes.bool,
    searchResults: PropTypes.arrayOf(PropTypes.shape()),
    searchCriteria: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HomeComponent;
