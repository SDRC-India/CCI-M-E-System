import { AppPage } from './app.po';

describe('workspace-project App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getTitleText()).toEqual('CCI Monitoring System');
  });

//   it('should Open Login Page', () => {
//     expect(page.getLink('Login').isPresent()).toBe(true).then(()=> {
//       console.log('login Icon is present');
//       page.openPage('Login').then(() =>{

//         console.log('Page opened')
//       });
//   });
// });
});
