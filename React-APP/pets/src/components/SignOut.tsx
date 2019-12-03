import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect, Provider} from "react-redux";
import {GlobalState} from "../App";
import {requestSignIn, signOut} from "../actions/SignInAction";
import  { Redirect } from 'react-router-dom'

export interface SignOutState { isSignedIn: boolean }

const ProtoSignOutForm = (
    props:{
        isSignedIn:boolean,
        performSignIn:(username:string, password:string)=>void,
        performSignOut:()=>void
    }) => {

    props.performSignOut()
    const [ username, setUsername ] = useState("");
    const [ password, setPassword ] = useState("");

    let submitHandler = (e:FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        props.performSignIn(username, password);
        setUsername("");
        setPassword("")
    };

    let usernameChangeHandler = (e:ChangeEvent<HTMLInputElement>) => { setUsername(e.target.value) };

    let passwordChangeHandler = (e:ChangeEvent<HTMLInputElement>) => { setPassword(e.target.value) };

    let SignOutForm =
        (<form onSubmit={submitHandler}>
            <div><label>Username: <input type="text" value={username} onChange={usernameChangeHandler}/></label></div>
    <div><label>Password: <input type="password" value={password} onChange={passwordChangeHandler}/></label></div>
    <button>Sign In</button>
    </form>);

    


    return (<Redirect to = {'/'} />);
    // add a message space for alerts (you were signed out, expired session)
};
const mapStateToProps = (state:GlobalState) => ({isSignedIn:state.signIn.isSignedIn = false});
const mapDispatchToProps =
    (dispatch:any) =>
        ({
            performSignIn: (username:string, password:string) => { dispatch(requestSignIn(username,password))},
            performSignOut: () => dispatch(signOut())
        });
const SignOutForm = connect(mapStateToProps,mapDispatchToProps)(ProtoSignOutForm);

export default SignOutForm

