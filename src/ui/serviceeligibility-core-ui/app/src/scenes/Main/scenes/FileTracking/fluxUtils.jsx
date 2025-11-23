import actions from './actions';
// Keep state var for search purpose
const tableStateEmis = {};
const tableStateRecu = {};
let scearchParameters = {};

const callFluxSearch = (fichierEmis, isNewSearch, tableState, searchParameters, dispatcher) => {
    const searchFlux = Object.assign(searchParameters);
    searchFlux.fichierEmis = fichierEmis;
    searchFlux.newSearch = isNewSearch;
    if (isNewSearch) {
        searchFlux.numberByPage = 5;
        searchFlux.position = 0;
    } else {
        const { pagination } = tableState;
        searchFlux.numberByPage = 5;
        searchFlux.position = pagination.start || 0;
    }

    (!fichierEmis
        ? dispatcher(actions.fetchingReceivedFlux(searchFlux, isNewSearch))
        : dispatcher(actions.fetchingSentFlux(searchFlux, isNewSearch))
    ).then(() => {
        dispatcher(
            !fichierEmis
                ? actions.fluxReceivedPaging(searchFlux.position)
                : actions.fluxSentPaging(searchFlux.position),
        );
    });
};

const searchCriteria = (searchData, dispatcher) => {
    scearchParameters = searchData;
    callFluxSearch(true, true, tableStateEmis, searchData, dispatcher);
    callFluxSearch(false, true, tableStateRecu, searchData, dispatcher);
};

const getCircuitLibelle = (circuits, codeCircuit) => {
    const circuit = (circuits || []).find((circuitItem) => circuitItem.codeCircuit === codeCircuit);
    return circuit ? circuit.label : null;
};

const getPourcentageRejet = (infoFichier) =>
    (parseInt(infoFichier.mouvementNonEmis, 10) /
        (parseInt(infoFichier.mouvementNonEmis, 10) + parseInt(infoFichier.mouvementEmis, 10))) *
    100;

const getDataFluxEntrantPagination = (page, dispatcher) => {
    if (!tableStateRecu.pagination) {
        tableStateRecu.pagination = {};
    }
    tableStateRecu.pagination.start = page;
    callFluxSearch(false, false, tableStateRecu, scearchParameters, dispatcher);
};

const getDataFluxSortantPagination = (page, dispatcher) => {
    if (!tableStateEmis.pagination) {
        tableStateEmis.pagination = {};
    }
    tableStateEmis.pagination.start = page;
    callFluxSearch(true, false, tableStateEmis, scearchParameters, dispatcher);
};

const enrichirRetourRest = (listeFlux, circuits) =>
    (listeFlux || []).map((flux) => {
        flux.circuit = getCircuitLibelle(circuits, flux.codeCircuit);
        flux.infoFichier.rejetPourcentage = 0;
        if (flux.infoFichier.mouvementEmis && flux.infoFichier.mouvementNonEmis) {
            flux.infoFichier.rejetPourcentage = getPourcentageRejet(flux.infoFichier);
        }
        if (flux.idDeclarant === '[ ]') {
            flux.idDeclarant = null;
        }
        if (flux.infoFichier.statut === 'TR' || flux.infoFichier.statut === 'OK') {
            flux.classStatut = true;
        } else {
            flux.classStatut = false;
        }
        return flux;
    });

const getGenericValueForCombobox = (processus) =>
    processus
        .map((process) => ({
            value: process.code,
            label: process.libelle,
        }))
        .sort((item) => item.value);

const getCircuitValueForCombobox = (circuits) =>
    circuits
        .map((circuit) => ({
            value: circuit.codeCircuit,
            label: `${circuit.codeCircuit} - ${circuit.libelleCircuit}`,
        }))
        .sort((item) => item.value);

const getEmittersValueForCombobox = (circuits) =>
    circuits
        .map((circuit) => circuit.emetteur)
        .filter((value, index, self) => self.indexOf(value) === index)
        .map((emitter) => ({
            value: emitter,
            label: emitter,
        }))
        .sort((item) => item.value);

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const fluxUtils = {
    searchCriteria,
    enrichirRetourRest,
    getGenericValueForCombobox,
    getCircuitValueForCombobox,
    getEmittersValueForCombobox,
    getDataFluxSortantPagination,
    getDataFluxEntrantPagination,
    getCircuitLibelle,
};

export default fluxUtils;
