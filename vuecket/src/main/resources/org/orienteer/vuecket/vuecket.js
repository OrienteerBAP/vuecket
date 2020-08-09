const Vuecket = {
	install : function(Vue, options) {
		Vue.mixin({
			data : function() {
				return {
						'vcConfig' : null
						};
			},
			methods : {
				'vcCall' : function () {
						if(this.vcConfig) Wicket.Ajax.ajax({"u":this.vcConfig.url,
					    				  "m":"POST",
					    				  "c":this.$el.id,
					    				  "ep": { args : JSON.stringify(Array.prototype.slice.call(arguments))}
					    				  });
		 			},
				'vcApply' : function(patch) {
					Object.assign(this, patch);
				} 
		 	},
		 	beforeMount : function() {
				if(this.$el) {
					this.$vcId = this.$el.id;
					this.$data.vcConfig = JSON.parse(this.$el.getAttribute('vc-config'));
				}
		 	},
			mounted : function() {
				if(this.$vcId) this.$el.id = this.$vcId;
				else {
					this.$vcId = this.$el.id;
					this.$data.vcConfig = JSON.parse(this.$el.getAttribute('vc-config'));
				}
			}
		 });
	}
}

Vue.use(Vuecket);

Vue.getVueById = function(id) {
	return document.getElementById(id).__vue__;
}