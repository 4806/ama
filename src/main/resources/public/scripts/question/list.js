window.Question = (function (Question) {

    function List (ama) {
        this.ama = ama;
        this.data = new webix.DataCollection({
            url : '/ama/' + this.ama.id + '/questions?page=0&limit=10',
            scheme : {
                $init : function(obj) {
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
            .fail(function(xhr) {
                webix.message({
                    type : 'error',
                    text : xhr.response
                });
            });
    }


    Question.List = List;
    return Question;
} (window.Question || {}));
