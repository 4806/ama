webix.ready(function () {
	
//	webix.ui({
//		type:"line",
//		rows: [
//			{ view:"toolbar", elements:[
//				{ view:"button", value:"Add", width: 70},
//				{ view:"button", value:"Delete", width: 70 },
//				{ view:"button", value:"Update", width: 70 },
//				{ view:"button", value:"Clear Form", width: 85 }]
//			},
//			{ cols:[
//				{ template: "Column 1"}, 
//				{ template: "Column 2"} ]
//			}
//		]
//	});
//});
    webix.ajax().get("../../java/org/sysc/ama/list", "page=0&limit=2", function(amas) {
//	        webix.ui({
//	            rows: [{
//	                view     : "template",
//	                type     : "header",
//	                template : "List of AMAs"
//	            }, {
//	                view       : "datatable",
//	                autoConfig : true,
//	                data       : amas
//	            }]
//	        });
    	webix.message(amas);
    });
});
