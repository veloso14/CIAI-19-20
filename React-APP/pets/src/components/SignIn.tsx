import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect, Provider} from "react-redux";
import {GlobalState} from "../App";
import {requestSignIn, signOut} from "../actions/SignInAction";
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
import VetDetails from "./VetDetails";


export interface SignInState {
    isSignedIn: boolean,
    currentUser: string
}

const ProtoSignInForm = (
    props: {
        isSignedIn: boolean,
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
                    <button>Registar</button>
                </form>
            </Container>);


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
                <Switch>
                    <Route path="/" exact component={Home}/>
                    <Route path="/pet/" exact component={PetList}/>
                    <Route path="/pet/:id" component={PetDetails}/>
                    <Route path="/appointment/:id" component={AppointmentDetails}/>
                    <Route path="/appointment/" exact component={AddAppointmentForm}/>
                    <Route path="/admin/" exact component={AdminList}/>
                    <Route path="/admin/:id" component={AdminDetails}/>
                    <Route path="/vet/:id" component={VetDetails}/>
                    <Route path="/profile/" component={ChangePassword}/>
                    <Route path="/schedule/" component={Schedule}/>
                    <Route path="/vet/" exact component={VetList}/>
                    <Route path="/client/" exact component={ClientPage}/>
                    <Route path="/logout/" exact component={SignOutForm}/>
                    <Route component={NoMatch}/>
                </Switch>
            </Router>
            <Page/>
        </Provider>
    );


    return (<> {props.isSignedIn ? signOutForm : signInForm} </>);
    // add a message space for alerts (you were signed out, expired session)
};
const mapStateToProps = (state: GlobalState) => ({isSignedIn: state.signIn.isSignedIn});
const mapDispatchToProps =
    (dispatch: any) =>
        ({
            performSignIn: (username: string, password: string) => {
                dispatch(requestSignIn(username, password))
            },
            performSignOut: () => dispatch(signOut())
        });
const SignInForm = connect(mapStateToProps, mapDispatchToProps)(ProtoSignInForm);

export default SignInForm

