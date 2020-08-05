<template>
  <p class="msg">
    {{ message2 }}
  </p>
</template>

<script>
module.exports = {
    data: function() {
        return {
            message2: 'Hello Vuecket'
        }
    }
}
</script>

<style>
p.msg {
  color: #2c3e50;
}
</style>
