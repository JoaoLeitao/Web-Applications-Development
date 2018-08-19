import React from 'react'
import { Redirect } from 'react-router-dom'
import { message } from 'antd'
import Cookies from 'universal-cookie'
import { UserManager } from 'oidc-client'
import configs from './config'
import fetch from 'isomorphic-fetch'

const cookies = new Cookies();

const mgr = new UserManager(configs.MITREidSettings)

export default class extends React.Component {

    componentDidMount() {
        mgr.getUser()
        .then(user => {
            mgr.signinPopup()
                .then(user => {
                    const url = configs.API+"/accounts/register"
                    const obj = {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${user.access_token}`,
                            'Access-Control-Allow-Origin': '*'
                        }
                    }
                    fetch(url, obj).then(res => {
                        if(!res.ok) message.error('Error in login!')
                        else {
                            message.success('Success in login!')
                            cookies.set('account', user.access_token)
                        }
                        this.props.history.push('/')
                    })
                })
        })
    }
    
    render() {
        return <Redirect to={configs.HOME}/>
    }
}