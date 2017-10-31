require('swiper/dist/css/swiper.css')

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import App from './App'
import router from './router'
import DatePicker from 'vue-md-date-picker'
import vue2Dropzone from 'vue2-dropzone'
import 'vue2-dropzone/dist/vue2Dropzone.css'
import Vuelidate from 'vuelidate'

Vue.use(VueAwesomeSwiper)
Vue.use(Vuelidate)

Vue.component('date-picker', DatePicker)
Vue.component('vue-dropzone', vue2Dropzone)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
