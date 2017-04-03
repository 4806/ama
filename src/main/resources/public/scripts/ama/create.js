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

            var amaAllowedUsers = [];
            $$('allowedUsersList').data.each(function(x) {
                amaAllowedUsers.push(x.name);
            });

            params = el.getValues();
            params.allowedUsers = amaAllowedUsers;
            params.public = $$('public').getValue();
            webix.ajax().headers({
                "Content-type":"application/json"
            }).post('/ama?title=' + params.title + '&public=' + params.public, JSON.stringify(amaAllowedUsers))
                .then(this.onCreate.bind(this))
                .fail(this.onError.bind(this));
        } else {
            webix.message({ type : 'error', 'text' : 'Title cannot be empty' });
        }
        $$('allowedUsersList').clearAll();
    };

    Create.prototype.toggleAllowedUsers = function() {
        if ($$('public').getValue() === 1){
            $$('allowedUser').disable();
            $$('addToList').disable();
        }else{
            $$('allowedUser').enable();
            $$('addToList').enable();
        }
    };

    Create.prototype.removeFromAllowedUsersList = function(name) {
        object = $$('allowedUsersList').data.find(function(x) {
            return name === x.name;
        });
        $$('allowedUsersList').data.remove(object.id);
    }

    Create.prototype.form = function () {
        return {
            view    : 'form',
            id      : 'create-ama-form',
            activeContent: {
                deleteButton:{
                    id:"deleteButtonId",
                    view:"button",
                    label:"Delete",
                    width:120
                }
            },
            rows : [

                {
                    view        : 'text',
                    label       : 'Title',
                    name        : 'title',
                    required    : true
                },
                {
                	id      : 'public',
                    view    : 'checkbox',  
                    label   : 'Public',
                    value   : 1,
                    click   : this.toggleAllowedUsers.bind(this)
                },
                {
                    cols : [{
                        view    : 'text',
                        id      : 'allowedUser',
                        disabled  : true
                    }, {
                        view    : 'button',
                        id      : 'addToList',
                        label   : '+',
                        disabled: true,
                        click   : function() {
                            var id = $$('allowedUsersList').add({ name :$$('allowedUser').getValue() });
                            $$('allowedUser').setValue('');
                        }
                    }]
                },
                {
                    view    : 'list',
                    id      : 'allowedUsersList',
                    width   : 320,
                    height  : 100,
                    template: "#name#",
                    type:{
                        height: 50
                    }
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
