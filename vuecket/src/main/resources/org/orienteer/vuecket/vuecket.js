const Vuecket = {
	install : function(Vue, options) {
		Vue.mixin({
			data : function() {
				return {
						'vcConfig' : null
						};
			},
			methods : {
				'vcInvoke' : function (method, ...args) {
						if(this.vcConfig) Wicket.Ajax.ajax({"u":this.vcConfig.url,
					    				  "m":"POST",
					    				  "c":this.$el.id,
					    				  "ep": {   
													a : true,
													m : method,		
													args : JSON.stringify(args)
												}
					    				  });
		 		},
				'vcCall' : function (method, ...args) {
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
														args : JSON.stringify(args)
													}
						    				  });
							});
						}
		 		},
				'vcApply' : function(dataPatch, propsPatch) {
					if(this.vcConfig) {
						if(dataPatch && Object.keys(dataPatch).length) Object.assign(this, dataPatch);
						if(propsPatch && Object.keys(propsPatch).length) {
							if(!this.vcConfig.props) this.vcConfig.props = {};
							Object.assign(this.vcConfig.props, propsPatch);
							
							if(this.$parent) this.$parent.$forceUpdate();
							else this.$forceUpdate();
						}
					}
				},
				'vcInit' : function(names) {
					if(this.vcConfig) {
						if(!names) names = this.vcConfig.init;
						this.vcInvoke("vcInit", names);
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
							collection.forEach(m => vueFunction.call(this, m, function(...args){
								this.vcInvoke(m, ...args);
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
						this.vcConfig.refreshTimer = setInterval(this.vcRefresh, this.vcConfig.rp*1000);
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
	var elm = document.getElementById(id);
	return elm?elm.__vue__:null;
};

Vue.loadRootVue = function(resourceURL, elm) {
	httpVueLoader.load(resourceURL)().then(function(d){new Vue(d).$mount(elm);})
};

Vue.loadComponentVue = function(resourceURL, name) {
	var promise = httpVueLoader.load(resourceURL)();
	Vue.component(name, function(resolve, reject){
					promise.then(resolve).catch(reject)
				  });
};

Vue.prototype.$vcMailbox = {
	genBoxId : function() {
		return Math.random().toString(36).substring(2, 15);
	}
}

Vue.prototype.$vcDataFiber = function(id, prop, value) {
	var v = Vue.getVueById(id);
	if(v) {
		if(v.vcConfig 
			&& v.vcConfig.props 
			&& v.vcConfig.props.hasOwnProperty(prop)) 
				return v.vcConfig.props[prop];
	}
	Vue.nextTick(function(){
		var v = Vue.getVueById(id);
		if(v) {
			if(!v.vcConfig.props) v.vcConfig.props = {};
			v.vcConfig.props[prop] = value;
		}
	});
	return value;
}