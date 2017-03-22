window.Ama = (function (Ama) {
     function Dialog (opts){
        opts = opts || {};
        this.id =opts.id || "";
        this.title =opts.title || "";
        this.body = opts.body || {};
     }

    Dialog.prototype.view= function() {
        return {
            view: "window",
            id: this.id,
            width: 400,
            position: "center",
            modal: true,
            head: {
                view: "toolbar",
                margin: -4,
                cols: [{
                    view: "label",
                    label: this.title
                }, {
                    view: "icon",
                    icon: "times-circle",
                    click: (function () {
                        $$(this.id).hide();
                    }).bind(this)
                }]

            },
            body: this.body
        };
    };

    Ama.showDialog = function (winId, node) {
        var $$winId = $$(winId);
        $$winId.getBody().clear();
        $$winId.show(node);
        $$winId.getBody().focus();
    };

    Ama.Dialog =Dialog;
    return Ama;
} (window.Ama));
