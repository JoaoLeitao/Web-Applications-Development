import React from 'react'
import { Redirect } from 'react-router-dom'
import Cookies from 'universal-cookie'
import { UserManager } from 'oidc-client'
import configs from './config'

const cookies = new Cookies();

const mgr = new UserManager(configs.MITREidSettings)

export default () => {
    mgr.signoutPopup().then(_ => true)
    cookies.remove('account')
    return <Redirect to={configs.HOME}/>
}