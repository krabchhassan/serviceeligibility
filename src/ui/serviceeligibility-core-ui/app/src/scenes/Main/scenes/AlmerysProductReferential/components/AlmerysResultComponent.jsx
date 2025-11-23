import React, { Component, Fragment } from 'react';
import autobind from 'autobind-decorator';
import isEqual from 'lodash/isEqual';
import {
    Button,
    CgdTable,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Tooltip,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import PropTypes from 'prop-types';
import StringUtils from '../../../../../common/utils/StringUtils';
import DateUtils from '../../../../../common/utils/DateUtils';
import businessUtils from '../../../../../common/utils/businessUtils';
import EditProductCombination from './EditProductCombination/EditProductCombination';
import CreateProductCombination from './CreateProductCombination/CreateProductCombination';
import PermissionConstants from '../../../PermissionConstants';
import style from '../style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getGtsForCombobox = (list) => {
    return (list || [])
        .filter((item) => !item.dateSuppressionLogique)
        .map((item) => ({
            label: `${(item || {}).codeAssureur} - ${(item || {}).codeGarantie}`,
            value: (item || {}).codeGarantie,
            number: (item || {}).codeAssureur,
        }));
};

const getLotsForCombobox = (list, allLot) => {
    return (list || [])
        .filter((item) => !item.dateSuppressionLogique)
        .map((item) => {
            const lotAlmerys = (allLot || []).find((lot) => lot.code === item.code);
            return {
                label: `${(lotAlmerys || {}).code} - ${(lotAlmerys || {}).libelle}`,
                value: (lotAlmerys || {}).code,
            };
        });
};

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class AlmerysResultComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            modalCreateProductCombination: false,
            modalEditProductCombination: false,
            combination: null,
            index: 0,
            product: props.product,
        };
    }

    componentDidUpdate(prevProps) {
        const { product } = this.props;
        if (!isEqual(prevProps.product, product)) {
            this.setProduct(product);
        }
    }

    setProduct(product) {
        this.setState({ product });
    }

    getColumns() {
        const { t, allLot } = this.props;
        return [
            {
                Header: StringUtils.splitIntoDivs(t('almerysProduct.dateDebut'), false),
                accessor: 'dateDebut',
                id: 'dateDebut',
                width: '15%',
                Cell: (props) => <div>{DateUtils.transformDateForDisplay(props.value)}</div>,
            },
            {
                Header: StringUtils.splitIntoDivs(t('almerysProduct.dateFin'), false),
                id: 'dateFin',
                width: '15%',
                accessor: 'dateFin',
                Cell: (props) => <div>{DateUtils.transformDateForDisplay(props.value)}</div>,
            },
            {
                Header: StringUtils.splitIntoDivs(t('almerysProduct.garantieTechniqueList'), false),
                id: 'garantieTechniqueList',
                width: '30%',
                accessor: 'garantieTechniqueList',
                Cell: (props) => {
                    if (props.value) {
                        const deletedGTs = props.value.filter((gt) => gt.dateSuppressionLogique !== undefined);
                        const activeGTs = props.value.filter((gt) => gt.dateSuppressionLogique === undefined);

                        const activeGTsStr = businessUtils.stringifyListGTs(activeGTs);
                        const deletedGTsStr = businessUtils.stringifyListGTs(deletedGTs);
                        const tooltipId = `toolgaranties-${props.row.index}`;
                        const tooltipContent = (
                            <div>
                                <div>{activeGTsStr}</div>
                                {deletedGTs.length > 0 && (
                                    <div style={{ textDecoration: 'line-through' }}>{deletedGTsStr}</div>
                                )}
                            </div>
                        );

                        return (
                            <span>
                                <div id={tooltipId} style={{ cursor: 'pointer' }}>
                                    <div>{activeGTsStr}</div>
                                    {deletedGTs.length > 0 && (
                                        <div style={{ textDecoration: 'line-through' }}>{deletedGTsStr}</div>
                                    )}
                                </div>
                                <Tooltip placement="top" target={`#${tooltipId}`}>
                                    {tooltipContent}
                                </Tooltip>
                            </span>
                        );
                    }
                    return <div />;
                },
            },
            {
                Header: StringUtils.splitIntoDivs(t('almerysProduct.lotList'), false),
                id: 'lotAlmerysList',
                width: '35%',
                accessor: 'lotAlmerysList',
                Cell: (props) => {
                    if (props.value) {
                        const deletedLots = props.value.filter((lot) => lot.dateSuppressionLogique !== undefined);
                        const activeLots = props.value.filter((lot) => lot.dateSuppressionLogique === undefined);
                        const deletedIdLots = deletedLots.map((lot) => lot.code);
                        const activeIdLots = activeLots.map((lot) => lot.code);

                        const tooltipId = `toollots-${props.row.index}`;
                        const tooltipContent = (
                            <div>
                                <div>{businessUtils.stringifyListLotsForAlm(activeIdLots, allLot)}</div>
                                {deletedLots.length > 0 && (
                                    <div style={{ textDecoration: 'line-through' }}>
                                        {businessUtils.stringifyListLotsForAlm(deletedIdLots, allLot)}
                                    </div>
                                )}
                            </div>
                        );

                        return (
                            <span>
                                <div id={tooltipId} style={{ cursor: 'pointer' }}>
                                    <div>{businessUtils.stringifyListLotsForAlm(activeIdLots, allLot)}</div>
                                    {deletedLots.length > 0 && (
                                        <div style={{ textDecoration: 'line-through' }}>
                                            {businessUtils.stringifyListLotsForAlm(deletedIdLots, allLot)}
                                        </div>
                                    )}
                                </div>
                                <Tooltip placement="top" target={`#${tooltipId}`}>
                                    {tooltipContent}
                                </Tooltip>
                            </span>
                        );
                    }
                    return <div />;
                },
            },
            {
                Header: t('almerysProduct.actions'),
                id: 'actions',
                width: '10%',
                allowedPermissions: PermissionConstants.CREATE_LOT_PERMISSION,
                accessor: (rowValues, rowIndex) => {
                    return (
                        <div className="d-flex justify-content-center">
                            <Button
                                onClick={() => this.toggleEditProductCombination(rowValues, rowIndex)}
                                outlineNoBorder
                                behavior="secondary"
                                type="button"
                            >
                                <CgIcon name="pencil" className="default" />
                            </Button>
                        </div>
                    );
                },
            },
        ];
    }

    toggleEditProductCombination(item, index) {
        const { allLot } = this.props;
        this.setState((prevState) => ({
            modalEditProductCombination: !prevState.modalEditProductCombination,
            index,
            combination: {
                ...item,
                garantieTechniqueList: item.garantieTechniqueList ? getGtsForCombobox(item.garantieTechniqueList) : [],
                lotAlmerysList: item.lotAlmerysList ? getLotsForCombobox(item.lotAlmerysList, allLot) : [],
            },
        }));
    }

    toggleModalEditProductCombination() {
        this.setState((prevState) => ({
            modalEditProductCombination: !prevState.modalEditProductCombination,
        }));
    }

    toggleModalCreateProductCombination() {
        this.setState((prevState) => ({
            modalCreateProductCombination: !prevState.modalCreateProductCombination,
        }));
    }

    @autobind
    handleProductCombinationEdited(product) {
        const { updatedPdt, creatingProduct, productUpdated } = this.props;
        const { modalEditProductCombination } = this.state;
        this.setState({
            product: creatingProduct ? product : updatedPdt,
            modalEditProductCombination: !modalEditProductCombination,
        });
        if (!creatingProduct) {
            productUpdated(this.state.product);
        }
    }

    @autobind
    handleProductCombinationCreated(combination) {
        // ICI
        const { t, updateAlmerysProduct, addAlert, combiAdded } = this.props;
        const { modalCreateProductCombination, product } = this.state;
        const updatedProduct = {
            ...product,
            productCombinations: [...product.productCombinations, combination],
        };
        updateAlmerysProduct(updatedProduct)
            .then((response) => {
                this.setState({
                    product: response?.action?.payload?.body(false),
                    modalCreateProductCombination: !modalCreateProductCombination,
                });
                addAlert({
                    message: t('almerysProduct.updatedAlmerysProduct'),
                });
                combiAdded();
            })
            .catch(() => {
                console.error('Erreur lors de la mise à jour du produit Almerys');
            });
    }

    render() {
        const { t, fromAlmerysResultList, creatingProduct } = this.props;
        const { modalEditProductCombination, modalCreateProductCombination, combination, index, product } = this.state;

        return (
            <Fragment>
                <Panel
                    key="resultItem"
                    id="resultItem"
                    onCollapseClick={this.toggleCollapse}
                    header={<PanelHeader title={t('almerysProduct.combinations')} />}
                    border={false}
                >
                    <PanelSection>
                        <Row>
                            <Col className="ml-3 pl-3 mr-3 mb-1">
                                <Row>
                                    <CgdTable
                                        id="combinationTable"
                                        data={(product || {}).productCombinations}
                                        columns={this.getColumns()}
                                        showPageSizeOptions
                                        showPagination={(product || {}).productCombinations?.length > 5}
                                        initialPageSize={5}
                                    />
                                </Row>
                            </Col>
                        </Row>
                        {fromAlmerysResultList && (
                            <Row className="mt-4">
                                <Col className="d-flex justify-content-start">
                                    <Button
                                        behavior="primary"
                                        disabled={modalCreateProductCombination}
                                        type="button"
                                        onClick={() => this.toggleModalCreateProductCombination()}
                                    >
                                        {t('almerysProduct.addProductCombination')}
                                    </Button>
                                </Col>
                            </Row>
                        )}
                    </PanelSection>
                </Panel>
                <Modal
                    size="md"
                    isOpen={modalEditProductCombination}
                    toggle={() => this.toggleModalEditProductCombination()}
                    backdrop="static"
                    className={style['modal-in-modal']}
                >
                    <ModalHeader toggle={() => this.toggleModalEditProductCombination()}>
                        {`${t('almerysProduct.productCombinationEdition')} ${DateUtils.transformDateForDisplay(
                            (combination || {}).dateDebut,
                        )}`}
                    </ModalHeader>
                    <ModalBody>
                        <EditProductCombination
                            toggle={() => this.toggleEditProductCombination(combination)}
                            combination={combination}
                            product={product}
                            index={index}
                            onCombinationUpdated={this.handleProductCombinationEdited}
                            creatingProduct={creatingProduct}
                        />
                    </ModalBody>
                </Modal>
                <Modal
                    size="md"
                    isOpen={modalCreateProductCombination}
                    toggle={() => this.toggleModalCreateProductCombination()}
                    backdrop="static"
                    className={style['modal-in-modal']}
                >
                    <ModalHeader toggle={() => this.toggleModalCreateProductCombination()}>
                        {t('almerysProduct.addProductCombination')}
                    </ModalHeader>
                    <ModalBody>
                        <CreateProductCombination onCreatedCombination={this.handleProductCombinationCreated} />
                    </ModalBody>
                </Modal>
            </Fragment>
        );
    }
}

AlmerysResultComponent.propTypes = {
    product: PropTypes.shape(),
    allLot: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AlmerysResultComponent;
