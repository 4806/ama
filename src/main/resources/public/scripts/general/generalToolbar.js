window.General = (function (General) {

    General.createToolbar = function () {
        return {
            type : 'line',
            rows : [
                {
                    view : 'toolbar',
                    elements : [
                        {
                            view : 'button',
                            value : 'Logout',
                            width : 70,
                            click : function () {
                            	window.location = '/logout';
                            },
                            css   : 'right'
                        }
                    ]

                }
            ]
        };
    };

    return General;

} (window.General || {}));
