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
class AlmerysResultList extends Component {
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
        const { almerysProductCombinationList, productListUpdated, pagination } = this.props;
        const { page } = pagination;
        const updateAlmerysProductCombinationList = almerysProductCombinationList.map((product) =>
            product.code === updatedProduct.code ? updatedProduct : product,
        );
        this.setState({
            product: updatedProduct,
        });
        productListUpdated(updateAlmerysProductCombinationList, page);
    }

    @autobind
    handleCombiAdded() {
        const { reloadList, pagination } = this.props;
        const { page } = pagination;
        reloadList(page);
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
        const { almerysProductCombinationList, allLot, pagination } = this.props;
        const { modalDetailsAlmerysProduct, product } = this.state;
        const { totalPages } = pagination || {};

        return (
            <Fragment>
                <Row>
                    <Col className="ml-3 pl-3 mr-3 mb-1">
                        <Row>
                            <CgdTable
                                id="almerysProductCombinationTable"
                                data={almerysProductCombinationList || []}
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

AlmerysResultList.propTypes = {
    t: PropTypes.func,
    searchAlmerysProduct: PropTypes.func.isRequired,
    almerysProductCombinationList: PropTypes.arrayOf(PropTypes.shape()),
    pagination: PropTypes.shape(),
    bodyCriterias: PropTypes.shape().isRequired,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AlmerysResultList;
