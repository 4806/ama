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
                'Content-type':'application/json'
            }).post('/ama?title=' + params.title + '&public=' + params.public,
                        JSON.stringify(amaAllowedUsers))
                .then(this.onCreate.bind(this))
                .fail(this.onError.bind(this));
        } else {
            webix.message({ type : 'error', 'text' : 'Title cannot be empty' });
        }
        $$('allowedUsersList').clearAll();
    };

    Create.prototype.toggleAllowedUsers = function() {
        if ($$('public').getValue() === 1){
            $$('allowedUser').hide();
            $$('addToList').hide(); 
            $$('allowedUsersList').hide();
            $$('user-persmission-label').hide();
        }else{
            $$('allowedUser').show();
            $$('addToList').show();
            $$('allowedUsersList').show();
            $$('user-persmission-label').show();
        }
    };

    Create.prototype.removeFromAllowedUsersList = function(name) {
        var object = $$('allowedUsersList').data.find(function(x) {
            return name === x.name;
        });
        $$('allowedUsersList').data.remove(object.id);
    };

    Create.prototype.form = function () {
        return {
            view    : 'form',
            id      : 'create-ama-form',
            hight: 'auto',
            width: 400,
            activeContent: {
                deleteButton:{
                    id:'deleteButtonId',
                    view:'button',
                    label:'Delete',
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
                },{
                	view : 'label',
                	id  :  'user-persmission-label',
                	label : 'Allowed Users',
                	hidden : true
                },
                {
                    cols : [{
                        view    : 'text',
                        placeholder : 'username',
                        id      : 'allowedUser',
                        hidden  : true
                    }, {
                        view    : 'button',
                        id      : 'addToList',
                        label   : '+',
                        hidden: true,
                        click   : function() {
                        	if('' != $$('allowedUser').getValue()){
                        		$$('allowedUsersList').add({
                        			name :$$('allowedUser').getValue()
                        		});
                        		$$('allowedUser').setValue('');
                        	}else{
                        		webix.message({
                        			type : 'error',
                        			text : 'username can\'t be empty'
                        		});
                        	}
                        }
                    }]
                },
                {
                    view    : 'list',
                    id      : 'allowedUsersList',
                    width   : 320,
                    height  : 100,
                    hidden : true,
                    template: '#name#<span class=\'info fa-trash-o webix_icon\'></span>',
                    onClick:{
                        info:function(e, id){
                            this.remove(id);
                            return false;
                        }
                    },
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
