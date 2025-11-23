/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    Button,
    Col,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Status,
    Tooltip,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import history from '../../../../../../../history';
import ServiceMetierUtils from '../../../ServiceMetierUtils';
import businessUtils from '../../../../../../../common/utils/businessUtils';
import './style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getLastContract = (benefDetails) =>
    benefDetails?.contrats?.length > 0 ? benefDetails.contrats[benefDetails.contrats.length - 1] : null;

const goToDetails = (id, tab, isExternal) => {
    const env = isExternal ? 'external' : 'internal';
    history.push(`/beneficiary_tracking/${env}/details/${id}/${tab}`);
};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class SearchResultItemComponent extends Component {
    setContractInHeader = (metaMap, header, t) => {
        if (metaMap.contrats && metaMap.contrats.length > 0) {
            const contractNb = metaMap.contrats.length;
            // eslint-disable-next-line no-plusplus
            for (let i = 0; i < contractNb; i++) {
                const number = metaMap.contrats[i].numeroContrat;
                if (number) {
                    if (i === 0) {
                        header.push(`${t('beneficiaries.contract')} : ${number}`);
                    } else {
                        header.push(`${number}`);
                    }
                }
            }
        }
    };

    setBirthDateInHeader = (identite, header, t) => {
        if (identite) {
            let birthDate = '';
            if (identite.dateNaissance) {
                birthDate = identite.dateNaissance;
                header.push(`${t('beneficiaries.dateNaissance')} : ${businessUtils.transformDate(birthDate)}`);
            } else if (
                identite.affiliationsRO &&
                identite.affiliationsRO.length > 0 &&
                identite.affiliationsRO[0].dateNaissance
            ) {
                birthDate = identite.affiliationsRO[0].dateNaissance;
                if (birthDate) {
                    header.push(`${t('beneficiaries.dateNaissance')} : ${businessUtils.transformDate(birthDate)}`);
                }
            }
        }
    };

    setNirInHeader = (identite, header) => {
        if (identite) {
            if (identite.nir && identite.nir.code) {
                const nir = identite.nir.code;
                header.push(`NIR : ${nir}`);
            } else if (
                identite.affiliationsRO &&
                identite.affiliationsRO.length > 0 &&
                identite.affiliationsRO[0].nir
            ) {
                const nir = identite.affiliationsRO[0].nir.code;
                if (nir) {
                    header.push(`NIR : ${nir}`);
                }
            }
        }
    };

    renderAmc(declarants, metaMap) {
        if (declarants && declarants.length > 0) {
            this.amcNumber = (metaMap.amc || {}).idDeclarant;
            const amc = declarants.find((declarant) => declarant.numeroRNM === this.amcNumber);
            const amcName = amc && amc.nom;
            const amcCode = amc && amc.numeroPrefectoral;
            let amcStatus = null;
            if (amcName && amcCode) {
                amcStatus = `AMC : ${amcCode} - ${amcName}`;
            } else if (amcName) {
                amcStatus = `AMC : ${amcName}`;
            } else if (amcCode) {
                amcStatus = `AMC : ${amcCode}`;
            }
            if (amcStatus) {
                return <Status label={amcStatus} behavior="secondary" />;
            }
        }
        return null;
    }

    renderService(index, text, iconName) {
        const { item, serviceMetierList, clientType } = this.props;
        const exists = (item.metaMap.services || []).some((service) =>
            ServiceMetierUtils.checkIfServiceIsActive(index, service, serviceMetierList, clientType),
        );

        return (
            <Col xs={4}>
                <span className="d-flex align-items-center">
                    <CgIcon name={iconName} size="lg" id={iconName} className={exists ? 'text-primary pr-1' : 'pr-1'} />
                    <span className={exists ? 'text-dark' : 'text-muted'}>{text}</span>
                </span>
            </Col>
        );
    }

    render() {
        const { t, serviceMetierList, declarants, item, fromExternalEnv, clientType } = this.props;
        const { metaMap } = item || {};
        const contract = getLastContract(metaMap);
        const { data } = contract || {};
        let name = '';
        if (data && data.nom) {
            const { nomFamille, nomUsage, prenom } = data.nom;
            name = businessUtils.getBenefIdentity(nomUsage, nomFamille, prenom);
        }
        const showExternalButton = fromExternalEnv && clientType === 'OTP';

        const header = [];
        const { identite } = metaMap;
        this.setBirthDateInHeader(identite, header, t);
        this.setNirInHeader(identite, header);
        this.setContractInHeader(metaMap, header, t);

        const sortedServiceMetier = ServiceMetierUtils.orderServiceMetierlist(serviceMetierList);

        let disabledButton = item.metaMap.services.length === 0;
        let index = 0;

        if (!disabledButton) {
            const existingServices = sortedServiceMetier.filter((service) =>
                item.metaMap.services.includes(service[1].code),
            );
            index = existingServices.length > 0 ? existingServices[0][1].ordre : -1;
        }

        if (index === -1) {
            disabledButton = true;
        }

        return (
            <Panel
                key="resultItem"
                id="resultItem"
                header={
                    <PanelHeader
                        title={name}
                        status={this.renderAmc(declarants, metaMap)}
                        context={header.join(' - ')}
                        actions={[
                            {
                                id: 'shuffle',
                                icon: 'shuffle',
                                component: () => (
                                    <div className="mr-2" style={{ display: 'flex', marginTop: '10px' }}>
                                        {showExternalButton && (
                                            <Fragment>
                                                <CgIcon id={`tooltipId-${item.id}`} name="shuffle" size="1x" />
                                                <Tooltip target={`tooltipId-${item.id}`} placement="top">
                                                    {t(`beneficiaries.toolTip.tooltipOrigineExterne`)}
                                                </Tooltip>
                                            </Fragment>
                                        )}
                                    </div>
                                ),
                            },
                            {
                                id: 'open',
                                label: t('open'),
                                icon: 'angle-right',
                                component: () => (
                                    <Button
                                        id="open-button"
                                        outlineNoBorder
                                        behavior="primary"
                                        disabled={disabledButton}
                                        onClick={() => goToDetails(item.id, index, showExternalButton)}
                                    >
                                        {`${t('open')} `}
                                        <CgIcon name="right-scroll" />
                                    </Button>
                                ),
                            },
                        ]}
                    />
                }
            >
                <PanelSection>
                    <Row>
                        {disabledButton ? (
                            <span className="text-primary ml-3">{t('beneficiaries.noService')}</span>
                        ) : (
                            (Object.keys(sortedServiceMetier) || []).map((key) => {
                                const service = sortedServiceMetier[key][1];
                                return this.renderService(service.ordre, service.libelle, service.icone);
                            })
                        )}
                    </Row>
                </PanelSection>
            </Panel>
        );
    }
}

SearchResultItemComponent.propTypes = {
    t: PropTypes.func,
    item: PropTypes.shape(),
    declarants: PropTypes.arrayOf(PropTypes.shape()),
    serviceMetierList: PropTypes.shape(),
    clientType: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SearchResultItemComponent;
