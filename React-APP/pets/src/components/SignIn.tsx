import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect, Provider} from "react-redux";
import {GlobalState} from "../App";
import {requestSignIn, signOut} from "../actions/SignInAction";
import {BrowserRouter as Router, Route} from "react-router-dom";
import thunk from 'redux-thunk';
import PetList from "./PetList";
import PetDetails from "./PetDetails";
import reducer from "../reducers";
import NavigationBar from "./NavigationBar";
import AppointmentDetails from "./AppointmentDetails";
import AddAppointmentForm from "./AddAppointmentForm";
import {applyMiddleware, createStore} from "redux";
import AdminList from "./AdminList";

export interface SignInState { isSignedIn: boolean }

const ProtoSignInForm = (
    props:{
        isSignedIn:boolean,
        performSignIn:(username:string, password:string)=>void,
        performSignOut:()=>void
    }) => {

    const [ username, setUsername ] = useState("");
    const [ password, setPassword ] = useState("");

    let submitHandler = (e:FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        props.performSignIn(username, password);
        setUsername("");
        setPassword("")
    };

    let handlerLogout = (e:FormEvent<HTMLButtonElement>) => { props.performSignOut() };

    let usernameChangeHandler = (e:ChangeEvent<HTMLInputElement>) => { setUsername(e.target.value) };

    let passwordChangeHandler = (e:ChangeEvent<HTMLInputElement>) => { setPassword(e.target.value) };

    let signInForm =
        (<form onSubmit={submitHandler}>
            <div><label>Username: <input type="text" value={username} onChange={usernameChangeHandler}/></label></div>
            <div><label>Password: <input type="password" value={password} onChange={passwordChangeHandler}/></label></div>
            <button>Sign In</button>
        </form>);


    const Content = () => {
        return (<>
        </>);

    };

    const Page = connect(mapStateToProps)(Content);

    let store = createStore(reducer, applyMiddleware(thunk));

    let signOutForm = (

        <Provider store={store}>
            <NavigationBar/>
            <Router>
                <Route path="/pet/" exact component={PetList} />
                <Route path="/pet/:id" component={PetDetails} />
                <Route path="/appointment/:id" component={AppointmentDetails} />
                <Route path="/appointment/" exact component={AddAppointmentForm} />
                <Route path="/admin/" exact component={AdminList} />
            </Router>
            <Page/>
        </Provider>
    );


    return (<> {props.isSignedIn ? signOutForm : signInForm} </>);
    // add a message space for alerts (you were signed out, expired session)
};
const mapStateToProps = (state:GlobalState) => ({isSignedIn:state.signIn.isSignedIn});
const mapDispatchToProps =
    (dispatch:any) =>
        ({
            performSignIn: (username:string, password:string) => { dispatch(requestSignIn(username,password))},
            performSignOut: () => dispatch(signOut())
        });
const SignInForm = connect(mapStateToProps,mapDispatchToProps)(ProtoSignInForm);

export default SignInForm

