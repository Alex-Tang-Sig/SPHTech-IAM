import React, { Component } from "react";
import { Table, Space} from "antd";

class Session extends Component {

  constructor() {
    super();
    this.state = {
      sessions: [],
      firstname: "ALEX",
      lastname: "",
    }
  }

  componentDidMount() {
    this.getSession();
  }

  getSession() {
    // fetch("./api/some.json")
    // .then(
    //   function(response) {
    //     if (response.status !== 200) {
    //       console.log("Looks like there was a problem. Status Code: " +
    //         response.status);
    //       return;
    //     }
  
    //     // Examine the text in the response
    //     response.json().then(function(data) {
    //       this.setState({
    //         sessions: JSON.parse(data)
    //       })
    //     });
    //   }
    // )
    // .catch(function(err) {
    //   console.log("Fetch Error :-S", err);
    // });
    let data = [
      {
        key: 1,
        firstname: "alex",
        lastname: "tang",
        sessionId: 1,
      },
      {
        key: 2,
        firstname: "alex1",
        lastname: "tang",
        sessionId: 1,
      },
      {
        key: 3,
        firstname: "alex2",
        lastname: "tang2",
        sessionId: 1,
      }
    ]
    this.setState({
      sessions: data
    })
    this.getName();
  }

  getName() {
    this.setState({
      firstname: "Alice",
      lastname: "Tang"
    })
  }


  handleSessionDelete() {
    // delete session
  }

  render() {
    let { sessions, firstname, lastname } = this.state;
    let columns = [
      {
        title: "First Name",
        dataIndex: "firstname",
        key: "firstname",
      },
      {
        title: "Last Name",
        dataIndex: "lastname",
        key: "lastname",
      },
      {
        title: "Session ID",
        dataIndex: "sessionId",
        key: "sessionId",
      }
    ];

    return (
      <div className="session">
        <h1>Hello, {firstname + " " + lastname}</h1>
        <Table dataSource={sessions}>
          <Table.ColumnGroup title="Name">
            <Table.Column title="First Name" dataIndex="firstname" key="firstname" />
            <Table.Column title="Last Name" dataIndex="lastname" key="lastname" />
          </Table.ColumnGroup>
          <Table.Column
            title="Action"
            key="action"
            render={(text, record) => (
              <Space size="middle">
                <a onClick={this.handleSessionDelete.bind(this, record)}></a>
                <a>Delete</a>
              </Space>
            )}
          />
        </Table>
      </div>
    );
  }
}

export default Session;