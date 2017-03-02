document.onreadystatechange = function () {

    webix.ajax().get("/ama/list", function(amas) {
        webix.ui({
            rows: [{
                view     : "template",
                type     : "header",
                template : "List of AMAs"
            }, {
                view       : "datatable",
                autoConfig : true,
                data       : amas
            }]
        });
    });
};
