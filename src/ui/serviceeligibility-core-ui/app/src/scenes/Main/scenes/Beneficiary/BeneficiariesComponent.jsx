/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { BodyHeader, Col, PageLayout, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import SearchForm from './components/SearchForm/SearchForm';
import ResultSearch from './components/ResultSearch/ResultSearch';
import BeneficiaireHistory from './components/SearchHistory/BeneficiaireHistory';
import BeneficiariesBreadcrumb from './components/BeneficiariesBreadcrumb';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class BeneficiariesComponent extends Component {
    render() {
        const { t, bodyHeaderPanels, bodyHeaderToolbar } = this.props;
        const pageTitle = t('beneficiaries.pageTitle');

        return (
            <PageLayout header={<BodyHeader title={pageTitle} panels={bodyHeaderPanels} toolbar={bodyHeaderToolbar} />}>
                <BeneficiariesBreadcrumb />
                <Row>
                    <Col xs={12}>
                        <SearchForm />
                    </Col>
                    <Col xs={12}>
                        <ResultSearch />
                        <BeneficiaireHistory />
                    </Col>
                </Row>
            </PageLayout>
        );
    }
}

BeneficiariesComponent.propTypes = {
    t: PropTypes.func,
    bodyHeaderToolbar: PropTypes.shape({}),
    bodyHeaderPanels: PropTypes.arrayOf(PropTypes.shape({})),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiariesComponent;
