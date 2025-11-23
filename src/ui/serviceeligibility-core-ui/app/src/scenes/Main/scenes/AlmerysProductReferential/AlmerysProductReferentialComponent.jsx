/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import autobind from 'autobind-decorator';
import _ from 'lodash';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import {
    BodyHeader,
    BreadcrumbPart,
    Button,
    Col,
    Filtering,
    Modal,
    ModalBody,
    ModalHeader,
    PageLayout,
    PanelFooter,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import ParametrageBreadcrumb from '../Parametrage/components/ParametrageBreadcrumb';
import { CommonInput, CommonMultiCombobox } from '../../../../common/utils/Form/CommonFields';
import CreateAlmerysProduct from './components/CreateAlmerysProduct/CreateAlmerysProduct';
import AlmerysResultList from './components/AlmerysResultList';
import PermissionConstants from '../../PermissionConstants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class AlmerysProductReferentialComponent extends Component {
    constructor(props) {
        super(props);
        this.autocompleteGTs = _.debounce(this.autocompleteGTsCall, 1000);
        this.autocompleteLots = _.debounce(this.autocompleteLotsCall, 1000);
        this.state = {
            chips: [],
            expanded: true,
            pageSizeState: 10,
            body: {
                page: 1,
            },
            modalCreateAlmerysProduct: false,
            showDeletedGTs: false,
            showDeletedLots: false,
        };
    }

    componentDidMount() {
        const { searchAlmerysProduct, getAllLot, getAllGT } = this.props;
        const { pageSizeState } = this.state;
        getAllGT();
        getAllLot();
        const body = {
            page: 1,
            perPage: pageSizeState,
        };
        searchAlmerysProduct(body);
    }

    componentWillReceiveProps(nextProps) {
        const { garantiesTechniques, lots } = nextProps;
        const chips = new Set();

        if (garantiesTechniques) {
            (garantiesTechniques || []).forEach((item) => {
                chips.add({ key: item.value, value: item.label });
            });
        }
        if (lots) {
            (lots || []).forEach((item) => {
                chips.add({ key: item.value, value: item.label });
            });
        }

        const newChips = [...chips];

        this.setState({
            chips: newChips,
        });
    }

    @autobind
    onCloseChip(chip) {
        const { garantiesTechniques, lots, change } = this.props;
        const { chips } = this.state;

        const newChips = chips.filter((elt) => elt.key !== chip.key);
        let newGarantiesTechniques = Array.isArray(garantiesTechniques) ? [...garantiesTechniques] : [];
        let newLots = Array.isArray(lots) ? [...lots] : [];

        newGarantiesTechniques = newGarantiesTechniques.filter((item) => item.value !== chip.key);

        newLots = newLots.filter((item) => item.value !== chip.key);

        const updatedFilters = {
            garantiesTechniques: newGarantiesTechniques,
            lots: newLots,
        };

        change('garantiesTechniques', newGarantiesTechniques);
        change('lots', newLots);

        this.setState({ chips: newChips });
        this.onSubmit(updatedFilters);
    }

    @autobind
    onSubmit(values) {
        const { searchAlmerysProduct } = this.props;

        const body = {
            page: 1,
        };
        if (values.productCode) {
            body.code = values.productCode;
        }
        if (values.description) {
            body.description = values.description;
        }
        if (values.gts) {
            body.gts = [];
            values.gts.forEach((gt) => {
                if (gt.number && gt.value) {
                    body.gts.push(`${gt.number}#${gt.value}`);
                }
            });
        }
        body.showDeletedGts = values.showDeletedGts ? values.showDeletedGts : false;
        if (values.lots) {
            body.lots = [];
            values.lots.forEach((lot) => {
                if (lot.value) {
                    body.lots.push(lot.value);
                }
            });
        }
        body.showDeletedLots = values.showDeletedLots ? values.showDeletedLots : false;
        this.setState({ body });
        searchAlmerysProduct(body);
    }

    @autobind
    setExpanded(a) {
        this.setState({ expanded: a });
    }

    @autobind
    handleAlmerysProductCreated() {
        const { searchAlmerysProduct } = this.props;
        const body = {
            page: 1,
        };

        this.toggleModalCreateAlmerysProduct();
        searchAlmerysProduct(body);
    }

    @autobind
    handleAlmerysProductUpdated(numPage) {
        const { searchAlmerysProduct } = this.props;
        const body = {
            page: numPage,
        };

        searchAlmerysProduct(body);
    }

    @autobind
    resetSearchFunction() {
        const { reset } = this.props;
        reset();
    }

    @autobind
    collapse() {
        const { expanded } = this.state;
        this.setExpanded(!expanded);
    }

    @autobind
    fetchData(dat) {
        const { pageSize, pageIndex, sortBy } = dat;
        const { searchAlmerysProduct } = this.props;
        const { body } = this.state;

        body.page = pageIndex + 1;
        body.perPage = pageSize;

        if (sortBy && sortBy.length > 0) {
            body.sortBy = sortBy[0].id;
            body.direction = sortBy[0].desc ? 'desc' : 'asc';
        }

        this.setState({ body });
        searchAlmerysProduct(body);
    }

    autocompleteGTsCall(input) {
        const { getAllGT } = this.props;
        if (input && input.length > 2) {
            const searchCriteria = {};
            searchCriteria.search = input;
            getAllGT(searchCriteria);
        }
    }

    autocompleteLotsCall(input) {
        const { getAllLot } = this.props;
        if (input && input.length > 2) {
            const searchCriteria = {};
            searchCriteria.search = input;
            getAllLot(input);
        }
    }

    toggleModalCreateAlmerysProduct() {
        this.setState((prevState) => ({
            modalCreateAlmerysProduct: !prevState.modalCreateAlmerysProduct,
        }));
    }

    addChip = (key, value) => {
        const { chips } = this.state;
        const chipExists = chips.some((chip) => chip.key === key);
        if (!chipExists) {
            const newChips = [...chips, { key, value }];
            this.setState({ chips: newChips });
        }
    };

    removeChip = (key) => {
        const { chips } = this.state;
        const newChips = chips.filter((chip) => chip.key !== key);
        this.setState({ chips: newChips });
    };

    handleShowDeletedGTsChange = () => {
        this.setState(
            (prevState) => ({
                showDeletedGTs: !prevState.showDeletedGTs,
            }),
            () => {
                if (this.state.showDeletedGTs) {
                    this.addChip('showDeletedGTS', 'Affichage des GT supprimées');
                } else {
                    this.removeChip('showDeletedGTs');
                }
            },
        );
    };
    handleShowDeletedLotsChange = () => {
        this.setState(
            (prevState) => ({
                showDeletedLots: !prevState.showDeletedLots,
            }),
            () => {
                if (this.state.showDeletedLots) {
                    this.addChip('showDeletedLots', 'Affichage des lots supprimés');
                } else {
                    this.removeChip('showDeletedLots');
                }
            },
        );
    };

    @autobind
    handleProductListUpdated(updatedProductList, numPage) {
        const { searchAlmerysProduct } = this.props;
        const { pageSizeState } = this.props;
        if (updatedProductList) {
            const body = {
                page: numPage,
                perPage: pageSizeState,
            };
            searchAlmerysProduct(body);
        }
    }

    render() {
        const {
            t,
            handleSubmit,
            invalid,
            comboLabelsAllGT,
            comboLabelsAllLot,
            almerysProductCombinationList,
            allLot,
            pagination,
            searchAlmerysProduct,
        } = this.props;
        const { expanded, chips, modalCreateAlmerysProduct, body } = this.state;
        const toolbar = {
            label: t('headers:home.actions'),
            items: [
                {
                    label: t('almerysProduct.createAlmerysProduct'),
                    action: () => this.toggleModalCreateAlmerysProduct(),
                    allowedPermissions: PermissionConstants.CREATE_LOT_PERMISSION,
                },
            ],
        };

        return (
            <form onSubmit={handleSubmit(this.onSubmit)}>
                <PageLayout header={<BodyHeader title={t('almerysProduct.pageTitle')} toolbar={toolbar} />}>
                    <BreadcrumbPart label={t('breadcrumb.almerysProduct')} parentPart={<ParametrageBreadcrumb />} />
                    <Filtering
                        chips={chips}
                        onCollapseClick={this.collapse}
                        expanded={expanded}
                        onCloseChip={this.onCloseChip}
                        filterLabel={t('filter')}
                    >
                        <PanelSection>
                            <Row>
                                <Col>
                                    <Field
                                        id="product-code"
                                        name="productCode"
                                        component={CommonInput}
                                        label={t('almerysProduct.code')}
                                        placeholder={t('almerysProduct.codePlaceholder')}
                                        clearable
                                    />
                                </Col>
                                <Col>
                                    <Field
                                        id="description"
                                        name="description"
                                        component={CommonInput}
                                        label={t('almerysProduct.description')}
                                        placeholder={t('almerysProduct.descriptionPlaceholder')}
                                        clearable
                                    />
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <Field
                                        id="garanties-techniques"
                                        name="gts"
                                        component={CommonMultiCombobox}
                                        label={t('almerysProduct.gts')}
                                        options={comboLabelsAllGT}
                                        placeholder={t('almerysProduct.gtsPlaceholder')}
                                        onInputChange={(input) => this.autocompleteGTsCall(input)}
                                        clearable
                                        searchable
                                        multi
                                    />
                                    <Field
                                        name="showDeletedGts"
                                        component={CommonInput}
                                        label={t('almerysProduct.gtsFilter')}
                                        type="checkbox"
                                        onChange={this.handleShowDeletedGTsChange}
                                    />
                                </Col>
                                <Col>
                                    <Field
                                        id="lots"
                                        name="lots"
                                        component={CommonMultiCombobox}
                                        label={t('almerysProduct.lots')}
                                        options={comboLabelsAllLot}
                                        placeholder={t('almerysProduct.lotsPlaceholder')}
                                        onInputChange={(input) => this.autocompleteLotsCall(input)}
                                        clearable
                                        searchable
                                        multi
                                    />
                                    <Field
                                        name="showDeletedLots"
                                        component={CommonInput}
                                        label={t('almerysProduct.lotsFilter')}
                                        type="checkbox"
                                        onChange={this.handleShowDeletedLotsChange}
                                    />
                                </Col>
                            </Row>
                        </PanelSection>
                        <PanelFooter>
                            <Button behavior="primary" type="submit" disabled={invalid}>
                                {t('search')}
                            </Button>
                        </PanelFooter>
                    </Filtering>
                    <br />
                    <AlmerysResultList
                        almerysProductCombinationList={almerysProductCombinationList}
                        allLot={allLot}
                        productListUpdated={this.handleProductListUpdated}
                        searchAlmerysProduct={searchAlmerysProduct}
                        pagination={pagination}
                        bodyCriterias={body}
                        reloadList={this.handleAlmerysProductUpdated}
                    />
                    <Modal
                        size="full-width"
                        isOpen={modalCreateAlmerysProduct}
                        toggle={() => this.toggleModalCreateAlmerysProduct()}
                        backdrop="static"
                    >
                        <ModalHeader toggle={() => this.toggleModalCreateAlmerysProduct()}>
                            {t('almerysProduct.addAlmerysProduct')}
                        </ModalHeader>
                        <ModalBody>
                            <CreateAlmerysProduct
                                onAlmerysProductCreated={this.handleAlmerysProductCreated}
                                allLot={allLot}
                            />
                        </ModalBody>
                    </Modal>
                </PageLayout>
            </form>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    getAllGT: PropTypes.func.isRequired,
    getAllLot: PropTypes.func.isRequired,
    searchAlmerysProduct: PropTypes.func.isRequired,
    almerysProductCombinationList: PropTypes.arrayOf(PropTypes.shape()),
    pagination: PropTypes.shape(),
};

// Default props
const defaultProps = {};

// Add prop types
AlmerysProductReferentialComponent.propTypes = propTypes;
// Add default props
AlmerysProductReferentialComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: 'almerysProductRefSearch',
})(AlmerysProductReferentialComponent);
