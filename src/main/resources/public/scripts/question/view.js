window.Question = (function (Question) {
    var removeIcon = '<span class="fa-trash-o webix_icon float_right"></span>';
    var answerButton = '<input class="webixtype_form webix_el_button float_right" type="button" value="Answer Question">';

    function View (opts) {
        opts = opts || {};
        this.ama = opts.ama;
        this.onDelete = opts.onDelete || function () {};
    }

    View.prototype.repr = function (obj) {
        // TODO add check if user created question
        return  'Created Date: ' + obj.created +
            removeIcon + '<br/>' + obj.body +
            answerButton;
    };

    View.prototype.view = function () {
        return {
            view : 'dataview',
            id : 'questions',
            template : this.repr.bind(this),
            type : {
                height : 100,
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
