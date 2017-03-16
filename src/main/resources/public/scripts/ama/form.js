window.Ama = (function (Ama) {
    Ama.showForm = function (winId, node) {
        var $$winId = $$(winId);
        $$winId.getBody().clear();
        $$winId.show(node);
        $$winId.getBody().focus();
    };
    return Ama;
} (window.Ama));
