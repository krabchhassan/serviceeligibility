/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    LoadingSpinner,
    Panel,
    PanelFooter,
    PanelHeader,
    PanelSection,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import ElasticSearch from '../../../../../../common/resources/ElasticSearch';
import Beneficiaries from '../../../../../../common/resources/Beneficiaries';
import SearchResultItem from '../ResultSearch/components/SearchResultItem';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class BeneficiaireHistoryComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            historyBenefs: [],
            loading: true,
        };
    }

    componentDidMount() {
        Beneficiaries.beneficiaireHistory()
            .get()
            .then(
                (result) => {
                    const beneficiaries = result.body(false).beneficiaires;
                    const idsWithBoolean = beneficiaries.map((benef) => {
                        const [booleanKey] = Object.keys(benef);
                        return {
                            id: benef[booleanKey],
                            externalOrigin: booleanKey === 'true',
                        };
                    });

                    // Extraire uniquement les IDs pour la recherche Elasticsearch
                    const ids = idsWithBoolean.map((item) => item.id);

                    if (ids && ids.length > 0) {
                        ElasticSearch.getIds(ids)
                            .then((benefs) => {
                                const ben = (benefs || []).map((item) => {
                                    const externalEnv = idsWithBoolean.find((b) => b.id === item.id)?.externalOrigin;

                                    return {
                                        metaMap: { ...item },
                                        id: item.id,
                                        fromExternalEnv: externalEnv,
                                    };
                                });

                                const orderedBenefs = [];
                                ids.forEach((id) => {
                                    const found = ben.find((benef) => benef.id === id);
                                    if (found) {
                                        orderedBenefs.push(found);
                                    }
                                });

                                this.setState({ historyBenefs: orderedBenefs, loading: false });
                            })
                            .catch((error) => {
                                console.error(error);
                            });
                    } else {
                        this.setState({ loading: false });
                    }
                },
                () => {
                    this.setState({ loading: false });
                },
            )
            .catch((error) => {
                console.error(error);
            });
    }

    returnResultComponent() {
        const { t, serviceMetierList, declarants } = this.props;
        const { historyBenefs } = this.state;

        return (
            <PanelSection className="pl-0 pr-0">
                {(historyBenefs || []).length > 0
                    ? historyBenefs.map((item) => (
                          <SearchResultItem
                              key={item.id}
                              declarants={declarants}
                              item={item}
                              fromExternalEnv={item.fromExternalEnv}
                              serviceMetierList={serviceMetierList}
                          />
                      ))
                    : t('noData')}
            </PanelSection>
        );
    }

    render() {
        const { t, serviceMetierList } = this.props;
        const { historyBenefs, loading } = this.state;

        const totalHistory = historyBenefs.length;
        const footerComment = `${totalHistory} ${t(`beneficiaries.noBenefHistory`)}`;

        if (loading) {
            return <LoadingSpinner type="container" iconSize="3x" iconType="circle-o-notch" behavior="primary" />;
        }

        return (
            <Panel
                header={
                    <PanelHeader
                        title={t('beneficiaries.resultHistory.pageTitle')}
                        counter={totalHistory || 0}
                        className="pl-0 pr-0"
                    />
                }
                footer={<PanelFooter comment={footerComment} />}
                id="history-panel"
                className="bg-background-variant-1"
                border={false}
            >
                {JSON.stringify(serviceMetierList) === '[]' || !serviceMetierList ? null : this.returnResultComponent()}
            </Panel>
        );
    }
}

BeneficiaireHistoryComponent.propTypes = {
    t: PropTypes.func,
    serviceMetierList: PropTypes.arrayOf(PropTypes.shape()),
    declarants: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiaireHistoryComponent;
