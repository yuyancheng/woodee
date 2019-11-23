<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <div>
      <form>
        <input v-model="iptVal" debounce="2000" lazy/>
      </form>
      <div>{{iptVal}}</div>
    </div>
    <p>{{msg}}</p>
    <div v-on:click.self="print('aaaa')">
      <button v-on:click.once="print('bbbb');show('dfdd');">click me</button>
    </div>
    <transition name="fade">
      <div v-if="showA" style="width:300px;height:200px;background-color:#999;"></div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      iptVal: '',
      showA: false
    }
  },
  mounted () {
  },
  methods: {
    print (s) {
      this.showA = true
      console.log(s)
      // this.$appendTo('#wrapper')
    },
    show (v) {
      console.log(v)
    }
  },
  filters: {
    up (v) {
      return String(v).toUpperCase()
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.fade-enter-active, .fade-leave-active {
  transition: opacity 1.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}

h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
