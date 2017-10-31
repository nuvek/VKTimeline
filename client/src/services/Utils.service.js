export default {
  hideMenu () {
    document.getElementsByClassName('mdl-layout__drawer')[0].classList.remove('is-visible')
    document.getElementsByClassName('mdl-layout__obfuscator')[0].classList.remove('is-visible')
  }
}
