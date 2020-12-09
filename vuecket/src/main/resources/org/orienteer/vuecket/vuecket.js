const Vuecket = {
	install : function(Vue, options) {
		Vue.mixin({
			data : function() {
				return {
						'vcConfig' : null
						};
			},
			methods : {
				'vcInvoke' : function (method) {
						if(this.vcConfig) Wicket.Ajax.ajax({"u":this.vcConfig.url,
					    				  "m":"POST",
					    				  "c":this.$el.id,
					    				  "ep": {   
													a : true,
													m : method,		
													args : JSON.stringify(Array.prototype.slice.call(arguments, 1))
												}
					    				  });
		 		},
				'vcCall' : function (method) {
						if(this.vcConfig) {
							return new Promise((resolve, reject) => {
								var ms = this.$vcMailbox;
								var mailboxId = ms.genBoxId();
								var sh = function() {
									var ret = ms[mailboxId];
									delete ms[mailboxId];
									resolve(ret);
								};
								var fh = function() {
									delete ms[mailboxId];
									reject(new Error("Can't retrieve response'"));
								}
								Wicket.Ajax.ajax({"u":this.vcConfig.url,
						    				  "m":"POST",
						    				  "c":this.$el.id,
											  "sh":[sh],
											  "fh":[fh],
						    				  "ep": {   
														a : false,
														m : method,
														mb : mailboxId,	
														args : JSON.stringify(Array.prototype.slice.call(arguments, 1))
													}
						    				  });
							});
						}
		 		},
				'vcApply' : function(patch) {
					if(this.vcConfig) Object.assign(this, patch);
				},
				'vcLoad' : function(names) {
					if(this.vcConfig) {
						if(!names) names = this.vcConfig.load;
						this.vcInvoke("vcLoad", names);
					}
				},
				'vcRefresh' : function(names) {
					if(this.vcConfig) {
						if(!names) names = this.vcConfig.refresh;
						this.vcInvoke("vcRefresh", names);
					}
				}
		 	},
		 	beforeMount : function() {
				if(this.$el && this.$el.getAttribute) {
					this.$vcId = this.$el.id;
					this.$data.vcConfig = JSON.parse(this.$el.getAttribute('vc-config'));
				}
		 	},
			mounted : function() {
				if(this.$vcId) this.$el.id = this.$vcId;
				else {
					this.$vcId = this.$el.id;
					if(this.$el.getAttribute) this.$data.vcConfig = JSON.parse(this.$el.getAttribute('vc-config'));
				}
				if(this.vcConfig) {
					var registerMethods = function (collection, vueFunction) {
						if(collection) {
							collection.forEach(m => vueFunction.call(this, m, function(){
								var args = Array.prototype.slice.call(arguments);
								args.unshift(m);
								this.vcInvoke.apply(this, args);
							}));
						}
					};
					registerMethods.call(this, this.vcConfig.on, this.$on);
					registerMethods.call(this, this.vcConfig.once, this.$once);
					registerMethods.call(this, this.vcConfig.watch, this.$watch);
					if(this.vcConfig.load) {
						this.vcLoad();
					}
					if(this.vcConfig.observe) {
						this.vcConfig.observe.forEach(m => this.$watch(m, function(newValue){
								this.vcInvoke("vcObserved", m, newValue);
							}));
					}
					if(this.vcConfig.refresh) {
						this.vcConfig.refreshTimer = setInterval(this.vcRefresh, this.vcConfig.rp*2000);
					}
				}
			},
		    beforeDestroy () {
		      if(this.vcConfig.refreshTimer) clearInterval(this.vcConfig.refreshTimer);
		    }
		 });
	}
}

Vue.use(Vuecket);

Vue.getVueById = function(id) {
	return document.getElementById(id).__vue__;
};

Vue.prototype.$vcMailbox = {
	genBoxId : function() {
		return Math.random().toString(36).substring(2, 15);
	}
}