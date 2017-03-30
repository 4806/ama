var AmaLogin = (function () {

    function submit(){
        webix.send( "/login",  $$("loginForm").getValues() );
    }

    function signUp() {
        var formVals    = $$("loginForm").getValues(),
            params      = {
                name        : formVals.username,
                password    : formVals.password
            };

        webix.ajax().post("/user/create", params, {
            success: function() {
                submit();
            }
        ,
            error: function(xhr) {
            console.log(response);
            webix.message({
                type : "error",
                text : xhr.response
            });
        }});
    }

    return {
        submit: submit,
        signUp: signUp
    };
})();

webix.ready(function() {
    webix.ui({
        view : "form",
        id : "loginForm",
        width : 300,
        position : "center",
        elements: [
            { view: "text", label: "Username", name: "username" },
            { view: "text", label: "Password", name: "password", type: "password" },
            { view: "button", value: "Login", width: 150, align: "center", click: AmaLogin.submit },
            { view: "button", value: "Sign Up", width: 150, align: "center", click: AmaLogin.signUp}
        ]
    });

    if (window.location.search.indexOf("?error") > -1) {
        webix.message("Invalid Credentials");
    }
});
