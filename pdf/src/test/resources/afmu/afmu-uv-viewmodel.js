var viewConfig = [{
    type: 'uv-kategori',
    labelKey: 'KAT_1.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_1.RBK',
        components: [{
            type: 'uv-boolean-value',
            modelProp: 'harFunktionsnedsattning'
        }, {
            type: 'uv-del-fraga',
            labelKey: 'DFR_1.2.RBK',
            hideExpression: '!harFunktionsnedsattning   ',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'funktionsnedsattning'
            }]
        }]
    }
    ]
}, {
    type: 'uv-kategori',
    labelKey: 'KAT_2.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_2.RBK',
        components: [{
            type: 'uv-boolean-value',
            modelProp: 'harAktivitetsbegransning'
        },{
            type: 'uv-del-fraga',
            labelKey: 'DFR_2.2.RBK',
            hideExpression: '!harAktivitetsbegransning',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'aktivitetsbegransning'
            }]
        }]
    }
    ]
}, {
    type: 'uv-kategori',
    labelKey: 'KAT_3.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_3.RBK',
        components: [{
            type: 'uv-boolean-value',
            modelProp: 'harUtredningBehandling'
        },{
            type: 'uv-del-fraga',
            labelKey: 'DFR_3.2.RBK',
            hideExpression: '!harUtredningBehandling',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'utredningBehandling'
            }]
        }]
    }
    ]
}, {
    type: 'uv-kategori',
    labelKey: 'KAT_4.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_4.RBK',
        components: [{
            type: 'uv-boolean-value',
            modelProp: 'harArbetetsPaverkan'
        },{
            type: 'uv-del-fraga',
            labelKey: 'DFR_4.2.RBK',
            hideExpression: '!harArbetetspaverkan',
            components: [{
                type: 'uv-simple-value',
                modelProp: 'arbetetsPaverkan'
            }]
        }]
    }
    ]
}, {
    type: 'uv-kategori',
    labelKey: 'KAT_5.RBK',
    components: [{
        type: 'uv-fraga',
        labelKey: 'FRG_5.RBK',
        components: [{
            type: 'uv-simple-value',
            modelProp: 'ovrigt'
        }]
    }
    ]
}, {
    type: 'uv-skapad-av',
    modelProp: 'grundData.skapadAv'
}];
