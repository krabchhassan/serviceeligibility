/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import get from 'lodash/get';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import {
    PanelSection,
    Panel,
    Row,
    Col,
    Button,
    CgdTable,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonInput, CommonRadio, CommonCombobox } from '../../../../../../../common/utils/Form/CommonFields';
import StringUtils from '../../../../../../../common/utils/StringUtils';
import formConstants from '../Constants';
import GTLotSummaryComponent from './GTLotSummaryComponent';
import ValidationFactories from '../../../../../../../common/utils/Form/ValidationFactories';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import style from '../style.module.scss';
import businessUtils from '../../../../../../../common/utils/businessUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */

/* ************************************* */

function move(arr, oldIndex, newIndex) {
    let numberOfDeletedElm = 1;

    const elm = arr.splice(oldIndex, numberOfDeletedElm)[0];

    numberOfDeletedElm = 0;

    arr.splice(newIndex, numberOfDeletedElm, elm);
}

const getElement = (array, index) => {
    if (array && array.length > index - 1) {
        return array[index];
    }
    return null;
};

function formatPopulationValues(list = []) {
    const normalized = list.map((item) => (typeof item === 'string' ? { label: item, value: item } : item));
    return normalized.length > 0 ? normalized.map((item) => item.value).join(',') : '-';
}

