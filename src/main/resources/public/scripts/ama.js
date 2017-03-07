var Ama = (function() {

	var amas = new webix.DataCollection({
		url : "/ama/list?page=0&limit=10",
		map : {
			author : "#subject.name#"
		} 
	});

	var createAmaForm = {
		view : "form",
		id : "create_ama_form",
		elements : [ {
			view : "text",
			label : "Title",
			name : "title",
			required : true
		}, {
			view : "checkbox",
			label : "Public",
			name : "public"
		}, {
			view : "button",
			label : "Create",
			click : function() {
				if (this.getParentView().validate()) {
					this.getTopParentView().hide();
					var params = this.getParentView().getValues();
					params.userId = webix.storage.cookie.get("userId");
					webix.ajax().post("/ama", params).then(function(result) {
						console.log(result.json());
						amas.add(result.json());
						amas.sort("id","desc");
					}).fail(function(xhr) {
						webix.message({
							type : "error",
							text : xhr.response
						});
					});
				} else {
					webix.message({
						type : "error",
						"text" : "Title can't be empty"
					});
				}
			}
		} ]
	};

	function showForm(winId, node) {
		var $$winId = $$(winId);
		$$winId.getBody().clear();
		$$winId.show(node);
		$$winId.getBody().focus();
	}

	return {
		showForm : showForm,
		createAmaForm : createAmaForm,
		amas : amas
	};
})();

// Setup page when DOM is ready
webix.ready(function() {
	var amaWindow={
		view : "window",
		id : "AMAWindow",
		fullscreen : true,
		head : {

			view : "toolbar",
			margin : -4,
			cols : [ {
				view : "icon",
				icon : "arrow-left",
				label : "Back",
				click : function() {
					$$("AMAWindow").hide();
				}
			}, {
				view : "label",
				name : "Title"
			} ]
		},
		body : webix.copy(Ama.createAmaForm)
	};


	// Create the modal window to create AMAs

	var modalWindow = {
		view : "window",
		id : "WinCreateAMA",
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
					$$("WinCreateAMA").hide();
				}
			} ]
			
		},
		body : webix.copy(Ama.createAmaForm)
	};

	var newAmaButton = {
		view : "toolbar",
		elements : [ {
			view : "button",
			value : "New AMA",
			width : 70,
			click : function() {
				Ama.showForm("WinCreateAMA");
			}
		} ]
	};

	var listAma = {
		id : "ama_list",
		view : "datatable",
		columns : [ 
			{id : "title"}, 
			{id : "author"}
		],
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
			onItemClick: function(id){
				Ama.showForm("AMAWindow");
			}
		}
	};

	var toolBar = {
		type : "line",
		rows : [ newAmaButton, listAma]
	};
	
	// Create the modal window to create AMAs
	webix.ui(modalWindow);
	
	// create the toolbar
	webix.ui(toolBar);
	
	// create window to display an AMA
	webix.ui(amaWindow);
	

	// sync the ama list with the
	$$("ama_list").sync(Ama.amas);

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



