window.Question = (function (Question) {
    var removeIcon = '<span class="fa-trash-o webix_icon right"></span>';
    var upvoteIcon = '<span class="fa fa-arrow-circle-o-up webix_icon">';
    var downvoteIcon = '<span class="fa fa-arrow-circle-o-down webix_icon">';

    function View (opts) {
        opts = opts || {};
        this.ama = opts.ama;
        this.onDelete = opts.onDelete || function () {};
    }

    View.prototype.repr = function (obj) {
        // TODO add check if user created question
    	
        return  'Created Date: ' + obj.created + " " +
            removeIcon + '<br/>' + obj.body + '<br/>' + upvoteIcon + downvoteIcon;
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
                'fa-trash-o' : this.onDelete.bind(this),
                'fa-arrow-cirlce-o-up' : this,
                'fa-arrow-cirlce-o-down' : this
            }
        };
    };

    Question.View = View;
    return Question;
} (window.Question || {}));
