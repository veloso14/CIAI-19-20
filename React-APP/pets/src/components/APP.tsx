import React, {ChangeEvent, FormEvent, useState} from "react";
import {connect, Provider} from "react-redux";
import {GlobalState} from "../App";
import {requestSignIn, signOut} from "../actions/SignInAction";
import {BrowserRouter as Router, Route, Switch, Redirect} from "react-router-dom";
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
import {REGISTER} from "../actions/RegisterAction";
import ProtoRegisterForm from "./Register";
import Login from "./Login";
import RegisterForm from "./Register";


export interface SignInState {
    isSignedIn: boolean,
    currentUser: string,
    currentRole: string
}



const ProtoSignInForm = (
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
            <Router>
            <Switch>
                <Route exact path="/" component={Login} />
                <Route exact path="/" component={Login} />
                <Route exact path="/logout" component={Login} />
                <Route exact path="/register" component={RegisterForm}/>
            </Switch>
        </Router>


    );



    const Content = () => {
        return (<>
        </>);

    };

    // @ts-ignore
    const ClientRoute = ({ children, ...rest }) => {
        return (
            <Route
                {...rest}
                render={({ location }) =>
                    props.currentRole == "CLIENT" ? (
                        children
                    ) : (
                        <Redirect
                            to={{
                                pathname: "/",
                                state: { from: location }
                            }}
                        />
                    )
                }
            />
        );
    }

    // @ts-ignore
    const AdminRoute = ({ children, ...rest }) => {
        return (
            <Route
                {...rest}
                render={({ location }) =>
                    props.currentRole == "ROLE_ADMIN" ? (
                        children
                    ) : (
                        <Redirect
                            to={{
                                pathname: "/",
                                state: { from: location }
                            }}
                        />
                    )
                }
            />
        );
    }

    // @ts-ignore
    const VetRoute = ({ children, ...rest }) => {
        return (
            <Route
                {...rest}
                render={({ location }) =>
                    props.currentRole == "ROLE_VET" ? (
                        children
                    ) : (
                        <Redirect
                            to={{
                                pathname: "/",
                                state: { from: location }
                            }}
                        />
                    )
                }
            />
        );
    }

    const Page = connect(mapStateToProps)(Content);

    let store = createStore(reducer, applyMiddleware(thunk));

    let signOutForm = (

        <Provider store={store}>
            <NavigationBar currentRole={props.currentRole}/>
            <Router>
                <Switch>
                    <Route path="/" exact component={Home}/>
                    <Route path="/pet/" exact component={PetList}/>
                    <Route path="/pet/:id" component={PetDetails}/>
                    <Route path="/appointment/:id" component={AppointmentDetails}/>
                    <Route path="/appointment/" exact component={AddAppointmentForm}/>
                    <AdminRoute path="/admin/" exact><AdminList/></AdminRoute>
                    <AdminRoute path="/admin/:id"><AdminDetails/></AdminRoute>
                    <Route path="/vet/:id" component={VetDetails}/>
                    <Route path="/profile/" component={ChangePassword}/>
                    <Route path="/schedule/" component={Schedule}/>
                    <VetRoute path="/vet/" exact><VetList/></VetRoute>
                    <ClientRoute path="/client/"><ClientPage/></ClientRoute>
                    <Route path="/logout/" exact component={Login}/>
                    <Route component={NoMatch}/>
                </Switch>
            </Router>
            <Page/>
        </Provider>
    );


    return (<> {props.isSignedIn ? signOutForm : signInForm} </>);
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
const SignInForm = connect(mapStateToProps, mapDispatchToProps)(ProtoSignInForm);

export default SignInForm

