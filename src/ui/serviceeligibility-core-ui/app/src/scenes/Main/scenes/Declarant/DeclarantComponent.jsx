/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { reduxForm } from 'redux-form';
import autobind from 'autobind-decorator';
import { isMoment } from 'moment';

import {
    BodyHeader,
    Button,
    Card,
    CardBlock,
    CardHeader,
    CardText,
    Col,
    ConfirmationModal,
    LoadingSpinner,
    Nav,
    NavItem,
    NavLink,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader,
    PageLayout,
    Row,
    TabContent,
    TabPane,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';

import history from '../../../../history';
import Identification from './components/components/Identification';
import Services from './components/Services';
import ValidationFactories from '../../../../common/utils/Form/ValidationFactories';
import DeclarantUtils from './DeclarantUtils';
import DeclarantBreadcrumb from './components/DeclarantBreadcrumb';
import './style.module.scss';
import Constants from './Constants';
import TranscodageComponent from './components/Transcodage/TranscodageComponent';
import ParametrageAMCComponent from './components/ParametrageAMC/ParametrageAMCComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const isIdUnique = (props, val, idValue) => {
    if (props.initialValues.numero === idValue) {
        return Promise.resolve();
    }

    return props.exists(idValue).then((exists) => {
        if (exists) {
            return Promise.reject(new Error('fields.uniqueCode'));
        }
        return null;
    });
};

const asyncFieldValidators = {
    numero: isIdUnique,
};

const asyncValidator = ValidationFactories.asyncValidator(asyncFieldValidators);

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
function isPeriodeValide(dateDebut, dateFin) {
    return dateDebut !== null && dateFin !== null ? dateDebut < dateFin : true;
}

function hasChevauchement(startDate1, endDate1, startDate2, endDate2) {
    return (
        (endDate1 && startDate2 <= endDate1 && (!endDate2 || endDate2 >= startDate1)) ||
        (endDate2 && startDate1 <= endDate2 && (!endDate1 || endDate1 >= startDate2)) ||
        (!endDate1 && !endDate2)
    );
}

function handleValueMessage(item2, valueMessage) {
    if (item2.conventionCible) {
        valueMessage.push('conventionTP');
        valueMessage.push(item2.reseauSoin);
        valueMessage.push(item2.domaineTP);
    } else if (item2.codeRenvoi) {
        valueMessage.push('codeRenvoi');
        valueMessage.push(item2.domaineTP);
        valueMessage.push(item2.reseauSoin);
    } else if (item2.fondCarte) {
        valueMessage.push('fondCarte');
        valueMessage.push(item2.reseauSoin);
    } else {
        valueMessage.push('domaineRegroupementTP');
        valueMessage.push(item2.domaineRegroupementTP);
        valueMessage.push(item2.codesDomainesTP);
    }
}

