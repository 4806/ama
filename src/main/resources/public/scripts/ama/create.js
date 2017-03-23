window.Ama = (function (Ama) {

    function Create (opts) {
        opts = opts || {};
        this.onCreate = opts.onCreate || function () {};
        this.onError = opts.onError || function () {};
    }

    Create.prototype.create = function () {
        var params, el = $$('create-ama-form');

        if (el.validate()) {
            el.getTopParentView().hide();

            params = el.getValues();
            webix.ajax().post('/ama', params)
                .then(this.onCreate.bind(this))
                .fail(this.onError.bind(this));
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
