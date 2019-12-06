import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect} from "react-redux";
import {GlobalState} from "../App";
import {requestRegister} from "../actions/RegisterAction";
import thunk from 'redux-thunk';
import reducer from "../reducers";
import {applyMiddleware, createStore} from "redux";
import Container from "react-bootstrap/Container";
import {Link} from "react-router-dom";


export interface RegisterState {

}

const ProtoRegisterForm = (
    props: {
        performRegister: (username: string, password: string , nome : string , email : string , cellphone : string , address : string ) => void
    }) => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [nome, setNome] = useState("");
    const [email, setEmail] = useState("");
    const [cellphone, setCellphone] = useState("");
    const [address, setAddress] = useState("");

    let submitHandler = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        props.performRegister(username, password , nome , email , cellphone , address);
        setUsername("");
        setPassword("");
        setNome("");
        setEmail("");
        setCellphone("");
        setAddress("");
    };

    let usernameChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value)
    };

    let passwordChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value)
    };

    let nameChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setNome(e.target.value)
    };

    let emailChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setEmail(e.target.value)
    };

    let cellphoneChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setCellphone(e.target.value)
    };

    let addressChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setAddress(e.target.value)
    };

    let registerForm =
        (
            <Container>

                <br/>

                <h1 className="text-center">Register</h1>


                <form onSubmit={submitHandler}>

                    <div className="form-group">
                        <label>Name: <input className="form-control" type="text" required value={nome}
                                            onChange={nameChangeHandler}/></label>
                    </div>
                    <div className="form-group">
                        <label>Email: <input className="form-control" type="text" required value={email}
                                             onChange={emailChangeHandler}/></label>
                    </div>
                    <div className="form-group">
                        <label>Username: <input className="form-control" type="text" required value={username}
                                                onChange={usernameChangeHandler}/></label>
                    </div>
                    <div className="form-group">
                        <label>Password: <input className="form-control" type="password" required value={password}
                                                onChange={passwordChangeHandler}/></label>
                    </div>

                    <div className="form-group">
                        <label>CellPhone: <input className="form-control" type="text" required value={cellphone}
                                                 onChange={cellphoneChangeHandler}/></label>
                    </div>

                    <div className="form-group">
                        <label>Address: <input className="form-control" type="text" required value={address}
                                               onChange={addressChangeHandler}/></label>
                    </div>

                    <button>Complete Register</button>
                    <br/> <br/>
                    <Link to="/">
                        <button>Login</button>
                    </Link>
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
            performRegister: (username: string, password: string , nome : string , email : string , cellphone : string , address : string ) => {
                dispatch(requestRegister(username, password , nome , email , cellphone , address))
            },
        });
const RegisterForm = connect(mapStateToProps, mapDispatchToProps)(ProtoRegisterForm);

export default RegisterForm

