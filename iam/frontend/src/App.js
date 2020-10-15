import React, { Component } from "react";
import Home from "./home";
import Login from "./login";
import Session from "./session";
import { Button, Layout } from 'antd';
import "./app.css";
import 'antd/dist/antd.css'; 

class App extends Component {

  constructor() {
    super()
    this.state = {
      loggedIn: false,
      visible: false,
    }
  }

  componentDidMount() {
    this.autoLogin();
    // session display
    // if login success, get session list and pass it to the session
  }

  autoLogin() {
    this.setState({
      loggedIn: true
    })
  }

  handleLoginClick() {

    // this.setState({
    //   loggedIn: true,
    //   visible: true
    // })
    // login component display
    this.openModal();
  };

   
  handleLogoutClick() {
    this.setState({
      loggedIn: false
    })
  };

  openModal() {
    this.setState({
      visible: true
    });
  }

  closeModal() {
    this.setState({
      visible: false,
    });
  }

  handleLoginFailure() {
    this.closeModal();
  }

  handleLoginCancel() {
    this.closeModal();
  }

  handleLoginSuccess() {
    this.setState({
      loggedIn: true
    })
    // reload the page and autologin
    window.location.reload()
  }

  render() {
    let { loggedIn, visible } = this.state;

    return (
      
      <Layout>
        <Layout.Header>
          <h1>SPHTech-IAM</h1>
          {
            loggedIn
              ? <Button type="primary" onClick={this.handleLogoutClick.bind(this)} danger>Logout</Button> 
              : <Button type="primary" onClick={this.handleLoginClick.bind(this)}>Login</Button> 
          }
        </Layout.Header>
        <Layout.Content>
          { loggedIn 
            ? <Session/> 
            : <Home />
          }
        </Layout.Content>
        <Login
          title="SPHTech-IAM"
          visible={visible}
          onSuccess={this.handleLoginSuccess.bind(this)}
          onFailure={this.handleLoginFailure.bind(this)}
          onCancel={this.handleLoginCancel.bind(this)}
        />
      </Layout>
    );
  }
}

export default App;
