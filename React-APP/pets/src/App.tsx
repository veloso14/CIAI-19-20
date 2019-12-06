import React from 'react';
import './App.css';
import {PetState} from './components/PetList'

import {connect, Provider} from "react-redux";
import {applyMiddleware, createStore} from "redux";
import reducer from "./reducers";
import thunk from 'redux-thunk';
import {AdminState} from "./components/AdminList";
import SignInForm, {SignInState} from "./components/APP";
import {VetState} from "./components/VetList";
import {AppointmentState} from "./components/Schedule";


export interface GlobalState {
    pets: PetState,
    signIn: SignInState,
    admins:AdminState,
    appointments: AppointmentState,
    vets:VetState,
        currentUser: {
        id:string,
        name:string,
        token:string
    }
}

const Content = (props:{isSignedIn:boolean}) => {
    return (<>
        <SignInForm/>
        { props.isSignedIn  }
    </>);

};


const mapStateToProps = (state: GlobalState) => ({isSignedIn: state.signIn.isSignedIn});
const Page = connect(mapStateToProps)(Content);


let store = createStore(reducer, applyMiddleware(thunk));

const App = () => {
    return (
        <Provider store={store}>
            <Page/>
        </Provider>
    );
};
export default App;