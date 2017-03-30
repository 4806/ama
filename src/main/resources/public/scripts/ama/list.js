window.Ama = (function (Ama) {
	var User = window.User;

    function List (opts) {
        opts = opts || {};
        this.onDelete = opts.onDelete || function () {};
        this.onView = opts.onView || function () {};
    }

    List.prototype.view = function () {
		var userView = new User.View({
			user 	: {
				id : window.getUserId() 
			}
		});		
		
        return {
            id      : 'ama-list',
            view    : 'datatable',
            columns : [
                {
                    id        : 'title',
                    header    : 'Title',
                    fillspace : 1,
                    template  : '<div class="title">#title#</div>'
                },
                {
                    id       : 'author',
                    header   : 'Author',
                    template : userView.repr.bind(userView) 
                },
                {
                    id       : 'icon',
                    header   : '',
                    template : '<div class="icon">#icon#</div>'
                }
            ],
            on : {
                onBeforeLoad : function() {
                    this.showOverlay('Loading...');
                },
                onAfterLoad : function() {
                    this.hideOverlay();
                    if (!this.count()) {
                        this.showOverlay('There are no AMAs');
                    }
                }
            },
            onClick : {
                'icon' : this.onDelete.bind(this),
                'title' : this.onView.bind(this),
				'fa-plus' : userView.onFollow.bind(userView),
				'fa-close': userView.onUnfollow.bind(userView)
            }
        };
    };

    Ama.List = List;
    return Ama;
} (window.Ama || {}));
