/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Panel,
    PanelSection,
    PanelHeader,
    CgdTable,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { Field } from 'redux-form';
import DateUtils from '../../../../../../common/utils/DateUtils';
import {
    CommonCombobox,
    CommonDatePicker,
    CommonMultiCombobox,
    CommonRadio,
} from '../../../../../../common/utils/Form/CommonFields';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import PermissionConstants from '../../../../PermissionConstants';
import formConstants from '../../../ParametrageMetier/components/ParamDomain/Constants';
import ParametrageAMCUtils from './ParametrageAMCUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();
const createField = (name, input) => {
    return <Field name={name} component={input} validate={required} inline labelPortion={0} />;
};

@TranslationsSubscriber(['eligibility', 'common'])
class RegroupementDomainesTP extends Component {
    constructor(props) {
        super(props);
        this.state = {
            addRegroupementDomainesTP: false,
        };
    }

    getColumns() {
        const { t } = this.props;
        return [
            ParametrageAMCUtils.generateCommonColumn('domaineRegroupementTP', 200, t),
            ParametrageAMCUtils.generateCommonColumn('codesDomainesTP', 200, t),
            ParametrageAMCUtils.generateBooleanCommonColumn('niveauRemboursementIdentique', 200, t),
            ParametrageAMCUtils.generateCommonColumn('dateDebut', 200, t),
            ParametrageAMCUtils.generateCommonColumn('dateFin', 200, t),
        ];
    }

    @autobind
    addRegroupementDomainesTP() {
        this.setState({ addRegroupementDomainesTP: true });
    }

    @autobind
    saveRegroupementDomainesTP() {
        // TODO save
        this.setState({ addRegroupementDomainesTP: false });
    }

    generateRowsData() {
        const { t, regroupementDomainesTP } = this.props;
        const { addRegroupementDomainesTP } = this.state;

        let regroupementDomaines = {};
        if (regroupementDomainesTP) {
            regroupementDomaines = { ...regroupementDomainesTP };
        }

        const rowsData = Object.values(regroupementDomaines).map((item, index) => ({
            key: index,
            domaineRegroupementTP: item.domaineRegroupementTP,
            codesDomainesTP: (item.codesDomainesTP || []).join(', '),
            niveauRemboursementIdentique: item.niveauRemboursementIdentique ? t('yes') : t('no'),
            dateDebut: DateUtils.formatDisplayDate(item.dateDebut),
            dateFin: DateUtils.formatDisplayDate(item.dateFin),
        }));

        if (addRegroupementDomainesTP) {
            const creationLine = {
                key: 'new',
                domaineRegroupementTP: createField(formConstants.FIELDS.domaineRegroupementTP, CommonCombobox),
                codesDomainesTP: createField(formConstants.FIELDS.codesDomainesTP, CommonMultiCombobox),
                niveauRemboursementIdentique: createField(
                    formConstants.FIELDS.niveauRemboursementIdentique,
                    CommonRadio,
                ),
                dateDebut: createField(formConstants.FIELDS.dateDebut, CommonDatePicker),
                dateFin: createField(formConstants.FIELDS.dateFin, CommonDatePicker),
            };

            rowsData.unshift(creationLine);
        }

        return rowsData;
    }

    renderRegroupementDomainesTP() {
        const { t, regroupementDomainesTP } = this.props;
        const { addRegroupementDomainesTP } = this.state;
        const cols = this.getColumns();

        const rowsData = (regroupementDomainesTP || []).map((item) => {
            return {
                domaineRegroupementTP: item.domaineRegroupementTP,
                codesDomainesTP: (item.codesDomainesTP || []).join(', '),
                niveauRemboursementIdentique: item.niveauRemboursementIdentique ? t('yes') : t('no'),
                dateDebut: DateUtils.formatDisplayDate(item.dateDebut),
                dateFin: DateUtils.formatDisplayDate(item.dateFin),
            };
        });

        return (
            <CgdTable
                id="regroupementDomainesTPTable"
                key="regroupementDomainesTPTable"
                data={rowsData}
                columns={cols}
                showPageSizeOptions={false}
                showPagination={false}
                pageSize={addRegroupementDomainesTP ? regroupementDomainesTP.length + 1 : regroupementDomainesTP.length}
            />
        );
    }

    render() {
        const { t, regroupementDomainesTP } = this.props;
        const { addRegroupementDomainesTP } = this.state;

        const actions = [];
        if (!addRegroupementDomainesTP) {
            actions.push({
                action: this.addRegroupementDomainesTP,
                key: 'addRegroupementDomainesTP',
                id: 'addRegroupementDomainesTP',
                behavior: 'secondary',
                icon: 'add',
                buttonProps: { type: 'button' },
                allowedPermissions: PermissionConstants.UPDATE_AMC_DATA_PERMISSION,
            });
        } else {
            actions.push({
                action: this.saveRegroupementDomainesTP,
                key: 'saveRegroupementDomainesTP',
                id: 'saveRegroupementDomainesTP',
                behavior: 'secondary',
                icon: 'save',
                buttonProps: { type: 'button' },
                allowedPermissions: PermissionConstants.UPDATE_AMC_DATA_PERMISSION,
            });
        }
        return (
            <Panel
                header={<PanelHeader title={t('declarant.regroupementDomainesTPTitle')} actions={actions} />}
                id="regroupementDomainesTP"
            >
                <PanelSection>
                    {regroupementDomainesTP && regroupementDomainesTP.length > 0 ? (
                        this.renderRegroupementDomainesTP()
                    ) : (
                        <div className="ml-2">{t('declarant.parametrageAMC.noRegroupementDomainesTP')}</div>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

RegroupementDomainesTP.propTypes = {
    t: PropTypes.func,
    regroupementDomainesTP: PropTypes.arrayOf(PropTypes.shape),
    // isEdit: PropTypes.bool,
};

export default RegroupementDomainesTP;
