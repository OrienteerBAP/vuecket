<template>
  <p>
    {{ message2 }}
  </p>
</template>

<script>
import Canvas from "./components/Canvas";

export default {
  name: "App2",
  data: function() {
  	return { message: 'Hello Vuecket 2'};
  }
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
