import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect, Provider} from "react-redux";
import {GlobalState} from "../App";
import {requestSignIn, signOut} from "../actions/SignInAction";
import Container from "react-bootstrap/Container";
import {Link} from "react-router-dom";



export interface LoginState {
    isSignedIn: boolean,
    currentUser: string,
    currentRole: string
}



const ProtoLogin = (
    props: {
        isSignedIn: boolean,
        currentRole: string,
        performSignIn: (username: string, password: string) => void,
        performSignOut: () => void
    }) => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    let submitHandler = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        props.performSignIn(username, password);
        setUsername("");
        setPassword("")
    };

    let usernameChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value)
    };

    let passwordChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value)
    };

    let signInForm =
        (
            <Container>

                <br/>

                <h1 className="text-center">Login</h1>


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
                    <Link to="/register">
                    <button>Register</button>
                    </Link>
                </form>
            </Container>);


    const Content = () => {
        return (<>
        </>);

    };


    return (<> { signInForm} </>);
    // add a message space for alerts (you were signed out, expired session)
};
const mapStateToProps = (state: GlobalState) => ({isSignedIn: state.signIn.isSignedIn, currentRole: state.signIn.currentRole});
const mapDispatchToProps =
    (dispatch: any) =>
        ({
            performSignIn: (username: string, password: string) => {
                dispatch(requestSignIn(username, password))
            },
            performSignOut: () => dispatch(signOut())
        });
const Login = connect(mapStateToProps, mapDispatchToProps)(ProtoLogin);

export default Login
