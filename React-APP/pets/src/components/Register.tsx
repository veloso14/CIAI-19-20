import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect} from "react-redux";
import {GlobalState} from "../App";
import {requestRegister} from "../actions/RegisterAction";
import thunk from 'redux-thunk';
import reducer from "../reducers";
import {applyMiddleware, createStore} from "redux";
import Container from "react-bootstrap/Container";


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

