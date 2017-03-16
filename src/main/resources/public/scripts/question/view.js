window.Question = (function (Question) {
    var removeIcon = '<span class="fa-trash-o webix_icon top_right"></span>';

    function View (ama) {
        this.ama = ama;
        this.onDelete = function () {};
    }

    View.prototype.repr = function (obj) {
        // TODO add check if user created question
        return  'Created Date: ' + obj.created +
            removeIcon + '<br/>' + obj.body;
    };

    View.prototype.view = function () {
        return {
            view : 'dataview',
            id : 'questions',
            template : this.repr.bind(this),
            type : {
                height : '100',
                width : 'auto'
            },
            xCount : 1,
            yCount : 10,
            onClick : {
                'fa-trash-o' : this.onDelete.bind(this)
            }
        };
    };

    Question.View = View;
    return Question;
} (window.Question || {}));
