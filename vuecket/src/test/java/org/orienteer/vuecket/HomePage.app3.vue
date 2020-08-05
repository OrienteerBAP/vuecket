<template>
  <p class="msg3">
    {{ message3 }}
  </p>
</template>

<script>
module.exports = {
    data: function() {
        return {
            message3: 'Hello Vuecket. App#3'
        }
    }
}
</script>

<style>
p.msg3 {
  color: red;
}
</style>
