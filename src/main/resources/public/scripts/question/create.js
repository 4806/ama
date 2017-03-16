window.Question = (function (Question) {

    function Create (ama) {
        this.ama = ama;
        this.onCreate = function () {};
    }


    Create.prototype.create = function () {
        var params, el = $$('create-question-form');

        if (el.validate()) {
            el.getTopParentView().hide();
            params = this.getParentView().getValues();
            params.userId = webix.storage.cookie.get('userId');

            webix.ajax().post('/ama/' + this.ama.id + '/question', params)
                .then(this.onCreate)
                .fail(function(xhr) {
                    webix.message({
                        type : 'error',
                        text : xhr.response
                    });
                });

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
