window.User = (function (User) {

	function View (opts) {
		this.user = opts.user || {};
		this.onChange = opts.onChange || function () {};
	}

	View.prototype.repr = function (obj) {
		var user = obj.subject,
			followed = user.followed,
			template = '<span data-id="' + user.id + '">' + user.name + '</span>';

		if (followed) {
			template += '<span title="Unfollow ' + user.name + 
						'" class="webix_icon fa-close name-button"></span>';
		}
		else if (user.id !== this.user.id) {
			template += '<span title="Follow ' + user.name + 
						'" class="webix_icon fa-plus name-button"></span>';
		}	
		
		return template;
	};


	View.prototype.onFollow = function (e) {
		console.log('Follow');
		console.log(e);
		var user = {};
		webix.ajax().post('/user/follow/' + user.id)
			.then(function () {
				this.followed = true;
				this.onChange();
			}.bind(this));			
	};

	View.prototype.onUnfollow = function (e) {
		console.log('Unfollow');
		console.log(e);
		var user = {};
		webix.ajax().del('/user/follow/' + user.id)
			.then(function () {
				this.followed = false;
				this.onChange();
			}.bind(this));
	};

	User.View = View;	
	return User;
} (window.User || {}));
