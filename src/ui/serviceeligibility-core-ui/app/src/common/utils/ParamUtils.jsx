/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

const label = 'parameters.label';
const code = 'parameters.code';
const codeLabelActionsColumns = (t) => [
    {
        id: 'code',
        Header: t(code),
        accessor: 'code',
        minWidth: 30,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'label',
        Header: t(label),
        accessor: 'libelle',
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'actions',
        Header: '',
        accessor: 'actions',
        minWidth: 30,
    },
];

const codeLabelActionsWithCellColumns = (t) => [
    {
        id: 'code',
        Header: t(code),
        accessor: 'code',
        minWidth: 30,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'label',
        Header: t(label),
        accessor: 'libelle',
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'actions',
        Header: '',
        accessor: 'actions',
        Cell: (props) => props.value(),
        minWidth: 30,
    },
];

const codeLabelActionsForClaimWithCellColumns = (t) => [
    {
        id: 'code',
        Header: t(code),
        accessor: 'code',
        minWidth: 15,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'label',
        Header: t(label),
        accessor: 'libelle',
        minWidth: 75,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'domains',
        Header: t('parameters.domaines'),
        accessor: 'domaines',
        minWidth: 140,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'actions',
        Header: '',
        accessor: 'actions',
        Cell: (props) => props.value(),
        minWidth: 20,
    },
];

const codeLabelActionsWithOrderAndCellColumns = (t) => [
    {
        id: 'code',
        Header: t(code),
        accessor: 'code',
        minWidth: 30,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'label',
        Header: t(label),
        accessor: 'libelle',
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'order',
        Header: t('parameters.order'),
        accessor: 'ordre',
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'icon',
        Header: t('parameters.icon'),
        accessor: 'icone',
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'actions',
        Header: '',
        accessor: 'actions',
        Cell: (props) => props.value(),
        minWidth: 30,
    },
];

const codeLabelTranscodeActionsColumns = (t) => [
    {
        id: 'code',
        Header: t(code),
        accessor: 'code',
        minWidth: 30,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'label',
        Header: t(label),
        accessor: 'libelle',
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'transcode',
        Header: t('parameters.transcode'),
        accessor: 'transcodification',
        minWidth: 30,
        headerStyle: { textAlign: 'left' },
    },
    {
        id: 'actions',
        Header: '',
        accessor: 'actions',
        Cell: (props) => props.value(),
        minWidth: 30,
    },
];

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamUtils = {
    codeLabelActionsColumns,
    codeLabelActionsWithCellColumns,
    codeLabelActionsWithOrderAndCellColumns,
    codeLabelTranscodeActionsColumns,
    codeLabelActionsForClaimWithCellColumns,
};

export default ParamUtils;
