<template>
    <div class="event-edition-container">
        <div class="mdl-card mdl-shadow--2dp event-edition-card">
            <div class="mdl-card__supporting-text">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="width: 100%">
                    <input class="mdl-textfield__input" type="text" id="title" v-model="eventItem.title" required>
                    <label class="mdl-textfield__label" for="title">Title</label>
                    <span class="mdl-textfield__error">Title is required</span>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="width: 100%">
                    <input class="mdl-textfield__input" type="text" id="eventDate" v-model="eventItem.eventDate"
                           readonly @focus="show = true" required>
                    <label class="mdl-textfield__label" for="title">Date</label>
                    <span class="mdl-textfield__error">Date is required</span>
                    <transition name="calendar-fade">
                        <date-picker color="#3F51B5"
                                     min="1900-1-1"
                                     :max="maxDate"
                                     :format="formatDate"
                                     @close="show = false"
                                     v-if="show"
                                     v-model="eventItem.eventDate"></date-picker>
                    </transition>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="width: 100%">
                    <textarea class="mdl-textfield__input" type="text" rows= "3" id="description"
                              v-model="eventItem.eventDescription" required></textarea>
                    <label class="mdl-textfield__label" for="description">Description</label>
                    <span class="mdl-textfield__error">Description is required</span>
                </div>
                <div class="event-edition-image" v-bind:style="{backgroundImage:'url(' + eventItem.imagePath + ')'}">
                    <vue-dropzone ref="eventImageDropzone" id="dropzone" :options="dropzoneOptions"
                                  v-on:vdropzone-success="uploadSuccess" v-on:vdropzone-complete="uploadComplete"></vue-dropzone>
                </div>
                <div>
                    <h5>References</h5>
                    <ul class="demo-list-item mdl-list">
                        <li v-for="(value, key) in eventItem.references" :key="key" class="mdl-list__item">
                            <div class="mdl-list__item-primary-content">
                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="width: 100%">
                                    <input class="mdl-textfield__input" type="text" :id="'reference' + key"
                                           v-model="eventItem.references[key]" required>
                                    <label class="mdl-textfield__label" :for="'reference' + key">Reference</label>
                                </div>
                            </div>
                            <a class="mdl-list__item-secondary-action" v-on:click="removeReference(key)"><i class="material-icons">delete</i></a>
                        </li>
                        <li>
                            <button class="mdl-button mdl-js-button" v-on:click="addReference()">
                                <i class="material-icons">add</i>
                                Add reference
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="mdl-card__actions">
                <button v-on:click="save()" :disabled="$v.$invalid" class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored mdl-js-ripple-effect">Save</button>
                <a v-on:click="goBack()" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">Cancel</a>
            </div>
        </div>
    </div>
</template>

<script>
    import TimelineService from '../services/Timeline.service'
    import UtilsService from '../services/Utils.service'
    import moment from 'moment'
    import { required } from 'vuelidate/lib/validators'
    export default {
      mounted: function () {
        this.$nextTick(() => {
          if (this.$route.params.id && this.$route.params.id !== '0') {
            TimelineService.getEvent(this.$route.params.id)
              .then(data => {
                this.eventItem = data
                this.setDomUpdated()
              })
          } else {
            this.setDomUpdated()
          }
        })
      },
      methods: {
        formatDate (date) {
          return moment(date).format('MM/DD/YYYY')
        },
        uploadSuccess (ev, response) {
          this.eventItem.imagePath = response.path
        },
        uploadComplete (file) {
          this.$refs.eventImageDropzone.removeFile(file)
        },
        addReference () {
          this.eventItem.references.push('')
          this.setDomUpdated()
        },
        removeReference (index) {
          this.eventItem.references.splice(index, 1)
          this.setDomUpdated()
        },
        hideMenu: function () {
          UtilsService.hideMenu()
        },
        goBack () {
          this.hideMenu()
          this.$router.push({name: 'home'})
        },
        save () {
          let itemToSave = {
            title: this.eventItem.title,
            eventDate: moment(this.eventItem.eventDate, 'MM/DD/YYYY'),
            eventDescription: this.eventItem.eventDescription,
            imagePath: this.eventItem.imagePath,
            references: this.eventItem.references.join('@@')
          }
          if (this.eventItem.id) {
            itemToSave.id = this.eventItem.id
          }
          TimelineService.saveEvent(itemToSave)
              .then(data => {
                if (data.id) {
                  this.goBack()
                }
              })
        },
        setDomUpdated () {
          this.$nextTick(() => {
            window.componentHandler.upgradeDom()
          })
        }
      },
      data () {
        return {
          eventItem: {
            imagePath: '/api/file/0',
            references: [],
            eventDate: moment(new Date()).format('MM/DD/YYYY')
          },
          show: false,
          dropzoneOptions: {
            url: '/api/file/upload',
            maxFilesize: 20,
            maxFiles: 1,
            acceptedFiles: 'image/*',
            addRemoveLinks: true,
            dictDefaultMessage: 'Click here or drag an image to set the cover'
          },
          maxDate: moment(new Date()).format('YYYY-M-D')
        }
      },
      validations: {
        eventItem: {
          title: {
            required
          },
          eventDate: {
            required
          },
          eventDescription: {
            required
          },
          references: {
            $each: {
              required
            }
          }
        }
      }
    }
</script>

<style>
    .event-edition-container {
        text-align: center;
    }
    .event-edition-card {
        text-align: left;
        margin:auto;
        width: 800px;
        overflow: visible;
    }
    .event-edition-image {
        background: transparent none no-repeat center;
        -webkit-background-size: cover;
        -moz-background-size: cover;
        -o-background-size: cover;
        background-size: cover;
        height: 250px;
    }
    .event-edition-image .dropzone {
        background-color: transparent;
        height: 250px;
    }
    .event-edition-image .dropzone .dz-message {
        margin: 3em 0;
        font-size: 2em;
        font-weight: 600;
        color: gray;
        mix-blend-mode: difference;
    }
</style>