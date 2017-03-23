window.Logout = (function (Logout) {

    function Button (opts) {
        opts = opts || {};
        this.onClick = opts.onClick || function () {};
    }

    Button.prototype.view = function () {
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
                            click : this.onClick,
                            css   : 'right'
                        }
                    ]

                }
            ]
        };
    };

    Logout.Button = Button;
    return Logout;

} (window.Logout || {}));
