// Setup page when DOM is ready
webix.ready(function() {
    var Ama = window.Ama;

    var amas = new webix.DataCollection({
        url : "/ama/list?page=0&limit=10",
        map : {
            author : "#subject.name#"
        }
    });

    // Create the modal window to create AMAs

    var modalWindow = {
        view : "window",
        id : "win-create-ama",
        width : 300,
        position : "center",
        modal : true,
        head : {
            view : "toolbar",
            margin : -4,
            cols : [ {
                view : "label",
                label : "New AMA"
            }, {
                view : "icon",
                icon : "times-circle",
                click : function() {
                    $$("win-create-ama").hide();
                }
            } ]

        },
        body : new Ama.Create().form()
    };

    var newAmaButton = {
        view : "toolbar",
        elements : [ {
            view : "button",
            value : "New AMA",
            width : 70,
            click : function() {
                Ama.showForm("win-create-ama");
            }
        } ]
    };

    var listAma = {
        id : "ama-list",
        view : "datatable",
        columns : [ {
            id : "title",
            header : "Title",
            fillspace : 1
        }, {
            id : "author",
            header : "Author"
        } ],
        on : {
            onBeforeLoad : function() {
                this.showOverlay("Loading...");
            },
            onAfterLoad : function() {
                this.hideOverlay();
                if (!this.count()) {
                    this.showOverlay("There are no AMAs");
                }
            },
            onItemClick : function(id) {
                Ama.viewAma(id);
            }
        }
    };

    var toolBar = {
        type : "line",
        rows : [ newAmaButton, listAma ]
    };

    // Create the modal window to create AMAs
    webix.ui(modalWindow);

    // create the toolbar
    webix.ui(toolBar);

    // sync the ama list with the
    $$("ama-list").sync(amas);

    // check if the user is already in the cookie
    var userId = webix.storage.cookie.get("userId");
    if (!userId) {
        // if user doesn't exist create one
        webix.ajax().post("/user/create", {
            name : "Foo"
        }).then(function(user) {
            webix.storage.cookie.put("userId", user.json().id);
        });
    }

});
