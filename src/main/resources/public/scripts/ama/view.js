window.Ama = (function (Ama) {

    function View (id, title) {
        this.id = id;
        this.title = title;
        this.questions = new window.Questions.List(this);
    }
/*
    function(result) {
        questions.add(result.json());
        questions.sort('id', 'desc');
    }

    function(event, id) {
        webix.confirm('Are you sure you want to delete this?',
        function(action) {
            if (action === true) {
                questions.remove(id);
            }
        });
    }
*/
    View.prototype.view = function () {
        return {
            view        : 'window',
            id          : 'ama-window',
            fullscreen  : true,
            head : {
                view    : 'toolbar',
                margin  : -4,
                cols    : [
                    {
                        view : 'icon',
                        icon : 'arrow-left',
                        label : 'Back',
                        click : function() {
                            $$('ama-window').hide();
                        }
                    },
                    {
                        view : 'label',
                        label : this.title,
                        align : 'center'
                    }
                ]
            },
            body : {
                rows : [ toolbar, new window.Question.List(this).form() ]
            }
        };
    };

    function toolbar () {
        return {
            view : 'toolbar',
            cols : [ {
                view  : 'button',
                id    : 'create',
                value : 'Create Question',
                width : 150,
                align : 'left',
                click : function() {
                    Ama.showForm('win-create-question');
                }
            } ]
        };

    }
/*
    function questionDialog () {
        return {
            view     : 'window',
            id       : 'win-create-question',
            width    : 400,
            position : 'center',
            modal    : true,
            head     : {
                view     : 'toolbar',
                margin   : -4,
                cols     : [
                    {
                        view  : 'label',
                        label : 'New Question'
                    },
                    {
                        view  : 'icon',
                        icon  : 'times-circle',
                        click : function() {
                            $$('win-create-question').hide();
                        }
                    }
                ]
            },
            body : new window.Question.Create().form()
        };
    }
*/

    //webix.ui(questionDialog());
    //$$('questions').sync(questions);
//    new webix.ui(new View().view()).show();

    Ama.View = View;
    return Ama;
} (window.Ama || {}));
