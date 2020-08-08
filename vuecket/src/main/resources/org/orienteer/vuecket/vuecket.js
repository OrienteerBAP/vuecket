const Vuecket = {
	install : function(Vue, options) {
		Vue.mixin({
			methods : {
				'$vcCall' : function (arg) {
						var vcUrl = this.$el.getAttribute("vc-url");
						if(vcUrl) Wicket.Ajax.ajax({"u":vcUrl,
					    				  "m":"POST",
					    				  "c":this.$el.id,
					    				  "ep": { arguments : JSON.stringify(Array.prototype.slice.call(arguments))}
					    				  });
		 			}
		 	}
		 });
	}
}

Vue.use(Vuecket);