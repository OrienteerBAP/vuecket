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
    		this.messages.push({
    			message: this.text,
    			date: new Date()
    		});
    		this.text = "";
    	}
    }
}
</script>