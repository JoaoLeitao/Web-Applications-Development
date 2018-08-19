import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom'

import 'antd/dist/antd.css'
import './App.css'

import Navbar from './Navbar'
import Home from './Home'
import Login from './Login'
import Logout from './Logout'
import Templates from './Templates'
import Checklists from './Checklists'
import PrivateRoute from './PrivateRoute'
import Template from './Template'
import Checklist from './Checklist'
import TemplateItem from './TemplateItem'
import ChecklistItem from './ChecklistItem'
import redirect from './redirect'
import Error from './Error'
import configs from './config';

class App extends Component {
  render() {
    return (
      <BrowserRouter>
        <div className="App">
          <Navbar />
          <header className="App-header">
            <h1 className="App-title">YACMS</h1>
          </header>
          <Switch>
            <Route exact path={configs.HOME} component={Home}/>
            <Route exact path={configs.LOGIN} component={Login}/>
            <PrivateRoute exact path={configs.LOGOUT} component={Logout}/>
            <PrivateRoute exact path={configs.TEMPLATES} component={Templates}/>
            <PrivateRoute exact path={configs.CHECKLISTS} component={Checklists}/>
            <PrivateRoute exact path={configs.TEMPLATE} component={Template}/>
            <PrivateRoute exact path={configs.CHECKLIST} component={Checklist}/>
            <PrivateRoute exact path={configs.TEMPLATEITEM} component={TemplateItem}/>
            <PrivateRoute exact path={configs.CHECKLISTITEM} component={ChecklistItem}/>
            <Route exact path={configs.REDIRECT} component={redirect}/>
            <Error />
          </Switch>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;