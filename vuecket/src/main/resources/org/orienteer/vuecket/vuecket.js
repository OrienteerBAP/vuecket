const Vuecket = {
	install : function(Vue, options) {
		Vue.mixin({
			methods : {
				'$vcCall' : function () {
						var vcUrl = this.$el.getAttribute("vc-url");
						if(vcUrl) Wicket.Ajax.ajax({"u":vcUrl,
					    				  "m":"POST",
					    				  "c":this.$el.id,
					    				  "ep": { args : JSON.stringify(Array.prototype.slice.call(arguments))}
					    				  });
		 			},
				'$vcApply' : function(patch) {
					console.log(patch);
					for(var id in patch) {
						this[id] = patch[id];
					}
				} 
		 	},
		 	beforeMount : function() {
				if(this.$el) this.$vcId = this.$el.id;
		 	},
			mounted : function() {
				if(this.$vcId) this.$el.id = this.$vcId;
				else this.$vcId = this.$el.id;
			}
		 });
	}
}

Vue.use(Vuecket);

window.$vc = function(id) {
	return document.getElementById(id).__vue__;
}