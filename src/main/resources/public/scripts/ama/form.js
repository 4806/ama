window.Ama = (function (Ama) {

    Ama.createForm = function (id, buttonName,onClick) {
        return {
            view: 'form',
            id: id,
            elements: [
                {
                    view: 'textarea',
                    label: 'Body',
                    name: 'body',
                    required: true
                },
                {
                    view: 'button',
                    label: buttonName,
                    click: onClick
                }
            ]
        };
    };

    return Ama;
}(window.Ama));