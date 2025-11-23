/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import autobind from 'autobind-decorator';
import PropTypes from 'prop-types';
import {
    Input,
    Label,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Col,
    PanelFooter,
    Button,
    HabilitationFragment,
    TranslationsSubscriber,
    Tooltip,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import formConstants from '../Constants';
import { CommonInput } from '../../../../../../../common/utils/Form/CommonFields';
import PermissionConstants from '../../../../../PermissionConstants';

/* ************************************* */
/* ********       VARIABLES       ******** */
/* ************************************* */
const modifyBody = (body, values) => {
    if (values.codeAssureur) {
        body.codeAssureur = values.codeAssureur;
    }
    if (values.codeGarantie) {
        body.codeGarantie = values.codeGarantie;
    }
    if (values.code) {
        body.code = values.code;
    }
    if (values.libelle) {
        body.libelle = values.libelle;
    }
};
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['bobb', 'common'], { wait: true })
class SearchPanelComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            checked: false,
        };
    }

    componentDidMount() {
        const { searchLots } = this.props;
        const body = {
            page: 1,
            perPage: 10,
            gTSupprimees: false,
        };
        searchLots(body);
    }

    @autobind
    setChecked(checked) {
        this.setState({ checked });
    }

    @autobind
    handleSubmit(values) {
        const { searchLots, handle } = this.props;
        const { checked } = this.state;
        const body = {
            page: 1,
            perPage: 10,
            gTSupprimees: checked,
        };
        modifyBody(body, values);
        handle(body);
        searchLots(body);
    }

    render() {
        const { handleSubmit, t } = this.props;
        const { checked } = this.state;

        return (
            <form onSubmit={handleSubmit(this.handleSubmit)}>
                <Panel header={<PanelHeader title={t('search.title')} />} border={false}>
                    <PanelSection>
                        <Row>
                            <Col xs={2}>
                                <Field
                                    name={formConstants.FIELDS.code}
                                    component={CommonInput}
                                    label={t('search.codeLot')}
                                    placeholder={t('search.codeLotPlaceholder')}
                                    clearable
                                />
                            </Col>
                            <Col xs={3}>
                                <Field
                                    name={formConstants.FIELDS.libelle}
                                    component={CommonInput}
                                    label={t('search.libelleLot')}
                                    placeholder={t('search.libelleLotPlaceholder')}
                                    clearable
                                />
                            </Col>
                            <Col xs={3}>
                                <Field
                                    name={formConstants.FIELDS_SEARCH.codeAssureur}
                                    component={CommonInput}
                                    label={t('search.codeAssureurGT')}
                                    placeholder={t('search.codeAssureurGTPlaceholder')}
                                    clearable
                                />
                            </Col>
                            <Col xs={3}>
                                <Field
                                    name={formConstants.FIELDS_SEARCH.codeGarantie}
                                    component={CommonInput}
                                    label={t('search.codeGT')}
                                    placeholder={t('search.codeGTPlaceholder')}
                                    clearable
                                />
                            </Col>
                        </Row>
                        <Row>
                            <Col xs={5} />
                            <Col xs={3}>
                                <Input
                                    name={formConstants.FIELDS_SEARCH.gTSupprimees}
                                    type="checkbox"
                                    id="gTSupprimees"
                                    onChange={(e) => this.setChecked(e.target.checked)}
                                    checked={checked}
                                />
                                <Label id="gTSupprimeesLabel" check for="gTSupprimees">
                                    {t('resultSearch.chipsGTSupprimees')}
                                </Label>
                                <CgIcon name="information-tooltip" className="default" />
                                <Tooltip target="gTSupprimeesLabel" placement="bottom">
                                    {t('resultSearch.chipsGTSupprimeesTooltip')}
                                </Tooltip>
                            </Col>
                        </Row>
                    </PanelSection>
                    <PanelFooter>
                        <HabilitationFragment
                            allowedPermissions={PermissionConstants.READ_LOT_PERMISSION}
                            fallbackComponent={<br />}
                        >
                            <Button behavior="primary" type="submit">
                                {t('action.search')}
                            </Button>
                        </HabilitationFragment>
                    </PanelFooter>
                </Panel>
            </form>
        );
    }
}

const propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    searchLots: PropTypes.func.isRequired,
    handle: PropTypes.func.isRequired,
};

const defaultProps = {};

SearchPanelComponent.propTypes = propTypes;
SearchPanelComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(SearchPanelComponent);
