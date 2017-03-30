window.Question = (function (Question) {

    function List (opts) {
        opts = opts || {};
        this.ama = opts.ama || {};
        this.onError = opts.onError || function () {};
        this.data = new webix.DataCollection({
            url : '/ama/' + this.ama.id + '/questions?page=0&limit=10',
            scheme : {
                $change : function(obj) {
                    obj.created = (new Date(obj.created)).toLocaleString();
                }
            },
            on : {
                onBeforeDelete : deleteQuestion.bind(this)
            }
        });
    }

    List.prototype.sort = function (field, direction) {
        this.data.sort(field, direction);
    };

    List.prototype.remove = function (id) {
        this.data.remove(id);
    };

    List.prototype.add = function (question) {
        this.data.add(question);
    };

    function deleteQuestion (id) {
        webix.ajax().del('/ama/' + this.ama.id + '/question/' + id)
            .fail(this.onError.bind(this));
    }


    Question.List = List;
    return Question;
} (window.Question || {}));
