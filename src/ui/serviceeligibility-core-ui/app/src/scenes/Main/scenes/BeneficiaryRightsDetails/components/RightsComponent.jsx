/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import fill from 'lodash/fill';
import {
    Col,
    Combobox,
    Label,
    Switch,
    LoadingSpinner,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import businessUtils from '../../../../../common/utils/businessUtils';
import RightsItem from './components/RightsItem';
import Constants from '../../../../../common/utils/Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getKey = (index) => {
    return index + 0;
};

const fillArrayWithEmptyStrings = (array, finalLength) => {
    const numberToFill = finalLength - array.length;
    return [...array, ...fill(Array(numberToFill), '')];
};

const formalizeDomain = (domains, conventions, t) =>
    domains.map((domain) => {
        // immutable pattern
        const clonedDomain = { ...domain };

        // conventions
        clonedDomain.periodesDroit.forEach((periode) => {
            periode.conventionnements.forEach((convention) => {
                convention.typeConventionnements.forEach((item) => {
                    const conventionReferentialObject = businessUtils.getConvention(
                        conventions,
                        item.typeConventionnement.code,
                    );
                    const libelle = conventionReferentialObject ? conventionReferentialObject.libelle : '';
                    return {
                        ...convention,
                        libelle: libelle || '',
                    };
                });
            });
        });

        // prestations
        clonedDomain.periodesDroit = (clonedDomain.periodesDroit || []).map((periodeDroit) => {
            periodeDroit.prestations = (periodeDroit.prestations || []).map((prestations) => {
                prestations.prestations = (prestations.prestations || []).map((prestation) => {
                    const params = prestation.formule?.parametres || [];
                    const maxLength = params.length;

                    let paramPlat = params.map((entry) => `${entry.numero} - ${entry.valeur}`);
                    if (paramPlat.length === 0) {
                        paramPlat[0] = t('beneficiaryRightsDetails.claim.noParameterFormula');
                    } else {
                        paramPlat = fillArrayWithEmptyStrings(paramPlat, maxLength);
                    }

                    return {
                        ...prestation,
                        formule: {
                            ...prestation.formule,
                            paramPlat,
                        },
                    };
                });
                return prestations;
            });

            return periodeDroit;
        });

        return clonedDomain;
    });

const getFormattedDomains = (domainesByMaille, conventions, t) => {
    Object.keys(domainesByMaille).forEach((maille) => {
        domainesByMaille[maille] = formalizeDomain(domainesByMaille[maille], conventions, t);
    });
    return domainesByMaille;
};

const displayStitches = (t, clientType) => {
    if (clientType === Constants.TOPOLOGY_INSURER) {
        return [
            t('beneficiaries.domaineTP'),
            t('beneficiaries.garantie'),
            t('beneficiaries.produit'),
            t('beneficiaries.referenceCouverture'),
            t('beneficiaries.naturePrestations'),
        ];
    }
    return [
        t('beneficiaries.domaineTP'),
        t('beneficiaries.garantie'),
        t('beneficiaries.produit'),
        t('beneficiaries.referenceCouverture'),
    ];
};

const toCamelCase = (str) => {
    return str
        .replace(/\s(.)/g, function ($1) {
            return $1.toUpperCase();
        })
        .replace(/\s/g, '')
        .replace(/^(.)/, function ($1) {
            return $1.toLowerCase();
        });
};

const getValueForCombobox = (list) => {
    return (list || []).map((item) => ({
        label: item,
        value: toCamelCase(item),
    }));
};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class RightsComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            domainesByMaille: {},
            TPOnlineSelected: true,
            selectedStitch:
                props.clientType === Constants.TOPOLOGY_INSURER
                    ? Constants.DEFAULT_STITCH_INSURER
                    : Constants.DEFAULT_STITCH_OTP,
        };
    }

    static getDerivedStateFromProps(nextProps) {
        if (nextProps) {
            const { t, domainesFromHistoConsoByMaille, benefToDisplay, conventions } = nextProps;
            if (t && domainesFromHistoConsoByMaille) {
                return {
                    domainesByMaille: getFormattedDomains(domainesFromHistoConsoByMaille, conventions, t),
                };
            }

            if (t && benefToDisplay) {
                return {
                    domainesByMaille: getFormattedDomains(benefToDisplay.maillesDomaineDroits, conventions, t),
                };
            }
        }

        return null;
    }

    setDisplayStitch(value) {
        this.setState({
            selectedStitch: value.value,
        });
    }

    switchOnlineOffline() {
        const { TPOnlineSelected } = this.state;
        this.setState({
            TPOnlineSelected: !TPOnlineSelected,
        });
    }

    renderButtons() {
        const { t } = this.props;
        const { TPOnlineSelected } = this.state;

        return (
            <Row>
                <Col className="pt-2 d-flex">
                    <Label check for="tp" className="pr-2">
                        {t('beneficiaryRightsDetails.TPOnline')}
                    </Label>
                    <Switch
                        behaviorOff="info"
                        behaviorOn="info"
                        checked={!TPOnlineSelected}
                        onChange={() => this.switchOnlineOffline()}
                    />
                    <Label check for="htp" className="pl-2">
                        {t('beneficiaryRightsDetails.TPOffline')}
                    </Label>
                </Col>
            </Row>
        );
    }

    render() {
        const { t, benefToDisplay, domainesFromHistoConsoByMaille, clientType } = this.props;
        const { TPOnlineSelected, selectedStitch } = this.state;
        if (
            (!benefToDisplay && !domainesFromHistoConsoByMaille) ||
            (benefToDisplay && Object.keys(benefToDisplay.maillesDomaineDroits).length === 0) ||
            (domainesFromHistoConsoByMaille && Object.keys(domainesFromHistoConsoByMaille).length === 0)
        ) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }
        const { domainesByMaille } = this.state;
        const panelHeader = (
            <PanelHeader
                title={domainesFromHistoConsoByMaille ? '' : t('beneficiaryRightsDetails.domainesTP')}
                actions={[
                    {
                        component: () => this.renderButtons(),
                    },
                ]}
            />
        );

        const panelContent = (
            <PanelSection border={false}>
                <Combobox
                    id="display-stitches"
                    key="displayStitchCombo"
                    name="displayStitchCombo"
                    options={getValueForCombobox(displayStitches(t, clientType))}
                    onChange={(value) => this.setDisplayStitch(value)}
                    placeholder={t('beneficiaries.placeHolder.displayStitchesPlaceholder')}
                    value={selectedStitch}
                    clearable={false}
                />
                {domainesByMaille &&
                    (domainesByMaille[selectedStitch] || domainesByMaille.mailleDomaineTP || []).map((item, index) => (
                        <RightsItem
                            id={`rights-${index}`}
                            key={`rights-${item.code}-${getKey(index)}`}
                            domain={item}
                            TPOnlineSelected={TPOnlineSelected}
                            selectedStitch={selectedStitch}
                            domainesFromHistoConsoByMaille={domainesFromHistoConsoByMaille}
                        />
                    ))}
            </PanelSection>
        );

        return (
            <Panel id="rights-panel" header={panelHeader} border={!domainesFromHistoConsoByMaille}>
                {panelContent}
            </Panel>
        );
    }
}

RightsComponent.propTypes = {
    t: PropTypes.func,
    benefToDisplay: PropTypes.shape(),
    domainesFromHistoConsoByMaille: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default RightsComponent;
