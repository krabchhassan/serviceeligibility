/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { t as tr } from 'i18next';
import {
    Button,
    Col,
    HabilitationFragment,
    Panel,
    PanelFooter,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import autobind from 'autobind-decorator';
import _ from 'lodash';
import { CommonCombobox, CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import Constants from '../../Constants';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import utils from '../../../../../../common/utils/businessUtils';
import DateUtils from '../../../../../../common/utils/DateUtils';
import PermissionConstants from '../../../../PermissionConstants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const maxLength20 = ValidationFactories.maxLength(20);
const maxLength1 = ValidationFactories.maxLength(1);
const nirValidation = ValidationFactories.strictLengthWithoutSpace(13);
const dateValidation = ValidationFactories.isDateStringDDMMYYYY();
const bicValidation = ValidationFactories.inBetweenLength(8, 11);
const ibanValidation = ValidationFactories.inBetweenLength(14, 34);
const emailValidation = ValidationFactories.isEmail();
const noNameAndFirstname = (values) => !values.prenom && !values.nom;
const noContractData = (values) =>
    !values.numeroOuNomAMC && !values.societeEmettrice && !values.numeroAdherentOuContrat;
const noContactData = (values) => !values.telephone && !values.email;
const noBankData = (values) => !values.bic && !values.iban;
const noAddressData = (values) => !values.codePostal && !values.localite && !values.numeroEtLibelleVoie;
const noAdvancedCriteria = (values) => noContactData(values) && noBankData(values) && noAddressData(values);
const birthDateOnly = (values) =>
    values.dateNaissance &&
    noNameAndFirstname(values) &&
    !values.nir &&
    noContractData(values) &&
    noAdvancedCriteria(values);
const validate = (values, object) => {
    const errors = {};
    if (
        values.prenom &&
        !values.nom &&
        noBankData(values) &&
        noContractData(values) &&
        !values.nir &&
        !values.dateNaissance
    ) {
        errors.prenom = tr('errors:fields.missingCriteria');
    } else if (birthDateOnly(values)) {
        errors.dateNaissance = tr('errors:fields.missingCriteria');
    }
    if (object.clientType !== Constants.TOPOLOGY_INSURER) {
        if (values.numeroAdherentOuContrat && !values.numeroOuNomAMC) {
            errors.numeroAdherentOuContrat = tr('errors:fields.missingAMC');
        }
    }
    if (!values.nom && values.prenom) {
        errors.prenom = tr('errors:fields.missingNom');
    }
    return errors;
};

const normalizeRo = (value) => {
    if (!value) {
        return value;
    }
    return utils.formatRO(value);
};

const normalizeIBAN = (value) => {
    if (!value) {
        return value;
    }
    return utils.formatIBAN(value);
};

const getDeclarantsValueForCombobox = (declarants) =>
    (declarants || []).map((declarant) => ({
        label: `${declarant.numeroRNM} - ${declarant.nom}`,
        value: declarant.numeroRNM,
    }));

const getSocieteEmettriceValueForCombobox = (mainOrganizations) => {
    const associatedSecondaryOrganizations = [];
    mainOrganizations.forEach((mainOrga) =>
        mainOrga.associatedSecondaryOrganizations
            .filter((orga) => orga.code !== 'UNKNOWN')
            .forEach((secondaryOrganization) => associatedSecondaryOrganizations.push(secondaryOrganization)),
    );
    return (associatedSecondaryOrganizations || []).map((societeEmettrice) => ({
        label: `${societeEmettrice.code} - ${societeEmettrice.commercialName}`,
        value: societeEmettrice.amc,
    }));
};

const getValueForCombobox = (list) =>
    (list || []).map((item) => ({
        label: item,
        value: item,
    }));

const setBeneficiaryObjectBirthDate = (dateNaissance, values, beneficiariesObject) => {
    let birthDate;
    if (dateNaissance) {
        beneficiariesObject.dateNaissance = DateUtils.formatBeneficiaryBirthDateForRequest(dateNaissance);
    }
    return birthDate;
};

const transformBirthDateForCriteria = (dateNaissance, shownCriteria, values) => {
    if (dateNaissance) {
        if (typeof dateNaissance === 'object') {
            shownCriteria.dateNaissance = DateUtils.formatServerDate(values.dateNaissance);
        } else {
            const transformDate = `${dateNaissance.substring(6, 8)}/${dateNaissance.substring(
                4,
                6,
            )}/${dateNaissance.substring(0, 4)}`;
            shownCriteria.dateNaissance = transformDate;
        }
    }
};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class SearchFormComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            expanded: false,
        };
        this.autocompleteName = _.debounce(this.autocompleteNameCall, 300);
        this.autocompleteFirstName = _.debounce(this.autocompleteFirstNameCall, 300);
        this.autocompleteLocalite = _.debounce(this.autocompleteLocaliteCall, 300);
        this.autocompleteVoie = _.debounce(this.autocompleteVoieCall, 300);
    }

    componentWillMount() {
        const { getAllDeclarants, getAllSocietesEmettrices } = this.props;
        getAllDeclarants();
        getAllSocietesEmettrices();
    }

    autocompleteNameCall(val) {
        const { getAllNames } = this.props;
        if (val !== '') {
            getAllNames(val, this.isOTPClientType());
        }
    }

    autocompleteFirstNameCall(val) {
        const { getAllFirstNames } = this.props;
        if (val !== '') {
            getAllFirstNames(val, this.isOTPClientType());
        }
    }

    autocompleteLocaliteCall(val) {
        const { getAllLocalites } = this.props;
        if (val !== '') {
            getAllLocalites(val);
        }
    }

    autocompleteVoieCall(val) {
        const { getAllVoies } = this.props;
        if (val !== '') {
            getAllVoies(val);
        }
    }

    isOTPClientType() {
        const { clientType } = this.props;
        return clientType === Constants.CLIENT_TYPE.OTP;
    }

    @autobind
    handleExpandClick() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    @autobind
    resetSearchFunction() {
        const { reset } = this.props;
        reset();
    }

    @autobind
    handleSubmit(values) {
        const { getBeneficiaries, changeCriteriaResearch } = this.props;
        let { dateNaissance } = values;
        const beneficiariesObject = {
            ...values,
            ...(values.nom && { nom: values.nom }),
            ...(values.prenom && { prenom: values.prenom }),
            ...(values.nir && { nir: values.nir }),
            ...(values.numeroOuNomAMC && { numeroOuNomAMC: values.numeroOuNomAMC.value }),
            ...(values.societeEmettrice && { societeEmettrice: values.societeEmettrice.value }),
            ...(values.numeroAdherentOuContrat && { numeroAdherentOuContrat: values.numeroAdherentOuContrat }),
            ...(values.rangNaissance && { rangNaissance: values.rangNaissance }),
            ...(values.localite && { localite: values.localite.label }),
            ...(values.numeroEtLibelleVoie && { voie: values.numeroEtLibelleVoie.label }),
        };

        dateNaissance = setBeneficiaryObjectBirthDate(dateNaissance, values, beneficiariesObject);
        const shownCriteria = { ...beneficiariesObject };
        transformBirthDateForCriteria(dateNaissance, shownCriteria, values);
        changeCriteriaResearch(shownCriteria);
        getBeneficiaries(beneficiariesObject);
    }

    countCriteria = (nir, nom, numeroOuNomAMC, numeroAdherentOuContrat, dateNaissance, prenom, societeEmettrice) => {
        let criteriaCount = 0;
        if (nir || nom || numeroOuNomAMC || societeEmettrice) {
            criteriaCount += 2;
        } else if (numeroAdherentOuContrat || dateNaissance || prenom) {
            criteriaCount += 1;
        }
        return criteriaCount;
    };

    renderFooter() {
        const {
            t,
            nom,
            prenom,
            numeroOuNomAMC,
            societeEmettrice,
            numeroAdherentOuContrat,
            nir,
            dateNaissance,
            telephone,
            mail,
            bic,
            iban,
            codePostal,
            localite,
            numeroEtLibelleVoie,
            invalid,
            pristine,
        } = this.props;
        const { expanded } = this.state;
        const contactInfo = telephone || mail;
        const bankInfo = bic || iban;
        const addressInfo = codePostal || localite || numeroEtLibelleVoie;
        const critereAvance = contactInfo || bankInfo || addressInfo;

        const criteriaCount = this.countCriteria(
            nir,
            nom,
            numeroOuNomAMC,
            numeroAdherentOuContrat,
            dateNaissance,
            prenom,
            societeEmettrice,
        );

        const enoughCriteria = criteriaCount >= 1 || critereAvance;
        return (
            <PanelFooter>
                {this.isOTPClientType() ? (
                    <div />
                ) : (
                    <Button
                        id="collapsed-icon"
                        behavior="secondary"
                        size="sm"
                        type="button"
                        onClick={this.handleExpandClick}
                    >
                        <CgIcon name={expanded ? 'angle-double-up' : 'angle-double-down'} fixedWidth />
                        <span>
                            {expanded
                                ? ` ${t('beneficiaries.detailedSearchOpened')}`
                                : ` ${t('beneficiaries.detailedSearch')}`}
                        </span>
                    </Button>
                )}
                <div>
                    <HabilitationFragment allowedPermissions={PermissionConstants.READ_DROITS_DATA_PERMISSION}>
                        <Button
                            type="button"
                            outline
                            size="md"
                            onClick={this.resetSearchFunction}
                            id="clear-search-beneficiary"
                            className="mr-2"
                        >
                            <CgIcon name="undo" size="1x" />
                        </Button>
                        <Button
                            behavior="primary"
                            type="submit"
                            disabled={!enoughCriteria || invalid || pristine}
                            id="submit-search-beneficiary"
                        >
                            {t('common:search')}
                        </Button>
                    </HabilitationFragment>
                </div>
            </PanelFooter>
        );
    }

    render() {
        const { handleSubmit, t, dateNaissance, declarants, names, firstNames, localites, voies, societesEmettrices } =
            this.props;
        const { expanded } = this.state;
        return (
            <form onSubmit={handleSubmit(this.handleSubmit)}>
                <Panel footer={this.renderFooter()} border={false}>
                    <PanelSection>
                        <Row>
                            <Col xs={3}>
                                <Field
                                    id="beneficiary-search-form-lastname"
                                    name={Constants.FIELDS.nom}
                                    component={CommonCombobox}
                                    label={t('beneficiaries.nom')}
                                    tooltip={t('beneficiaries.toolTip.nom')}
                                    placeholder={t('beneficiaries.placeHolder.nom')}
                                    options={getValueForCombobox(names)}
                                    promptTextCreator={(label) => label}
                                    onInputChange={(val) => this.autocompleteName(val)}
                                    canCreateOption
                                    searchable
                                    clearable
                                />
                            </Col>
                            <Col xs={3}>
                                <Field
                                    id="beneficiary-search-form-firstname"
                                    name={Constants.FIELDS.prenom}
                                    component={CommonCombobox}
                                    label={t('beneficiaries.prenom')}
                                    tooltip={t('beneficiaries.toolTip.prenom')}
                                    placeholder={t('beneficiaries.placeHolder.prenom')}
                                    options={getValueForCombobox(firstNames)}
                                    promptTextCreator={(label) => label}
                                    onInputChange={(val) => this.autocompleteFirstName(val)}
                                    canCreateOption
                                    searchable
                                    clearable
                                />
                            </Col>
                            <Col xs={3}>
                                <Field
                                    id="beneficiary-search-form-ro-number"
                                    name={Constants.FIELDS.nir}
                                    label={t('beneficiaries.nir')}
                                    tooltip={t('beneficiaries.toolTip.nir')}
                                    placeholder={t('beneficiaries.placeHolder.nir')}
                                    component={CommonInput}
                                    normalize={normalizeRo}
                                    searchable
                                    clearable
                                    validate={nirValidation}
                                />
                            </Col>
                            <Col xs={2}>
                                <Field
                                    id="beneficiary-search-form-birthday"
                                    name={Constants.FIELDS.dateNaissance}
                                    component={CommonInput}
                                    label={t('beneficiaries.dateNaissance')}
                                    tooltip={t('beneficiaries.toolTip.dateNaissance')}
                                    placeholder={t('beneficiaries.placeHolder.dateNaissance')}
                                    formGroupClassName="pr-2"
                                    validate={dateValidation}
                                />
                            </Col>
                            <Col xs={1} className="d-flex justify-content-between">
                                <Field
                                    id="beneficiary-search-form-rank-number"
                                    name={Constants.FIELDS.rangNaissance}
                                    component={CommonInput}
                                    label={t('beneficiaries.rangNaissance')}
                                    tooltip={t('beneficiaries.toolTip.rangNaissance')}
                                    placeholder={t('beneficiaries.placeHolder.rangNaissance')}
                                    validate={[maxLength1, ValidationFactories.isNumber()]}
                                    disabled={!dateNaissance}
                                    formGroupClassName="pl-2"
                                />
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                {this.isOTPClientType() ? (
                                    <Field
                                        id="beneficiary-search-form-amc-number"
                                        name={Constants.FIELDS.numeroOuNomAMC}
                                        component={CommonCombobox}
                                        label={t('beneficiaries.numeroOuNomAMC')}
                                        tooltip={t('beneficiaries.toolTip.numeroOuNomAMC')}
                                        placeholder={t('beneficiaries.placeHolder.numeroOuNomAMC')}
                                        options={getDeclarantsValueForCombobox(declarants)}
                                        searchable
                                        clearable
                                    />
                                ) : (
                                    <Field
                                        id="beneficiary-search-form-societe-emettrice"
                                        name={Constants.FIELDS.societeEmettrice}
                                        component={CommonCombobox}
                                        label={t('beneficiaries.societeEmettrice')}
                                        tooltip={t('beneficiaries.toolTip.societeEmettrice')}
                                        placeholder={t('beneficiaries.placeHolder.societeEmettrice')}
                                        options={getSocieteEmettriceValueForCombobox(societesEmettrices)}
                                        searchable
                                        clearable
                                    />
                                )}
                            </Col>
                            <Col>
                                <Field
                                    id="beneficiary-search-form-memberOrContract-number"
                                    name={Constants.FIELDS.numeroAdherentOuContrat}
                                    component={CommonInput}
                                    label={t(
                                        `beneficiaries.numeroAdherent${this.isOTPClientType() ? '' : 'OuContrat'}`,
                                    )}
                                    tooltip={t('beneficiaries.toolTip.numeroAdherentOuContrat')}
                                    placeholder={t('beneficiaries.placeHolder.numeroAdherentOuContrat')}
                                    validate={maxLength20}
                                />
                            </Col>
                            {!this.isOTPClientType() && (
                                <Col>
                                    <Field
                                        id="beneficiary-search-form-mail"
                                        name={Constants.FIELDS.mail}
                                        component={CommonInput}
                                        label={t('beneficiaries.mail')}
                                        tooltip={t('beneficiaries.toolTip.mail')}
                                        placeholder={t('beneficiaries.placeHolder.mail')}
                                        validate={emailValidation}
                                    />
                                </Col>
                            )}
                        </Row>
                        {expanded && (
                            <Row>
                                <Col xs={2}>
                                    <Field
                                        id="beneficiary-search-form-bic"
                                        name={Constants.FIELDS.bic}
                                        component={CommonInput}
                                        label={t('beneficiaries.bic')}
                                        tooltip={t('beneficiaries.toolTip.bic')}
                                        placeholder={t('beneficiaries.placeHolder.bic')}
                                        validate={bicValidation}
                                    />
                                </Col>
                                <Col xs={3}>
                                    <Field
                                        id="beneficiary-search-form-iban"
                                        name={Constants.FIELDS.iban}
                                        component={CommonInput}
                                        label={t('beneficiaries.iban')}
                                        tooltip={t('beneficiaries.toolTip.iban')}
                                        placeholder={t('beneficiaries.placeHolder.iban')}
                                        normalize={normalizeIBAN}
                                        validate={ibanValidation}
                                    />
                                </Col>
                                <Col xs={3}>
                                    <Field
                                        id="beneficiary-search-form-localite"
                                        name={Constants.FIELDS.localite}
                                        component={CommonCombobox}
                                        label={t('beneficiaries.localite')}
                                        tooltip={t('beneficiaries.toolTip.localite')}
                                        placeholder={t('beneficiaries.placeHolder.localite')}
                                        options={getValueForCombobox(localites)}
                                        promptTextCreator={(label) => label}
                                        onInputChange={(val) => this.autocompleteLocalite(val)}
                                        canCreateOption
                                        searchable
                                        clearable
                                    />
                                </Col>
                                <Col xs={4}>
                                    <Field
                                        id="beneficiary-search-form-numeroEtLibelleVoie"
                                        name={Constants.FIELDS.numeroEtLibelleVoie}
                                        component={CommonCombobox}
                                        label={t('beneficiaries.numeroEtLibelleVoie')}
                                        tooltip={t('beneficiaries.toolTip.numeroEtLibelleVoie')}
                                        placeholder={t('beneficiaries.placeHolder.numeroEtLibelleVoie')}
                                        options={getValueForCombobox(voies)}
                                        promptTextCreator={(label) => label}
                                        onInputChange={(val) => this.autocompleteVoie(val)}
                                        canCreateOption
                                        searchable
                                        clearable
                                    />
                                </Col>
                            </Row>
                        )}
                    </PanelSection>
                </Panel>
            </form>
        );
    }
}

SearchFormComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    reset: PropTypes.func,
    nom: PropTypes.string,
    prenom: PropTypes.string,
    numeroOuNomAMC: PropTypes.string,
    numeroAdherentOuContrat: PropTypes.string,
    nir: PropTypes.string,
    telephone: PropTypes.string,
    mail: PropTypes.string,
    bic: PropTypes.string,
    iban: PropTypes.string,
    codePostal: PropTypes.string,
    localite: PropTypes.string,
    numeroEtLibelleVoie: PropTypes.string,
    getBeneficiaries: PropTypes.func,
    changeCriteriaResearch: PropTypes.func,
    invalid: PropTypes.bool,
    pristine: PropTypes.bool,
    dateNaissance: PropTypes.string,
    getAllDeclarants: PropTypes.func,
    getAllNames: PropTypes.func,
    getAllFirstNames: PropTypes.func,
    getAllLocalites: PropTypes.func,
    getAllVoies: PropTypes.func,
    declarants: PropTypes.arrayOf(PropTypes.shape()),
    names: PropTypes.arrayOf(PropTypes.shape()),
    firstNames: PropTypes.arrayOf(PropTypes.shape()),
    localites: PropTypes.arrayOf(PropTypes.shape()),
    voies: PropTypes.arrayOf(PropTypes.shape()),
    clientType: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: Constants.FORM_NAME,
    validate,
})(SearchFormComponent);
