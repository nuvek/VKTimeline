import axios from 'axios'

export default {
  getEvents () {
    return axios.get('/api/timeline/items')
        .then(response => response.data)
  },
  getEvent (id) {
    return axios.get('/api/timeline/items/' + id)
      .then(response => this.translateEventData(response.data))
  },
  translateEventData (eventData) {
    if (!eventData.references) {
      eventData.references = []
    } else {
      eventData.references = eventData.references.split('@@')
    }
    return eventData
  },
  saveEvent (timelineEvent) {
    return axios[timelineEvent.id ? 'put' : 'post']('/api/timeline/items', timelineEvent)
        .then(response => response.data)
  }
}
