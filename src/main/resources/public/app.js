var data =
{
  flowers: [],
  myFlowers: [],
  user: null
}

var flower =
{
  name: "",
  description: ""
};

var myFlower =
{
  notes: "",
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

Vue.component('add-my-flower', {
  template: '#add-my-flower-template',
  data: function() {
    return myFlower
  },
  methods: {
    onSubmit: function() {
      var that = this;
      axios.post('/api/user/flowers', {
        flowerId: this.$route.params.id,
        notes: this.notes
      })
      .then(function (response) {
        this.notes = "";
        that.$emit('added', response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
    }
  }
});

Vue.component('flower', {
  props: ['flower'],
  template: '<div><h3>{{ flower.name }}</h3><p>{{ flower.description }}</p>' +
    '<button class="button-primary" v-on:click.prevent="add" >Add to my flowers</button><hr></div>',
  methods: {
    add: function() {
      router.push('/add-my-flower/' + this.flower.id);
    }
  }
});

Vue.component('flower-list', {
  props: ['flowers'],
  template: '<div><flower v-for="flower in flowers" v-bind:flower="flower"></flower></div>'
});

Vue.component('my-flower', {
  props: ['flower'],
  template: '<div><h3>{{ flower.id }}</h3><p>{{ flower.notes }}</p><hr></div>',
});

Vue.component('my-flower-list', {
  props: ['myFlowers'],
  template: '<div><my-flower v-for="flower in myFlowers" v-bind:flower="flower"></flower></div>'
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
      data.user = user;
      fetchUserFlowers();
      router.push("/");
    }
  }
}

var MyFlowerList = {
  data: function() {
    return data;
  },
  template: '<my-flower-list v-bind:myFlowers="myFlowers">'
}

var FlowerList = {
  data: function() {
    return data;
  },
  template: '<flower-list v-bind:flowers="flowers">'
}

var AddMyFlower = {
  template: '<add-my-flower v-on:added="flowerAdded"/>',
  methods: {
    flowerAdded: function(flower) {
      data.myFlowers.push(flower);
      router.push("/");
    }
  }
}

var AddFlower = {
  template: '<create-flower v-on:created="flowerCreated"/>',
  methods: {
    flowerCreated: function(flower) {
      data.flowers.push(flower);
      router.push("/flowers");
    }
  }
}

var About = { template: '<div>Flower watering management app</div>' }

var routes = [
  { path: '/', component: MyFlowerList },
  { path: '/flowers', component: FlowerList },
  { path: '/add-my-flower/:id', component: AddMyFlower },
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

    //TODO only for logged in
    fetchUserFlowers();

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

function fetchUserFlowers() {
  axios.get('/api/user/flowers')
  .then(function (response) {
    data.myFlowers = response.data;
  }).catch(function (error) {
    console.log('fail: ' + error);
  });
}
