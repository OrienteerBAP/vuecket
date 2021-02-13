<template>
  <p class="custom-component">
    {{ text }} {{ content }}
  </p>
</template>

<script>
module.exports = {
    props: ['content'],
    data: function() {
        return {
            text: 'Internal Text'
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
