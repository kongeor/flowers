var flower =
{
  name: "",
  description: ""
};

Vue.component('heading', {
  template: '<h1>Flowers</h1>'
});

Vue.component('create-flower', {
  template: '#create-flower-template',
  data: function() {
    return flower;
  },
  methods: {
    onSubmit: function() {
      var that = this;
      if (!this.name) {
        alert('name is required!')
      } else {
        axios.post('/api/flowers', {
          name: this.name,
          description: this.description
        })
        .then(function (response) {
          flower.name = "";
          flower.description = "";
          that.$emit('created', response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
      }
    }
  }
});

Vue.component('flower', {
  props: ['flower'],
  template: '<div><h3>{{ flower.name }}</h3><p>{{ flower.description }}</p></div>'
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
  template: '<div class="container"><heading /><create-flower v-on:created="flowerCreated" /><flower-list v-bind:flowers="flowers"></flower-list></div>',
  methods: {
    flowerCreated: function(flower) {
        this.flowers.push(flower);
    }
  }
});
