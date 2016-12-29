var data =
{
  flowers: [],
  user: null
}

var flower =
{
  name: "",
  description: ""
};

var user =
{
  username: "",
  password: ""
}

Vue.component('login-form', {
  template: '#login-template',
  data: function() {
    return user;
  },
  methods: {
    onSubmit: function() {
      var that = this;
      if (!this.username && !this.password) {
        alert('username and password are required!')
      } else {
        axios.post('/api/login', user)
        .then(function (response) {
          user.username = "";
          user.password = "";
          that.$emit('loggedIn', response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
      }
    }
  }
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


// --------------------
// Top Level Components

var Logout = {
  template: '<p>loading...</p>', // hack!
  beforeCreate: function() {
    axios.post('/api/logout')
      .then(function (response) {
        data.user = null;
        router.push("/");
      }).catch(function (error) {
        data.user = null;
        router.push("/");
      });
  }
}

var LoginForm = {
  template: '<login-form v-on:loggedIn="userLoggedIn"/>',
  methods: {
    userLoggedIn: function(user) {
      console.log(user);
      data.user = user;
      router.push("/");
    }
  }
}

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
  { path: '/about', component: About },
  { path: '/login', component: LoginForm },
  { path: '/logout', component: Logout }
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
  beforeCreate: function() {
    var that = this;
    axios.get('/api/flowers')
      .then(function (response) {
        that.flowers = response.data;
      }).catch(function (error) {
        console.log('fail: ' + error);
      });

    axios.get('/api/session')
      .then(function (response) {
        data.user = response.data;
      }).catch(function (error) {
        console.log('fail: ' + error);
      });
  },
  template: '#main-container',
});
