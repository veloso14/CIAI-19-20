import React from "react";
import {connect} from "react-redux";
import {GlobalState} from "../App";
import {requestSignIn, signOut} from "../actions/SignInAction";
import {Redirect} from 'react-router-dom'

export interface SignOutState {
    isSignedIn: boolean
}

const ProtoSignOutForm = (
    props: {
        isSignedIn: boolean,
        performSignIn: (username: string, password: string) => void,
        performSignOut: () => void
    }) => {

    props.performSignOut()
    return (<Redirect to={'/'}/>);
    // add a message space for alerts (you were signed out, expired session)
};
const mapStateToProps = (state: GlobalState) => ({isSignedIn: state.signIn.isSignedIn = false});
const mapDispatchToProps =
    (dispatch: any) =>
        ({
            performSignIn: (username: string, password: string) => {
                dispatch(requestSignIn(username, password))
            },
            performSignOut: () => dispatch(signOut())
        });
const SignOutForm = connect(mapStateToProps, mapDispatchToProps)(ProtoSignOutForm);

export default SignOutForm