@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class SecondStepComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            codesItelis: this.props.codesItelis.map((item) => ({
                code: item.value.code,
                label: item.value.label,
            })),
        };
    }

    componentDidMount() {
        const { change, codeItelisFromParam, showItelisCode } = this.props;
        const { codesItelis } = this.state;
        if (showItelisCode && codeItelisFromParam && codesItelis) {
            const matchingCodeItelis =
                codesItelis?.find((codeItelis) => codeItelis.code === codeItelisFromParam) || null;
            change(formConstants.FIELDS.codeItelis, matchingCodeItelis);
        }
    }

    componentWillReceiveProps(nextProps) {
        const { selectedDroits, change } = this.props;
        const { selectedDroits: newSelectedDroits } = nextProps;

        if ((selectedDroits || []).length === (newSelectedDroits || []).length) {
            newSelectedDroits.forEach((item, index) => {
                const oldElement = getElement(selectedDroits, index);
                const newElement = getElement(newSelectedDroits, index);
                if (oldElement.codeDomaineTP !== newElement.codeDomaineTP) {
                    const newValue =
                        newElement.codeDomaineTP && newElement.codeDomaineTP.label
                            ? newElement.codeDomaineTP.label
                            : null;

                    change(`${formConstants.FIELDS.detailsDroit}[${index}].libelleDomaineTP`, newValue);
                    this.forceUpdate();
                }
            });
        }
    }

    shouldComponentUpdate(nextProps) {
        const { selectedDroits } = this.props;
        const { selectedDroits: newSelectedDroits } = nextProps;
        let refresh = true;
        newSelectedDroits.forEach((item, index) => {
            const oldElement = getElement(selectedDroits, index);
            const newElement = getElement(newSelectedDroits, index);
            if (oldElement && oldElement.libelleDomaineTP !== newElement.libelleDomaineTP) {
                refresh = false;
            }
            if (oldElement && oldElement.codeRenvoi !== newElement.codeRenvoi) {
                refresh = false;
            }
            if (oldElement && oldElement.messageRenvoi !== newElement.messageRenvoi) {
                refresh = false;
            }
        });
        return refresh;
    }

    getUnselectedDomains() {
        const { selectedDroits, detailsDroit, domainList } = this.props;

        const selectedDomains = Object.values(selectedDroits || []).map((domain) => get(domain, 'codeDomaineTP.code'));
        const domainesTP = Object.values(domainList || []).map((domain) => domain.value);

        if (detailsDroit.length > 0) {
            const codesDetailsDroit = detailsDroit.map((droit) => droit.codeDomaineTP);

            return domainesTP
                .filter((domain) => !selectedDomains.includes(get(domain, 'code')))
                .filter((domain) => !codesDetailsDroit.includes(get(domain, 'code')));
        }

        return domainesTP.filter((domain) => !selectedDomains.includes(get(domain, 'code')));
    }

    getColumns() {
        const { t, conventions, calculatedErrors, optionCodesRenvoi, change } = this.props;
        const optionConventions = [...conventions];
        optionConventions.unshift({ code: '', label: t('parametersDroits.form.noConvention') });
        const domainesTP = this.getUnselectedDomains();
        const optionActionsCodeRenvoi = businessUtils.getCodeRenvoiActions('parametersDroits', t);
        const smallCall = (code) => businessUtils.disabledCodeRenvoiAction(code);
        const noEllipsis = 'no-ellipsis';
        return [
            {
                ...this.generateCommonColumn('ordreAffichage'),
                width: 110,
                Cell: (props) => (
                    <div key="1">
                        <Button
                            outlineNoBorder
                            type="button"
                            onClick={() => this.move(props.row.index, true)}
                            className="pt-0"
                            disabled={props.row.index === 0}
                        >
                            <CgIcon name="level-up" />
                        </Button>
                        <Button
                            outlineNoBorder
                            type="button"
                            onClick={() => this.move(props.row.index, false)}
                            disabled={props.row.index >= props.data.length - 1}
                            className="pt-0"
                        >
                            <CgIcon name="level-down" />
                        </Button>
                        {props.row.index + 1}
                    </div>
                ),
            },
            {
                ...this.generateCommonColumn('codeDomaineTP', true),
                className: noEllipsis,
                Cell: (props) => {
                    const filedName = `${formConstants.FIELDS.detailsDroit}[${props.row.index}].codeDomaineTP`;
                    return (
                        <Field
                            name={filedName}
                            component={CommonCombobox}
                            inline
                            labelPortion={0}
                            options={domainesTP}
                            placeholder={t('parametersDroits.form.codeDomaineTPPlaceholder')}
                            valueKey="code"
                            labelKey="code"
                            searchable
                            showFirstValidation
                            forceError={calculatedErrors[props.row.index].codeDomaineTP}
                        />
                    );
                },
                width: 135,
            },
            {
                ...this.generateCommonColumn('libelleDomaineTP', true),
                Cell: (props) => {
                    const filedName = `${formConstants.FIELDS.detailsDroit}[${props.row.index}].libelleDomaineTP`;
                    return (
                        <Field
                            name={filedName}
                            component={CommonInput}
                            inline
                            labelPortion={0}
                            placeholder={t('parametersDroits.form.libelleDomaineTPPlaceholder')}
                            key="3"
                            showFirstValidation
                            forceError={calculatedErrors[props.row.index].libelleDomaineTP}
                        />
                    );
                },
                width: 200,
            },
            {
                ...this.generateCommonColumn('convention', true),
                className: noEllipsis,
                Cell: (props) => {
                    const fieldName = `${formConstants.FIELDS.detailsDroit}[${props.row.index}].convention`;
                    return (
                        <Field
                            name={fieldName}
                            component={CommonCombobox}
                            inline
                            labelPortion={0}
                            options={optionConventions}
                            placeholder={t('parametersDroits.form.convention')}
                            valueKey="code"
                            searchable
                            key="5"
                            showFirstValidation
                            clearable
                        />
                    );
                },
            },
            {
                ...this.generateCommonColumn('codeRenvoiAction'),
                className: noEllipsis,
                Cell: (props) => {
                    const filedName = `${formConstants.FIELDS.detailsDroit}[${props.row.index}].codeRenvoiAction`;
                    if (!(props.row.values.codeRenvoiAction || {}).code) {
                        change(filedName, optionActionsCodeRenvoi[0]);
                    }
                    return (
                        <Field
                            name={filedName}
                            component={CommonCombobox}
                            inline
                            labelPortion={0}
                            options={optionActionsCodeRenvoi}
                            placeholder={t('parametersDroits.form.codeRenvoiActionPlaceholder')}
                            valueKey="code"
                            key="6"
                            showFirstValidation
                            forceError={calculatedErrors[props.row.index].codeRenvoiAction}
                            searchable
                        />
                    );
                },
            },
            {
                ...this.generateCommonColumn('codeRenvoi'),
                className: noEllipsis,
                Cell: (props) => {
                    const filedName = `${formConstants.FIELDS.detailsDroit}[${props.row.index}].codeRenvoi`;
                    if (smallCall((props.row.values.codeRenvoiAction || {}).code)) {
                        change(filedName, null);
                    }
                    return (
                        <Field
                            name={filedName}
                            component={CommonCombobox}
                            inline
                            labelPortion={0}
                            options={optionCodesRenvoi}
                            placeholder={t('parametersDroits.form.codeRenvoiPlaceholder')}
                            valueKey="code"
                            key="7"
                            showFirstValidation
                            forceError={calculatedErrors[props.row.index].codeRenvoi}
                            searchable
                            clearable
                            disabled={smallCall((props.row.values.codeRenvoiAction || {}).code)}
                        />
                    );
                },
            },
            {
                id: 'action',
                width: 50,
                Cell: (props) => (
                    <div key="8">
                        <Button onClick={() => this.deleteRow(props.row.index)} type="button" outlineNoBorder>
                            <CgIcon name="trash" />
                        </Button>
                    </div>
                ),
            },
        ];
    }

    generateCommonColumn(key, showRequired) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`parametersDroits.form.${key}`), showRequired),
            accessor: key,
            id: key,
            disableSortBy: true,
            key,
        };
    }

    @autobind
    deleteRow(id) {
        const { change, selectedDroits } = this.props;
        const newArray = selectedDroits.filter((item, index) => index !== id);

        change(formConstants.FIELDS.detailsDroit, newArray);
        this.forceUpdate();
    }

    @autobind
    pushNew() {
        const { selectedDroits, change } = this.props;
        const newArray = [...selectedDroits];

        if (selectedDroits.length > 0) {
            const lastRights = selectedDroits[selectedDroits.length - 1];
            newArray.push({ convention: lastRights.convention });
        } else {
            newArray.push({});
        }

        change(formConstants.FIELDS.detailsDroit, newArray);
    }

    @autobind
    move(id, isUp) {
        const { change, selectedDroits } = this.props;
        const newArray = [...selectedDroits];
        const newIndex = id + (isUp ? -1 : 1);
        move(newArray, id, newIndex);
        change(formConstants.FIELDS.detailsDroit, newArray);
    }

    showParametersWithoutItelis() {
        const { t } = this.props;

        return (
            <Row>
                <Col xs="4">
                    <Field
                        name={formConstants.FIELDS.refFondCarte}
                        component={CommonInput}
                        label={t('parametersDroits.form.refFondCarte')}
                        placeholder={t('parametersDroits.form.refFondCartePlaceholder')}
                    />
                </Col>
                <Col xs="4">
                    <Field
                        name={formConstants.FIELDS.codeAnnexe1}
                        component={CommonInput}
                        label={t('parametersDroits.form.codeAnnexe1')}
                        placeholder={t('parametersDroits.form.codeAnnexePlaceholder')}
                    />
                </Col>
                <Col xs="4">
                    <Field
                        name={formConstants.FIELDS.codeAnnexe2}
                        component={CommonInput}
                        label={t('parametersDroits.form.codeAnnexe2')}
                        placeholder={t('parametersDroits.form.codeAnnexePlaceholder')}
                    />
                </Col>
            </Row>
        );
    }

    showParametersWithItelis() {
        const { t, codesItelis } = this.props;
        const optionCodesItelis = codesItelis.map((item) => ({
            code: item.value.code,
            label: item.value.label,
        }));

        return (
            <Row>
                <Col xs="3">
                    <Field
                        name={formConstants.FIELDS.refFondCarte}
                        component={CommonInput}
                        label={t('parametersDroits.form.refFondCarte')}
                        placeholder={t('parametersDroits.form.refFondCartePlaceholder')}
                    />
                </Col>
                <Col xs="3">
                    <Field
                        name={formConstants.FIELDS.codeAnnexe1}
                        component={CommonInput}
                        label={t('parametersDroits.form.codeAnnexe1')}
                        placeholder={t('parametersDroits.form.codeAnnexePlaceholder')}
                    />
                </Col>
                <Col xs="3">
                    <Field
                        name={formConstants.FIELDS.codeAnnexe2}
                        component={CommonInput}
                        label={t('parametersDroits.form.codeAnnexe2')}
                        placeholder={t('parametersDroits.form.codeAnnexePlaceholder')}
                    />
                </Col>
                <Col xs="3">
                    <Field
                        name={formConstants.FIELDS.codeItelis}
                        component={CommonCombobox}
                        options={optionCodesItelis}
                        label={t('parametersDroits.form.codeItelis')}
                        valueKey="code"
                        placeholder={t('parametersDroits.form.codeItelisPlaceholder')}
                        clearable
                    />
                </Col>
            </Row>
        );
    }

    renderLotGTSummary() {
        const { selectedLots, selectedGTs } = this.props;
        const separator = ',';
        const concatLots = (selectedLots || []).map((lot) => lot.label).join(separator);
        const concatGTs = (selectedGTs || []).map((gt) => gt.label).join(separator);
        return <GTLotSummaryComponent concatLots={concatLots} concatGTs={concatGTs} />;
    }

    renderFirstStep() {
        const { t, selectedCollectivityID, selectedPopulationGroup, selectedCriterias, selectedAmc } = this.props;

        return (
            <Row className={style['full-height']}>
                <Col xs="6">
                    <Panel border={false} panelTheme="secondary">
                        <h4 className="text-colored-mix-0 pl-2">{t('parametersTPContrat.populationSummary')}</h4>
                        <Row className="pl-2">
                            <Col xs="6">
                                <div>
                                    <LabelValuePresenter
                                        id="coll"
                                        label={t('parametersTPContrat.amc')}
                                        value={`${selectedAmc.value} - ${selectedAmc.number}`}
                                        inline
                                    />
                                </div>

                                <div>
                                    <LabelValuePresenter
                                        id="coll"
                                        label={t('parametersTPContrat.groupePopulation')}
                                        value={formatPopulationValues(selectedPopulationGroup)}
                                        inline
                                    />
                                </div>
                            </Col>
                            <Col>
                                <div>
                                    <LabelValuePresenter
                                        id="coll"
                                        label={t('parametersTPContrat.identifiantCollectivite')}
                                        value={formatPopulationValues(selectedCollectivityID)}
                                        inline
                                    />
                                </div>
                                <LabelValuePresenter
                                    id="coll"
                                    label={t('parametersTPContrat.critereSecondaireDetaille')}
                                    value={formatPopulationValues(selectedCriterias)}
                                    inline
                                />
                            </Col>
                        </Row>
                    </Panel>
                </Col>
                <Col xs="6">{this.renderLotGTSummary()}</Col>
            </Row>
        );
    }

    renderParameters() {
        const { t, conventions, optionCodesRenvoi, showItelisCode } = this.props;
        const optionConventions = [...conventions];

        return (
            <Fragment>
                <Row>
                    <Col xs="6" md="3">
                        <Field
                            name={formConstants.FIELDS.codeConventionTP}
                            component={CommonCombobox}
                            placeholder={t('parametersDroits.form.codeConventionTPPlaceholder')}
                            label={t('parametersDroits.form.codeConventionTP')}
                            options={optionConventions}
                            valueKey="code"
                            validate={required}
                            showRequired
                            showFirstValidation
                            searchable
                        />
                    </Col>
                    <Col xs="6" md="3">
                        <Field
                            name={formConstants.FIELDS.codeOperateurTP}
                            component={CommonInput}
                            placeholder={t('parametersDroits.form.codeOperateurTPPlaceholder')}
                            label={t('parametersDroits.form.codeOperateurTP')}
                            validate={required}
                            showRequired
                            showFirstValidation
                        />
                    </Col>
                    <Col xs="6" md="3">
                        <Panel panelTheme="secondary" border={false}>
                            <div className="pl-2 pb-2">{t('parametersDroits.form.isCarteEditablePapier')}</div>

                            <Row>
                                <Col xs="6">
                                    <Field
                                        type="radio"
                                        name={formConstants.FIELDS.isCarteEditablePapier}
                                        component={CommonRadio}
                                        label={t('yes')}
                                        value="true"
                                        cgdComment={false}
                                        labelPortion={0}
                                        validate={required}
                                        inline
                                    />
                                </Col>
                                <Col>
                                    <Field
                                        type="radio"
                                        name={formConstants.FIELDS.isCarteEditablePapier}
                                        component={CommonRadio}
                                        label={t('no')}
                                        cgdComment={false}
                                        value="false"
                                        labelPortion={0}
                                        inline
                                        validate={required}
                                    />
                                </Col>
                            </Row>
                        </Panel>
                    </Col>
                    <Col xs="6" md="3">
                        <Panel panelTheme="secondary" border={false}>
                            <div className="pl-2 pb-2">{t('parametersDroits.form.isCarteDematerialisee')}</div>
                            <Row>
                                <Col xs="6">
                                    <Field
                                        type="radio"
                                        name={formConstants.FIELDS.isCarteDematerialisee}
                                        component={CommonRadio}
                                        label={t('yes')}
                                        value="true"
                                        labelPortion={0}
                                        cgdComment={false}
                                        validate={required}
                                        inline
                                    />
                                </Col>
                                <Col>
                                    <Field
                                        type="radio"
                                        name={formConstants.FIELDS.isCarteDematerialisee}
                                        component={CommonRadio}
                                        label={t('no')}
                                        value="false"
                                        labelPortion={0}
                                        cgdComment={false}
                                        validate={required}
                                        inline
                                    />
                                </Col>
                            </Row>
                        </Panel>
                    </Col>
                </Row>
                {showItelisCode ? this.showParametersWithItelis() : this.showParametersWithoutItelis()}
                <Row>
                    <Col xs="6">
                        <Field
                            name={formConstants.FIELDS.codeRenvoi}
                            component={CommonCombobox}
                            options={optionCodesRenvoi}
                            valueKey="code"
                            label={t('parametersDroits.form.codeRenvoi')}
                            placeholder={t('parametersDroits.form.codeRenvoiPlaceholder')}
                            clearable
                        />
                    </Col>
                </Row>
                <div className="cgd-comment mt-1 small top-border">{t('mandatoryHint')} </div>
            </Fragment>
        );
    }

    renderDomainsTable() {
        const { t, selectedDroits } = this.props;
        return (
            <Fragment>
                <PanelSection>
                    <CgdTable
                        id="formTable"
                        data={selectedDroits}
                        columns={this.getColumns()}
                        showPageSizeOptions={false}
                        showPagination={false}
                        initialPageSize={6}
                        pageSize={(selectedDroits || []).length > 6 ? (selectedDroits || []).length : 6}
                        manual
                    />
                </PanelSection>
                <PanelSection>
                    <div>
                        <Button type="button" onClick={this.pushNew}>
                            {t('parametersDroits.form.createNew')}
                        </Button>
                    </div>
                </PanelSection>
            </Fragment>
        );
    }

    render() {
        return (
            <Fragment>
                <Panel>
                    <PanelSection>{this.renderFirstStep()}</PanelSection>
                    <PanelSection>{this.renderParameters()}</PanelSection>
                </Panel>
                <Panel separators={false}>{this.renderDomainsTable()}</Panel>
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    change: PropTypes.func,
    selectedDroits: PropTypes.arrayOf(PropTypes.shape()),
    codesItelis: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    calculatedErrors: PropTypes.arrayOf(PropTypes.shape()),
    selectedCollectivityID: PropTypes.arrayOf(PropTypes.shape()),
    selectedPopulationGroup: PropTypes.arrayOf(PropTypes.shape()),
    selectedCriterias: PropTypes.arrayOf(PropTypes.shape()),
    selectedAmc: PropTypes.shape(),
    selectedLots: PropTypes.arrayOf(PropTypes.shape()),
    selectedGTs: PropTypes.arrayOf(PropTypes.shape()),
    codeItelisFromParam: PropTypes.string,
    showItelisCode: PropTypes.bool,
    detailsDroit: PropTypes.arrayOf(PropTypes.shape()),
};
// Default props
const defaultProps = {};

// Add prop types
SecondStepComponent.propTypes = propTypes;
// Add default props
SecondStepComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SecondStepComponent;
