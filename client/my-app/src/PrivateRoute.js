import React from 'react'
import { Redirect } from 'react-router-dom'
import { message } from 'antd'
import Cookies from 'universal-cookie'
import configs from './config';

const cookies = new Cookies();

const redirect = () => {
    message.warning('This resource is private, please login first.')
    return (<Redirect to={configs.LOGIN}/>)
}

const PrivateRoute = ({component: Component, ...props}) => (
    cookies.get('account') ? <Component {...props}/> : redirect()
)

export default PrivateRoute