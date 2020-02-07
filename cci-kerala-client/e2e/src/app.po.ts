import { browser, by, element } from 'protractor';

export class AppPage {
  navigateTo() {
    return browser.get('/');
  }

  getTitleText() {
    return element(by.css('scps-root h1')).getText();
  }

  getLink(link)
  {
    return element(by.linkText(link));
  }

  openPage(link)
  {
    return element(by.linkText(link)).click();
  }
}
