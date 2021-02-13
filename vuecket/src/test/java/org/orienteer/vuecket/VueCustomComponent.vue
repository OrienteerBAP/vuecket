<template>
  <p class="custom-component">
    {{ text }}
  </p>
</template>

<script>
module.exports = {
    data: function() {
        return {
            text: 'Default Text'
        }
    }
}
</script>

<style>
p.custom-component {
  color: green;
  font-weight: bold;
}
</style>
