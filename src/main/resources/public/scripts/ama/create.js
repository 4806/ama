window.Ama = (function (Ama) {

    function Create () {
        this.onCreate = function () {};
    }

    Create.prototype.create = function () {
        var params, el = $$('create-ama-form');

        if (el.validate()) {
            el.getTopParentView().hide();

            params = el.getValues();
            params.userId = webix.storage.cookie.get('userId');

            webix.ajax().post('/ama', params)
                .then(this.onCreate)
                .fail(function (xhr) {
                    webix.message({
                        type : 'error',
                        text : xhr.response
                    });
                });
        } else {
            webix.message({ type : 'error', 'text' : 'Title cannot be empty' });
        }
    };


    Create.prototype.form = function () {
        return {
            view    : 'form',
            id      : 'create-ama-form',
            elements : [
                {
                    view        : 'text',
                    label       : 'Title',
                    name        : 'title',
                    required    : true
                },
                {
                    view    : 'checkbox',
                    label   : 'Public',
                    name    : 'public'
                },
                {
                    view    : 'button',
                    label   : 'Create',
                    click   : this.create.bind(this)
                }
            ]
        };
    };


    Ama.Create = Create;
    return Ama;

} (window.Ama || {}));
