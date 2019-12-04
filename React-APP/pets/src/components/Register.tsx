import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect, Provider} from "react-redux";
import App, {GlobalState} from "../App";
import {requestRegister} from "../actions/RegisterAction";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import thunk from 'redux-thunk';
import PetList from "./PetList";
import PetDetails from "./PetDetails";
import reducer from "../reducers";
import NavigationBar from "./NavigationBar";
import AppointmentDetails from "./AppointmentDetails";
import AddAppointmentForm from "./AddAppointmentForm";
import {applyMiddleware, createStore} from "redux";
import AdminList from "./AdminList";
import Home from "./Home";
import AdminDetails from "./AdminDetails";
import SignOutForm from "./SignOut";
import ChangePassword from "./ChangePassword";
import VetList from "./VetList";
import Container from "react-bootstrap/Container";
import NoMatch from "./NoMatch";
import Schedule from "./Schedule";
import ClientPage from "../pages/ClientPage";


export interface RegisterState {

}

const ProtoRegisterForm = (
    props: {

        performRegister: (username: string, password: string) => void,
    }) => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    let submitHandler = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        props.performRegister(username, password);
        setUsername("");
        setPassword("")
    };

    let usernameChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value)
    };

    let passwordChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value)
    };

    let registerForm =
        (
            <Container>

                <br/>

            <h1 className="text-center">Registar</h1>


                <form onSubmit={submitHandler}>
    <div className="form-group">
        <label>Username: <input className="form-control" type="text" value={username}
    onChange={usernameChangeHandler}/></label>
    </div>
    <div className="form-group">
        <label>Password: <input className="form-control" type="password" value={password}
    onChange={passwordChangeHandler}/></label>
    </div>
    <button>Sign In</button>
    <br/> <br/>
    <button>Registar</button>
    </form>
    </Container>);


    const Content = () => {
        return (<>
            </>);

    };

    const Page = connect(mapStateToProps)(Content);

    let store = createStore(reducer, applyMiddleware(thunk));



    return (<> {registerForm} </>);
    // add a message space for alerts (you were signed out, expired session)
};
const mapStateToProps = (state: GlobalState) => ({});
const mapDispatchToProps =
    (dispatch: any) =>
        ({
            performRegister: (username: string, password: string) => {
                dispatch(requestRegister(username, password))
            },
        });
const RegisterForm = connect(mapStateToProps, mapDispatchToProps)(ProtoRegisterForm);

export default ProtoRegisterForm

