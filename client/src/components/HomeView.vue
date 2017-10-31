<template>
    <div class="timeline-container">
        <div class="mdl-card timeline-thumbnails-card">
            <div class="mdl-card__media timeline-wrapper">
                <swiper ref="eventSwiper" :options="swiperOption">
                    <swiper-slide class="timeline-thumbnail" v-for="(event, index) in events" :key="event.id">
                        <a v-on:click="selectEvent(index)" class="timeline-thumbnail-container"
                            v-bind:class="{'selected-item':event.id == selectedEvent.id}">
                            <img :src="event.imagePath + '/thumbnail'">
                            <span class="timeline-thumbnail-date">{{event.eventDate}}</span>
                        </a>
                    </swiper-slide>
                    <swiper-slide class="timeline-thumbnail">
                        <a v-on:click="addNewItem" class="timeline-new-event" title="Add new Event"><i class="material-icons">add</i></a>
                    </swiper-slide>
                </swiper>
            </div>
        </div>
        <div class="timeline-details-component">
            <div class="mdl-card mdl-shadow--2dp timeline-details-card">
                <div class="mdl-card__media">
                    <div class="timeline-image" v-bind:style="{backgroundImage:'url(' + selectedEvent.imagePath + ')'}"></div>
                </div>
                <div class="mdl-card__title">
                    <h2 class="mdl-card__title-text">{{ selectedEvent.title }} - {{ selectedEvent.eventDate }}</h2>
                </div>
                <div class="mdl-card__supporting-text">{{ selectedEvent.eventDescription }}</div>
                <div class="mdl-card__menu">
                    <button class="mdl-button mdl-js-button mdl-button--icon" title="Edit this Event"
                        v-on:click="editItem(selectedEvent.id)">
                        <i class="material-icons">edit</i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import UtilsService from '../services/Utils.service'
    import TimelineService from '../services/Timeline.service'
    export default {
      data () {
        return {
          swiperOption: {
            slidesPerView: 'auto',
            spaceBetween: 10,
            centeredSlides: true,
            paginationClickable: true,
            mousewheelControl: true,
            observeParents: true,
            freeMode: true
          },
          events: [],
          selectedEvent: {}
        }
      },
      mounted () {
        this.loadEvents()
      },
      methods: {
        loadEvents: function () {
          TimelineService.getEvents()
            .then(events => {
              this.events = events
              if (events.length > 0) {
                this.selectEvent(0)
              }
              this.setDomUpdated()
            }
          )
        },
        hideMenu: function () {
          UtilsService.hideMenu()
        },
        addNewItem: function () {
          this.hideMenu()
          this.$router.push({name: 'post', params: {id: 0}})
        },
        editItem: function (eventId) {
          this.hideMenu()
          this.$router.push({name: 'post', params: {id: eventId}})
        },
        selectEvent: function (eventIndex) {
          this.selectedEvent = this.events[eventIndex]
          this.$refs.eventSwiper.swiper.slideTo(eventIndex)
        },
        setDomUpdated () {
          this.$nextTick(() => {
            window.componentHandler.upgradeDom()
          })
        }
      }
    }
</script>

<style scoped>
    .swiper-container {
        background-color: #eee;
    }
    .timeline-container .timeline-thumbnails-card {
        width: 100%;
        min-height: 120px;
    }
    .timeline-container .swiper-container {
        height: 100px;
        width: 100%;
    }
    .timeline-container .timeline-details-component {
        width: 100%;
        text-align: center;
    }
    .timeline-wrapper {
        position: relative;
    }
    .timeline-wrapper .timeline-thumbnail {
        width: 100px;
    }
    .timeline-wrapper .timeline-thumbnail a {
        width: 100%;
        height: 100%;
        display: block;
        position: relative;
    }
    .timeline-wrapper .timeline-thumbnail a img {
        opacity: 0.6;
    }
    .timeline-wrapper .timeline-thumbnail a.selected-item img,
    .timeline-wrapper .timeline-thumbnail a:hover img{
        opacity: 1;
    }
    .timeline-wrapper .timeline-thumbnail .timeline-thumbnail-date {
        width: 100%;
        display: block;
        position: absolute;
        bottom: 0px;
        background: rgba(43, 43, 43, 0.5);
        color: white;
        transform: scaleY(0);
        transform-origin: bottom;
        transition: transform 0.26s ease;
    }
    .timeline-wrapper .timeline-thumbnail a:hover .timeline-thumbnail-date,
    .timeline-wrapper .timeline-thumbnail a.selected-item .timeline-thumbnail-date{
        transform: scaleY(1);
    }
    .timeline-wrapper .timeline-thumbnail .timeline-new-event {
        display: block;
        line-height: 110px;
        vertical-align: middle;
        border: 2px dotted grey;
        box-sizing: border-box;
    }
    .timeline-wrapper .timeline-thumbnail .timeline-new-event:hover {
        background-color: lightgrey;
    }
    .timeline-details-card {
        margin: auto;
        width: 800px;
        text-align: left;
    }

    .timeline-image {
        background: transparent none no-repeat center;
        -webkit-background-size: cover;
        -moz-background-size: cover;
        -o-background-size: cover;
        background-size: cover;
        height: 250px;
    }
    .timeline-details-card > .mdl-card__menu {
         color: #fff;
     }
</style>