window.Question = (function (Question) {

    function Create (opts) {
        opts = opts || {};
        this.ama = opts.ama || {};
        this.onCreate = opts.onCreate || function () {};
        this.onError  = opts.onError || function () {};
    }


    Create.prototype.create = function () {
        var params, el = $$('create-question-form');

        if (el.validate()) {
            el.getTopParentView().hide();
            params = el.getValues();
            params.userId = webix.storage.cookie.get('userId');

            webix.ajax().post('/ama/' + this.ama.id + '/question', params)
                .then(this.onCreate.bind(this))
                .fail(this.onError.bind(this));

        } else {
            webix.message({
                type : 'error',
                text : 'Body cannot be empty'
            });
        }
    };

    Create.prototype.form = function () {
        return {
            view : 'form',
            id : 'create-question-form',
            elements :
            [
                {
                    view     : 'textarea',
                    label    : 'Body',
                    name     : 'body',
                    required : true
                },
                {
                    view  : 'button',
                    label : 'Create',
                    click : this.create.bind(this)
                }
            ]
        };
    };


    Question.Create = Create;
    return Question;
} (window.Question || {}));
