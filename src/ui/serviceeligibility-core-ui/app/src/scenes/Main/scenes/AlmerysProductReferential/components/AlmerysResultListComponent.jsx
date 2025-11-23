import React, { Component, Fragment } from 'react';
import autobind from 'autobind-decorator';
import PropTypes from 'prop-types';
import {
    Button,
    CgdTable,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import style from '../../ParametrageDroits/style.module.scss';
import StringUtils from '../../../../../common/utils/StringUtils';
import AlmerysResult from './AlmerysResult';

@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class AlmerysResultListComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            modalDetailsAlmerysProduct: false,
            product: null,
            isFirstRun: true,
        };
    }

    getColumns() {
        const { t } = this.props;
        return [
            this.generateCommonColumn('code', '40%'),
            this.generateCommonColumn('description', '40%'),
            {
                Header: t('almerysProduct.actions'),
                id: 'actions',
                width: '20%',
                accessor: 'actions',
                disableSortBy: true,
                Cell: (props) => (
                    <div className={style['align-table-elements']}>{this.renderDetails(props.row.original)}</div>
                ),
            },
        ];
    }
    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`almerysProduct.${key}`), false),
            accessor: key,
            id: key,
            width,
            key,
        };
    }
    toggleModalDetailsAlmerysProduct(item) {
        this.setState((prevState) => ({
            modalDetailsAlmerysProduct: !prevState.modalDetailsAlmerysProduct,
            product: item,
        }));
    }

    @autobind
    handleProductUpdated(updatedProduct) {
        const { almerysProductCombinationList, productListUpdated } = this.props;
        const updateAlmerysProductCombinationList = almerysProductCombinationList.map((product) =>
            product.code === updatedProduct.code ? updatedProduct : product,
        );
        this.setState({
            product: updatedProduct,
        });
        productListUpdated(updateAlmerysProductCombinationList);
    }
    @autobind
    handleCombiAdded() {
        const { reloadList } = this.props;
        reloadList();
    }

    @autobind
    fetchData(data) {
        const { pageSize, pageIndex, sortBy } = data;
        const { searchAlmerysProduct, bodyCriterias } = this.props;
        const { isFirstRun } = this.state;

        bodyCriterias.page = pageIndex + 1;
        bodyCriterias.perPage = pageSize;

        if (isFirstRun) {
            this.setState({ isFirstRun: false });
        } else {
            if (sortBy && sortBy.length > 0) {
                bodyCriterias.sortBy = sortBy[0].id;
                bodyCriterias.direction = sortBy[0].desc ? 'desc' : 'asc';
            }
            searchAlmerysProduct(bodyCriterias);
        }
    }

    filterDeletedGTs(bodyCriterias) {
        const { almerysProductCombinationList } = this.props;
        const { gts } = bodyCriterias;
        if (gts && gts.length > 0) {
            const productsToDisplay = [];
            (almerysProductCombinationList || []).forEach((product) => {
                const { productCombinations } = product;
                let showProduct = false;
                (productCombinations || []).forEach((combi) => {
                    const { garantieTechniqueList } = combi;
                    (garantieTechniqueList || []).forEach((gt) => {
                        if (gts.includes(gt.codeGarantie) && !gt.dateSuppressionLogique) {
                            showProduct = true;
                        }
                    });
                });
                if (showProduct) {
                    productsToDisplay.push(product);
                }
            });
            return productsToDisplay;
        }
        return almerysProductCombinationList;
    }
    filterDeletedLots(bodyCriterias) {
        const { almerysProductCombinationList } = this.props;
        const { lots } = bodyCriterias;
        if (lots && lots.length > 0) {
            const productsToDisplay = [];
            (almerysProductCombinationList || []).forEach((product) => {
                const { productCombinations } = product;
                let showProduct = false;
                (productCombinations || []).forEach((combi) => {
                    const { lotAlmerysList } = combi;
                    (lotAlmerysList || []).forEach((lot) => {
                        if (lots.includes(lot.code) && !lot.dateSuppressionLogique) {
                            showProduct = true;
                        }
                    });
                });
                if (showProduct) {
                    productsToDisplay.push(product);
                }
            });
            return productsToDisplay;
        }
        return almerysProductCombinationList;
    }

    renderDetails(item) {
        const { t } = this.props;
        return (
            <Button
                id="details-button"
                outlineNoBorder
                behavior="primary"
                onClick={() => this.toggleModalDetailsAlmerysProduct(item)}
            >
                {`${t('details')} `}
                <CgIcon name="right-scroll" />
            </Button>
        );
    }

    render() {
        const { almerysProductCombinationList, showDeletedGTs, showDeletedLots, allLot, pagination, bodyCriterias } =
            this.props;
        const { modalDetailsAlmerysProduct, product } = this.state;
        const { totalPages } = pagination || {};
        let productsToDisplay = almerysProductCombinationList;
        if (!showDeletedGTs) {
            productsToDisplay = this.filterDeletedGTs(bodyCriterias);
        }
        if (!showDeletedLots) {
            productsToDisplay = this.filterDeletedLots(bodyCriterias);
        }

        return (
            <Fragment>
                <Row>
                    <Col className="ml-3 pl-3 mr-3 mb-1">
                        <Row>
                            <CgdTable
                                id="almerysProductCombinationTable"
                                data={productsToDisplay || []}
                                columns={this.getColumns()}
                                fetchData={this.fetchData}
                                pageCount={totalPages}
                                showPagination={totalPages > 1}
                                initialPageIndex={0}
                                manual
                            />
                        </Row>
                    </Col>
                </Row>
                <Modal
                    size="full-width"
                    isOpen={modalDetailsAlmerysProduct}
                    toggle={() => this.toggleModalDetailsAlmerysProduct()}
                    backdrop="static"
                >
                    <ModalHeader toggle={() => this.toggleModalDetailsAlmerysProduct()}>
                        {`${(product || {}).code} ${(product || {}).description}`}
                    </ModalHeader>
                    <ModalBody>
                        <AlmerysResult
                            toggle={() => this.toggleModalDetailsAlmerysProduct(product)}
                            productCombinations={(product || {}).productCombinations}
                            product={product}
                            allLot={allLot}
                            productUpdated={this.handleProductUpdated}
                            combiAdded={this.handleCombiAdded}
                            fromAlmerysResultList
                        />
                    </ModalBody>
                </Modal>
            </Fragment>
        );
    }
}

AlmerysResultListComponent.propTypes = {
    t: PropTypes.func,
    searchAlmerysProduct: PropTypes.func.isRequired,
    almerysProductCombinationList: PropTypes.arrayOf(PropTypes.shape()),
    pagination: PropTypes.shape(),
    bodyCriterias: PropTypes.shape().isRequired,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AlmerysResultListComponent;
