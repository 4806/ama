window.Ama = (function (Ama) {

    function View (opts) {
        opts = opts || {};
        this.ama = opts.ama || {};
        this.onCreate = opts.onCreate || {};
        this.questions = new window.Question.List(this);
    }
/*
    function(result) {
        questions.add(result.json());
        questions.sort('id', 'desc');
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
                rows : [
                    toolbar.call(this),
                    new window.Question.View({
                        ama : this.ama,
                        onDelete : onDelete.bind(this)
                    }).view()
                ]
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
                click : this.onCreate
            } ]
        };

    }

    function onDelete (event, id) {
        webix.confirm('Are you sure you want to delete this?',
        function(action) {
            if (action === true) {
                this.questions.remove(id);
            }
        }.bind(this));
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

    Ama.View = View;
    return Ama;
} (window.Ama || {}));
