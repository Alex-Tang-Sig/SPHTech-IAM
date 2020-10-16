import React, { Component } from 'react'
import { Modal, Button, Input } from 'antd'
import { PropTypes } from 'prop-types'
import { GoogleOutlined } from '@ant-design/icons'

class Login extends Component {
  static propTypes = {
    title: PropTypes.string,
    visible: PropTypes.bool,
    onSuccess: PropTypes.func,
    onFailure: PropTypes.func,
  }

  constructor(props) {
    super(props)
    this.state = {
      firstname: '',
      lastname: '',
      password: '',
      signUp: true,
    }
  }

  handleFirstnameInput(e) {
    this.setState({
      firstname: e.target.value,
    })
  }

  handleLastnameInput(e) {
    this.setState({
      lastname: e.target.value,
    })
  }

  handlePasswordInput(e) {
    this.setState({
      password: e.target.value,
    })
  }

  handleSignIn() {
    this.setState({
      signUp: false,
    })
    this._clearInput()
  }

  handleSignUp() {
    this.setState({
      signUp: true,
    })
    this._clearInput()
  }

  _clearInput() {
    this.setState({
      firstname: '',
      lastname: '',
      password: '',
    })
  }

  handleSignInSubmit() {
    // use login api
    let { firstname, lastname, password } = this.state
    // user creation api
    console.log('submit sign in ', firstname, lastname, password)
    // if it is right, but exceeds, i need to delete
  }

  handleSignUpSubmit() {
    let { firstname, lastname, password } = this.state
    // user creation api
    console.log('submit sign up ', firstname, lastname, password)
  }

  handleGoogleSubmit() {
    // go to google
    console.log('go to google')
  }

  render() {
    let { signUp } = this.state
    let { title, visible, onCancel } = this.props
    return (
      <Modal title={title} visible={visible} onCancel={onCancel} className="login-modal">
        <div className="header">Create your free account</div>
        <div className="sign-in-google">
          <Button type="primary" icon={<GoogleOutlined />} block onClick={this.handleGoogleSubmit.bind(this)}>
            Sign in with Google
          </Button>
        </div>
        <div className="separator">
          <span>or</span>
        </div>
        <div className="sign-area">
          <div className="sign-username">
            <div className="form-group">
              <div className="form-title">
                <p>{signUp ? 'Sign up' : 'Sign in'}</p>
              </div>
              <div className="name-group">
                <div className="input-wrapper wapperfirstname-wrapper">
                  <Input
                    placeholder="First Name"
                    type="text"
                    name="firstname"
                    id="firstname"
                    value={this.state.firstname}
                    onChange={this.handleFirstnameInput.bind(this)}
                  />
                </div>
                <div className="input-wrapper lastname-wrapper">
                  <Input
                    placeholder="Last Name"
                    type="text"
                    name="lastname"
                    id="lastname"
                    value={this.state.lastname}
                    onChange={this.handleLastnameInput.bind(this)}
                  />
                </div>
                <div className="input-wrapper password-wrapper">
                  <Input
                    placeholder="Password"
                    type="password"
                    id="password"
                    value={this.state.password}
                    onChange={this.handlePasswordInput.bind(this)}
                  />
                </div>
              </div>
              <div className="sign-submit">
                {signUp ? (
                  <Button type="primary" onClick={this.handleSignUpSubmit.bind(this)}>
                    Submit
                  </Button>
                ) : (
                  <Button type="primary" onClick={this.handleSignInSubmit.bind(this)}>
                    Submit
                  </Button>
                )}
              </div>
              <div className="sign-link">
                {signUp ? (
                  <p>
                    Already have an account?<span onClick={this.handleSignIn.bind(this)}>&nbsp; Log in</span>
                  </p>
                ) : (
                  <p>
                    Don’t have an account?<span onClick={this.handleSignUp.bind(this)}> Sign up now</span>
                  </p>
                )}
              </div>
            </div>
          </div>
        </div>
      </Modal>
    )
  }
}

export default Login
