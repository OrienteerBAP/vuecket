<script>
module.exports = {
    data: function() {
        return {
            text : "",
            messages: []
        }
    },
    methods: {
    	addMessage : function() {
    		this.vcInvoke("addMessage", {
    			message: this.text,
    			date: new Date()
    		});
    		this.text = "";
    	}
    }
}
</script>