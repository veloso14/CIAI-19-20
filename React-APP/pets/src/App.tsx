import React from 'react';
import './App.css';
import {PetState, Pet} from './components/PetList'
import {PetList} from './components/PetList'
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
import PetDetails from "./components/PetDetails";

export interface GlobalState {
    pets: PetState,
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
            <Router>
                <Route path="/pet" exact component={Page} />
                <Route path="/pet/:id" component={PetDetails} />
            </Router>
        </Provider>
    );
};
export default App;