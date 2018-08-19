import React from 'react';
import logo from './cl.png';

export default class Home extends React.Component {
    render() {
        return (
            <div>
                <div>
                    <h1> Welcome to Yet Another Checklist Management System! </h1>
                </div>
                <img src={logo} className="App-logo" alt="logo" />
                <div className="App-footer">
                    Made by: João Leitão and Renato Júnior.
                </div>
            </div>
        )
    }
}