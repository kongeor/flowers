var data =
{
  flowers: []
}

var flower =
{
  name: "",
  description: ""
};

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


// --------------------
// Top Level Components

var FlowerList = {
  data: function() {
    return data;
  },
  template: '<flower-list v-bind:flowers="flowers">'
}

var AddFlower = {
  template: '<create-flower v-on:created="flowerCreated"/>',
  methods: {
    flowerCreated: function(flower) {
      data.flowers.push(flower);
      router.push("/");
    }
  }
}

var About = { template: '<div>Flower watering management app</div>' }

var routes = [
  { path: '/', component: FlowerList },
  { path: '/add-flower', component: AddFlower },
  { path: '/about', component: About }
]

var router = new VueRouter({
  routes: routes
});

var app = new Vue({
  router: router,
  el: '#app',
  data: function() {
    return data;
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
  template: '#main-container',
});