function validateOverlap(list, keys) {
    const valueMessage = [];
    for (let i = 0; i < list.length; i += 1) {
        for (let j = i + 1; j < list.length; j += 1) {
            const item1 = list[i];
            const item2 = list[j];

            if (keys.every((key) => item1[key] === item2[key])) {
                const startDate1 = new Date(item1.dateDebut);
                const endDate1 = item1.dateFin ? new Date(item1.dateFin) : null;
                const startDate2 = new Date(item2.dateDebut);
                const endDate2 = item2.dateFin ? new Date(item2.dateFin) : null;

                if (
                    isPeriodeValide(startDate1, endDate1) &&
                    isPeriodeValide(startDate2, endDate2) &&
                    hasChevauchement(startDate1, endDate1, startDate2, endDate2)
                ) {
                    handleValueMessage(item2, valueMessage);
                }
            }
        }
    }
    return valueMessage; // Aucun chevauchement détecté
}
function isFieldEmpty(field, itemsRequired) {
    if (field) {
        // eslint-disable-next-line no-restricted-syntax
        for (const itemRequired of itemsRequired) {
            if (
                field.some(
                    (item) =>
                        item[itemRequired] === undefined ||
                        item[itemRequired] === null ||
                        item[itemRequired].length === 0,
                )
            ) {
                return true;
            }
        }
    }
    return false;
}
function fieldsEmpty(declarant) {
    return (
        isFieldEmpty(declarant.codeRenvoiTP, ['domaineTP', 'codeRenvoi', 'dateDebut']) ||
        isFieldEmpty(declarant.conventionTP, ['reseauSoin', 'conventionCible', 'dateDebut']) ||
        isFieldEmpty(declarant.regroupementDomainesTP, ['domaineRegroupementTP', 'codesDomainesTP', 'dateDebut']) ||
        isFieldEmpty(declarant.fondCarteTP, ['reseauSoin', 'fondCarte', 'dateDebut'])
    );
}

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class DeclarantComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            feedbackModal: false,
            checked: false,
            selectedTab: 'services',
        };
    }

    componentDidMount() {
        const {
            t,
            id,
            isCreate,
            isEdit,
            getDeclarant,
            isCopy,
            getDomainesTP,
            getReseauxSoin,
            getAllReturnCodes,
            getLightDeclarants,
        } = this.props;
        let breadcrumb = t('breadcrumb.details');
        if (isCreate) {
            breadcrumb = t('breadcrumb.create');
        } else if (isEdit) {
            breadcrumb = t('breadcrumb.edit');
        } else if (isCopy) {
            breadcrumb = t('breadcrumb.copy');
        }
        this.breadcrumb = breadcrumb;

        if (!isCreate) {
            getDeclarant(id);
        }
        getDomainesTP();
        getReseauxSoin();
        getAllReturnCodes();
        getLightDeclarants();
    }

    componentWillReceiveProps(nextProps) {
        const { id, getDeclarant } = this.props;
        if (!nextProps.isCreate && id !== nextProps.id) {
            getDeclarant(nextProps.id);
        }

        if (nextProps.isCreate || nextProps.isEdit || nextProps.isCopy) {
            const emetteurInCircuit = get(nextProps.declarant, 'codeCircuit.emetteur');
            const emetteur = get(nextProps.declarant, 'emetteurDroits');
            if (emetteurInCircuit && emetteurInCircuit !== emetteur) {
                nextProps.change('emetteurDroits', emetteurInCircuit);
            }
        }
        if (nextProps.isCreate || nextProps.isEdit || nextProps.isCopy) {
            nextProps.change('pilotages[2].regroupements[0].typeConventionnement', 'IS');
            nextProps.change('pilotages[3].regroupements[0].typeConventionnement', 'SP');
        }
    }

    setPageTitle = (t, isEdit, isCreate, isCopy, duplicatedDeclarant) => {
        let pageTitle = t('declarant.details.pageTitle');
        if (isEdit) {
            pageTitle = t('declarant.edit.pageTitle');
        } else if (isCreate) {
            pageTitle = t('declarant.create.pageTitle');
        } else if (isCopy) {
            pageTitle = t('declarant.duplicatePageTitle', {
                number: (duplicatedDeclarant || {}).numero,
                name: (duplicatedDeclarant || {}).nom,
            });
        }
        return pageTitle;
    };

    setSelectedTab(tab) {
        this.setState({ selectedTab: tab });
    }

    getRegroupementErrors() {
        const { declarant } = this.props;

        const almv3Service = ((declarant || {}).pilotages || []).find((pilotage) => pilotage.nom === 'ALMV3') || {};
        const regroupementMandatoryValues = {};

        const mandatoryFields = Constants.REGROUPEMENT_MANDATORY_FIELDS;
        (almv3Service.regroupements || []).forEach((regroupement) => {
            const missingFields = Object.keys(mandatoryFields)
                .filter((field) => {
                    const missing =
                        field === Constants.REGROUPEMENT_MANDATORY_FIELDS.dateOuverture
                            ? !isMoment(regroupement[field])
                            : regroupement[field] === undefined || regroupement[field] === '';
                    if (missing) {
                        return true;
                    }
                    return false;
                })
                .map((field) => field);
            regroupementMandatoryValues[almv3Service.regroupements.indexOf(regroupement)] = {
                regroupementNumber: almv3Service.regroupements.indexOf(regroupement),
                regroupementDetaille: regroupement.critereRegroupementDetaille,
                missingFields,
            };
        });

        return Object.values(regroupementMandatoryValues).filter((value) => value.missingFields.length !== 0);
    }

    @autobind
    getReferenceFromConfirmModal(ref) {
        this.confirmRef = ref;
    }

    getTypeErrorMessages = (result) => {
        const { t, isCreate } = this.props;
        let errorMessage;

        const suffix = isCreate ? '' : 'Edit';

        switch (result[0]) {
            case 'conventionTP':
                errorMessage = t('declarant.errorUnicityConventionTP'.concat(suffix), {
                    reseauSoin: result[1],
                    domaineTP: result[2] || 'null',
                });
                break;
            case 'codeRenvoi':
                errorMessage = t('declarant.errorUnicityCodeRenvoiTP'.concat(suffix), {
                    domaineTP: result[1],
                    reseauSoin: result[2] || 'null',
                });
                break;
            case 'domaineRegroupementTP':
                errorMessage = t('declarant.errorUnicityDomaineRegroupementTP'.concat(suffix), {
                    regroupementDomaineTP: result[1],
                    codeDomainesTP: result[2].join(', '),
                });
                break;
            case 'fondCarte':
                errorMessage = t('declarant.errorUnicityFondCarteTP'.concat(suffix), {
                    reseauSoin: result[1],
                });
                break;
            default:
                errorMessage = null;
        }
        return errorMessage;
    };

    @autobind
    openModal() {
        this.confirmRef.open();
    }

    @autobind
    triggerValidation() {
        const { fields, blur } = this.props;
        Object.keys(fields || {}).forEach((key) => blur(key));
    }

    newDeclarantValid = (declarant) => {
        const errorMessages = [];

        const types = [
            { key: 'conventionTP', keys: ['reseauSoin', 'domaineTP'] },
            { key: 'codeRenvoiTP', keys: ['domaineTP', 'reseauSoin'] },
            { key: 'regroupementDomainesTP', keys: ['domaineRegroupementTP'] },
            { key: 'fondCarteTP', keys: ['reseauSoin'] },
        ];

        // eslint-disable-next-line no-restricted-syntax
        for (const { key, keys } of types) {
            const result = validateOverlap(declarant[key], keys);

            // eslint-disable-next-line no-restricted-syntax
            if (result.length > 0) {
                const errorMessage = this.getTypeErrorMessages(result);

                if (errorMessage) {
                    errorMessages.push(errorMessage);
                }
            }
        }
        return errorMessages;
    };

    @autobind
    handleSubmit(values) {
        const {
            t,
            updateDeclarant,
            createDeclarant,
            isEdit,
            isCreate,
            getDeclarant,
            isCopy,
            addAlertError,
            lightDeclarants,
            clientType,
        } = this.props;
        const { feedbackModal } = this.state;
        const transformedValues = DeclarantUtils.transformDeclarantToApi(values, clientType);
        const errorMessages = this.newDeclarantValid(transformedValues);

        const hasErrors = this.checkErrorsInRegroupementForm();
        if (hasErrors) {
            return this.toggleFeedbackModal(!feedbackModal);
        }
        if (errorMessages.length === 0) {
            if (isEdit) {
                return updateDeclarant(transformedValues).then(() => {
                    getDeclarant(values.numero);
                    return history.push(`/insurer/${values.numero}`);
                });
            } else if (isCreate || isCopy) {
                if (lightDeclarants.find((declarant) => declarant.numero === values.numero)) {
                    addAlertError({
                        message: t('fields.uniqueInsurerCode'),
                        behavior: 'danger',
                    });
                    return null;
                }
                return createDeclarant(transformedValues).then(() => history.push(`/insurer/${values.numero}`));
            }
        } else {
            errorMessages.forEach((message) =>
                addAlertError({
                    message,
                    behavior: 'danger',
                }),
            );
            return null;
        }

        this.setState({ checked: false });
        return history.push(`/insurer/${values.numero}`);
    }

    @autobind
    closeModal() {
        this.confirmRef.close();
    }

    @autobind
    confirmAnnulation() {
        const { isCreate, isEdit, isCopy, id } = this.props;
        if (isCreate) {
            history.push('/');
        } else if (isEdit || isCopy) {
            history.push(`/insurer/${id}`);
        }
    }

    checkErrorsInRegroupementForm() {
        const calculatedErrors = this.getRegroupementErrors();
        return (calculatedErrors || []).length > 0;
    }

    toggleFeedbackModal(displayFeedback) {
        this.setState({
            feedbackModal: displayFeedback,
            checked: true,
        });
    }

    renderFeedbackModal(errors) {
        const { t } = this.props;
        const { feedbackModal } = this.state;
        return (
            <Modal
                size="md"
                isOpen={feedbackModal}
                toggle={() => this.toggleFeedbackModal(!feedbackModal)}
                backdrop="static"
            >
                <ModalHeader toggle={() => this.toggleFeedbackModal(!feedbackModal)}>
                    {t('declarant.errorListTitle')}
                </ModalHeader>
                <ModalBody>
                    {(errors || []).map((error) => {
                        const regroupementNumber = parseInt(error.regroupementNumber, 10) + 1;
                        const title = t('declarant.errorRegroupementTitle', {
                            regroupementNumber,
                            regroupementDetaille: error.regroupementDetaille ? error.regroupementDetaille : '-',
                        });
                        return (
                            <Fragment>
                                <Card>
                                    <CardHeader>{title}</CardHeader>
                                    <CardBlock>
                                        <CardText>
                                            {error.missingFields.map((field) => {
                                                const fieldName = t(`declarant.${field}`);
                                                return (
                                                    <div>
                                                        {t('declarant.missingFieldMessage', {
                                                            field: fieldName,
                                                        })}
                                                    </div>
                                                );
                                            })}
                                        </CardText>
                                    </CardBlock>
                                </Card>
                            </Fragment>
                        );
                    })}
                </ModalBody>
                <ModalFooter>
                    <Button
                        className="btn-max-width-modal"
                        behavior="secondary"
                        onClick={() => this.toggleFeedbackModal(!feedbackModal)}
                    >
                        Retour
                    </Button>
                </ModalFooter>
            </Modal>
        );
    }

    render() {
        const {
            t,
            declarant,
            isEdit,
            isCreate,
            isCopy,
            handleSubmit,
            loading,
            conventions,
            circuits,
            duplicatedDeclarant,
            invalid,
            pristine,
            change,
            formName,
            domainList,
            reseauxSoin,
            returnCodesList,
            clientType,
        } = this.props;
        const { checked, selectedTab } = this.state;
        const pageTitle = this.setPageTitle(t, isEdit, isCreate, isCopy, duplicatedDeclarant);
        const hasErrors = this.checkErrorsInRegroupementForm();
        let regroupementsErrors = [];
        if (hasErrors) {
            regroupementsErrors = this.getRegroupementErrors();
        }

        let fieldEmpty = false;
        if (declarant) {
            fieldEmpty = fieldsEmpty(declarant);
        }
        return (
            <Fragment>
                <form onSubmit={handleSubmit(this.handleSubmit)}>
                    <PageLayout header={<BodyHeader title={pageTitle} />}>
                        <DeclarantBreadcrumb breadcrumb={this.breadcrumb} />

                        {loading && (
                            <LoadingSpinner
                                type="over-container"
                                iconSize="3x"
                                iconType="circle-o-notch"
                                behavior="primary"
                            />
                        )}
                        <Identification
                            isEdit={isEdit && !isCopy}
                            isCreate={isCreate || isCopy}
                            isCopy={isCopy}
                            declarant={declarant}
                            conventions={conventions}
                            circuits={circuits}
                            clientType={clientType}
                            change={change}
                        />
                        <Nav tabs>
                            <NavItem>
                                <NavLink
                                    id="tab-services"
                                    active={selectedTab === 'services'}
                                    onClick={() => this.setSelectedTab('services')}
                                >
                                    {t('declarant.serviceSectionTitle')}
                                </NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink
                                    id="tab-transcodage"
                                    active={selectedTab === 'transcodage'}
                                    onClick={() => this.setSelectedTab('transcodage')}
                                >
                                    {t('declarant.transcodageTitle')}
                                </NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink
                                    id="tab-parametrage"
                                    active={selectedTab === 'parametrage'}
                                    onClick={() => this.setSelectedTab('parametrage')}
                                >
                                    {t('declarant.parametrageTPTitle')}
                                </NavLink>
                            </NavItem>
                        </Nav>
                        <TabContent activeTab={selectedTab}>
                            <TabPane tabId="services">
                                <Services
                                    declarant={declarant}
                                    isEdit={isEdit}
                                    isCreate={isCreate || isCopy}
                                    isCopy={isCopy}
                                    conventions={conventions}
                                    domainList={domainList}
                                    change={change}
                                    formName={formName}
                                    calculatedErrors={checked && hasErrors ? regroupementsErrors : [false]}
                                />
                            </TabPane>
                            <TabPane tabId="transcodage">
                                <TranscodageComponent
                                    declarant={declarant}
                                    isEdit={isEdit}
                                    isCreate={isCreate}
                                    isCopy={isCopy}
                                    domainList={domainList}
                                />
                            </TabPane>
                            <TabPane tabId="parametrage">
                                <ParametrageAMCComponent
                                    declarant={declarant}
                                    isEdit={isEdit}
                                    isCreate={isCreate}
                                    isCopy={isCopy}
                                    domainList={domainList}
                                    reseauxSoin={reseauxSoin}
                                    conventions={conventions}
                                    returnCodesList={returnCodesList}
                                />
                            </TabPane>
                        </TabContent>
                        {!pristine && (
                            <ConfirmationModal
                                ref={this.getReferenceFromConfirmModal}
                                onConfirm={this.closeModal}
                                onCancel={this.confirmAnnulation}
                                message={t('declarant.confirmLeaveQuestion')}
                                confirmButton={t('declarant.confirmLeaveYes')}
                                cancelButton={t('declarant.confirmLeaveNo')}
                            />
                        )}
                        {(isEdit || isCreate || isCopy) && (
                            <Fragment>
                                <p className="cgd-comment mt-4 small bottom-border">
                                    <span>{`${t('mandatoryHint')}`}</span>
                                </p>
                                <Row className="mt-2 mb-3 justify-content-between">
                                    <Col xs="auto" className="col">
                                        <Button
                                            id="cancel-form"
                                            type="button"
                                            behavior="secondary"
                                            onClick={pristine ? this.confirmAnnulation : this.openModal}
                                            outline
                                        >
                                            {t('cancel')}
                                        </Button>
                                    </Col>
                                    <Col xs="auto" className="col">
                                        <Button
                                            id="submit-declarant-form"
                                            type="submit"
                                            behavior="primary"
                                            disabled={invalid || fieldEmpty}
                                        >
                                            {t('validate')}
                                        </Button>
                                    </Col>
                                </Row>
                            </Fragment>
                        )}
                    </PageLayout>
                </form>
                {(regroupementsErrors || []).length !== 0 && this.renderFeedbackModal(regroupementsErrors)}
            </Fragment>
        );
    }
}

DeclarantComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    change: PropTypes.func,
    invalid: PropTypes.bool,
    pristine: PropTypes.bool,
    getDeclarant: PropTypes.func,
    updateDeclarant: PropTypes.func,
    createDeclarant: PropTypes.func,
    getDomainesTP: PropTypes.func,
    getReseauxSoin: PropTypes.func,
    getAllReturnCodes: PropTypes.func,
    getLightDeclarants: PropTypes.func,
    // loading: PropTypes.bool,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    isCopy: PropTypes.bool,
    loading: PropTypes.bool,
    id: PropTypes.string,
    formName: PropTypes.string,
    declarant: PropTypes.shape(),
    duplicatedDeclarant: PropTypes.shape(),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
    returnCodesList: PropTypes.shape(),
    lightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    blur: PropTypes.func,
    addAlertError: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    asyncBlurFields: ['numero'],
    asyncValidate: asyncValidator.asyncValidate,
    enableReinitialize: true,
})(DeclarantComponent);
