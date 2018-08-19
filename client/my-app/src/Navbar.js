import React from 'react'
import { Link } from 'react-router-dom'
import { Menu, Icon } from 'antd';
import Cookies from 'universal-cookie'
import configs from './config'

const cookies = new Cookies();

class Navbar extends React.Component {
    render() {
      return (
        <Menu className="navbar" mode="horizontal">
          <Menu.Item key="home">
            <Link to={configs.HOME}>
                <Icon type="home"/>
                Home
            </Link>
          </Menu.Item>
          {(!cookies.get('account')) ?
            <Menu className="navbar" mode="horizontal">
              <Menu.Item key="login" className="navbar_login">
                <Link to={configs.LOGIN}>
                    <Icon type="login"/>
                    Login
                </Link>
              </Menu.Item>
            </Menu>
            :
            <Menu className="navbar" mode="horizontal">
              <Menu.Item key="templates" className="navbar_templates">
                <Link to={configs.TEMPLATES}>
                    <Icon type="database"/>
                    Templates
                </Link>
              </Menu.Item>
              <Menu.Item key="checklists" className="navbar_checklists">
                <Link to={configs.CHECKLISTS}>
                    <Icon type="database"/>
                    Checklists
                </Link>
              </Menu.Item>
              <Menu.Item key="logout" className="navbar_logout">
                <Link to={configs.LOGOUT}>
                    <Icon type="logout"/>
                    Logout
                </Link>
              </Menu.Item>
            </Menu>
          }
        </Menu>
      );
    }
  }
  
  export default Navbar