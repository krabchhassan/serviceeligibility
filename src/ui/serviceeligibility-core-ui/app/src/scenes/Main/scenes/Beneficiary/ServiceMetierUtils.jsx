/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */
const checkIfServiceIsActive = (index, service, serviceMetierList, clientType) => {
    let isServiceActive = false;

    Object.keys(serviceMetierList).forEach((property) => {
        if (clientType === 'INSURER') {
            if (index === serviceMetierList[property].ordre && service === serviceMetierList[property].code) {
                isServiceActive = true;
            }
        } else if (
            index === serviceMetierList[property].ordre &&
            service === serviceMetierList[property].code &&
            service === 'Service_TP'
        ) {
            isServiceActive = true;
        }
    });
    return isServiceActive;
};

const orderServiceMetierlist = (serviceMetierList) => {
    if ((Object.entries(serviceMetierList) || []).length > 1) {
        return (Object.entries(serviceMetierList) || []).sort((a, b) => a[1].ordre.localeCompare(b[1].ordre));
    }
    return Object.entries(serviceMetierList);
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ServiceMetierUtils = {
    checkIfServiceIsActive,
    orderServiceMetierlist,
};

export default ServiceMetierUtils;
