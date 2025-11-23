/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Panel,
    Row,
    Col,
    Modal,
    ModalHeader,
    ModalBody,
    PanelSection,
    PanelFooter,
    Button,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import formConstants from '../../Constants';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import CreateProductCombination from '../CreateProductCombination/CreateProductCombination';
import AlmerysResult from '../AlmerysResult';
import style from '../../style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class CreateAlmerysProductComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // eslint-disable-next-line react/no-unused-state
            body: {
                page: 1,
                perPage: 10,
            },
            modalCreateProductComb: false,
            productCombinations: [],
        };
    }

    modifyBody(body, values) {
        const { productCombinations } = this.state;
        if (values.code) {
            body.code = values.code;
        }
        if (values.description) {
            body.description = values.description;
        }
        if (productCombinations) {
            body.productCombinations = productCombinations;
        }
        return body;
    }

    @autobind
    handleSubmit(values) {
        const {
            t,
            createAlmerysProduct,
            onAlmerysProductCreated,
            addAlert,
            addAlertError,
            almerysProductCombinationList,
        } = this.props;
        const body = {};
        const { productCombinations } = this.state;
        const bodyToSend = this.modifyBody(body, values);

        if (productCombinations.length < 1) {
            addAlertError({
                message: t('almerysProduct.erreurCreationAlmerysProduct'),
                behavior: 'danger',
            });
        } else if (almerysProductCombinationList.find((almerysProduct) => almerysProduct.code === bodyToSend.code)) {
            addAlertError({
                message: t('almerysProduct.erreurAlmerysProductExistant'),
                behavior: 'danger',
            });
        } else {
            createAlmerysProduct(body).then(() => {
                addAlert({
                    message: t('almerysProduct.createdAlmerysProduct'),
                });
                onAlmerysProductCreated();
            });
        }
    }
    toggleModalCreateProductCombination() {
        this.setState((prevState) => ({
            modalCreateProductComb: !prevState.modalCreateProductComb,
        }));
    }

    @autobind
    handleProductCombinationCreated(combination) {
        const { productCombinations, modalCreateProductComb } = this.state;
        const updatedCombinations = [...productCombinations, combination];
        this.setState({
            productCombinations: updatedCombinations,
            modalCreateProductComb: !modalCreateProductComb,
        });
    }

    render() {
        const { t, handleSubmit, invalid, allLot, code, description } = this.props;
        const { productCombinations, modalCreateProductComb } = this.state;
        const product = {
            code,
            description,
            productCombinations,
        };

        return (
            <Fragment>
                <form onSubmit={handleSubmit(this.handleSubmit)}>
                    <Panel border={false}>
                        <PanelSection>
                            <Row>
                                <Col xs={5}>
                                    <Field
                                        id="create-code"
                                        key={formConstants.FIELDS.code}
                                        name={formConstants.FIELDS.code}
                                        component={CommonInput}
                                        label={t('almerysProduct.code')}
                                        placeholder={t('almerysProduct.codePlaceholder')}
                                        clearable
                                        validate={required}
                                        showRequired
                                    />
                                </Col>
                                <Col xs={5}>
                                    <Field
                                        id="create-description"
                                        key={formConstants.FIELDS.description}
                                        name={formConstants.FIELDS.description}
                                        component={CommonInput}
                                        label={t('almerysProduct.description')}
                                        placeholder={t('almerysProduct.descriptionPlaceholder')}
                                        clearable
                                        validate={required}
                                        showRequired
                                    />
                                </Col>
                            </Row>
                            <AlmerysResult
                                productCombinations={productCombinations}
                                allLot={allLot}
                                productCombinationCreated={this.handleProductCombinationCreated}
                                product={product}
                                creatingProduct
                            />
                        </PanelSection>
                    </Panel>
                    <PanelFooter>
                        <Button
                            behavior="primary"
                            disabled={modalCreateProductComb}
                            type="button"
                            onClick={() => this.toggleModalCreateProductCombination()}
                        >
                            {t('almerysProduct.addProductCombination')}
                        </Button>
                        <Button
                            behavior="primary"
                            type="submit"
                            disabled={invalid || (productCombinations || []).length < 1 || modalCreateProductComb}
                        >
                            {t('validate')}
                        </Button>
                    </PanelFooter>
                </form>
                <Modal
                    size="md"
                    isOpen={modalCreateProductComb}
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

CreateAlmerysProductComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    addAlert: PropTypes.func,
    addAlertError: PropTypes.func,
    invalid: PropTypes.bool,
    almerysProductCombinationList: PropTypes.arrayOf(PropTypes.shape()),
    allLot: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.CREATE_FORM_NAME,
})(CreateAlmerysProductComponent);
