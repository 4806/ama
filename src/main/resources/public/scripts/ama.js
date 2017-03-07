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

						amas.add(result.json());
						amas.sort("id", "desc");
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

	function viewAma(id) {
		var questions = new webix.DataCollection({
			url : "/ama/" + id + "/questions?page=0&limit=10"
		});
		
		var ama = amas.getItem(id);
		function deleteAma(id){
			aelrt
		}
		var questionViewer = {
			view : "dataview",
			id : "questions",
			template : "created Date: #created# <br/> #body#",
		    type:{
		        height:"100", 
		        width:"auto"
		    },
		    xCount:1, yCount:10
		};

		var createQuestionForm = {
			view : "form",
			id : "create_question_form",
			elements : [
					{
						view : "textarea",
						label : "Body",
						name : "body",
						required : true
					},
					{
						view : "button",
						label : "Create",
						click : function() {
							if (this.getParentView().validate()) {
								this.getTopParentView().hide();
								var params = this.getParentView().getValues();
								params.userId = webix.storage.cookie
										.get("userId");
								webix.ajax().post("/ama/" + id + "/question",
										params).then(function(result) {
									questions.add(result.json());
									questions.sort("id", "desc");
								}).fail(function(xhr) {
									webix.message({
										type : "error",
										text : xhr.response
									});
								});
							} else {
								webix.message({
									type : "error",
									"text" : "Body can't be empty"
								});
							}
						}
					} ]
		};

		var modalWindow = {
			view : "window",
			id : "WinCreateQuestion",
			width : 400,
			position : "center",
			modal : true,
			head : {
				view : "toolbar",
				margin : -4,
				cols : [ {
					view : "label",
					label : "New Question"
				}, {
					view : "icon",
					icon : "times-circle",
					click : function() {
						$$("WinCreateQuestion").hide();
					}
				} ]

			},
			body : webix.copy(createQuestionForm)
		};

		var toolbar = {
			view : "toolbar",
			cols : [ {
				view : "button",
				id : "create",
				value : "Create Question",
				width : 150,
				align : "left",
				click : function() {
					Ama.showForm("WinCreateQuestion");
				}
			} ]
		};

		var window = new webix.ui({
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
					label : ama.title,
					align : "center"

				} ]
			},
			body : {
				rows : [ toolbar, questionViewer]
			}
		});

		webix.ui(modalWindow);
		$$("questions").sync(questions);
		window.show();
	}

	function showForm(winId, node) {
		var $$winId = $$(winId);
		$$winId.getBody().clear();
		$$winId.show(node);
		$$winId.getBody().focus();
	}

	return {
		showForm : showForm,
		createAmaForm : createAmaForm,
		amas : amas,
		viewAma : viewAma
	};
})();

// Setup page when DOM is ready
webix.ready(function() {

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
		columns : [ {
			id : "title",
			label : "Title",
			fillspace : 1
		}, {
			id : "author"
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
