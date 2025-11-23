/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    BodyHeader,
    DesktopBreadcrumbPart,
    PageLayout,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import VolumetrySearchForm from './components/VolumetrySearchForm/VolumetrySearchForm';
import VolumetryResultSearch from './components/VolumetryResultSearch/VolumetryResultSearch';
import style from './style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class VolumetryComponent extends Component {
    constructor(props) {
        super(props);
        props.getAllVolumetryData();
    }

    componentDidMount() {
        const { getAllVolumetryData } = this.props;
        getAllVolumetryData();
    }

    render() {
        const { t } = this.props;
        const pageTitle = t('volumetry.title');

        return (
            <PageLayout header={<BodyHeader title={pageTitle} />} className={style['flex-column-page-layout']}>
                <DesktopBreadcrumbPart label={t('breadcrumb.volumetry')} />
                <VolumetrySearchForm />
                <VolumetryResultSearch />
            </PageLayout>
        );
    }
}

VolumetryComponent.propTypes = {
    t: PropTypes.func,
    getAllVolumetryData: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default VolumetryComponent;
