import React from 'react';
import './App.css';
import {PetState, Pet} from './components/PetList'

import {connect, Provider} from "react-redux";
import {applyMiddleware, createStore} from "redux";
import reducer from "./reducers";
import thunk from 'redux-thunk';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";
import PetList from "./components/PetList";
import PetDetails from "./components/PetDetails";
import NavigationBar from "./components/NavigationBar";
import AppointmentDetails from "./components/AppointmentDetails";
import AddAppointmentForm from "./components/AddAppointmentForm";
import AdminList, {AdminState} from "./components/AdminList";
export interface GlobalState {
    pets: PetState,
    admins:AdminState,
    currentUser: {
        id:string,
        name:string,
    }
}

const Content = () => {
    return (<>
        <PetList/>
    </>);

};

const mapStateToProps = (state: GlobalState) => ({});
const Page = connect(mapStateToProps)(Content);

let store = createStore(reducer, applyMiddleware(thunk));

const App = () => {
    return (
        <Provider store={store}>
            <NavigationBar/>
            <Router>
                <Route path="/pet/" exact component={PetList} />
                <Route path="/pet/:id" component={PetDetails} />
                <Route path="/appointment/:id" component={AppointmentDetails} />
                <Route path="/appointment/" exact component={AddAppointmentForm} />
                <Route path="/admin/" exact component={AdminList} />
            </Router>
        </Provider>
    );
};
export default App;