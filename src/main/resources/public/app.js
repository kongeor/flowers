Vue.component('heading', {
  template: '<h1>Flowers</h1>'
});

Vue.component('flower', {
  props: ['flower'],
  template: '<div><h3>{{ flower.name }}</h3></div>'
});

Vue.component('flower-list', {
  props: ['flowers'],
  template: '<div><flower v-for="flower in flowers" v-bind:flower="flower"></flower></div>'
});

var app = new Vue({
  el: '#app',
  data: {
    flowers: []
  },
  created: function() {
    var that = this;
    axios.get('/api/flowers')
      .then(function (response) {
        that.flowers = response.data;
      }).catch(function (error) {
        console.log('fail: ' + error);
      });
  },
  template: '<div class="container"><heading /><flower-list v-bind:flowers="flowers"></flower-list></div>'
});
